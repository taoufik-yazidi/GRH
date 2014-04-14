package negocio.ingenioti.org;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Alexys
 * @version 1.0 Creado el 13/04/2013
 */
public class NGeneralidades {

    private String consulta = null;
    private Connection conexion = null;
    protected Statement sentencia = null;
    protected PreparedStatement sentenciaPreparada = null;
    protected CallableStatement sentenciaProcedimiento = null;
    protected ResultSet resultados = null;
    private final String NOAUTENTICADO = "No está autorizado para realizar esta acción.";

    protected String getNoAutenticado() {
        return this.NOAUTENTICADO;
    }

    protected void conectar(String s) throws SQLException {
        this.consulta = s;
        if (this.conexion == null || this.conexion.isClosed()) {
            this.conexion = NUtilidades.getConexion();
        }
        this.sentenciaProcedimiento = this.conexion.prepareCall(consulta);
    }

    protected void cerrarConexion() throws SQLException {
        if (this.resultados != null) {
            this.resultados.close();
        }
        if (this.sentenciaProcedimiento != null) {
            this.sentenciaProcedimiento.close();
        }
        if (this.sentenciaPreparada != null) {
            this.sentenciaPreparada.close();
        }
        if (this.sentencia != null) {
            this.sentencia.close();
        }
        if (this.conexion != null) {
            this.conexion.close();
        }
    }

    protected void setSentencia() throws SQLException {
        this.sentencia = this.conexion.createStatement();
    }

    protected void getResultadosSentencia() throws SQLException {
        this.resultados = this.sentencia.executeQuery(consulta);
    }

    protected void setSentenciaPreparada() throws SQLException {
        this.sentenciaPreparada = this.conexion.prepareStatement(consulta);
    }

    protected void getResultadosPreparada() throws SQLException {
        this.resultados = this.sentenciaPreparada.executeQuery();
    }

    protected void getResultadosProcedimiento() throws SQLException {
        this.resultados = this.sentenciaProcedimiento.executeQuery();
    }

    public int getCantidadRegistros(String tabla) {
        int respuesta = 0;
        try {
            conectar("select * from fn_contar_registros(?)");
            sentenciaProcedimiento.setString(1, tabla);
            getResultadosProcedimiento();
            if (this.resultados.next()) {
                respuesta = this.resultados.getInt(1);
            }
        } catch (SQLException sqle) {
            System.err.println("Error en NGeneralidades, no se puede consultar la cantidad de registros de la tabla: "+ tabla + ", " + sqle.getMessage());
        } finally {
            try {
                cerrarConexion();
            } catch (SQLException sqle) {
            }
        }
        return respuesta;
    }
}
