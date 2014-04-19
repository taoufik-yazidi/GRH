package objetos.ingenioti.org;

import interfaces.ingenioti.org.IObjetoHci;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OHojadevida implements IObjetoHci {

    private int idhojadevida;
    private OTiposDeDocumento tipodedocumento;
    private String numerodocumento;
    private String primerapellido;
    private String segundoapellido;
    private String nombres;
    private OGenero genero;
    private String libretamilitar;
    private String distritolm;
    private OMunicipios lugarnacimiento, lugarexpedicion, lugarresidencia;
    private Calendar fechanacimiento, fechaexpedicion;
    private OEstadoCivil estadocivil;
    private boolean disponibilidadviaje;
    private String direccion;
    private String telefono;
    private String correo;
    private String foto;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    public OHojadevida() {
    }

    public OHojadevida(int idhojadevida, OTiposDeDocumento tipodedocumento, String numerodocumento,
            String primerapellido, String segundoapellido, String nombres, OGenero genero,
            String libretamilitar, String distritolm, OMunicipios lugarnacimiento,
            OMunicipios lugarexpedicion, OMunicipios lugarresidencia, Calendar fechanacimiento,
            Calendar fechaexpedicion, OEstadoCivil estadocivil, boolean disponibilidadviaje,
            String direccion, String telefono, String correo, String foto) {
        this.idhojadevida = idhojadevida;
        this.tipodedocumento = tipodedocumento;
        this.numerodocumento = numerodocumento;
        this.primerapellido = primerapellido;
        this.segundoapellido = segundoapellido;
        this.nombres = nombres;
        this.genero = genero;
        this.libretamilitar = libretamilitar;
        this.distritolm = distritolm;
        this.lugarnacimiento = lugarnacimiento;
        this.lugarexpedicion = lugarexpedicion;
        this.lugarresidencia = lugarresidencia;
        this.fechanacimiento = fechanacimiento;
        this.fechaexpedicion = fechaexpedicion;
        this.estadocivil = estadocivil;
        this.disponibilidadviaje = disponibilidadviaje;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.foto = foto;
    }

    public OHojadevida(int idhojadevida, short tipodedocumento, String numerodocumento,
            String primerapellido, String segundoapellido, String nombres, short genero,
            String libretamilitar, String distritolm, short lugarnacimiento,
            short lugarexpedicion, short lugarresidencia, Calendar fechanacimiento,
            Calendar fechaexpedicion, short estadocivil, boolean disponibilidadviaje,
            String direccion, String telefono, String correo, String foto) {
        this.idhojadevida = idhojadevida;
        this.tipodedocumento = new OTiposDeDocumento();
        this.tipodedocumento.setIdtipodedocumento(tipodedocumento);
        this.numerodocumento = numerodocumento;
        this.primerapellido = primerapellido;
        this.segundoapellido = segundoapellido;
        this.nombres = nombres;
        this.genero = new OGenero();
        this.genero.setIdgenero(genero);
        this.libretamilitar = libretamilitar;
        this.distritolm = distritolm;
        this.lugarnacimiento = new OMunicipios();
        this.lugarnacimiento.setIdmunicipio(lugarnacimiento);
        this.lugarexpedicion = new OMunicipios();
        this.lugarexpedicion.setIdmunicipio(lugarexpedicion);
        this.lugarresidencia = new OMunicipios();
        this.lugarresidencia.setIdmunicipio(lugarresidencia);
        this.fechanacimiento = fechanacimiento;
        this.fechaexpedicion = fechaexpedicion;
        this.estadocivil = new OEstadoCivil();
        this.estadocivil.setIdestadocivil(estadocivil);
        this.disponibilidadviaje = disponibilidadviaje;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.foto = foto;
    }

    public int getIdhojadevida() {
        return idhojadevida;
    }

    public void setIdhojadevida(int idhojadevida) {
        this.idhojadevida = idhojadevida;
    }

    public OTiposDeDocumento getTipodedocumento() {
        return tipodedocumento;
    }

    public void setTipodedocumento(OTiposDeDocumento tipodedocumento) {
        this.tipodedocumento = tipodedocumento;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public OGenero getGenero() {
        return genero;
    }

    public void setGenero(OGenero genero) {
        this.genero = genero;
    }

    public String getLibretamilitar() {
        return libretamilitar;
    }

    public void setLibretamilitar(String libretamilitar) {
        this.libretamilitar = libretamilitar;
    }

    public String getDistritolm() {
        return distritolm;
    }

    public void setDistritolm(String distritolm) {
        this.distritolm = distritolm;
    }

    public OMunicipios getLugarnacimiento() {
        return lugarnacimiento;
    }

    public void setLugarnacimiento(OMunicipios lugarnacimiento) {
        this.lugarnacimiento = lugarnacimiento;
    }

    public OMunicipios getLugarexpedicion() {
        return lugarexpedicion;
    }

    public void setLugarexpedicion(OMunicipios lugarexpedicion) {
        this.lugarexpedicion = lugarexpedicion;
    }

    public OMunicipios getLugarresidencia() {
        return lugarresidencia;
    }

    public void setLugarresidencia(OMunicipios lugarresidencia) {
        this.lugarresidencia = lugarresidencia;
    }

    public Calendar getFechanacimiento() {
        return fechanacimiento;
    }

    public String getFechanacimientoSDF(){
        return sdf.format(this.fechanacimiento);
    }

    public void setFechanacimiento(Calendar fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento){
        this.fechanacimiento.setTime(fechanacimiento);
    }
    
    public Calendar getFechaexpedicion() {
        return fechaexpedicion;
    }

    public String getFechaexpedicionSDF(){
        return sdf.format(this.fechaexpedicion);
    }
    
    public void setFechaexpedicion(Calendar fechaexpedicion) {
        this.fechaexpedicion = fechaexpedicion;
    }

    public void setFechaexpedicion(Date fechaexpedicion){
        this.fechaexpedicion.setTime(fechaexpedicion);
    }
    
    public OEstadoCivil getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(OEstadoCivil estadocivil) {
        this.estadocivil = estadocivil;
    }

    public boolean isDisponibilidadviaje() {
        return disponibilidadviaje;
    }

    public void setDisponibilidadviaje(boolean disponibilidadviaje) {
        this.disponibilidadviaje = disponibilidadviaje;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
