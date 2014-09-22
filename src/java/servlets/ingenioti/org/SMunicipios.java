/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import objetos.ingenioti.org.OCredencial;
import negocio.ingenioti.org.NMunicipios;
import objetos.ingenioti.org.ODepartamentos;
import objetos.ingenioti.org.OMunicipios;

/**
 *
 * @author Alexys
 */
@WebServlet(name = "SMunicipios", urlPatterns = {"/SMunicipios"})
public class SMunicipios extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        HttpSession sesion = request.getSession();
        if (SUtilidades.autenticado(sesion)) {
            // Elementos de respuesta
            String mensaje = "";
            short tipoMensaje;
            // Objeto Json de respuesta
            JsonObject modelo;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String accion = request.getParameter("accion");
            byte sAccion;
            try {
                sAccion = Byte.parseByte(accion);
            } catch (NumberFormatException nfe) {
                sAccion = 0;
            }

            String sidmunicipio = request.getParameter("idunico");
            short iidmunicipio = 0;
            if (sAccion != SUtilidades.ACCIONINSERTAR) {
                try {
                    iidmunicipio = Short.parseShort(sidmunicipio);
                } catch (NumberFormatException nfe) {
                    SUtilidades.generaLogServer(LOG, Level.SEVERE, "Error al convertir: idmunicipio en Short: "+sidmunicipio);
                }
            }
            // Se obtienen los campos enviados por la vista
            String siddepartamento = request.getParameter("iddepartamento");
            String scodigo = request.getParameter("codigo");
            String snombre = request.getParameter("nombre");
            short iiddepartamento = 0;
            try {
                iiddepartamento = Short.parseShort(siddepartamento);
            } catch (NumberFormatException nfe) {
                SUtilidades.generaLogServer(LOG,Level.WARNING, "Error al convertir: iddepartamento en Short "+siddepartamento);
            }
            
            // Para realizar cualquier accion: insertar, modificar o borrar
            NMunicipios nMunicipios = new NMunicipios();
            ODepartamentos oDepartamentos = new ODepartamentos();
            oDepartamentos.setIddepartamento(iiddepartamento);
            OMunicipios oMunicipios = new OMunicipios(iidmunicipio, scodigo, snombre);
            oDepartamentos.addMunicipio(oMunicipios);

            // Para realizar la acción de la 1 a la 3
            //if (sAccion > 0 && sAccion < 4) {
            if (sAccion == SUtilidades.ACCIONINSERTAR
                    || sAccion == SUtilidades.ACCIONACTUALIZAR
                    || sAccion == SUtilidades.ACCIONBORRAR) {

                // Validación de campos vacios
                if (sAccion != SUtilidades.ACCIONBORRAR
                        && (siddepartamento == null || siddepartamento.length() == 0
                        || scodigo == null || scodigo.length() == 0 || snombre == null
                        || snombre.length() == 0)) {
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", SUtilidades.TIPO_MSG_ADVERTENCIA)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else {

                    int respuesta = 0;
                    try {
                        respuesta = nMunicipios.ejecutarSQL(sAccion, oDepartamentos, credencial);
                        tipoMensaje = SUtilidades.TIPO_MSG_ADVERTENCIA; // Inicia en warning porque es la mayor cantidad de opciones
                        if (respuesta == SUtilidades.MSG_BD_ERROR_UK) {
                            mensaje = "Error de llave duplicada";
                        }
                        if (respuesta == SUtilidades.MSG_BD_ERROR_FK) {
                            mensaje = "Error de violación de llave foranea. Problema de dependencias.";
                        }
                        if (respuesta == SUtilidades.MSG_BD_ERROR) {
                            mensaje = "No se encontró el registro en la BD, o error desconocido de BD.";
                        }
                        if (respuesta > 0) {
                            mensaje = "Proceso realizado correctamente - ID: " + respuesta;
                            tipoMensaje = SUtilidades.TIPO_MSG_CORRECTO;
                        }
                    } catch (ExcepcionGeneral eg) {
                        tipoMensaje = SUtilidades.TIPO_MSG_ERROR;
                        mensaje = eg.getMessage();
                    }
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                }
            }

            jsEscritor.close();

            String jsObjeto = sEscritor.toString();
            PrintWriter out = response.getWriter();
            try {
                out.println(jsObjeto);
            } finally {
                out.close();
            }
        } else {
            SUtilidades.irAPagina("/index.jsp", request, response, getServletContext());
        }
    }
    private static final Logger LOG = Logger.getLogger(SMunicipios.class.getName());

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
