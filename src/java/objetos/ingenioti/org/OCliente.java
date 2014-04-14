package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;

public class OCliente implements IObjetoHci{
    private short idcliente;
    private short tipodocumento;
    private String numerodocumento;
    private String direccion;
    private String telefono;
    private String correoelectronico;
    private String responsable;
    private String consejo;

    public OCliente() {
    }

    public OCliente(short idcliente, short tipodocumento, String numerodocumento, String direccion, String telefono, String correoelectronico, String responsable, String consejo) {
        this.idcliente = idcliente;
        this.tipodocumento = tipodocumento;
        this.numerodocumento = numerodocumento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoelectronico = correoelectronico;
        this.responsable = responsable;
        this.consejo = consejo;
    }

    public short getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(short idcliente) {
        this.idcliente = idcliente;
    }

    public short getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(short tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoelectronico() {
        return correoelectronico;
    }

    public void setCorreoelectronico(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getConsejo() {
        return consejo;
    }

    public void setConsejo(String consejo) {
        this.consejo = consejo;
    }

    
    @Override
    public String getDescripcion(){
        return "Objeto cliente";
    }
    @Override
    public String getXML(){
        return null;
    }
}
