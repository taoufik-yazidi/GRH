package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import java.util.ArrayList;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.OEstadoCivil;

public class NEstadocivil extends NGeneralidades {

    private final String MIOBJETO = "PARESC";

    public int ejecutarSQL(short ta, OEstadoCivil obj, OCredencial cre) throws ExcepcionGeneral {
        int respuesta = 0;
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                switch (ta) {
                    case 1: // Insertar
                        conectar("select * from fn_estadocivil_ins(?, ?)");
                        sentenciaProcedimiento.setString(1, obj.getAbreviatura());
                        sentenciaProcedimiento.setString(2, obj.getNombre());
                        break;
                    case 2: //ACTUALIZAR
                        conectar("select * from fn_estadocivil_upd(?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdestadocivil());
                        sentenciaProcedimiento.setString(2, obj.getAbreviatura());
                        sentenciaProcedimiento.setString(3, obj.getNombre());
                        break;
                    case 3: //BORRAR
                        conectar("select * from fn_estadocivil_del(?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdestadocivil());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getInt(1);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NEstadocivil insertar: " + sql.getMessage());
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

    public ArrayList<OEstadoCivil> consultar(short ta, short tc, OEstadoCivil obj, OCredencial cre, int pagina, int limite, int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<OEstadoCivil> lista = new ArrayList<OEstadoCivil>();
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                conectar("select * from fn_estadocivil_sel(?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setShort(2, obj.getIdestadocivil());
                sentenciaProcedimiento.setInt(3, pagina);
                sentenciaProcedimiento.setInt(4, limite);
                sentenciaProcedimiento.setInt(5, columnaOrden);
                sentenciaProcedimiento.setString(6, tipoOrden);
                getResultadosProcedimiento();
                while (resultados.next()) {
                    OEstadoCivil temp = new OEstadoCivil();
                    temp.setIdestadocivil(resultados.getShort(1));
                    temp.setAbreviatura(resultados.getString(2));
                    temp.setNombre(resultados.getString(3));
                    lista.add(temp);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NEstadocivil consultar: " + sql.getMessage());
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
