package servlets.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

/**
 *
 * @author Alexys
 */
@WebServlet(name = "SHojadevida", urlPatterns = {"/SHojadevida"})
public class SHojadevida extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SHojadevida.class.getName());

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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sidhojadevida = request.getParameter("idhojadevida");
            String sidtipodedocumento = request.getParameter("idtipodedocumento");
            String snumerodocumento = request.getParameter("numerodocumento");
            String sprimerapellido = request.getParameter("primerapellido");
            String ssegundoapellido = request.getParameter("segundoapellido");
            String snombres = request.getParameter("nombres");
            String sidgenero = request.getParameter("idgenero");
            String slibretamilitar = request.getParameter("libretamilitar");
            String sdistritolm = request.getParameter("distritolm");
            String slugarnacimiento = request.getParameter("idlugarnacimiento");
            String slugarexpediciond = request.getParameter("idlugarexpedicion");
            String slugarresidencia = request.getParameter("idlugarresidencia");
            String sfechanacimiento = request.getParameter("fechanacimiento");
            String sfechaexpediciond = request.getParameter("fechaexpediciond");
            String sidestadocivil = request.getParameter("idestadocivil");
            String sdisponibilidadviaje = request.getParameter("disponibilidadviaje");
            String sdireccion = request.getParameter("direccion");
            String stelefono = request.getParameter("telefono");
            String scorreo = request.getParameter("correo");
            String sfoto = request.getParameter("foto");

            // Preparación de la lista de objetos a retornar
            ArrayList<OHojadevida> lista = new ArrayList<OHojadevida>();
            int totalPaginas = 0;
            int totalRegistros = 0;

            NHojadevida nHojadevida = new NHojadevida();
            OHojadevida oHojadevida = new OHojadevida();
            
            // Para realizar la acción de la 1 a la 3
            if (sAccion.equals(SUtilidades.INSERTAR) || sAccion.equals(SUtilidades.MODIFICAR)
                    || sAccion.equals(SUtilidades.BORRAR)) {
                // Validación de campos vacios
                if ( !sAccion.equals(SUtilidades.BORRAR) && (sidtipodedocumento == null || sidtipodedocumento.length() == 0 
                        || snumerodocumento == null || snumerodocumento.length() == 0 || sprimerapellido == null 
                        || sprimerapellido.length() == 0 || ssegundoapellido == null || ssegundoapellido.length() == 0 
                        || snombres == null || snombres.length() == 0 || sidgenero == null || sidgenero.length() == 0 
                        || slibretamilitar == null || slibretamilitar.length() == 0 || sdistritolm == null 
                        || sdistritolm.length() == 0 || slugarnacimiento == null || slugarnacimiento.length() == 0 
                        || slugarexpediciond == null || slugarexpediciond.length() == 0 || slugarresidencia == null 
                        || slugarresidencia.length() == 0 || sfechanacimiento == null || sfechanacimiento.length() == 0 
                        || sfechaexpediciond == null || sfechaexpediciond.length() == 0 || sidestadocivil == null 
                        || sidestadocivil.length() == 0 || sdireccion == null || sdireccion.length() == 0 
                        || stelefono == null || stelefono.length() == 0 || scorreo == null || scorreo.length() == 0)) {
                    tipoMensaje = 4;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else {
                    int iidhojadevida = 0;
                    if(!sAccion.equals(SUtilidades.INSERTAR)){
                        try {
                            iidhojadevida = Integer.parseInt(sidhojadevida);
                        } catch (NumberFormatException nfe) {
                            System.err.println("Error al convertir: idhojadevida en int en el servlet SHojadevida");
                        }
                    }
                    short iidtipodedocumento = 0;
                    try {
                        iidtipodedocumento = Short.parseShort(sidtipodedocumento);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: idtipodedocumento en Short  en el servlet SHojadevida");
                    }
                    short iidgenero = 0;
                    try {
                        iidgenero = Short.parseShort(sidgenero);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: idgenero en Short  en el servlet SHojadevida");
                    }
                    short ilugarnacimiento = 0;
                    try {
                        ilugarnacimiento = Short.parseShort(slugarnacimiento);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: lugarnacimiento en Short  en el servlet SHojadevida");
                    }
                    short ilugarexpediciond = 0;
                    try {
                        ilugarexpediciond = Short.parseShort(slugarexpediciond);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: lugarexpediciond en Short  en el servlet SHojadevida");
                    }
                    short ilugarresidencia = 0;
                    try {
                        ilugarresidencia = Short.parseShort(slugarresidencia);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: lugarresidencia en Short  en el servlet SHojadevida");
                    }
                    Calendar cfechanacimiento = new GregorianCalendar();
                    try {
                        cfechanacimiento.setTime(sdf.parse(sfechanacimiento));
                    } catch (ParseException pe) {
                        LOG.log(Level.INFO, "Error al convertir: sfechanacimiento en fecha en el Servlet SHojadevida");
                    } catch (NullPointerException npe){
                        LOG.log(Level.INFO, "Error al convertir: sfechanacimiento en fecha en el Servlet SHojadevida");
                    }
                    Calendar cfechaexpediciond = new GregorianCalendar();
                    try {
                        cfechaexpediciond.setTime(sdf.parse(sfechaexpediciond));
                    } catch (ParseException pe) {
                        LOG.log(Level.INFO, "Error al convertir: sfechanacimiento en fecha en el Servlet SHojadevida");
                    } catch (NullPointerException npe){
                        LOG.log(Level.INFO, "Error al convertir: sfechanacimiento en fecha en el Servlet SHojadevida");
                    }
                    short iidestadocivil = 0;
                    try {
                        iidestadocivil = Short.parseShort(sidestadocivil);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Error al convertir: idestadocivil en Short  en el servlet SHojadevida");
                    }
                    boolean bdisponibilidadviaje = sdisponibilidadviaje != null;

                    // Para realizar cualquier accion: insertar, modificar o borrar
                    oHojadevida = new OHojadevida(iidhojadevida, iidtipodedocumento, snumerodocumento, sprimerapellido, ssegundoapellido, snombres, iidgenero, slibretamilitar, sdistritolm, ilugarnacimiento, ilugarexpediciond, ilugarresidencia, cfechanacimiento, cfechaexpediciond, iidestadocivil, bdisponibilidadviaje, sdireccion, stelefono, scorreo, sfoto);

                    int respuesta = 0;
                    try {
                        respuesta = nHojadevida.ejecutarSQL(sAccion, oHojadevida, credencial);
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
                    totalRegistros = nHojadevida.getCantidadRegistros("hojadevida");
                    if (totalRegistros > 0) {
                        totalPaginas = (int) Math.ceil((double) totalRegistros / (double) iLimite);
                    } else {
                        totalPaginas = 0;
                    }
                    if (iPagina > totalPaginas) {
                        iPagina = totalPaginas;
                    }
                    lista = nHojadevida.consultar((short) 4, sTipoConsulta, oHojadevida, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
                    tipoMensajeLista = 1;
                    mensajeLista = "Cargue de consulta realizado correctamente.";
                } catch (ExcepcionGeneral eg) {
                    tipoMensajeLista = 3;
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
