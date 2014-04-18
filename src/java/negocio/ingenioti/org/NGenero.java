package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import java.util.ArrayList;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.OGenero;

public class NGenero extends NGeneralidades {

    private final String MIOBJETO = "PARGEN";

    public int ejecutarSQL(short ta, OGenero obj, OCredencial cre) throws ExcepcionGeneral {
        int respuesta = 0;
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                switch (ta) {
                    case 1: // Insertar
                        conectar("select * from fn_genero_ins(?, ?)");
                        sentenciaProcedimiento.setString(1, obj.getSigla());
                        sentenciaProcedimiento.setString(2, obj.getGenero());
                        break;
                    case 2: //ACTUALIZAR
                        conectar("select * from fn_genero_upd(?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdgenero());
                        sentenciaProcedimiento.setString(2, obj.getSigla());
                        sentenciaProcedimiento.setString(3, obj.getGenero());
                        break;
                    case 3: //BORRAR
                        conectar("select * from fn_genero_del(?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdgenero());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getInt(1);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NGenero insertar: " + sql.getMessage());
            } finally {
                try {
                    cerrarConexion();
                } catch (SQLException sqle) {
                }
            }
        } else {
            throw new ExcepcionGeneral("Usted no tiene permiso para realizar esta acción.");
        }
        return respuesta;
    }

    public ArrayList<OGenero> consultar(short ta, short tc, OGenero obj, OCredencial cre, int pagina, int limite, int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<OGenero> lista = new ArrayList<OGenero>();
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                conectar("select * from fn_genero_sel(?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setShort(2, obj.getIdgenero());
                sentenciaProcedimiento.setInt(3, pagina);
                sentenciaProcedimiento.setInt(4, limite);
                sentenciaProcedimiento.setInt(5, columnaOrden);
                sentenciaProcedimiento.setString(6, tipoOrden);
                getResultadosProcedimiento();
                while (resultados.next()) {
                    OGenero temp = new OGenero();
                    temp.setIdgenero(resultados.getShort(1));
                    temp.setSigla(resultados.getString(2));
                    temp.setGenero(resultados.getString(3));
                    lista.add(temp);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NGenero consultar: " + sql.getMessage());
            } finally {
                try {
                    cerrarConexion();
                } catch (SQLException sqle) {
                }
            }
        } else {
            throw new ExcepcionGeneral("No está autorizado para consultar la lista.");
        }
        return lista;
    }
}
