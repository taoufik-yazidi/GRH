package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;

public class OGenero implements IObjetoHci {

    private short idgenero;
    private String sigla;
    private String genero;

    public OGenero() {
    }

    public OGenero(short idgenero, String sigla, String genero) {
        this.idgenero = idgenero;
        this.sigla = sigla;
        this.genero = genero;
    }

    public short getIdgenero() {
        return idgenero;
    }

    public void setIdgenero(short idgenero) {
        this.idgenero = idgenero;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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
