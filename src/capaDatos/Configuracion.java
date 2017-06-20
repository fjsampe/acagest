package capaDatos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Clase Configuración
 * Esta clase gestiona los parámetros de configuración para el acceso a la BBDD
 * 
 * Atributos:
 *  
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  validarParametrosAccesoBD   : Verifica que existan los parámetros de conexión.
 *  obtenerParametrosConfig     : Obtiene los parámetros de conexión a la BBDD
 *  guardarParametrosConfig     : Almacena en el fichero configuration.properties 
 *                                los parámetros de conexión a la BBDD
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Configuracion {
    
    /***
     * Función que verifica si existen los parámetros de conexión a la BBDD
     * @return      Retorna True si existen o false si no existem
     */
    public static boolean validarParametrosAccesoBD(){
        boolean accesoOK=true;
        Properties propiedades = new Properties();
        try (InputStream entrada=new FileInputStream("configuracion.properties")){
            propiedades.load(entrada);
            if (propiedades.size()<1) accesoOK=false;
        } catch (IOException ex) {
            accesoOK=false;
        }
        return accesoOK;
    }
    
    /***
     * Método que obtiene los parámetros de conexión a la BBDD
     * @return      Retorna los parámetros
     */
    public static Properties obtenerParametrosConfig(){
        Properties propiedades = new Properties();
        try(InputStream entrada=new FileInputStream("configuracion.properties")) {
            propiedades.load(entrada);
        } catch (IOException ex) {
            propiedades=null;
        }
        return propiedades;
    }

    /***
     * Función que almacena en el fichero configuration.properties los parámetros
     * de conexión a la BBDD
     * @param servidor  Nombre del servidor donde se ubica la BBDD
     * @param puerto    Puerto de acceso a la BBDD
     * @param password  Contraseña del usuario administrador de la BBDD
     * @return          Returna True si se han guardado correctamente, False en 
     *                  caso contrario
     */
    public static boolean guardarParametrosConfig(String servidor, String puerto, String password){
        Properties propiedades = new Properties();
        boolean grabacionOK=true;
        try (OutputStream salida=new FileOutputStream("configuracion.properties")) {
            // asignamos los valores a las propiedades
            propiedades.setProperty("servidor", servidor);
            propiedades.setProperty("puerto", puerto);
            propiedades.setProperty("clave", password);
            propiedades.store(salida, null);
        } catch (IOException ex) {
            grabacionOK=false;
        }
        return grabacionOK;
    }
}
