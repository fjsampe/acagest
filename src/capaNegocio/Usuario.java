package capaNegocio;

/**
 * Clase Usuario
 * Esta clase contiene los parámetros de Usuarios
 * 
 * Atributos:
 *  username    : Nombre de usuario
 *  password    : Password del usuario
 *  nivel       : Nivel de Acceso
 * 
 * Constructores, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre del usuario
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Usuario {
    private String username;
    private String password;
    private int nivel;

    /**
     * Constructor parametrizado
     * @param username  Nombre de usuario
     * @param password  Password del usuario
     */
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor parametrizado
     * @param username  Nombre de usuario
     * @param password  Password del usuario
     * @param nivel     Nivel de acceso
     */
    public Usuario(String username, String password, int nivel) {
        this.username = username;
        this.password = password;
        this.nivel = nivel;
    }
    
    /**
     * Setter nivel del acceso
     * @param nivel     Nivel de acceso
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    /**
     * Getter nombre de usuario
     * @return  Devuelve el nombre del usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter password del usuario
     * @return  Devuelve la contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter del nivel de acceso
     * @return  Devuelve el nivel de acceso
     */
    public int getNivel() {
        return nivel;
    }
    
    /**
     * Método que devuelve una cadena con el nombre del usuario
     * @return  Devuelve el nombre del usuario
     */
    @Override
    public String toString(){
        return username;
    }
}
