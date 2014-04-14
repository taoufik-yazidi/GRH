package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import java.util.ArrayList;
import objetos.ingenioti.org.OCliente;
import objetos.ingenioti.org.OCredencial;

public class NClientes extends NGeneralidades {

    private final String MIOBJETO = "PARCLI";

    public int ejecutarSQL(short ta, OCliente obj, OCredencial cre) throws ExcepcionGeneral {
        int respuesta = 0;
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                switch (ta) {
                    case 1: // Insertar
                        conectar("select * from fn_clientes_ins(?, ?, ?, ?, ?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getTipodocumento());
                        sentenciaProcedimiento.setString(2, obj.getNumerodocumento());
                        sentenciaProcedimiento.setString(3, obj.getDireccion());
                        sentenciaProcedimiento.setString(4, obj.getTelefono());
                        sentenciaProcedimiento.setString(5, obj.getCorreoelectronico());
                        sentenciaProcedimiento.setString(6, obj.getResponsable());
                        sentenciaProcedimiento.setString(7, obj.getConsejo());
                        break;
                    case 2: //ACTUALIZAR
                        conectar("select * from fn_clientes_upd(?, ?, ?, ?, ?, ?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdcliente());
                        sentenciaProcedimiento.setShort(2, obj.getTipodocumento());
                        sentenciaProcedimiento.setString(3, obj.getNumerodocumento());
                        sentenciaProcedimiento.setString(4, obj.getDireccion());
                        sentenciaProcedimiento.setString(5, obj.getTelefono());
                        sentenciaProcedimiento.setString(6, obj.getCorreoelectronico());
                        sentenciaProcedimiento.setString(7, obj.getResponsable());
                        sentenciaProcedimiento.setString(8, obj.getConsejo());
                        break;
                    case 3: //BORRAR
                        conectar("select * from fn_clientes_del(?)");
                        sentenciaProcedimiento.setShort(1, obj.getIdcliente());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                System.err.println(sentenciaProcedimiento.toString());
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getInt(1);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NClientes insertar: " + sql.getMessage());
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

    public ArrayList<OCliente> consultar(short ta, short tc, OCliente obj, OCredencial cre, int pagina, int limite, int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<OCliente> lista = new ArrayList<OCliente>();
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                conectar("select * from fn_clientes_sel(?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setShort(2, obj.getIdcliente());
                sentenciaProcedimiento.setInt(3, pagina);
                sentenciaProcedimiento.setInt(4, limite);
                sentenciaProcedimiento.setInt(5, columnaOrden);
                sentenciaProcedimiento.setString(6, tipoOrden);
                getResultadosProcedimiento();
                while (resultados.next()) {
                    OCliente temp = new OCliente();
                    temp.setIdcliente(resultados.getShort(1));
                    temp.setTipodocumento(resultados.getShort(2));
                    temp.setNumerodocumento(resultados.getString(3));
                    temp.setDireccion(resultados.getString(4));
                    temp.setTelefono(resultados.getString(5));
                    temp.setCorreoelectronico(resultados.getString(6));
                    temp.setResponsable(resultados.getString(7));
                    temp.setConsejo(resultados.getString(8));
                    lista.add(temp);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NClientes consultar: " + sql.getMessage());
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