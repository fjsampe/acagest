package capaNegocio;

import java.time.LocalDate;

/**
 * Clase Profesor
 * Esta clase contiene los parámetros de Profesores
 * 
 * Atributos:
 *  cifDni          : Dni del profesor
 *  nombre          : Nombre del profesor
 *  apellidos       : Apellidos del profesor
 *  domicilio       : Domicilio del profesor
 *  poblacion       : Población del profesor
 *  cp              : Código Postal del profesor
 *  provincia       : Província del profesor
 *  fechaNacimiento : Fecha de nacimiento del profesor
 *  telefono        : Teléfono fijo del profesor
 *  movil           : Teléfono móvil del profesor
 *  ctaBanco        : Cuenta bancaria del profesor
 *  email           : Cuenta de correo
 *  foto            : Fotografía del profesor
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre del profesor
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Profesor {
    private String cifDni;
    private String nombre;
    private String apellidos;
    private String domicilio;
    private String poblacion;
    private String cp;
    private String provincia;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String movil;
    private String ctaBanco;
    private String email;
    private byte[] foto;

    /**
     * Constructor parametrizado
     * @param cifDni            Dni del profesor
     * @param nombre            Nombre del profesor
     * @param apellidos         Appellidos del profesor
     * @param domicilio         Domicilio del profesor
     * @param poblacion         Población del profesor
     * @param cp                Código postal del profesor
     * @param provincia         Província del profesor
     * @param fechaNacimiento   Fecha de nacimiento del profesor
     * @param telefono          Teléfono fijo del profesor
     * @param movil             Teléfono móvil del profesor
     * @param ctaBanco          Cuenta bancaria del profesor
     * @param email             Cuenta email
     * @param foto              Fotografía del profesor
     */
    public Profesor(String cifDni, String nombre, String apellidos, String domicilio, String poblacion, String cp, String provincia, LocalDate fechaNacimiento, String telefono, String movil, String ctaBanco, String email, byte[] foto) {
        this.cifDni = cifDni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.domicilio = domicilio;
        this.poblacion = poblacion;
        this.cp = cp;
        this.provincia = provincia;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.movil = movil;
        this.ctaBanco = ctaBanco;
        this.email = email;
        this.foto = foto;
    }

    /**
     * Getter dni del profesor
     * @return  Devuelve el dni del profesor
     */
    public String getCifDni() {
        return cifDni;
    }

    /**
     * Setter dni del profesor
     * @param cifDni    Cif del profesor
     */
    public void setCifDni(String cifDni) {
        this.cifDni = cifDni;
    }

    /**
     * Getter nombre del profesor
     * @return  Devuelve el nombre del profesor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter nombre del profesor
     * @param nombre    Nombre del profesor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter apellidos del profesor
     * @return  Devuelve los apellidos del profesor
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Setter apellidos del profesor
     * @param apellidos     Apellidos del profesor
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Getter domicilio del profesor
     * @return  Devuelve el domicilio del profesor
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * Setter domicilio del profesor
     * @param domicilio     Domicilio del profesor
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * Getter población del profesor
     * @return  Devuelve la población del profesor
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Setter población del profesor
     * @param poblacion     Población del profesor
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Getter código postal del profesor
     * @return  Devuelve el código postal del profesor
     */
    public String getCp() {
        return cp;
    }

    /**
     * Setter código postal del profesor
     * @param cp    Código postal del profesor
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Getter província del profesor
     * @return  Devuelve la província del profesor
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Setter província del profesor
     * @param provincia     Província del profesor
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Getter fecha de nacimiento del profesor
     * @return Devuelve la fecha de nacimiento del profesor
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Setter fecha de nacimiento del profesor
     * @param fechaNacimiento   Fecha de nacimiento del profesor
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Getter teléfono fijo del profesor
     * @return  Devuelve el teléfono fijo del profesor
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Setter teléfono fijo del profesor
     * @param telefono  Teléfono fijo del profesor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Getter teléfono móvil del profesor
     * @return  Devuelve el teléfono móvil de profesor
     */
    public String getMovil() {
        return movil;
    }

    /**
     * Setter teléfono móvil de profesor
     * @param movil     Teléfono móvil del profesor
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * Getter cuenta bancaria del profesor
     * @return  Devuelve la cuenta bancaria del profesor 
     */
    public String getCtaBanco() {
        return ctaBanco;
    }

    /**
     * Setter cuenta bancaria del profesor
     * @param ctaBanco  Cuenta bancaria del profesor
     */
    public void setCtaBanco(String ctaBanco) {
        this.ctaBanco = ctaBanco;
    }

    /**
     * Getter cuenta de email del profesor
     * @return  Devuelve la cuenta de correo del profesor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter cuenta email del profesor
     * @param email     Cuenta email del profesor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter fotografía del profesor
     * @return  Devuelve la fotografía del profesor
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * Setter fotografía del profesor
     * @param foto  Fotografía del profesor
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    /**
     * Método que devuelve la cadena con el nombre y apellidos del profesor
     * @return  Devuelve el nombre completo del profesor
     */
    @Override
    public String toString() {
        return apellidos+", "+nombre;
    }
}
