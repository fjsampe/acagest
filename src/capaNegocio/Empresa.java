package capaNegocio;

/**
 * Clase Empresa
 * Esta clase contiene los parámetros de configuración de la empresa
 * 
 * Atributos:
 *  nif                 : Nif de la empresa
 *  nombre              : Nombre de la empresa
 *  domicilio           : Domicilio de la empresa
 *  poblacion           : Población de la empresa
 *  cp                  : Código postal de la empresa
 *  provincia           : Província de la empresa
 *  telefono            : Teléfono de la empresa
 *  email               : Email de la empresa
 *  impuesto            : Impuesto en facturas
 *  porcentajeImpuesto  : Porcentaje del impuesto
 *  logo                : Logo de la empresa
 * 
 * Constructores, Getters y Setters
 * 
 * Métodos:
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.2
 */
public class Empresa {
    private String nif;
    private String nombre;
    private String domicilio;
    private String poblacion;
    private String cp;
    private String provincia;
    private String telefono;
    private String email;
    private String impuesto;
    private double porcentajeImpuesto;
    private boolean tutorPorCurso;
    private byte[] logo;

    /**
     * Constructor por defecto
     */
    public Empresa() {
    }

    /**
     * Constructor parametrizado
     * @param nif                   Nif de la empresa
     * @param nombre                Nombre de la empresa
     * @param domicilio             Domicilio de la empresa
     * @param poblacion             Población de la empresa
     * @param cp                    Código postal de la empresa
     * @param provincia             Província de la empresa
     * @param telefono              Teléfono de la empresa
     * @param email                 Email de la empresa
     * @param impuesto              Descripción del impuesto
     * @param porcentajeImpuesto    Porcentaje de impuesto
     * @param tutorPorCurso         Tutor por curso (true/false)
     * @param logo      Logo de la empresa
     */
    public Empresa(String nif, String nombre, String domicilio, String poblacion, String cp, String provincia, String telefono, String email, String impuesto, double porcentajeImpuesto, boolean tutorPorCurso, byte[] logo) {
        this.nif = nif;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.poblacion = poblacion;
        this.cp = cp;
        this.provincia = provincia;
        this.telefono = telefono;
        this.email = email;
        this.impuesto = impuesto;
        this.porcentajeImpuesto = porcentajeImpuesto;
        this.tutorPorCurso = tutorPorCurso;
        this.logo = logo;
    }

    /**
     * Getter nif de la empresa
     * @return  Devuelve el nif de la empresa
     */
    public String getNif() {
        return nif;
    }

    /**
     * Setter del nif de la empresa
     * @param nif   Nif de la empresa
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Getter nombre de la empresa
     * @return  Devuelve el nombre de la empresa
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter del nombre de la empresa
     * @param nombre    Nombre de la empresa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter domicilio de la empresa
     * @return  Devuelve el domicilio de la empresa
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * Setter del domicilio de la empresa
     * @param domicilio     Domicilio de la empresa
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    /**
     * Getter poblacion de la empresa
     * @return  Devuelve la población de la empresa
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Setter de la población de la empresa
     * @param poblacion     Población de la empresa
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * Getter código postal de la empresa
     * @return  Devuelve el código postal de la empresa
     */
    public String getCp() {
        return cp;
    }

    /**
     * Setter del código postal de la empresa
     * @param cp    Código postal de la empresa
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * Getter província de la empresa
     * @return  Devuelve la província de la empresa
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Setter de la província de la empresa
     * @param provincia     Província de la empresa
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Getter teléfono de la empresa
     * @return  Devuelve el teléfono de la empresa
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Setter del teléfono de la empresa
     * @param telefono  Teléfono de la empresa
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Getter email de la empresa
     * @return  Devuelve el email de la empresa
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter del email de la empresa
     * @param email     Email de la empresa
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter descripción del impuesto
     * @return  Devuelve el nombre del impuesto
     */
    public String getImpuesto() {
        return impuesto;
    }

    /**
     * Setter del nombre del impuesto
     * @param impuesto  Nombre del impuesto
     */
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    /**
     * Getter porcentaje de impuesto
     * @return  Devuelve el porcentaje del impuesto
     */
    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }
    
    /**
     * Getter porcentaje de impuesto formateado
     * @return  Devuelve el porcentaje del impuesto
     */
    public String getPorcentajeImpuestoFormateado() {
        return String.format("%.2f",porcentajeImpuesto);
    }

    /**
     * Setter porcentaje de impuesto
     * @param porcentajeImpuesto    Porcentaje de impuesto
     */
    public void setPorcentajeImpuesto(double porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }


    /**
     * Getter tutor por curso 
     * @return Devuelve True=Tutor por Curso - False=Tutor con varios cursos
     */
    public boolean isTutorPorCurso() {
        return tutorPorCurso;
    }

    /**
     * Setter tutor por curso
     * @param tutorPorCurso True=Tutor por Curso - False=Tutor con varios cursos
     */
    public void setTutorPorCurso(boolean tutorPorCurso) {
        this.tutorPorCurso = tutorPorCurso;
    }
    
    /**
     * Getter logo de la empresa
     * @return  Devuelve el logo de la empresa
     */
    public byte[] getLogo() {
        return logo;
    }

    /**
     * Setter del logo de la empresa
     * @param logo  Logo de la empresa
     */
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
