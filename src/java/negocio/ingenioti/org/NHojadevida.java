package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.ingenioti.org.OCredencial;
import objetos.ingenioti.org.ODepartamentos;
import objetos.ingenioti.org.OEstadoCivil;
import objetos.ingenioti.org.OGenero;
import objetos.ingenioti.org.OHojadevida;
import objetos.ingenioti.org.OMunicipios;
import objetos.ingenioti.org.OTiposDeDocumento;

public class NHojadevida extends NGeneralidades {

    private final String MIOBJETO = "NEGHDV";
    private static final Logger LOG = Logger.getLogger(NHojadevida.class.getName());

    public int ejecutarSQL(byte ta, OHojadevida obj, OCredencial cre) throws ExcepcionGeneral {
        int respuesta = 0;
        if (NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                switch (ta) {
                    case NUtilidades.ACCIONINSERTAR: 
                        conectar("select * from fn_hojadevida_ins(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        sentenciaProcedimiento.setShort(1, obj.getTipodedocumento().getIdtipodedocumento());
                        sentenciaProcedimiento.setString(2, obj.getNumerodocumento());
                        sentenciaProcedimiento.setString(3, obj.getPrimerapellido());
                        sentenciaProcedimiento.setString(4, obj.getSegundoapellido());
                        sentenciaProcedimiento.setString(5, obj.getNombres());
                        sentenciaProcedimiento.setShort(6, obj.getGenero().getIdgenero());
                        sentenciaProcedimiento.setString(7, obj.getLibretamilitar());
                        sentenciaProcedimiento.setString(8, obj.getDistritolm());
                        sentenciaProcedimiento.setShort(9, obj.getLugarnacimiento().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setShort(10, obj.getLugarexpedicion().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setShort(11, obj.getLugarresidencia().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setDate(12, new java.sql.Date(obj.getFechanacimiento().getTimeInMillis()));
                        sentenciaProcedimiento.setDate(13, new java.sql.Date(obj.getFechaexpedicion().getTimeInMillis()));
                        sentenciaProcedimiento.setShort(14, obj.getEstadocivil().getIdestadocivil());
                        sentenciaProcedimiento.setBoolean(15, obj.isDisponibilidadviaje());
                        sentenciaProcedimiento.setString(16, obj.getDireccion());
                        sentenciaProcedimiento.setString(17, obj.getTelefono());
                        sentenciaProcedimiento.setString(18, obj.getCorreo());
                        sentenciaProcedimiento.setString(19, obj.getFoto());
                        break;
                    case NUtilidades.ACCIONACTUALIZAR:
                        conectar("select * from fn_hojadevida_upd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        sentenciaProcedimiento.setInt(1, obj.getIdhojadevida());
                        sentenciaProcedimiento.setShort(2, obj.getTipodedocumento().getIdtipodedocumento());
                        sentenciaProcedimiento.setString(3, obj.getNumerodocumento());
                        sentenciaProcedimiento.setString(4, obj.getPrimerapellido());
                        sentenciaProcedimiento.setString(5, obj.getSegundoapellido());
                        sentenciaProcedimiento.setString(6, obj.getNombres());
                        sentenciaProcedimiento.setShort(7, obj.getGenero().getIdgenero());
                        sentenciaProcedimiento.setString(8, obj.getLibretamilitar());
                        sentenciaProcedimiento.setString(9, obj.getDistritolm());
                        sentenciaProcedimiento.setShort(10, obj.getLugarnacimiento().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setShort(11, obj.getLugarexpedicion().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setShort(12, obj.getLugarresidencia().getMunicipio().getIdmunicipio());
                        sentenciaProcedimiento.setDate(13, new java.sql.Date(obj.getFechanacimiento().getTimeInMillis()));
                        sentenciaProcedimiento.setDate(14, new java.sql.Date(obj.getFechaexpedicion().getTimeInMillis()));
                        sentenciaProcedimiento.setShort(15, obj.getEstadocivil().getIdestadocivil());
                        sentenciaProcedimiento.setBoolean(16, obj.isDisponibilidadviaje());
                        sentenciaProcedimiento.setString(17, obj.getDireccion());
                        sentenciaProcedimiento.setString(18, obj.getTelefono());
                        sentenciaProcedimiento.setString(19, obj.getCorreo());
                        sentenciaProcedimiento.setString(20, obj.getFoto());
                        break;
                    case NUtilidades.ACCIONBORRAR: 
                        conectar("select * from fn_hojadevida_del(?)");
                        sentenciaProcedimiento.setInt(1, obj.getIdhojadevida());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getInt(1);
                }
            } catch (SQLException sql) {
                NUtilidades.generaLogServer(LOG, Level.SEVERE, "Error en NHojadevida %s", sql.getMessage());
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

    public ArrayList<OHojadevida> consultar(short tc, OHojadevida obj, 
                                            OCredencial cre, int pagina, int limite, 
                                            int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<OHojadevida> lista = new ArrayList<OHojadevida>();
        if (NUtilidades.tienePermiso(NUtilidades.ACCIONCONSULTAR, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)) {
            try {
                conectar("select * from fn_hojadevida_sel(?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setInt(2, obj.getIdhojadevida());
                sentenciaProcedimiento.setInt(3, pagina);
                sentenciaProcedimiento.setInt(4, limite);
                sentenciaProcedimiento.setInt(5, columnaOrden);
                sentenciaProcedimiento.setString(6, tipoOrden);
                getResultadosProcedimiento();
                while (resultados.next()) {
                    OHojadevida temp = new OHojadevida();
                    temp.setIdhojadevida(resultados.getInt(1));
                    OTiposDeDocumento tipodocumento = new OTiposDeDocumento(resultados.getShort(2), "", resultados.getString(3), true);
                    temp.setTipodedocumento(tipodocumento);
                    temp.setNumerodocumento(resultados.getString(4));
                    temp.setPrimerapellido(resultados.getString(5));
                    temp.setSegundoapellido(resultados.getString(6));
                    temp.setNombres(resultados.getString(7));
                    OGenero genero = new OGenero(resultados.getShort(8),"",resultados.getString(9));
                    temp.setGenero(genero);
                    temp.setLibretamilitar(resultados.getString(10));
                    temp.setDistritolm(resultados.getString(11));
                    OMunicipios municipio = new OMunicipios(resultados.getShort(12), "", resultados.getString(15));
                    ODepartamentos departamento = new ODepartamentos(resultados.getShort(13),"",resultados.getString(14), municipio);
                    temp.setLugarnacimiento(departamento);
                    municipio = new OMunicipios(resultados.getShort(16), "", resultados.getString(19));
                    departamento = new ODepartamentos(resultados.getShort(17),"",resultados.getString(18), municipio);
                    temp.setLugarexpedicion(departamento);
                    municipio = new OMunicipios(resultados.getShort(20), "", resultados.getString(23));
                    departamento = new ODepartamentos(resultados.getShort(21),"",resultados.getString(22), municipio);
                    temp.setLugarresidencia(departamento);
                    temp.setFechanacimiento(resultados.getDate(24));
                    temp.setFechaexpedicion(resultados.getDate(25));
                    OEstadoCivil estadocivil = new OEstadoCivil(resultados.getShort(26),"",resultados.getString(27));
                    temp.setEstadocivil(estadocivil);
                    temp.setDisponibilidadviaje(resultados.getBoolean(28));
                    temp.setDireccion(resultados.getString(29));
                    temp.setTelefono(resultados.getString(30));
                    temp.setCorreo(resultados.getString(31));
                    temp.setFoto(resultados.getString(32));
                    lista.add(temp);
                }
            } catch (SQLException sql) {
                NUtilidades.generaLogServer(LOG, Level.SEVERE, "Error en NHojadevida consultar: %s", sql.getMessage());
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
