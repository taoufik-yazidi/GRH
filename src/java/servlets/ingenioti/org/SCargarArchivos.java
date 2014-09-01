package servlets.ingenioti.org;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import negocio.ingenioti.org.NUtilidades;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Alexys
 */
@WebServlet(name = "SCargarArchivos", urlPatterns = {"/SCargarArchivos"})
public class SCargarArchivos extends HttpServlet {

    private final String directorioImagenes = NUtilidades.getDirectorioImagenes();
    private static final Logger LOG = Logger.getLogger(SCargarArchivos.class.getName());

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

        JsonObject jsLista;
        StringWriter sEscritor = new StringWriter();
        JsonWriter jsEscritor = Json.createWriter(sEscritor);
        PrintWriter out = response.getWriter();

        try {
            boolean ismultipart = ServletFileUpload.isMultipartContent(request);
            if (!ismultipart) {

            } else {
                FileItemFactory fabrica = new DiskFileItemFactory();
                ServletFileUpload carga = new ServletFileUpload(fabrica);
                List items = null;
                try {
                    items = carga.parseRequest(request);
                } catch (Exception e) {
                    LOG.log(Level.WARNING, e.getMessage());
                }
                if (items != null) {
                    Iterator iterador = items.iterator();
                    while (iterador.hasNext()) {
                        FileItem archivo = (FileItem) iterador.next();
                        if (archivo.isFormField()) {

                        } else {
                            String nombreArchivo = archivo.getName();
                            if (nombreArchivo == null || nombreArchivo.length() == 0) {
                                continue;
                            }
                            String archivoReal = FilenameUtils.getName(nombreArchivo);
                            File archivoCargado = checkExist(archivoReal);
                            archivo.write(archivoCargado);
                        }
                    }
                    jsLista = Json.createObjectBuilder()
                            .add("mensaje", "Cargue de archivos OK")
                            .build();
                    jsEscritor.writeObject(jsLista);
                    jsEscritor.close();
                    String jsObjeto = sEscritor.toString();
                    out.println(jsObjeto);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        } finally {
            out.close();
        }
    }

    private File checkExist(String archivo) {
        File f = new File(directorioImagenes + "/" + archivo);
        if (f.exists()) {
            StringBuilder sb = new StringBuilder(archivo);
            sb.insert(sb.lastIndexOf("."), "-" + new Date().getTime());
            f = new File(directorioImagenes + "/" + sb.toString());
        }
        return f;
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
