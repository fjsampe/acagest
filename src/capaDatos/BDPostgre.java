package capaDatos;

import capaPresentacion.resources.Mensajes;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase BDPostgre
 * Esta clase gestiona el acceso a la BBDD PostgreSQL
 * 
 * Atributos:
 *  server          : Nombre del servidor donde se ubica la BBDD
 *  puerto          : Puerto de acceso a la BBDD
 *  baseDatos       : Nombre de la BBDD
 *  usuario         : Usuario administrador de la BBDD
 *  password        : Password usuario administrador de la BBDD
 *  estadoConexion  : Estado de la conexion (True = ok False = no hay conexión)
 *  conexion        : Conexión de la BBDD
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  cerrarConexion  : Cierra la conexión y retorna si ha sido exitoso o no.
 *  ejecutarDDL     : Ejecuta una sentencia SQL DDL
 *  ejecutarDDLIMG  : Ejecuta una sentencia SQL DDL para añadir/insertar imagenes
 *  ejecutarDML     : Ejecuta una sentencia SQL DML y retorna el resultado de la
 *                    consulta.
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class BDPostgre {
    private String server;
    private String puerto;
    private String baseDatos;
    private String usuario;
    private String password;
    private boolean estadoConexion;
    private Connection conexion;

    /**
     * Constructor parametrizado. Abre la conexión con la BBDD
     * @param server        Nombre del servidor donde se ubica la BBDD
     * @param puerto        Puerto de acceso a la BBDD
     * @param baseDatos     Nombre de la BBDD
     * @param usuario       Usuario administrador de la BBDD
     * @param password      Password usuario administrador de la BBDD
     */
    public BDPostgre(String server, String puerto, String baseDatos, String usuario, String password) {
        this.server = server;
        this.puerto = puerto;
        this.usuario = usuario;
        this.password = password;
        this.estadoConexion=false;
        try {
            this.conexion = DriverManager.getConnection
                ("jdbc:postgresql://"+server+":"+puerto+"/"+baseDatos,usuario,password);
            this.estadoConexion = true;
        } catch (SQLException ex) {
            Mensajes.msgError("ERROR: "+ex.getSQLState(),ex.getMessage());
        } catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());
        }
    }
    
    /**
     * Getter isEstadoConexion
     * @return  Devuelve True si existe conexión a la BBDD o False en caso contrario
     */
    public boolean isEstadoConexion() {
        return estadoConexion;
    }

    /**
     * Getter getConexion
     * @return  Devuelve la conexión con la BBDD
     */
    public Connection getConexion() {
        return conexion;
    }
    
    /**
     * Método que cierra la conexión con la BBDD
     * @param con   Conexión con la BBDD
     * @return      Devuelve True si se ha cerrado correctamente ó False si ha 
     *              existido algún error
     */
    public boolean cerrarConexion(Connection con){
        try {
            con.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Método que ejecuta una sentencia SQL DDL
     * @param sql   Sentencia SQL
     * @return      Retorna el número de filas afectadas
     */
    public int ejecutarDDL(String sql) {    
        int filasAfectadas=0;
        String mensajeError="";
        try {
            Statement statement = conexion.createStatement();
            filasAfectadas=statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println("ERROR: Ejecutar DDL "+sql);
            System.err.println("ERROR: Capa de datos | BDPosrgre.java | ejecutarDDL "+ex.getMessage());
            System.err.println("ERROR: "+ex.getSQLState());
            switch(ex.getSQLState()){
                case "23503":   mensajeError="Registro utilizado en otra tabla. No puede ser borrado";    
                                break;
                default:        mensajeError=ex.getSQLState()+ex.getMessage();
                                break;
            }
            
            Mensajes.msgError("ERROR",mensajeError);
            filasAfectadas=-1;
        }
        return filasAfectadas;    
    }
    
    /**
     * Método que ejecuta una sentencia SQL DDL en la que usamos un array de bytes
     * @param sql       Sentencia SQL
     * @param bytes     Array de bytes (imagen)
     */
    public void ejecutarDDLIMG(String sql, byte[] bytes){
        try {
            PreparedStatement ps=conexion.prepareStatement(sql);
            InputStream is = new ByteArrayInputStream(bytes);
            ps.setBinaryStream(1,is,bytes.length);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("ERROR: Capa de datos | BDPosrgre.java | ejecutarDDLIMG "+ex.getMessage());
        }
    }
    
    /**
     * Método que ejecuta una sentencia SQL DML en la que recuperamos datos de la BBDD 
     * @param sql       Sentencia SQL
     * @return          Retorna el resultado de la consulta
     */
    public ResultSet ejecutarDML(String sql)
    {
       ResultSet resultado=null;
       try {
          PreparedStatement preparedStat = conexion.prepareStatement(sql);
          resultado = preparedStat.executeQuery();
          return resultado;
       } catch (SQLException ex) {
          System.err.println("ERROR: Ejecutar DML "+sql);
          System.err.println("ERROR: "+ex.getMessage());
          System.err.println("ERROR: "+ex.getSQLState());
       }
       return resultado;
    }
    
    
}