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
import negocio.ingenioti.org.NEstadocivil;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.OEstadoCivil;

@WebServlet(name = "SEstadocivilSel", urlPatterns = {"/SEstadocivilSel"})
public class SEstadocivilSel extends HttpServlet {
    
    private static final Logger LOG = Logger.getLogger(SEstadocivilSel.class.getName());
    
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
            String mensajeLista;
            short tipoMensajeLista;
            JsonObject jsLista;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String tipoConsulta = request.getParameter("tipoConsulta");
            byte sTipoConsulta = 0;
            try {
                sTipoConsulta = Byte.parseByte(tipoConsulta);
            } catch (NumberFormatException nfe) {}

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

            String sidestadocivil = request.getParameter("idestadocivil");
            short iidestadocivil = 0;
            if (sTipoConsulta == SUtilidades.CONSULTA_ID) {
                try {
                    iidestadocivil = Short.parseShort(sidestadocivil);
                } catch (NumberFormatException nfe) {
                    SUtilidades.generaLogServer(LOG, Level.WARNING, "Error al convertir: idestadocivil en Short  en el servlet SEstadocivil");
                }
            }
            NEstadocivil nEstadocivil = new NEstadocivil();
            OEstadoCivil oEstadocivil = new OEstadoCivil(iidestadocivil, null, null);

            // Preparación de la lista de objetos a retornar
            ArrayList<OEstadoCivil> lista = new ArrayList<OEstadoCivil>();
            int totalPaginas = 0;
            int totalRegistros = 0;
            totalRegistros = nEstadocivil.getCantidadRegistros("estadocivil");
            if (totalRegistros > 0) {
                totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
            } else {
                totalPaginas = 0;
            }
            if (iPagina > totalPaginas) {
                iPagina = totalPaginas;
            }
            try {
                lista = nEstadocivil.consultar(sTipoConsulta, oEstadocivil, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                tipoMensajeLista = SUtilidades.TIPO_MSG_CORRECTO;
                mensajeLista = "Cargue de consulta realizado correctamente.";
            } catch (ExcepcionGeneral eg) {
                tipoMensajeLista = SUtilidades.TIPO_MSG_ERROR;
                mensajeLista = eg.getMessage();
            }
            jsArray = Json.createArrayBuilder();
            for (OEstadoCivil obj : lista) {
                JsonObject temp = Json.createObjectBuilder()
                        .add("idestadocivil", obj.getIdestadocivil())
                        .add("abreviatura", obj.getAbreviatura())
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
