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
import negocio.ingenioti.org.NHojadevida;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.OHojadevida;

@WebServlet(name = "SHojadevidaSel", urlPatterns = {"/SHojadevidaSel"})
public class SHojadevidaSel extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SHojadevidaSel.class.getName());

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
            byte tipoMensajeLista;
            // Objetos Json de respuesta
            JsonObject jsLista;
            JsonArrayBuilder jsArray;
            StringWriter sEscritor = new StringWriter();
            JsonWriter jsEscritor = Json.createWriter(sEscritor);

            OCredencial credencial = (OCredencial) sesion.getAttribute("credencial");

            String tipoConsulta = request.getParameter("tipoConsulta");
            short sTipoConsulta = 0;
            try {
                sTipoConsulta = Short.parseShort(tipoConsulta);
            } catch (NumberFormatException nfe) {
            }

            String sidhojadevida = request.getParameter("idhojadevida");
            int iidhojadevida = 0;
            if (sTipoConsulta == SUtilidades.CONSULTA_ID) {
                try {
                    iidhojadevida = Integer.parseInt(sidhojadevida);
                } catch (NumberFormatException nfe) {
                    SUtilidades.generaLogServer(LOG, Level.SEVERE, "Error al convertir: idhojadevida %s", sidhojadevida);
                }
            }

            NHojadevida nHojadevida = new NHojadevida();
            OHojadevida oHojadevida = new OHojadevida(iidhojadevida);

            // Preparación de la lista de objetos a retornar
            ArrayList<OHojadevida> lista = new ArrayList<OHojadevida>();
            int totalPaginas;
            int totalRegistros;

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
            totalRegistros = nHojadevida.getCantidadRegistros("hojadevida");
            if (totalRegistros > 0) {
                totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
            } else {
                totalPaginas = 0;
            }
            if (iPagina > totalPaginas) {
                iPagina = totalPaginas;
            }
            try {
                lista = nHojadevida.consultar(sTipoConsulta, oHojadevida, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                tipoMensajeLista = SUtilidades.TIPO_MSG_CORRECTO;
                mensajeLista = "Cargue de consulta realizado correctamente.";
            } catch (ExcepcionGeneral eg) {
                tipoMensajeLista = SUtilidades.TIPO_MSG_ERROR;
                mensajeLista = eg.getMessage();
            }
            jsArray = Json.createArrayBuilder();
            for (OHojadevida obj : lista) {
                JsonObject temp = Json.createObjectBuilder()
                        .add("idhojadevida", obj.getIdhojadevida())
                        .add("idtipodocumento", obj.getTipodedocumento().getIdtipodedocumento())
                        .add("tipodedocumento", obj.getTipodedocumento().getTipodedocumento())
                        .add("numerodocumento", obj.getNumerodocumento())
                        .add("primerapellido", obj.getPrimerapellido())
                        .add("segundoapellido", obj.getSegundoapellido())
                        .add("nombres", obj.getNombres())
                        .add("idgenero", obj.getGenero().getIdgenero())
                        .add("genero", obj.getGenero().getGenero())
                        .add("libretamilitar", obj.getLibretamilitar())
                        .add("distritolm", obj.getDistritolm())
                        .add("idnacimiento", obj.getLugarnacimiento().getMunicipio().getIdmunicipio())
                        .add("iddeptonacimiento", obj.getLugarnacimiento().getIddepartamento())
                        .add("deptonacimiento", obj.getLugarnacimiento().getNombre())
                        .add("lugarnacimiento", obj.getLugarnacimiento().getMunicipio().getNombre())
                        .add("idlugarexpedicion", obj.getLugarexpedicion().getMunicipio().getIdmunicipio())
                        .add("iddeptoexpedicion", obj.getLugarexpedicion().getIddepartamento())
                        .add("deptoexpedicion", obj.getLugarexpedicion().getNombre())
                        .add("lugarexpedicion", obj.getLugarexpedicion().getMunicipio().getNombre())
                        .add("idlugarresidencia", obj.getLugarresidencia().getMunicipio().getIdmunicipio())
                        .add("iddeptoresidencia", obj.getLugarresidencia().getIddepartamento())
                        .add("deptoresidencia", obj.getLugarresidencia().getNombre())
                        .add("lugarresidencia", obj.getLugarresidencia().getMunicipio().getNombre())
                        .add("fechanacimiento", obj.getFechanacimientoSDF())
                        .add("fechaexpediciond", obj.getFechaexpedicionSDF())
                        .add("idestadocivil", obj.getEstadocivil().getIdestadocivil())
                        .add("estadocivil", obj.getEstadocivil().getNombre())
                        .add("disponibilidadviaje", obj.isDisponibilidadviaje())
                        .add("direccion", obj.getDireccion())
                        .add("telefono", obj.getTelefono())
                        .add("correo", obj.getCorreo())
                        .add("foto", obj.getFoto())
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
