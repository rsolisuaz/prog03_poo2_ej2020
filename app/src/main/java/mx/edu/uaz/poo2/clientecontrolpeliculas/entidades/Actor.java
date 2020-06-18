package mx.edu.uaz.poo2.clientecontrolpeliculas.entidades;

import java.io.Serializable;
import java.util.Date;

public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idActor;
    private String nombreActor;
    private Date fechaNacimiento;
    private String lugarNacimiento;
    private Date fechaMuerte;
    private String urlActor;
    private String urlImagenActor;

    public Actor() {
    }

    public Actor(Integer idActor) {
        this.idActor = idActor;
    }

    public Actor(Integer idActor, String nombreActor) {
        this.idActor = idActor;
        this.nombreActor = nombreActor;
    }

    public Integer getIdActor() {
        return idActor;
    }

    public void setIdActor(Integer idActor) {
        this.idActor = idActor;
    }

    public String getNombreActor() {
        return nombreActor;
    }

    public void setNombreActor(String nombreActor) {
        this.nombreActor = nombreActor;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public Date getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Date fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public String getUrlActor() {
        return urlActor;
    }

    public void setUrlActor(String urlActor) {
        this.urlActor = urlActor;
    }

    public String getUrlImagenActor() {
        return urlImagenActor;
    }

    public void setUrlImagenActor(String urlImagenActor) {
        this.urlImagenActor = urlImagenActor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActor != null ? idActor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Actor)) {
            return false;
        }
        Actor other = (Actor) object;
        if ((this.idActor == null && other.idActor != null) || (this.idActor != null && !this.idActor.equals(other.idActor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreActor;
    }

}

