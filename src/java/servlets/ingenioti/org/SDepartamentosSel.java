package servlets.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import negocio.ingenioti.org.NDepartamentos;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.ODepartamentos;

@WebServlet(name = "SDepartamentosSel", urlPatterns = {"/SDepartamentosSel"})
public class SDepartamentosSel extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SDepartamentosSel.class.getName());

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
            String mensaje;
            byte tipoMensaje;

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");
            String tipoConsulta = request.getParameter("tc");
            byte sTipoConsulta = 0;
            try {
                sTipoConsulta = Byte.parseByte(tipoConsulta);
            } catch (NumberFormatException nfe) {}

            String siddepartamento = request.getParameter("id");
            short iiddepartamento = 0;
            if (sTipoConsulta == SUtilidades.CONSULTA_ID) {
                try {
                    iiddepartamento = Short.parseShort(siddepartamento);
                } catch (NumberFormatException nfe) {
                    SUtilidades.generaLogServer(LOG, Level.WARNING, "Error al convertir: iddepartamento en Short: "+siddepartamento);
                }
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

            NDepartamentos nDepartamentos = new NDepartamentos();
            ODepartamentos oDepartamentos = new ODepartamentos(iiddepartamento);

            // Preparación de la lista de objetos a retornar
            ArrayList<ODepartamentos> lista = new ArrayList<ODepartamentos>();
            int totalPaginas;
            int totalRegistros = nDepartamentos.getCantidadRegistros("departamentos");
            
            if (totalRegistros > 0) {
                totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
            } else {
                totalPaginas = 0;
            }
            if (iPagina > totalPaginas) {
                iPagina = totalPaginas;
            }

            try {
                lista = nDepartamentos.consultar(sTipoConsulta, oDepartamentos, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                tipoMensaje = SUtilidades.TIPO_MSG_CORRECTO;
                mensaje = "Cargue de consulta realizado correctamente.";
            } catch (ExcepcionGeneral eg) {
                tipoMensaje = SUtilidades.TIPO_MSG_ADVERTENCIA;
                mensaje = eg.getMessage();
            }

            // Se construye la lista
            StringBuilder listaJson = new StringBuilder();
            int i = 0;
            for (ODepartamentos obj : lista) {
                i++;
                listaJson.append(obj.toJson());
                if(i < lista.size()){
                    listaJson.append(",");
                }
            }

            String respuesta = SUtilidades.generaJson(tipoMensaje, mensaje, totalRegistros, totalPaginas, listaJson.toString());
            PrintWriter out = response.getWriter();
            try {
                out.println(respuesta);
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
