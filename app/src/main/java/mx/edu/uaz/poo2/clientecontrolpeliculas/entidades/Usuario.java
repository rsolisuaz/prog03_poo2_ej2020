package mx.edu.uaz.poo2.clientecontrolpeliculas.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    private String rfc;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String email;
    private String telefono;
    private String calle;
    private String colonia;
    private Integer idMunicipio;
    private Short idEntidad;
    private Integer codpostal;

    public Usuario() {
    }

    public Usuario(String rfc) {
        this.rfc = rfc;
    }

    public Usuario(String rfc, String nombre, String apPaterno, String email, String telefono) {
        this.rfc = rfc;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.email = email;
        this.telefono = telefono;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Short getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Short idEntidad) {
        this.idEntidad = idEntidad;
    }

    public Integer getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(Integer codpostal) {
        this.codpostal = codpostal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfc != null ? rfc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.rfc == null && other.rfc != null) || (this.rfc != null && !this.rfc.equals(other.rfc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String apM=(apMaterno!=null)?" "+apMaterno:"";
        return nombre+" "+apPaterno+apM;
    }

}