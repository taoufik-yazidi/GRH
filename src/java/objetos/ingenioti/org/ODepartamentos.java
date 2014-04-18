package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;
import java.util.ArrayList;

public class ODepartamentos implements IObjetoHci {

    private short iddepartamento;
    private String codigo;
    private String nombre;
    private ArrayList<OMunicipios> municipios;
    private OMunicipios municipio;

    public ODepartamentos() {
        this.iddepartamento = 0;
        this.codigo = "";
        this.nombre = "";
        this.municipios = null;
    }

    public ODepartamentos(short iddepartamento, String codigo, String nombre, ArrayList<OMunicipios> municipios) {
        this.iddepartamento = iddepartamento;
        this.codigo = codigo;
        this.nombre = nombre;
        this.municipios = municipios;
    }

    public ODepartamentos(short iddepartamento, String codigo, String nombre, OMunicipios municipio) {
        this.iddepartamento = iddepartamento;
        this.codigo = codigo;
        this.nombre = nombre;
        this.municipio = municipio;
    }

    
    public short getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(short iddepartamento) {
        this.iddepartamento = iddepartamento;
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

    public OMunicipios getMunicipio() {
        return municipio;
    }

    public void setMunicipio(OMunicipios municipio) {
        this.municipio = municipio;
    }

    public void addMunicipio(OMunicipios municipio){
        if(this.municipios == null){
            this.municipios = new ArrayList<OMunicipios>();
        }
        this.municipios.add(municipio);
    }
    
    public ArrayList<OMunicipios> getMunicipios(){
        return this.municipios;
    }
    
    public ArrayList<ODepartamentos> listaTipoTabla(){
        ArrayList<ODepartamentos> departamentos = new ArrayList<ODepartamentos>();
        for(OMunicipios temp: this.municipios){
            ODepartamentos departamento = new ODepartamentos(this.iddepartamento, this.codigo, this.nombre, temp);
            departamentos.add(departamento);
        }
        return departamentos;
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