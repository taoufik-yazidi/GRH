package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;

public class OEstadoCivil implements IObjetoHci {

    private short idestadocivil;
    private String abreviatura;
    private String nombre;

    public OEstadoCivil() {
    }

    public OEstadoCivil(short idestadocivil, String abreviatura, String nombre) {
        this.idestadocivil = idestadocivil;
        this.abreviatura = abreviatura;
        this.nombre = nombre;
    }

    public short getIdestadocivil() {
        return idestadocivil;
    }

    public void setIdestadocivil(short idestadocivil) {
        this.idestadocivil = idestadocivil;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
