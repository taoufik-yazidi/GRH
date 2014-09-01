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
            short tipoMensaje;
            String mensajeLista;
            short tipoMensajeLista;
            // Objetos Json de respuesta
            JsonObject modelo, jsLista;
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            NHojadevida nHojadevida = new NHojadevida();
            OHojadevida oHojadevida = new OHojadevida();

            // Para Insertar, Modificar o Borrar
            if (sAccion == SUtilidades.ACCIONINSERTAR
                    || sAccion == SUtilidades.ACCIONACTUALIZAR
                    || sAccion == SUtilidades.ACCIONBORRAR) {
                // Se capturan los datos enviados por la capa vista
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

                // Validación de campos vacios excepto para borrar
                if (sAccion != SUtilidades.ACCIONBORRAR && (sidtipodedocumento == null || sidtipodedocumento.length() == 0
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
                    tipoMensaje = SUtilidades.TIPO_MSG_ERROR;
                    mensaje = "Todos los campos son obligatorios";
                    modelo = Json.createObjectBuilder()
                            .add("tipoMensaje", tipoMensaje)
                            .add("mensaje", mensaje)
                            .build();
                    jsEscritor.writeObject(modelo);
                } else { // Si los datos están completos
                    int iidhojadevida = 0;
                    if (sAccion != SUtilidades.ACCIONINSERTAR) {
                        try {
                            iidhojadevida = Integer.parseInt(sidhojadevida);
                        } catch (NumberFormatException nfe) {
                            LOG.log(Level.WARNING, "Error al convertir: idhojadevida en int en el servlet SHojadevida");
                        }
                    }
                    
                    // Variables para crear el objeto Hoja de Vida
                    short iidtipodedocumento = 0;
                    short iidgenero = 0;
                    short ilugarnacimiento = 0;
                    short ilugarexpediciond = 0;
                    short ilugarresidencia = 0;
                    Calendar cfechanacimiento = new GregorianCalendar();
                    Calendar cfechaexpediciond = new GregorianCalendar();
                    short iidestadocivil = 0;
                    boolean bdisponibilidadviaje = sdisponibilidadviaje != null;

                    if (sAccion != SUtilidades.ACCIONBORRAR) {
                        try {
                            iidtipodedocumento = Short.parseShort(sidtipodedocumento);
                            iidgenero = Short.parseShort(sidgenero);
                            ilugarnacimiento = Short.parseShort(slugarnacimiento);
                            ilugarexpediciond = Short.parseShort(slugarexpediciond);
                            ilugarresidencia = Short.parseShort(slugarresidencia);
                            cfechanacimiento.setTime(sdf.parse(sfechanacimiento));
                            cfechaexpediciond.setTime(sdf.parse(sfechaexpediciond));
                            iidestadocivil = Short.parseShort(sidestadocivil);
                        } catch (NumberFormatException nfe) {
                            LOG.log(Level.WARNING, "Error al convertir un tipo de dato en SHojadevida. %s",nfe.getMessage());
                        } catch (ParseException pe){
                            LOG.log(Level.WARNING, "Error al convertir un tipo de dato en SHojadevida. %s",pe.getMessage());
                        } catch (NullPointerException npe){
                            LOG.log(Level.WARNING, "Error en un tipo de dato nulo en SHojadevida. %s",npe.getMessage());
                        }
                    }
                    
                    // Para realizar cualquier accion: insertar, modificar o borrar
                    oHojadevida = new OHojadevida(iidhojadevida, iidtipodedocumento,
                            snumerodocumento, sprimerapellido,
                            ssegundoapellido, snombres, iidgenero,
                            slibretamilitar, sdistritolm, ilugarnacimiento,
                            ilugarexpediciond, ilugarresidencia, cfechanacimiento,
                            cfechaexpediciond, iidestadocivil, bdisponibilidadviaje,
                            sdireccion, stelefono, scorreo, sfoto);

                    int respuesta;
                    tipoMensaje = SUtilidades.TIPO_MSG_ADVERTENCIA; // Inicia en warning porque es la mayor cantidad de opciones
                    try {
                        respuesta = nHojadevida.ejecutarSQL(sAccion, oHojadevida, credencial);
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
            
            if (sAccion == SUtilidades.ACCIONCONSULTAR) {
                String tipoConsulta = request.getParameter("tipoConsulta");
                short sTipoConsulta;
                try {
                    sTipoConsulta = Short.parseShort(tipoConsulta);
                } catch (NumberFormatException nfe) {
                    sTipoConsulta = 0;
                }

                // Preparación de la lista de objetos a retornar
                ArrayList<OHojadevida> lista = new ArrayList<OHojadevida>();
                int totalPaginas = 0;
                int totalRegistros = 0;

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
                    lista = nHojadevida.consultar(sAccion, sTipoConsulta, oHojadevida, credencial, iPagina, iLimite, iColumnaOrden, tipoOrden);
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
