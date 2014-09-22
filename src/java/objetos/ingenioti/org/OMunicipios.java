package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;

public class OMunicipios implements IObjetoHci {

    private short idmunicipio;
    private String codigo;
    private String nombre;

    public OMunicipios() {
    }

    public OMunicipios(short idmunicipio, String codigo, String nombre) {
        this.idmunicipio = idmunicipio;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public short getIdmunicipio() {
        return idmunicipio;
    }

    public void setIdmunicipio(short idmunicipio) {
        this.idmunicipio = idmunicipio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toJson(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"idmunicipio\":").append(getIdmunicipio()).append(",");
        sb.append("\"codigo\":\"").append(getCodigo()).append("\",");
        sb.append("\"nombre\":\"").append(getNombre()).append("\"");
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public String getDescripcion() {
        return null;
    }

    @Override
    public String getXML() {
        return null;
    }
}
