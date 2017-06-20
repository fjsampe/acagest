package capaNegocio;

/**
 * Clase ShareData
 * Esta clase contiene los datos de intercambio 
 * 
 * Variables estáticas:
 *  SERVER      : Nombre del servidor que posee la BBDD
 *  PORT        : Puerto acceso a la BBDD
 *  USER        : Usuario logonado
 *  EMPRESA     : Datos de la Empresa
 *  MODOEDICION : Indica si estamos editando ó añadiendo un dato
 * 
 * 
 * Métodos:
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class ShareData {
    public static String SERVER;
    public static String PORT;
    public static Usuario USER;
    public static Empresa EMPRESA;
    public static boolean MODOEDICION;
}
