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
import objetos.ingenioti.org.OCredencial;
import negocio.ingenioti.org.NGenero;
import objetos.ingenioti.org.OGenero;

/**
 *
 * @author Alexys
 */
@WebServlet(name = "SGenero", urlPatterns = {"/SGenero"})
public class SGenero extends HttpServlet {

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

            String sidgenero = request.getParameter("idgenero");
            String ssigla = request.getParameter("sigla");
            String sgenero = request.getParameter("genero");
            short iidgenero = 0;
            try {
                iidgenero = Short.parseShort(sidgenero);
            } catch (NumberFormatException nfe) {
                System.err.println("Error al convertir: idgenero en Short  en el servlet SGenero");
            }

            // Para realizar cualquier accion: insertar, modificar o borrar
            NGenero nGenero = new NGenero();
            OGenero oGenero = new OGenero(iidgenero, ssigla, sgenero);

            // Preparación de la lista de objetos a retornar
            ArrayList<OGenero> lista = new ArrayList<OGenero>();
            int totalPaginas = 0;
            int totalRegistros = 0;

            // Para realizar la acción de la 1 a la 3
            if (sAccion > 0 && sAccion < 4) {
                // Validación de campos vacios
                if (sAccion != 3 && (ssigla == null || ssigla.length() == 0 || sgenero == null || sgenero.length() == 0)) {
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
                        respuesta = nGenero.ejecutarSQL(sAccion, oGenero, credencial);
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
                    totalRegistros = nGenero.getCantidadRegistros("genero");
                    if (totalRegistros > 0) {
                        totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
                    } else {
                        totalPaginas = 0;
                    }
                    if (iPagina > totalPaginas) {
                        iPagina = totalPaginas;
                    }
                    lista = nGenero.consultar((short) 4, sTipoConsulta, oGenero, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                    tipoMensajeLista = 1;
                    mensajeLista = "Cargue de consulta realizado correctamente.";
                } catch (ExcepcionGeneral eg) {
                    tipoMensajeLista = 3;
                    mensajeLista = eg.getMessage();
                }

                jsArray = Json.createArrayBuilder();
                for (OGenero obj : lista) {
                    JsonObject temp = Json.createObjectBuilder()
                            .add("idgenero", obj.getIdgenero())
                            .add("sigla", obj.getSigla())
                            .add("genero", obj.getGenero())
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
