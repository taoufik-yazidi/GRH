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
import negocio.ingenioti.org.NTiposDeDocumentos;
import objetos.ingenioti.org.OTiposDeDocumento;

@WebServlet(name = "STiposDeDocumento", urlPatterns = {"/STiposDeDocumento"})
public class STiposDeDocumento extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(STiposDeDocumento.class.getName());

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
            String mensajeLista;
            short tipoMensajeLista;
            JsonObject modelo;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String accion = request.getParameter("accion");
            String tipoConsulta = request.getParameter("tipoConsulta");
            String idTipoDocumento = request.getParameter("idunico");
            String abreviatura = request.getParameter("abreviatura");
            String tipoDeDocumento = request.getParameter("tipoDocumento");
            String activo = request.getParameter("activo");

            boolean bActivo = activo != null;

            byte sAccion;
            try {
                sAccion = Byte.parseByte(accion);
            } catch (NumberFormatException nfe) {
                sAccion = 0;
            }

            short iidTipoDocumento = 0;
            if (sAccion != SUtilidades.ACCIONINSERTAR) {
                try {
                    iidTipoDocumento = Short.parseShort(idTipoDocumento);
                } catch (NumberFormatException nfe) {
                    SUtilidades.generaLogServer(LOG, Level.WARNING, "Error al convertir idTipoDocumento "+idTipoDocumento);
                }
            }

            // Para realizar cualquier accion de insertar, modificar o borrar
            NTiposDeDocumentos nTiposDeDocumento = new NTiposDeDocumentos();
            OTiposDeDocumento oTiposDeDocumento = new OTiposDeDocumento(iidTipoDocumento, abreviatura, tipoDeDocumento, bActivo);

            if (sAccion == SUtilidades.ACCIONINSERTAR
                    || sAccion == SUtilidades.ACCIONACTUALIZAR
                    || sAccion == SUtilidades.ACCIONBORRAR) {
                if (sAccion != SUtilidades.ACCIONBORRAR && (abreviatura == null || abreviatura.length() == 0
                        || tipoDeDocumento == null || tipoDeDocumento.length() == 0)) {
                    tipoMensaje = SUtilidades.TIPO_MSG_ADVERTENCIA;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else {
                    int respuesta = 0;
                    try {
                        respuesta = nTiposDeDocumento.ejecutarSQL(sAccion, oTiposDeDocumento, credencial);
                        tipoMensaje = SUtilidades.TIPO_MSG_ADVERTENCIA; // Inicia en Warning porque es la mayor cantidad de opciones
                        if (respuesta == SUtilidades.MSG_BD_ERROR_UK) {
                            mensaje = "Error de llave duplicada.";
                        }
                        if (respuesta == SUtilidades.MSG_BD_ERROR_FK) {
                            mensaje = "Error de violación de llave foranea. Problema de dependencias.";
                        }
                        if (respuesta == SUtilidades.MSG_BD_ERROR) {
                            mensaje = "No se encontró el registro en la BD.";
                        }
                        if (respuesta > 0) {
                            tipoMensaje = SUtilidades.TIPO_MSG_CORRECTO;
                            mensaje = "Proceso realizado correctamente - ID: " + respuesta;
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
