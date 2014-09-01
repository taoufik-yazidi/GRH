package negocio.ingenioti.org;

import org.postgresql.ds.PGPoolingDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;


/**
 * NUtilidades.java
 * Contiene las utilidades generales de la aplicacion
 * Create 2013/02/25
 * 
 * @author Alexys
 * @version 1.0
 */
public final class NUtilidades {
    private static PGPoolingDataSource piscina = null;
    private static ServletContext contextoApp;
    private static boolean creado = false;
    
    private static String DIRECTORIO_IMAGENES;
    
    public static final byte MSG_BD_ERROR = 0;
    public static final byte MSG_BD_ERROR_FK = -1;
    public static final byte MSG_BD_ERROR_UK = -2;
    public static final byte ACCIONINSERTAR = 1;
    public static final byte ACCIONACTUALIZAR = 2;
    public static final byte ACCIONBORRAR = 3;
    public static final byte ACCIONCONSULTAR = 4;
    public static final byte CONSULTA_TODOS = 0;
    public static final byte CONSULTA_ID = 1;
    
    public static boolean contextoCreado(){
        return creado;
    }
    
    public static void creaPiscina(ServletContext contexto){
        if(!creado){
            contextoApp = contexto;
            setPiscina();
            DIRECTORIO_IMAGENES = contextoApp.getInitParameter("directorioImagenes");
        }
    }

    public static String getDirectorioImagenes(){
        return DIRECTORIO_IMAGENES;
    }
    
    private static void setPiscina(){
        String servidor = contextoApp.getInitParameter("servidor");
        int puertobd = Integer.parseInt(contextoApp.getInitParameter("puertobd"));
        String basededa = contextoApp.getInitParameter("basededatos");
        String usuariob = contextoApp.getInitParameter("usuariobd");
        String clavebda = contextoApp.getInitParameter("clavebd");
        int conexionesiniciales = Integer.parseInt(contextoApp.getInitParameter("conexionesiniciales"));
        int conexionesmaximas = Integer.parseInt(contextoApp.getInitParameter("conexionesmaximas"));
        piscina = new PGPoolingDataSource();
        piscina.setDataSourceName("Conexion a la Base Ingenio T.I.");
        piscina.setServerName(servidor);
        piscina.setPortNumber(puertobd);
        piscina.setDatabaseName(basededa);
        piscina.setUser(usuariob);
        piscina.setPassword(clavebda);
        piscina.setInitialConnections(conexionesiniciales);
        piscina.setMaxConnections(conexionesmaximas);
        creado = true;
    }
    
    public static Connection getConexion() throws SQLException{
        Connection conexion = null;
        if(piscina==null){
            setPiscina();
            if(piscina==null){
                System.err.println("Error en NUtilidades.java No fue posible cargar la piscina de conexiones");
            } else {
                conexion = piscina.getConnection();
            }
        } else {
            conexion = piscina.getConnection();
        }
        return conexion;
    }
    
    public static boolean tienePermiso(byte tipoDeAccion, Short perfil, String objeto){
        NAutenticacion autenticacion = new NAutenticacion();
        return autenticacion.tienePermiso(tipoDeAccion, perfil, objeto);
    }
    
    private static boolean isLogServerActivo(){
        if(creado){
            if(contextoApp.getInitParameter("activar_log_server").equals("1")){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Método generaLogServer permite generar el log del servidor si está configurado en el web.xml
     * @param logger
     * @param level
     * @param mensaje Es el mensaje enviado que se escribe en el servlet
     * @param msgexcepcion  Es el mensaje de excepción generado por el catch
     */
    public static void generaLogServer(Logger logger, Level level, String mensaje, Object msgexcepcion){
        if(isLogServerActivo()){
            logger.log(level, mensaje, msgexcepcion);
        }
    }
}