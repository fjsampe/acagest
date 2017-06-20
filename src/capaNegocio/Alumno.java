package capaNegocio;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase Usuario
 * Esta clase contiene los parámetros de Alumnos
 * 
 * Atributos:
 *  codigo          : Código del alumno
 *  nia             : Nia del alumno
 *  nombre          : Nombre del alumno
 *  apellidos       : Apellidos del alumnos
 *  domicilio       : Domicilio del alumno
 *  poblacion       : Población del alumno
 *  cp              : Código Postal del alumno
 *  provincia       : Província del alumno
 *  nacimiento      : Fecha de nacimiento del alumno
 *  telefono        : Teléfono fijo del alumno
 *  movil           : Teléfono móvil del alumno
 *  padre           : Nombre del padre del alumno
 *  dniPadre        : DNI del padre del alumno
 *  telefonoPadre   : Teléfono del padre del alumno
 *  emailPadre      : Email del padre del alumno
 *  madre           : Nombre de la madre del alumno
 *  dniMadre        : DNI de la madre del alumno
 *  telefonoMadre   : Teléfono de la madre del alumno
 *  emailMadre      : Email de la madre del alumno
 *  centro          : Centro de estudios donde proviene
 *  observaciones   : Observaciones o anotaciones
 *  foto            : Fotografía del alumno
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Devuelve el nombre del usuario
 *  hashCode    : Devuelve código hash
 *  equals      : Devuelve true si la comparación de objetos es igual
 *  
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Alumno {
    private int codigo;
    private String nia;
    private String nombre;
    private String apellidos;
    private String domicilio;
    private String poblacion;
    private String cp;
    private String provincia;
    private LocalDate nacimiento;
    private String telefono;
    private String movil;
    private String padre;
    private String dniPadre;
    private String telefonoPadre; 
    private String emailPadre;
    private String madre;
    private String dniMadre;
    private String telefonoMadre;
    private String emailMadre;
    private String centro;
    private String observaciones;
    private byte[] foto;
    
    /**
     * Constructor parametrizado
     * @param nia           Nia del alumno
     * @param nombre        Nombre del alumno
     * @param apellidos     Apellidos del alumno
     * @param domicilio     Domicilio del alumno
     * @param poblacion     Población del alumno
     * @param cp            Código postal del alumno
     * @param provincia     Província del alumno
     * @param nacimiento    Fecha de nacimiento del alumno
     * @param telefono      Teléfono fijo del alumno
     * @param movil         Teléfono móvil del alumno
     * @param padre         Nombre del padre del alumno
     * @param dniPadre      DNI del padre del alumno
     * @param telefonoPadre Teléfono del padre del alumno
     * @param emailPadre    Email del padre del alumno
     * @param madre         Nombre de la madre del alumno
     * @param dniMadre      DNI de la madre del alumno
     * @param telefonoMadre Teléfono de la madre del alumno
     * @param emailMadre    Email de la madre del alumno
     * @param centro        Centro de estudios del alumno
     * @param observaciones Observaciones y notas
     * @param foto          Fotografía del alumno
     */
    public Alumno(String nia, String nombre, String apellidos, String domicilio, String poblacion, String cp, String provincia, LocalDate nacimiento, String telefono, String movil, String padre, String dniPadre, String telefonoPadre, String emailPadre, String madre, String dniMadre, String telefonoMadre, String emailMadre, String centro, String observaciones, byte[] foto) {
        this.nia=nia;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.domicilio = domicilio;
        this.poblacion = poblacion;
        this.cp = cp;
        this.provincia = provincia;
        this.nacimiento = nacimiento;
        this.telefono = telefono;
        this.movil = movil;
        this.padre = padre;
        this.dniPadre = dniPadre;
        this.telefonoPadre = telefonoPadre;
        this.emailPadre = emailPadre;
        this.madre = madre;
        this.dniMadre = dniMadre;
        this.telefonoMadre = telefonoMadre;
        this.emailMadre = emailMadre;
        this.centro = centro;
        this.observaciones = observaciones;
        this.foto = foto;
    }

    /**
     * Getter código del alumno
     * @return  Devuelve el código del alumno
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Setter código del alumno
     * @param codigo    Código del alumno
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter nia del alumno
     * @return  Devuelve el nia del alumno
     */
    public String getNia() {
        return nia;
    }

    /**
     * Setter nia del alumno
     * @param nia   Nia del alumno
     */
    public void setNia(String nia) {
        this.nia = nia;
    }
    
    /**
     * Getter nombre del alumno
     * @return  Devuelve el nombre del alumno
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter nombre del alumno
     * @param nombre    Nombre del alumno
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter apellidos del alumno
     * @return  Devuelve los apellidos del alumno
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Setter apellidos del alumno
     * @param apellidos     Apellidos del alumno
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Getter domicilio del alumno
     * @return  Devuelve el domicilio del alumno
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * Setter domicilio del alumno
     * @param domicilio     Domicilio del alumno
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * Getter población del alumno
     * @return  Devuelve la población de alumno
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Setter poblacion del alumno
     * @param poblacion     Población del alumno
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Getter código postal del alumno
     * @return  Devuelve el código postal del alumno
     */
    public String getCp() {
        return cp;
    }

    /**
     * Setter código postal del alumno
     * @param cp    Código postal del alumno
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Getter província del alumno
     * @return  Devuelve la províncial del alumno
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Setter província del alumno
     * @param provincia     Província del alumno
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Getter fecha de nacimiento del alumno
     * @return  Devuelve la fecha de nacimiento 
     */
    public LocalDate getNacimiento() {
        return nacimiento;
    }

    /**
     * Setter fecha de nacimiento del alumno
     * @param nacimiento    Fecha de nacimiento del alumno
     */
    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    /**
     * Getter teléfono fijo del alumno
     * @return  Devuelve el teléfono fijo del alumno
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Setter teléfono fijo del alumno
     * @param telefono  Teléfono fijo del alumno
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Getter teléfono móvil del alumno
     * @return  Devuelve el teléfono móvil del alumno
     */
    public String getMovil() {
        return movil;
    }

    /**
     * Setter teléfono móvil del alumno
     * @param movil     Teléfono móvil del alumno
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * Getter nombre del padre del alumno
     * @return  Devuelve el nombre del padre del alumno
     */
    public String getPadre() {
        return padre;
    }
    
    /**
     * Setter nombre del padre del alumno
     * @param padre     Nombre del padre del alumno
     */
    public void setPadre(String padre) {
        this.padre = padre;
    }

    /**
     * Getter DNI del padre del alumno
     * @return  Devuelve el DNI del padre del alumno
     */
    public String getDniPadre() {
        return dniPadre;
    }

    /**
     * Setter del dni del padre del alumno
     * @param dniPadre  DNI del padre del alumno
     */
    public void setDniPadre(String dniPadre) {
        this.dniPadre = dniPadre;
    }

    /**
     * Getter teléfono del padre del alumno
     * @return  Devuelve el teléfono del padre del alumno
     */
    public String getTelefonoPadre() {
        return telefonoPadre;
    }

    /**
     * Setter del teléfono del padre del alumno
     * @param telefonoPadre     Teléfono del padre del alumno
     */
    public void setTelefonoPadre(String telefonoPadre) {
        this.telefonoPadre = telefonoPadre;
    }

    /**
     * Getter email del padre del alumno
     * @return  Devuelve el email del padre del alumno
     */
    public String getEmailPadre() {
        return emailPadre;
    }
    
    /**
     * Setter del email del padre del alumno 
     * @param emailPadre    Email del padre del alumno
     */
    public void setEmailPadre(String emailPadre) {
        this.emailPadre = emailPadre;
    }

    /**
     * Getter nombre de la madre del alumno
     * @return  Devuelve el nombre de la madre del alumno
     */
    public String getMadre() {
        return madre;
    }

    /**
     * Setter del nombre de la madre del alumno
     * @param madre Nombre de la madre del alumno 
     */
    public void setMadre(String madre) {
        this.madre = madre;
    }

    /**
     * Getter DNI de la madre del alumno
     * @return  Devuelve el nombre de la madre del alumno
     */
    public String getDniMadre() {
        return dniMadre;
    }

    /**
     * Setter del DNI de la madre del alumno
     * @param dniMadre  DNI de la madre del alumno 
     */
    public void setDniMadre(String dniMadre) {
        this.dniMadre = dniMadre;
    }

    /**
     * Getter teléfono de la madre del alumno
     * @return  Devuelve el teléfono de la madre del alumno
     */
    public String getTelefonoMadre() {
        return telefonoMadre;
    }

    /**
     * Setter del teléfono de la madre del alumno
     * @param telefonoMadre     Teléfono de la madre del alumno
     */
    public void setTelefonoMadre(String telefonoMadre) {
        this.telefonoMadre = telefonoMadre;
    }

    /**
     * Getter email de la madre del alumno
     * @return  Devuelve el email de la madre del alumno
     */
    public String getEmailMadre() {
        return emailMadre;
    }

    /**
     * Setter del email de la madre del alumno
     * @param emailMadre    Email de la madre del alumno
     */
    public void setEmailMadre(String emailMadre) {
        this.emailMadre = emailMadre;
    }

    /**
     * Gettre centro de estudios anterior/actual del alumno 
     * @return  Devuelve el centro de estudios del alumno
     */
    public String getCentro() {
        return centro;
    }

    /**
     * Setter del centro de estudios anterior/actual del alumno
     * @param centro    Centro de estudios anterior/actual del alumno
     */
    public void setCentro(String centro) {
        this.centro = centro;
    }

    /**
     * Getter observaciones
     * @return  Devuelve las observaciones o anotaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Setter de las observaciones 
     * @param observaciones     Observaciones o anotaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Getter fotografía
     * @return  Devuelve la fotografía del alumno
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * Setter de la fotografía del alumno
     * @param foto  Fotografia del alumno
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * HasCode
     * @return  Devuelve código hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.codigo);
        return hash;
    }

    /**
     * Método equals
     * @param obj   Objeto a Comparar   
     * @return      Devuelve True=si son iguales los objetos a comparar, en caso 
     *              contrario devuelve false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alumno other = (Alumno) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Método que devuelve la cadena con el nombre del alumno
     * @return  Devuelve el nombre completo de alumno
     */
    @Override
    public String toString(){
        return apellidos+", "+nombre;
    }
}
