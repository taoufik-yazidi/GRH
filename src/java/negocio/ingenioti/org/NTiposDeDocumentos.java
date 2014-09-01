package negocio.ingenioti.org;

import excepciones.ingenioti.org.ExcepcionGeneral;
import java.sql.SQLException;
import objetos.ingenioti.org.OTiposDeDocumento;
import java.util.ArrayList;
import objetos.ingenioti.org.OCredencial;

/**
 *
 * @author Alexys
 */
public class NTiposDeDocumentos extends NGeneralidades {

    private final String MIOBJETO = "PARTDO";
    
    public NTiposDeDocumentos() {
    }

    public Short ejecutarSQL(byte ta, OTiposDeDocumento td, OCredencial cre) throws ExcepcionGeneral {
        Short respuesta = 0;
        if(NUtilidades.tienePermiso(ta, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)){
            try {
                switch(ta){
                    case 1: // Insertar
                        conectar("select * from fn_tiposdedocumento_ins(?,?,?)");
                        sentenciaProcedimiento.setString(1, td.getAbreviatura());
                        sentenciaProcedimiento.setString(2, td.getTipodedocumento());
                        sentenciaProcedimiento.setBoolean(3, td.isActivo());
                        break;
                    case 2: // Actualizar
                        conectar("select * from fn_tiposdedocumento_upd(?,?,?,?)");
                        sentenciaProcedimiento.setShort(1, td.getIdtipodedocumento());
                        sentenciaProcedimiento.setString(2, td.getAbreviatura());
                        sentenciaProcedimiento.setString(3, td.getTipodedocumento());
                        sentenciaProcedimiento.setBoolean(4, td.isActivo());
                        break;
                    case 3: // Borrar
                        conectar("select * from fn_tiposdedocumento_del(?)");
                        sentenciaProcedimiento.setShort(1, td.getIdtipodedocumento());
                        break;
                    default:
                        throw new ExcepcionGeneral("Acción no valida.");
                }
                getResultadosProcedimiento();
                if (resultados.next()) {
                    respuesta = resultados.getShort(1);
                }
            } catch (SQLException sql) {
                System.err.println("Error en NTiposDeDocumentos: " + sql.getMessage());
            } finally {
                try{
                    cerrarConexion();
                } catch (SQLException sqle){}
            }
        } else {
            throw new ExcepcionGeneral(getNoAutenticado());
        }
        return respuesta;
    }

    
    public ArrayList<OTiposDeDocumento> consultar(short tc, OTiposDeDocumento td, OCredencial cre, int pagina, int limite, int columnaOrden, String tipoOrden)
            throws ExcepcionGeneral {
        ArrayList<OTiposDeDocumento> lista = new ArrayList<OTiposDeDocumento>();
        if(NUtilidades.tienePermiso(NUtilidades.ACCIONCONSULTAR, cre.getUsuario().getPerfil().getIdperfil(), MIOBJETO)){
            try{
                conectar("select * from fn_tiposdedocumento_sel(?,?,?,?,?,?,?,?,?)");
                sentenciaProcedimiento.setShort(1, tc);
                sentenciaProcedimiento.setShort(2, td.getIdtipodedocumento());
                sentenciaProcedimiento.setString(3,td.getAbreviatura());
                sentenciaProcedimiento.setString(4, td.getTipodedocumento());
                sentenciaProcedimiento.setBoolean(5, td.isActivo());
                sentenciaProcedimiento.setInt(6, pagina);
                sentenciaProcedimiento.setInt(7, limite);
                sentenciaProcedimiento.setInt(8, columnaOrden);
                sentenciaProcedimiento.setString(9, tipoOrden);
                getResultadosProcedimiento();
                while(resultados.next()){
                    OTiposDeDocumento temp = new OTiposDeDocumento();
                    temp.setIdtipodedocumento(resultados.getShort(1));
                    temp.setAbreviatura(resultados.getString(2));
                    temp.setTipodedocumento(resultados.getString(3));
                    temp.setActivo(resultados.getBoolean(4));
                    lista.add(temp);
                }
            } catch (SQLException sqle){
                System.err.println("Error de NTiposDeDocumentos consultar: "+sqle.getMessage());
            } finally {
                try{
                    cerrarConexion();
                } catch (SQLException sqle){}
            }
        } else {
            throw new ExcepcionGeneral(getNoAutenticado());
        }
        return lista;
    }
}