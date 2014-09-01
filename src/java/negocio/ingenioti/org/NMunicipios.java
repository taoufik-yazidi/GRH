package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.ODepartamentos;
import objetos.ingenioti.org.OMunicipios;

public class NMunicipios extends NGeneralidades {

    private final String MIOBJETO = "PARMUN";
    private static final Logger LOG = Logger.getLogger(NMunicipios.class.getName());

    public int ejecutarSQL(byte ta, ODepartamentos obj, OCredencial cre) throws ExcepcionGeneral {
        int respuesta = 0;
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                switch (ta) {
                    case 1: // Insertar
                        conectar("select * from fn_municipios_ins(?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getIddepartamento());
                        sentenciaProcedimiento.setString(2, obj.getMunicipios().get(0).getCodigo());
                        sentenciaProcedimiento.setString(3, obj.getMunicipios().get(0).getNombre());
                        break;
                    case 2: //ACTUALIZAR
                        conectar("select * from fn_municipios_upd(?, ?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getMunicipios().get(0).getIdmunicipio());
                        sentenciaProcedimiento.setShort(2, obj.getIddepartamento());
                        sentenciaProcedimiento.setString(3, obj.getMunicipios().get(0).getCodigo());
                        sentenciaProcedimiento.setString(4, obj.getMunicipios().get(0).getNombre());
                        break;
                    case 3: //BORRAR
                        conectar("select * from fn_municipios_del(?)");
                        sentenciaProcedimiento.setShort(1, obj.getMunicipios().get(0).getIdmunicipio());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getInt(1);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NMunicipios insertar: " + sql.getMessage());
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

    public ArrayList<ODepartamentos> consultar(short tc, ODepartamentos obj, OCredencial cre, int pagina, int limite, int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<ODepartamentos> lista = new ArrayList<ODepartamentos>();
        if (NUtilidades.tienePermiso(NUtilidades.ACCIONCONSULTAR, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                conectar("select * from fn_municipios_sel(?,?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setShort(2, obj.getMunicipios().get(0).getIdmunicipio());
                sentenciaProcedimiento.setShort(3, obj.getIddepartamento());
                sentenciaProcedimiento.setInt(4, pagina);
                sentenciaProcedimiento.setInt(5, limite);
                sentenciaProcedimiento.setInt(6, columnaOrden);
                sentenciaProcedimiento.setString(7, tipoOrden);
                getResultadosProcedimiento();
                int iddepartamento = 0;
                ODepartamentos temp = new ODepartamentos();
                while (resultados.next()) {
                    if (iddepartamento != resultados.getShort(1)){
                        if(!resultados.isFirst() && !resultados.isLast()){
                            lista.add(temp);
                        }
                        temp = new ODepartamentos();
                        temp.setIddepartamento(resultados.getShort(1));
                        temp.setCodigo(resultados.getString(2));
                        temp.setNombre(resultados.getString(3));
                    }
                    OMunicipios temp2 = new OMunicipios();
                    temp2.setIdmunicipio(resultados.getShort(4));
                    temp2.setCodigo(resultados.getString(5));
                    temp2.setNombre(resultados.getString(6));
                    temp.addMunicipio(temp2);
                    iddepartamento = resultados.getShort(1);
                    if(resultados.isLast()){
                        lista.add(temp);
                    }
                }
            } catch (SQLException sql) {
                LOG.log(Level.INFO, "Error en NMunicipios consultar: %s", sql.getMessage());
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
