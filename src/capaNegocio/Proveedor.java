package capaNegocio;

/**
 * Clase Proveedor
 * Esta clase contiene los parámetros de Proveedores
 * 
 * Atributos:
 *  cifDni          : Dni del proveedor
 *  nombre          : Nombre del proveedor
 *  apellidos       : Apellidos del proveedor
 *  telefono        : Teléfono fijo del proveedor
 *  movil           : Teléfono móvil del proveedor
 *  ctaBanco        : Cuenta bancaria del proveedor
 *  email           : Cuenta de correo
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre del profesor
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Proveedor {
    private String cifDni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String movil;
    private String ctaBanco;
    private String email;  

    /**
     * Constructor parametrizado
     * 
     * @param cifDni        Dni del proveedor
     * @param nombre        Nombre del proveedor
     * @param apellidos     Apeliidos del proveddor
     * @param telefono      Teléfono fijo del proveedor
     * @param movil         Teléfono móvil del proveedor
     * @param ctaBanco      Cuenta bancaria del proveedor
     * @param email         Cuenta email
     */
    public Proveedor(String cifDni, String nombre, String apellidos, String telefono, String movil, String ctaBanco, String email) {
        this.cifDni = cifDni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.movil = movil;
        this.ctaBanco = ctaBanco;
        this.email = email;
    }
    
    /**
     * Getter cif del proveedor
     * @return  Devuelve el cif del proveedor
     */
    public String getCifDni() {
        return cifDni;
    }

    /**
     * Setter dni del proveedor
     * @param cifDni    Cif del proveedor
     */
    public void setCifDni(String cifDni) {
        this.cifDni = cifDni;
    }

    /**
     * Getter nombre del proveedor
     * @return  Devuelve el nombre del proveedor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter nombre del proveedor
     * @param nombre    Nombre del proveedor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter apellidos del proveedor
     * @return  Devuelve los apellidos del proveedor 
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Setter apellidos del proveedor
     * @param apellidos     Apellidos del proveedor
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Getter teléfono fijo del proveedor
     * @return  Devuelve el teléfono fijo del proveedor
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Setter teléfono fijo del proveedor
     * @param telefono  Teléfono fijo del proveedor
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Getter teléfono móvil del proveedor
     * @return  Devuelve el teléfono móvil del proveedor
     */
    public String getMovil() {
        return movil;
    }

    /**
     * Setter teléfono móvil del proveedor
     * @param movil Teléfono móvil del proveedor
     */
    public void setMovil(String movil) {
        this.movil = movil;
    }

    /**
     * Getter cuenta banco del proveedor
     * @return  Devuelve la cuenta del banco del proveedor
     */
    public String getCtaBanco() {
        return ctaBanco;
    }

    /**
     * Setter cuenta banco del proveedor
     * @param ctaBanco  Cuenta banco del proveedor
     */
    public void setCtaBanco(String ctaBanco) {
        this.ctaBanco = ctaBanco;
    }

    /**
     * Getter email del proveedor
     * @return  Devuelve el email del proveedor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter email del proveedor
     * @param email    Email del proveedor
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Método que devuelve la cadena con el nombre y apellidos del proveedor
     * @return  Devuelve el nombre completo del proveedor
     */
    @Override
    public String toString() {
        return apellidos+", "+nombre;
    }
}



