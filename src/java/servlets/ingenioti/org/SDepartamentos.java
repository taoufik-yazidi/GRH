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
import negocio.ingenioti.org.NDepartamentos;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.ODepartamentos;
import objetos.ingenioti.org.OMunicipios;

/**
 *
 * @author Alexys
 */
@WebServlet(name = "SDepartamentos", urlPatterns = {"/SDepartamentos"})
public class SDepartamentos extends HttpServlet {

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
            short tipoMensaje = 0;
            String mensajeLista = "";
            short tipoMensajeLista = 0;
            JsonObject modelo, jsLista;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String accion = request.getParameter("accion");
            String tipoConsulta = request.getParameter("tipoConsulta");
            Short sAccion;
            Short sTipoConsulta;
            try {
                sAccion = Short.parseShort(accion);
            } catch (NumberFormatException nfe) {
                sAccion = 0;
            }
            try {
                sTipoConsulta = Short.parseShort(tipoConsulta);
            } catch (NumberFormatException nfe) {
                sTipoConsulta = 0;
            }

            // Variables de paginación
            String pagina = request.getParameter("pag");
            String limite = request.getParameter("lim");
            String columnaOrden = request.getParameter("cor");
            String tipoOrden = request.getParameter("tor");

            if (tipoOrden == null || tipoOrden.length() == 0) {
                tipoOrden = "asc";
            }

            int iPagina, iLimite, iColumnaOrden;
            try {
                iPagina = Integer.parseInt(pagina);
                iLimite = Integer.parseInt(limite);
                iColumnaOrden = Integer.parseInt(columnaOrden);
            } catch (NumberFormatException nfe) {
                iPagina = 1;
                iLimite = 5;
                iColumnaOrden = 1;
            }

            String siddepartamento = request.getParameter("iddepartamento");
            String scodigo = request.getParameter("codigo");
            String snombre = request.getParameter("nombre");
            short iiddepartamento = 0;
            try {
                iiddepartamento = Short.parseShort(siddepartamento);
            } catch (NumberFormatException nfe) {
                System.err.println("Error al convertir: iddepartamento en Short  en el servlet SDepartamentos");
            }

            // Para realizar cualquier accion: insertar, modificar o borrar
            NDepartamentos nDepartamentos = new NDepartamentos();
            ODepartamentos oDepartamentos = new ODepartamentos(iiddepartamento, scodigo, snombre, (OMunicipios) null);

            // Preparación de la lista de objetos a retornar
            ArrayList<ODepartamentos> lista = new ArrayList<ODepartamentos>();
            int totalPaginas = 0;
            int totalRegistros = 0;

            // Insertar, modificar o borrar
            if (sAccion.equals(SUtilidades.INSERTAR) || sAccion.equals(SUtilidades.MODIFICAR)
                    || sAccion.equals(SUtilidades.BORRAR)) {
                // Validación de campos vacios
                if ( !sAccion.equals(SUtilidades.BORRAR) && (scodigo == null || scodigo.length() == 0 || snombre == null || snombre.length() == 0)) {
                    tipoMensaje = 4;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else {
                    int respuesta = 0;
                    try {
                        respuesta = nDepartamentos.ejecutarSQL(sAccion, oDepartamentos, credencial);
                        tipoMensaje = 3; // Inicia en warning porque es la mayor cantidad de opciones
                        if (respuesta == -2) {
                            mensaje = "Error de llave duplicada";
                        }
                        if (respuesta == -1) {
                            mensaje = "Error de violación de llave foranea. Problema de dependencias.";
                        }
                        if (respuesta == 0 || respuesta < -1) {
                            mensaje = "No se encontró el registro en la BD, o error desconocido de BD.";
                        }
                        if (respuesta > 0) {
                            mensaje = "Proceso realizado correctamente - ID: " + respuesta;
                            tipoMensaje = 1;
                        }
                    } catch (ExcepcionGeneral eg) {
                        tipoMensaje = 3;
                        mensaje = eg.getMessage();
                    }
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                }
            }
            if (sAccion == 4) { // Consultar
                try {
                    totalRegistros = nDepartamentos.getCantidadRegistros("departamentos");
                    if (totalRegistros > 0) {
                        totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
                    } else {
                        totalPaginas = 0;
                    }
                    if (iPagina > totalPaginas) {
                        iPagina = totalPaginas;
                    }
                    lista = nDepartamentos.consultar((short) 4, sTipoConsulta, oDepartamentos, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                    tipoMensajeLista = 1;
                    mensajeLista = "Cargue de consulta realizado correctamente.";
                } catch (ExcepcionGeneral eg) {
                    tipoMensajeLista = 3;
                    mensajeLista = eg.getMessage();
                }

                jsArray = Json.createArrayBuilder();
                for (ODepartamentos obj : lista) {
                    JsonObject temp = Json.createObjectBuilder()
                            .add("iddepartamento", obj.getIddepartamento())
                            .add("codigo", obj.getCodigo())
                            .add("nombre", obj.getNombre())
                            .build();
                    jsArray.add(temp);
                }
                jsLista = Json.createObjectBuilder()
                        .add("tipoMensajeLista", tipoMensajeLista)
                        .add("mensajeLista", mensajeLista)
                        .add("registros", totalRegistros)
                        .add("paginas", totalPaginas)
                        .add("lista", jsArray)
                        .build();
                jsEscritor.writeObject(jsLista);
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
