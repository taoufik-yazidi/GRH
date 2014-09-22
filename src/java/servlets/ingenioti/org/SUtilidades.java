package servlets.ingenioti.org;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import negocio.ingenioti.org.NUtilidades;

@WebServlet(name = "SUtilidades", urlPatterns = {"/SUtilidades"})
public final class SUtilidades extends HttpServlet {

    protected final static byte ACCIONINSERTAR = NUtilidades.ACCIONINSERTAR;
    protected final static byte ACCIONACTUALIZAR = NUtilidades.ACCIONACTUALIZAR;
    protected final static byte ACCIONBORRAR = NUtilidades.ACCIONBORRAR;
    protected final static byte ACCIONCONSULTAR = NUtilidades.ACCIONCONSULTAR;
    protected static final byte MSG_BD_ERROR = NUtilidades.MSG_BD_ERROR;
    protected static final byte MSG_BD_ERROR_FK = NUtilidades.MSG_BD_ERROR_FK;
    protected static final byte MSG_BD_ERROR_UK = NUtilidades.MSG_BD_ERROR_UK;
    protected static final byte CONSULTA_TODOS = NUtilidades.CONSULTA_TODOS;
    protected static final byte CONSULTA_ID = NUtilidades.CONSULTA_ID;
    protected final static byte TIPO_MSG_CORRECTO = NUtilidades.TIPO_MSG_CORRECTO;
    protected final static byte TIPO_MSG_INFORMATIVO = NUtilidades.TIPO_MSG_INFORMATIVO;
    protected final static byte TIPO_MSG_ADVERTENCIA = NUtilidades.TIPO_MSG_ADVERTENCIA;
    protected final static byte TIPO_MSG_ERROR = NUtilidades.TIPO_MSG_ERROR;
   
    /**
     * Metodo irAPagina, utilizado para redireccionar una pagina
     * @param direccion Direccion a la que desea ir (absoluta o relativa)
     * @param solicitud Tipo HttpServletRequest
     * @param respuesta Tipo HttpServletResponse
     * @param contexto Tipo ServletContext
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected static void irAPagina(String direccion, HttpServletRequest solicitud,
                            HttpServletResponse respuesta, ServletContext contexto)
            throws ServletException, IOException, IllegalStateException{
        direccion = respuesta.encodeURL(direccion);
        RequestDispatcher despachador = contexto.getRequestDispatcher(direccion);
        despachador.forward(solicitud, respuesta);
    }
    
    /**
     * Metodo autenticado, se debe utilizar en todos los servlets, (a excepcion del de autenticar),
     * este metodo permite saber si el usuario esta o no autenticado.
     * @param sesion Sesión activa de la petición
     * @return True si esta autenticado False si no lo esta y lo reenvia a la pagina de autenticacion
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected static boolean autenticado(HttpSession sesion)
            throws ServletException, IOException{
        if (sesion == null || sesion.getAttribute("credencial") == null){ /* No autenticado */
          return (false);
        }
        return (true);
    }
    
    /**
     * Método tienePermiso, para conocer si el usuario tiene permiso de realizar la acción
     * @param tipoDeAccion Es un byte que informa el tipo de acción a realizar
     * @param perfil Es el perfil que tiene el usuario de la sesión actual
     * @param objeto Es el nombre del objeto al que se le realizará la acción
     * @return True si tiene permiso, False si no lo tiene
     */
    protected static boolean tienePermiso(byte tipoDeAccion, Short perfil, String objeto){
        return NUtilidades.tienePermiso(tipoDeAccion, perfil, objeto);
    }
    
    /**
     * Método generaLogServer permite generar el log del servidor si está configurado en el web.xml
     * @param logger
     * @param level
     * @param mensaje Es el mensaje enviado que se escribe en el servlet
     */
    protected static void generaLogServer(Logger logger, Level level, String mensaje){
        NUtilidades.generaLogServer(logger,level,mensaje);
    }
    
    protected static String generaJson(byte tipo, String mensaje) {
        return NUtilidades.generaJson(tipo, mensaje);
    }
    
    protected static String generaJson(
            byte tipo, String mensaje, int registros, int paginas, String lista) {
        return NUtilidades.generaJson(tipo, mensaje, registros, paginas, lista);
    }
}
