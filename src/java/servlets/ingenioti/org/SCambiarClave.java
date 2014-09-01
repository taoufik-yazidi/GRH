package servlets.ingenioti.org;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import objetos.ingenioti.org.OCredencial;
import negocio.ingenioti.org.NUsuarios;

@WebServlet(name = "SCambiarClave", urlPatterns = {"/SCambiarClave"})
public class SCambiarClave extends HttpServlet {

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

        HttpSession sesion = request.getSession();

        if (SUtilidades.autenticado(sesion)) {
            String mensaje = "";
            String anterior = request.getParameter("txtAnterior");
            String nueva = request.getParameter("txtNueva");
            String confirma = request.getParameter("txtConfirma");
            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");

            if (nueva.equals(confirma)) {
                NUsuarios nusuarios = new NUsuarios();
                Short resultado = nusuarios.cambiarClave(credencial.getUsuario().getId(), anterior, nueva);
                switch (resultado) {
                    case 0:
                        mensaje = "No se pudo realizar la acci&oacute;n de cambio de clave.";
                        break;
                    case 1:
                        mensaje = "Proceso realizado correctamente.";
                        break;
                    case 2:
                        mensaje = "Error de actualizaci&oacute;n en la BD.";
                        break;
                    case 3:
                        mensaje = "Clave anterior errada";
                }
            } else {
                mensaje = "Clave de confirmaci&oacute;n no coincide.";
            }

            response.setContentType("text/html;charset=UTF-8");
            request.setAttribute("mensaje", mensaje);
            SUtilidades.irAPagina("/cambiarclave.jsp", request, response, request.getServletContext());
        } else {
            SUtilidades.irAPagina("/index.jsp", request, response, null);
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
