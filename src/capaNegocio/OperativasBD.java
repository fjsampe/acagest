package capaNegocio;

import capaDatos.BDPostgre;
import static capaDatos.Configuracion.guardarParametrosConfig;
import static capaDatos.Configuracion.obtenerParametrosConfig;
import static capaDatos.Configuracion.validarParametrosAccesoBD;
import capaPresentacion.resources.Campos;
import capaPresentacion.resources.Mensajes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Clase OperativasBD
 * En esta clase se realizan todas las operativas de acceso y validación a la 
 * base de datos para la obtención de los datos solicitados o aportados para su
 * grabación en la BBDD, así como la inicialización de tablas de la BBDD.
 * 
 * Atributos:
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  validarLogin                        : Valida el acceso de los usuarios.
 *  extraerUsuarios                     : Extrae todo los usuarios que tienen acceso a la aplicación.
 *  insertarUsuario                     : Inserta un usuario.
 *  modificarUsuario                    : Modifica los valores de un usuario.
 *  borrarUsuario                       : Borrar un usuario.
 *  extraerEmpresa                      : Extrae los valores de la empresa configurada.
 *  cambiarEmpresa                      : Modifica los valores de la empresa configurada.
 *  extraerAlumnos                      : Extrae una lista con todos los alumnos.
 *  extraerID                           : Extrae la clave generada automáticamente.
 *  insertarAlumno                      : Inserta un alumno.
 *  modificarAlumno                     : Modifica los valores de un alumno.
 *  borrarAlumno                        : Borra un alumno.
 *  extraerProfesores                   : Extrae una lista con todos los profesores.
 *  extraerProfesoresNoUsadosEnCursos   : Extrae una lista con todos los profesores que no son tutores.
 *  insertarProfesor                    : Inserta un profesor.
 *  modificarProfesor                   : Modifica los valores de un profesor.
 *  borrarProfesor                      : Borra un profesor.
 *  extraerAsignaturas                  : Extrae una lista con todas las asignaturas.
 *  insertarAsignatura                  : Inserta un asignatura.
 *  modificarAsignatura                 : Modifica los valores de una asignatura
 *  borrarAsignatura                    : Borra una asignatura.
 *  extraerCursos                       : Extrae una lista con todos los cursos.
 *  insertarCurso                       : Inserta un curso.
 *  modificarCurso                      : Modifica los valores de un curso.
 *  borrarCurso                         : Borra un curso.
 *  extraeAulas                         : Extrae lista de aulas existentes.
 *  insertarAula                        : Inserta un aula.
 *  modificarAula                       : Modifica los valores de un aula.
 *  borrarAula                          : Borra un aula.
 *  extraerProveedores                  : Extrae lista de los proveedores existente.
 *  insertarProveedor                   : Inserta un proveedor.
 *  modificarProveedor                  : Modifica los valores de un proveedor.
 *  borrarProveedor                     : Borra un proveedor.
 *  extraerFichasMatriculas             : Extrae lista de las fichas de las matrículas.
 *  insertarMatriculaciones             : Inserta una ficha de matrícula.
 *  borrarMatriculaciones               : Borra una ficha de matrícula.
 *  extraerFichasPlanificador           : Extrae lista de las fichas del planificador horas
 *                                        aulas/asignaturas/profesores/horas.
 *  insertarPlanes                      : Inserta un plan horario aulas/asignaturas/profesores/horas-
 *  borrarPlanes                        : Borra un plan horario de un profesor-
 *  extraerFichasExamenes               : Extrae la lista de los examenes de cada alumno.
 *  extraerAsignaturasMatriculadasPorAlumno : Extrae la lista de las asignaturas matriculadas por el alummno.    
 *  extraerFichasGastos                 : Extrae la lista con los gastos de cada proveedor.
 * 
 *  cambiarImagen                       : Cambia la imagen indicada en la secuencia SQL.
 *  ejecucionSentenciaDDL               : Ejecuta una sentencia SQL DDL.
 *  ejecucionSentenciaDML               : Ejecuta una sentencia SQL DML.
 *  validarParametros                   : Validamos los parámetros de configuración de la aplicación.
 *  validarAccesoServerBD               : Validamos el acceso del usuario a la aplicación.
 * 
 *  crearUsuarioBD                      : Crea el usuario para usar la BBDD dbacademia.
 *  crearTablasBD                       : Crea las tablas de la BBDD dbacademia.
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.2
 */
public class OperativasBD {
    
    /**
     * Función validarLogin
     * Se encarga de validar los parámetros aportados por el usuario en la BBDD
     * 
     * @param usuario   Usuario
     * @return          Devuelve booleano (true=acceso OK, false=error acceso)
     */
    public static boolean validarLogin(Usuario usuario){
        boolean validacion=false;
        ResultSet resultado=ejecucionSentenciaDML("select * from usuarios where username='"+usuario.getUsername()+
                    "' and password='"+DigestUtils.sha256Hex(usuario.getPassword())+"';");
        try {
            while(resultado.next()){
                validacion=true;
                ShareData.USER=new Usuario(usuario.getUsername(),usuario.getPassword(),Integer.parseInt(resultado.getString(3)));
            }
        } catch (SQLException ex) {
            System.err.println("ERROR: Capa Negocio - AccesoBD.java "+ex.getMessage());
        }
        return validacion;
    }
    
    /**
     * Función que extrae la lista de usuarios dados de alta en la BBDD
     * @return  Devuelve la lista de usuarios existentes 
     */
    public static List<Usuario> extraerUsuarios(){
        List<Usuario> listaUsuarios=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select * from usuarios;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    listaUsuarios.add(new Usuario(resultado.getString(1),
                        resultado.getString(2),Integer.parseInt(resultado.getString(3))));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaUsuarios;
    }
    
    /**
     * Función que inserta un usuario en la BBDD
     * @param usuario   Usuario
     * @return          Devuelve booleano (true=inserción OK, false=error insercción)
     */
    public static boolean insertarUsuario(Usuario usuario){
        return ejecucionSentenciaDDL("INSERT INTO usuarios VALUES ('"+
                usuario.getUsername()+"','"+DigestUtils.sha256Hex(usuario.getPassword())+"',"
                + usuario.getNivel()+");") == 1;
    }
    
    /**
     * Función que modifica un usuario en la BBDD
     * @param usuario   Usuario
     * @return          Devuelve booleano (true=acceso OK, false=error borrado)
     */
    public static boolean modificarUsuario(Usuario usuario) {
        return ejecucionSentenciaDDL("UPDATE usuarios SET password='"+DigestUtils.sha256Hex(usuario.getPassword())+"',"
                + " nivel="+usuario.getNivel()+" WHERE username='"+
                usuario.getUsername()+"';") == 1;
    }
    
    /**
     * Función que borra un usuario en la BBDD
     * @param usuario   Usuario
     * @return          Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarUsuario(Usuario usuario){
        return ejecucionSentenciaDDL("DELETE FROM usuarios WHERE Username='"+
                usuario.getUsername()+"';") == 1;
    }
    
    /**
     * Función que extrae los datos de la empresa en la BBDD
     * @return  Devuelve clase Empresa
     */
    public static Empresa extraerEmpresa(){
        Empresa empresa=new Empresa();
        ResultSet resultado=ejecucionSentenciaDML("SELECT * FROM empresas WHERE codigo=1;");
        if (resultado!=null){
            try {
                while (resultado.next()) {
                    empresa.setNif(resultado.getString(2)!=null?resultado.getString(2):"");
                    empresa.setNombre(resultado.getString(3)!=null?resultado.getString(3):"");
                    empresa.setDomicilio(resultado.getString(4)!=null?resultado.getString(4):"");
                    empresa.setPoblacion(resultado.getString(5)!=null?resultado.getString(5):"");
                    empresa.setCp(resultado.getString(6)!=null?resultado.getString(6):"");
                    empresa.setProvincia(resultado.getString(7)!=null?resultado.getString(7):"");
                    empresa.setTelefono(resultado.getString(8)!=null?resultado.getString(8):"");
                    empresa.setEmail(resultado.getString(9)!=null?resultado.getString(9):"");
                    empresa.setLogo(resultado.getBytes(10));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return empresa;
    }
    
    /**
     * Función que modifica los valores de la empresa en la BBDD
     * @param empresa   Empresa
     * @return          Devuelve booleano (true=modificación OK, false=error modificación)    
     */
    public static boolean cambiarEmpresa(Empresa empresa) {
        ShareData.EMPRESA=empresa;
        if (empresa.getLogo()!=null) cambiarImagen("UPDATE empresas SET Logo=? WHERE Codigo=1",ShareData.EMPRESA.getLogo());
        return ejecucionSentenciaDDL("UPDATE empresas SET Nifnie='"+empresa.getNif()+"',Nombre='"+
                    empresa.getNombre()+"',Domicilio='"+empresa.getDomicilio()+
                    "',Poblacion='"+empresa.getPoblacion()+
                    "',Cp='"+empresa.getCp()+
                    "',Provincia='"+empresa.getProvincia()+
                    "',Telefono='"+empresa.getTelefono()+
                    "',Email='"+empresa.getEmail()+
                    "' WHERE Codigo=1;")==1;
    }
    
    /**
     * Función que extrae la lista de alumnos existentes en la BBDD
     * @return  Devuelve la lista de alumnos existentes
     */
    public static List<Alumno> extraerAlumnos(){
        LocalDate fecha;
        Alumno a;
        List<Alumno> listaAlumnos=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select * from alumnos;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    fecha=(resultado.getString(9)==null?null:Campos.stringToFecha(resultado.getString(9)));
                    a=new Alumno(
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getString(3)!=null?resultado.getString(3):"",
                        resultado.getString(4)!=null?resultado.getString(4):"",
                        resultado.getString(5)!=null?resultado.getString(5):"",
                        resultado.getString(6)!=null?resultado.getString(6):"",
                        resultado.getString(7)!=null?resultado.getString(7):"",
                        resultado.getString(8)!=null?resultado.getString(8):"",
                        fecha,
                        resultado.getString(10)!=null?resultado.getString(10):"",
                        resultado.getString(11)!=null?resultado.getString(11):"",
                        resultado.getString(12)!=null?resultado.getString(12):"",
                        resultado.getString(13)!=null?resultado.getString(13):"",
                        resultado.getString(14)!=null?resultado.getString(14):"",
                        resultado.getString(15)!=null?resultado.getString(15):"",
                        resultado.getString(16)!=null?resultado.getString(16):"",
                        resultado.getString(17)!=null?resultado.getString(17):"",
                        resultado.getString(18)!=null?resultado.getString(18):"",
                        resultado.getString(19)!=null?resultado.getString(19):"",
                        resultado.getString(20)!=null?resultado.getString(20):"",
                        resultado.getString(21)!=null?resultado.getString(21):"",
                        resultado.getBytes(22));
                    a.setCodigo(resultado.getInt(1));
                    listaAlumnos.add(a);
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaAlumnos;
    }
    
    /**
     * Función que extrae la clave generada automáticamente en la inserción de este
     * @param resultado     Result de la inserción del alumno
     * @return              Devuelve el id Alumno (AUTOINCREMENTAL)
     */
    private static int extraerID(ResultSet resultado){
        int ultimoId=0;
        if (resultado!=null){
            try {
                while(resultado.next()){
                    ultimoId=resultado.getInt(1);
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        } 
        return ultimoId;
    }
    
    /**
     * Función que inserta un alumno en la BBDD
     * @param alumno    Alumno
     * @return  Devuelve el número del campo autoincrementado
     */
    public static int insertarAlumno(Alumno alumno){
        int ultimoId=0;
        String sentencia="INSERT INTO alumnos VALUES (default,'"+
            alumno.getNia()+"','"+alumno.getNombre()+"','"+alumno.getApellidos()+
            "','"+alumno.getDomicilio()+"','"+alumno.getPoblacion()+
            "','"+alumno.getCp()+"','"+alumno.getProvincia()+"',"+
            (alumno.getNacimiento()==null?null:"'"+alumno.getNacimiento()+"'")+
            ",'"+alumno.getTelefono()+"','"+alumno.getMovil()+
            "','"+alumno.getPadre()+"','"+alumno.getDniPadre()+
            "','"+alumno.getTelefonoPadre()+"','"+alumno.getEmailPadre()+
            "','"+alumno.getMadre()+"','"+alumno.getDniMadre()+
            "','"+alumno.getTelefonoMadre()+"','"+alumno.getEmailMadre()+
            "','"+alumno.getCentro()+"','"+alumno.getObservaciones()+
            "') RETURNING codAlumno;";
        ResultSet resultado=ejecucionSentenciaDML(sentencia); 
        ultimoId=extraerID(resultado);
        if (alumno.getFoto()!=null) cambiarImagen("UPDATE alumnos SET Foto=? WHERE CodAlumno="+
            ultimoId,alumno.getFoto());
        return ultimoId;
    }
    
    /**
     * Función que modifica los valores de un alumno en la BBDD
     * @param alumno    Alumno
     * @return          Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarAlumno(Alumno alumno) {
        if (alumno.getFoto()!=null) cambiarImagen("UPDATE alumnos SET Foto=? WHERE CodAlumno="+alumno.getCodigo(),alumno.getFoto());
        return ejecucionSentenciaDDL("UPDATE alumnos SET Nia='"+alumno.getNia()+"',"+
                    " Nombre='"+alumno.getNombre()+"',"+
                    " Apellidos='"+alumno.getApellidos()+"',"+
                    " Domicilio='"+alumno.getDomicilio()+"',"+
                    " Poblacion='"+alumno.getPoblacion()+"',"+
                    " Cp='"+alumno.getCp()+"',"+
                    " Provincia='"+alumno.getProvincia()+"',"+
                    " FechaNacimiento="+(alumno.getNacimiento()==null?null:"'"+alumno.getNacimiento()+"'")+","+
                    " Telefono='"+alumno.getTelefono()+"',"+
                    " Movil='"+alumno.getMovil()+"',"+
                    " Padre='"+alumno.getPadre()+"',"+
                    " DniPadre='"+alumno.getDniPadre()+"',"+
                    " TelefonoPadre='"+alumno.getTelefonoPadre()+"',"+
                    " Email1='"+alumno.getEmailPadre()+"',"+
                    " Madre='"+alumno.getMadre()+"',"+
                    " DniMadre='"+alumno.getDniMadre()+"',"+
                    " TelefonoMadre='"+alumno.getTelefonoMadre()+"',"+
                    " Email2='"+alumno.getEmailMadre()+"',"+
                    " CentroEstudio='"+alumno.getCentro()+"',"+
                    " Observaciones='"+alumno.getObservaciones()+"'"+
                    " WHERE CodAlumno="+
                    alumno.getCodigo()+";")==1;
    }
    
    /**
     * Función que borra un alumno en la BBDD
     * @param alumno    Alumno
     * @return          Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarAlumno(Alumno alumno){
        return ejecucionSentenciaDDL("DELETE FROM alumnos WHERE Codalumno='"+
                alumno.getCodigo()+"';") == 1;
    }
    
    /**
     * Función que extrae la lista de profesores existentes en la BBDD
     * @return  Devuelve la lista de profesores existentes
     */
    public static List<Profesor> extraerProfesores(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha;
        String fechaString;
        List<Profesor> listaProfesores=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select profesores.cifdni,nombre,"
            + "apellidos,domicilio,poblacion,cp,provincia,fechanacimiento,telefono,"
            + "movil,ctabancaria,email,foto from proveedores,profesores where "
            + "profesores.cifdni=proveedores.cifdni;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    fecha=(resultado.getString(8)==null?null:Campos.stringToFecha(resultado.getString(8)));
                    listaProfesores.add(new Profesor(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getString(3)!=null?resultado.getString(3):"",
                        resultado.getString(4)!=null?resultado.getString(4):"",
                        resultado.getString(5)!=null?resultado.getString(5):"",
                        resultado.getString(6)!=null?resultado.getString(6):"",
                        resultado.getString(7)!=null?resultado.getString(7):"",
                        fecha,
                        resultado.getString(9)!=null?resultado.getString(9):"",
                        resultado.getString(10)!=null?resultado.getString(10):"",
                        resultado.getString(11)!=null?resultado.getString(11):"",
                        resultado.getString(12)!=null?resultado.getString(12):"",
                        resultado.getBytes(13)));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaProfesores;
    }
    
    /**
     * Función que extrae la lista de profesores no puestos como tutores en la BBDD
     * @return  Devuelve la lista de profesores no puestos como tutores
     */
    public static List<Profesor> extraerProfesoresNoUsadosEnCursos(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha;
        String fechaString;
        List<Profesor> listaProfesores=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select profesores.cifdni,nombre,"+
            "apellidos,domicilio,poblacion,cp,provincia,fechanacimiento,"+
            "telefono,movil,ctabancaria,email,foto from proveedores " +
            "join profesores on profesores.cifdni=proveedores.cifdni " +
            "where profesores.cifdni not in (select cifdni from cursos);");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    fecha=(resultado.getString(8)==null?null:Campos.stringToFecha(resultado.getString(8)));
                    listaProfesores.add(new Profesor(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getString(3)!=null?resultado.getString(3):"",
                        resultado.getString(4)!=null?resultado.getString(4):"",
                        resultado.getString(5)!=null?resultado.getString(5):"",
                        resultado.getString(6)!=null?resultado.getString(6):"",
                        resultado.getString(7)!=null?resultado.getString(7):"",
                        fecha,
                        resultado.getString(9)!=null?resultado.getString(9):"",
                        resultado.getString(10)!=null?resultado.getString(10):"",
                        resultado.getString(11)!=null?resultado.getString(11):"",
                        resultado.getString(12)!=null?resultado.getString(12):"",
                        resultado.getBytes(13)));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaProfesores;
    }
    
    /**
     * Función que inserta un profesor en la BBDD
     * @param profesor  Profesor
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarProfesor(Profesor profesor){
        boolean retorno=false;
        if (ejecucionSentenciaDDL("INSERT INTO proveedores VALUES ('"+
                profesor.getCifDni()+"','"+profesor.getNombre()+"','"+profesor.getApellidos()+
                "','"+profesor.getTelefono()+"','"+profesor.getMovil()+
                "','"+profesor.getCtaBanco()+"','"+profesor.getEmail()+
                "');") == 1 &&
                ejecucionSentenciaDDL("INSERT INTO profesores VALUES ('"+
                profesor.getCifDni()+"','"+profesor.getDomicilio()+"','"+profesor.getPoblacion()+
                "','"+profesor.getCp()+"','"+profesor.getProvincia()+"',"+
                (profesor.getFechaNacimiento()==null?null:"'"+profesor.getFechaNacimiento()+"'")+
                ");") == 1 ){
                if (profesor.getFoto()!=null) cambiarImagen("UPDATE profesores SET Foto=? WHERE CifDni='"+
                    profesor.getCifDni()+"'",profesor.getFoto());
                retorno=true;    
        }
        return retorno;
    }
    
    /**
     * Función que modifica los valores de un profesor en la BBDD
     * @param profesor  Profesor
     * @return          Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarProfesor(Profesor profesor) {
        if (profesor.getFoto()!=null) cambiarImagen("UPDATE profesores SET Foto=? WHERE CifDni='"+
                profesor.getCifDni()+"'",profesor.getFoto());
        return(ejecucionSentenciaDDL("UPDATE proveedores SET Nombre='"+profesor.getNombre()+"',"+
                " Apellidos='"+profesor.getApellidos()+"',"+
                " Telefono='"+profesor.getTelefono()+"',"+
                " Movil='"+profesor.getMovil()+"',"+
                " CtaBancaria='"+profesor.getCtaBanco()+"',"+
                " Email='"+profesor.getEmail()+"'"+
                " WHERE CifDni='"+profesor.getCifDni()+"';") == 1 &&
                ejecucionSentenciaDDL("UPDATE profesores SET Domicilio='"+profesor.getDomicilio()+"',"+
                " Poblacion='"+profesor.getPoblacion()+"',"+
                " Cp='"+profesor.getCp()+"',"+
                " Provincia='"+profesor.getProvincia()+"',"+
                " FechaNacimiento="+(profesor.getFechaNacimiento()==null?null:"'"+profesor.getFechaNacimiento()+"'")+
                " WHERE CifDni='"+profesor.getCifDni()+"';") == 1);
    }
    
    /**
     * Función que borra un profesor en la BBDD
     * @param profesor  Profesor
     * @return          Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarProfesor(Profesor profesor){
        return ejecucionSentenciaDDL("DELETE FROM profesores WHERE cifdni='"+
                profesor.getCifDni()+"';")==1 && ejecucionSentenciaDDL("DELETE FROM proveedores WHERE cifdni='"+
                profesor.getCifDni()+"';") == 1;
    }
    
    /**
     * Función que extrae la lista de asignaturas existentes en la BBDD
     * @param curso Curso
     * @return Devuelve la lista de asignaturas existente
     */
    public static List<Asignatura> extraerAsignaturas(String curso){
        List<Asignatura> listaAsignaturas=new ArrayList<>();
        ResultSet resultado=null;
        if (curso==null){
            resultado=ejecucionSentenciaDML("select * from asignaturas;");
        }else{
            resultado=ejecucionSentenciaDML("select a.codasignatura,a.descripcion,"+
                    "a.cargahoras from asignaturas a " +
                    "left join contener c on a.codAsignatura=c.codAsignatura " +
                    "where c.codcurso='"+curso+"' order by a.codasignatura;");
        }
        if (resultado!=null){
            try {
                while(resultado.next()){
                    listaAsignaturas.add(new Asignatura(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getInt(3)));
                    
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaAsignaturas;
    }
    
    /**
     * Función que inserta una asignatura en la BBDD
     * @param asignatura    Asignatura
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarAsignatura(Asignatura asignatura){
        return ejecucionSentenciaDDL("INSERT INTO asignaturas VALUES ('"+
                asignatura.getCodigo()+"','"+asignatura.getNombre()+
                "',"+asignatura.getCargaHoras()+");") == 1;
    }
    
    /**
     * Función que modifica los valores de una asignatura en la BBDD
     * @param asignatura    Asignatura
     * @return              Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarAsignatura(Asignatura asignatura) {
        return ejecucionSentenciaDDL("UPDATE asignaturas SET Descripcion='"+asignatura.getNombre()+"',"+
                " CargaHoras="+asignatura.getCargaHoras()+
                " WHERE CodAsignatura='"+
                asignatura.getCodigo()+"';") == 1;
    }
    
    /***
     * Función que borra una asignatura en la BBDD
     * @param asignatura   Asignatura
     * @return             Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarAsignatura(Asignatura asignatura){
        return ejecucionSentenciaDDL("DELETE FROM asignaturas WHERE CodAsignatura='"+
                asignatura.getCodigo()+"';") == 1;
    }

    /**
     * Función que extrae la lista de cursos existentes en la BBDD
     * @return  Devuelve la lista de cursos existentes
     */
    public static List<Curso> extraerCursos(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha;
        String fechaStringNacimiento;
        String codigoCursoInicial="";
        String codigoCursoActual;
        String consultaSQL="";
        Curso curso=null;
        Asignatura asignatura;
        List<Curso> listaCursos=new ArrayList<>();
        List<Asignatura> listaAsignaturas=new ArrayList<>();
        consultaSQL="select c.codcurso,c.descripcion,c.importehora," +
                "c.pago,c.cifdni,p.nombre,p.apellidos,prof.domicilio," +
                "prof.poblacion,prof.cp,prof.provincia,prof.fechanacimiento," +
                "p.telefono,p.movil,p.ctabancaria,p.email,prof.foto,a.codasignatura,"+
                "a.descripcion,a.cargahoras from cursos c " +
                    "join proveedores p on c.cifdni=p.cifdni " +
                    "join profesores prof on c.cifdni=prof.cifdni " +
                    "full outer join contener on c.codcurso = contener.codcurso " +
                    "left join asignaturas a on contener.codasignatura = a.codasignatura " +
                    "order by c.codcurso, a.codasignatura;";
            
        ResultSet resultado=ejecucionSentenciaDML(consultaSQL);
        try {
            while(resultado.next()){
                codigoCursoActual=resultado.getString(1);

                    if (!codigoCursoInicial.equals(codigoCursoActual)){
                        if (!codigoCursoInicial.equals("")){
                            curso.setAsignaturas(listaAsignaturas);
                            listaCursos.add(curso);
                            listaAsignaturas=new ArrayList<>();
                        }   
                        fecha=(resultado.getString(12)==null?null:Campos.stringToFecha(resultado.getString(12)));
                    
                        curso= new Curso(
                            codigoCursoActual,
                            resultado.getString(2)!=null?resultado.getString(2):"",
                            resultado.getString(3)!=null?resultado.getDouble(3):0.0,
                            resultado.getString(4)!=null?resultado.getString(4):"",
                            new Profesor(resultado.getString(5)!=null?resultado.getString(5):"",
                                resultado.getString(6)!=null?resultado.getString(6):"",
                                resultado.getString(7)!=null?resultado.getString(7):"",
                                resultado.getString(8)!=null?resultado.getString(8):"",
                                resultado.getString(9)!=null?resultado.getString(9):"",
                                resultado.getString(10)!=null?resultado.getString(10):"",
                                resultado.getString(11)!=null?resultado.getString(11):"",
                                fecha,
                                resultado.getString(13)!=null?resultado.getString(13):"",
                                resultado.getString(14)!=null?resultado.getString(14):"",
                                resultado.getString(15)!=null?resultado.getString(15):"",
                                resultado.getString(16)!=null?resultado.getString(16):"",
                                resultado.getBytes(17)
                            ));
                            if(resultado.getString(18)!=null){
                                listaAsignaturas.add(
                                    new Asignatura(
                                        resultado.getString(18),
                                        resultado.getString(19),
                                        resultado.getInt(20)));
                            } 
                            codigoCursoInicial=codigoCursoActual;
                    }else{
                        if(resultado.getString(18)!=null){
                            listaAsignaturas.add(
                                new Asignatura(
                                    resultado.getString(18),
                                    resultado.getString(19),
                                    resultado.getInt(20)));
                        }
                    }
            }
            if (curso!=null){
                    curso.setAsignaturas(listaAsignaturas);
                    listaCursos.add(curso); 
            }
        } catch (SQLException ex) {
            System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
        }
        return listaCursos;
    }
    
    /**
     * Función que inserta un curso en la BBDD
     * @param curso    Curso
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarCurso(Curso curso){
        boolean retorno=false;
        if (ejecucionSentenciaDDL("INSERT INTO cursos VALUES ('"+
            curso.getCodigo()+"','"+curso.getDescripcion()+"',"+curso.getImporteHora()+
            ",'"+curso.getPago()+"','"+curso.getTutor().getCifDni()+
            "');") == 1){
            retorno=true; 
            if (curso.getAsignaturas().size()>0){
                for (Asignatura a: curso.getAsignaturas()){
                    if (ejecucionSentenciaDDL("INSERT INTO contener VALUES ('"+
                        curso.getCodigo()+"','"+a.getCodigo()+
                        "');") != 1){
                        // SE DEBE BORRAR LA INSERCION ANTERIOR
                        retorno= false;
                    }
                }
            }
        }
        return retorno;
    }
    
    /**
     * Función que modifica los valores de un curso en la BBDD
     * @param curso    Curso
     * @return         Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarCurso(Curso curso) {
        boolean retorno=false;
        if (ejecucionSentenciaDDL("UPDATE cursos SET Descripcion='"+curso.getDescripcion()+"',"+
                " ImporteHora="+curso.getImporteHora()+","+
                " Pago='"+curso.getPago()+"',"+
                " CifDni='"+curso.getTutor().getCifDni()+"'"+
                " WHERE CodCurso='"+curso.getCodigo()+"';") == 1){
                retorno=true;
                if (ejecucionSentenciaDDL("DELETE FROM contener WHERE CodCurso='"+
                    curso.getCodigo()+"';") == -1){
                    retorno=false;
                }else{
                    if (curso.getAsignaturas().size()>0){
                        for (Asignatura a: curso.getAsignaturas()){
                            if (ejecucionSentenciaDDL("INSERT INTO contener VALUES ('"+
                                curso.getCodigo()+"','"+a.getCodigo()+
                                "');") != 1){
                                        retorno = false;
                            }
                        }
                    }
                }        
        }
        return retorno;
    }
    
    /**
     * Función que borra un curso de la BBDD
     * @param curso Curso a borrar
     * @return      Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarCurso(Curso curso) {
        return ejecucionSentenciaDDL("DELETE FROM contener WHERE codcurso='"+
                curso.getCodigo()+"';")>-1 && ejecucionSentenciaDDL("DELETE FROM cursos WHERE codcurso='"+
                curso.getCodigo()+"';")==1;
    }
    
    /**
     * Función que extrae la lista de aulas existentes en la BBDD
     * @return  Devuelve la lista de aulas existentes
     */
    public static List<Aula> extraerAulas(){
        List<Aula> listaAulas=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select * from aulas;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    listaAulas.add(new Aula(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getString(2)!=null?resultado.getString(3):""));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaAulas;
    }
    
    /***
     * Función que inserta un aula en la BBDD
     * @param aula  Aula
     * @return      Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarAula(Aula aula){
        return ejecucionSentenciaDDL("INSERT INTO aulas VALUES ('"+
                aula.getCodigo()+"','"+aula.getDescripcion()+"','"+
                aula.getUbicacion()+"');")==1;
    }
    
    /**
     * Función que modifica los valores de un aula en la BBDD
     * @param aula  Aula
     * @return      Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarAula(Aula aula) {
        return ejecucionSentenciaDDL("UPDATE aulas SET Descripcion='"+aula.getDescripcion()+"',"+
                " Ubicacion='"+aula.getUbicacion()+"'"+
                " WHERE CodAula='"+
                aula.getCodigo()+"';")==1;
    }
    
    /**
     * Función que borra un aula en la BBDD
     * @param aula   Aula
     * @return       Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarAula(Aula aula){
        return ejecucionSentenciaDDL("DELETE FROM aulas WHERE CodAula='"+
                aula.getCodigo()+"';")==1;
    }
    
    
    /**
     * Función que extrae la lista de proveedores existentes en la BBDD
     * @return  Devuelve la lista de proveedores existentes
     */
    public static List<Proveedor> extraerProveedores(){
        List<Proveedor> listaProveedores=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select proveedores.cifdni,nombre,"
            + "apellidos,telefono,movil,ctabancaria,email from proveedores "
            + "left join profesores "
            + "on  proveedores.cifdni=profesores.cifdni "
            + "where profesores.cifdni is null;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    listaProveedores.add(new Proveedor(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getString(3)!=null?resultado.getString(3):"",
                        resultado.getString(4)!=null?resultado.getString(4):"",
                        resultado.getString(5)!=null?resultado.getString(5):"",
                        resultado.getString(6)!=null?resultado.getString(6):"",
                        resultado.getString(7)!=null?resultado.getString(7):""));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaProveedores;
    }
    
    /**
     * Función que inserta un proveedor en la BBDD
     * @param proveedor  Proveedor
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarProveedor(Proveedor proveedor){
        return (ejecucionSentenciaDDL("INSERT INTO proveedores VALUES ('"+
                proveedor.getCifDni()+"','"+proveedor.getNombre()+"','"+proveedor.getApellidos()+
                "','"+proveedor.getTelefono()+"','"+proveedor.getMovil()+
                "','"+proveedor.getCtaBanco()+"','"+proveedor.getEmail()+
                "');") == 1);    
    }
    
    /**
     * Función que modifica los valores de un proveedor en la BBDD
     * @param proveedor Proveedor
     * @return          Devuelve booleano (true=modificación OK, false=error modificación)
     */
    public static boolean modificarProveedor(Proveedor proveedor) {
        return(ejecucionSentenciaDDL("UPDATE proveedores SET Nombre='"+proveedor.getNombre()+"',"+
                " Apellidos='"+proveedor.getApellidos()+"',"+
                " Telefono='"+proveedor.getTelefono()+"',"+
                " Movil='"+proveedor.getMovil()+"',"+
                " CtaBancaria='"+proveedor.getCtaBanco()+"',"+
                " Email='"+proveedor.getEmail()+"'"+
                " WHERE CifDni='"+proveedor.getCifDni()+"';") == 1);
    }
    
    /**
     * Función que borra un proveedor en la BBDD
     * @param proveedor Proveedor
     * @return          Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarProveedor(Proveedor proveedor){
        return ejecucionSentenciaDDL("DELETE FROM proveedores WHERE cifdni='"+
                proveedor.getCifDni()+"';")==1;
    }
    
    /**
     * Función que extrae la lista de fichas de matrículas existentes en la BBDD
     * @return  Devuelve la lista de fichas de matrículas existentes
     */
    public static List<FichaMatricula> extraerFichasMatriculas(){
        int codigoMatriculaActual=-1;
        int codigoMatriculaInicial=-1;
        LocalDate fecha;
        FichaMatricula fichaMatricula=null;
        List<FichaMatricula> listaAlumnos=new ArrayList<>();
        List<Matricula> listaMatriculasPorAlumno = new ArrayList<>();
        Alumno a=null;
        Aula aula=null;
        
        
        ResultSet resultado=ejecucionSentenciaDML("select alumnos.CodAlumno,Nia,"
            + "Nombre,Apellidos,Domicilio,Poblacion,Cp,Provincia,FechaNacimiento,Telefono,"
            + "Movil,Padre,DniPadre,TelefonoPadre,Email1,Madre,DniMadre,TelefonoMadre,"
            + "Email2,CentroEstudio,Foto,cursos.CodCurso,cursos.Descripcion,"
            + "asignaturas.CodAsignatura,asignaturas.Descripcion,fechaInicio,fechaFin "
            + "from alumnos " 
            + "left join matricular on alumnos.codAlumno=matricular.codAlumno " 
            + "left join cursos on matricular.codCurso=cursos.codCurso "
            + "left join asignaturas on matricular.codAsignatura=asignaturas.codAsignatura "
            + "order by CodAlumno,CodCurso,CodAsignatura;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                    codigoMatriculaActual=resultado.getInt(1);
                    if (codigoMatriculaInicial!=codigoMatriculaActual){
                        if (codigoMatriculaInicial>-1){
                            fichaMatricula=new FichaMatricula(a,listaMatriculasPorAlumno);
                            listaAlumnos.add(fichaMatricula);
                            listaMatriculasPorAlumno=new ArrayList<>();
                        }   
                        fecha=(resultado.getString(9)==null?null:Campos.stringToFecha(resultado.getString(9)));
                    
                        a=new Alumno(
                            resultado.getString(2)!=null?resultado.getString(2):"",  //Nia
                            resultado.getString(3)!=null?resultado.getString(3):"",  //Nombre
                            resultado.getString(4)!=null?resultado.getString(4):"",  //Apellidos
                            resultado.getString(5)!=null?resultado.getString(5):"",  //Domicilio
                            resultado.getString(6)!=null?resultado.getString(6):"",  //Poblacion
                            resultado.getString(7)!=null?resultado.getString(7):"",  //Cp
                            resultado.getString(8)!=null?resultado.getString(8):"",  //Provincia
                            fecha,  //FechaNacimiento
                            resultado.getString(10)!=null?resultado.getString(10):"",//Telefono
                            resultado.getString(11)!=null?resultado.getString(11):"",//Movil
                            resultado.getString(12)!=null?resultado.getString(12):"",//Padre
                            resultado.getString(13)!=null?resultado.getString(13):"",//DniPadre    
                            resultado.getString(14)!=null?resultado.getString(14):"",//TelefonoPadre
                            resultado.getString(15)!=null?resultado.getString(15):"",//Email1
                            resultado.getString(16)!=null?resultado.getString(16):"",//Madre
                            resultado.getString(17)!=null?resultado.getString(17):"",//DniMadre
                            resultado.getString(18)!=null?resultado.getString(18):"",//TelefonoMadre
                            resultado.getString(19)!=null?resultado.getString(19):"",//Email2
                            resultado.getString(20)!=null?resultado.getString(20):"",//CentroEstudios
                            "",                                                      //Observaciones
                            resultado.getBytes(21));                                  //Foto   
                            a.setCodigo(resultado.getInt(1));
                        
                        codigoMatriculaInicial=codigoMatriculaActual;
                    } 
                    if (resultado.getString(22)!=null){
                            Matricula m=new Matricula(resultado.getString(22),
                                resultado.getString(23),resultado.getString(24),
                                resultado.getString(25),
                                LocalDate.parse(resultado.getString(26)),
                                LocalDate.parse(resultado.getString(27)));
                            listaMatriculasPorAlumno.add(m);
                    }
                }
                if (listaAlumnos!=null){
                    fichaMatricula=new FichaMatricula(a,listaMatriculasPorAlumno);
                    listaAlumnos.add(fichaMatricula); 
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaAlumnos;
    }
    
    /**
     * Función que inserta una ficha de matrícula en la BBDD
     * @param fichaMatricula    Ficha de matrícula
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarMatriculaciones(FichaMatricula fichaMatricula) {
        boolean estado=true;
        int codigoAlumno=fichaMatricula.getAlumno().getCodigo();
        for (Matricula m: fichaMatricula.getListaMatriculas()){
            if (ejecucionSentenciaDDL("INSERT INTO matricular VALUES ("+
                fichaMatricula.getAlumno().getCodigo()+",'"+m.getCodCurso()+"','"+
                m.getCodAsignatura()+"',"+
                (m.getFechaInicio().equals("")?null:"'"+m.getFechaInicio()+"'")+","+
                (m.getFechaFin().equals("")?null:"'"+m.getFechaFin()+"'")+
                ");") != 1){
                    estado=false;
            }
        }
        return estado;
    }
    
    /**
     * Función que borra una ficha de matrícula en la BBDD
     * @param codigo    Código de alumno
     * @return          Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarMatriculaciones(int codigo) {
        return ejecucionSentenciaDDL("DELETE FROM matricular WHERE codAlumno='"+
                codigo+"';")>-1;
    }
    
    /**
     * Función que extrae la lista de fichas del planificador existentes en la BBDD
     * @return  Devuelve la lista de fichas del planificador existentes (profesor/aula/asignatura/horario)
     */
    public static List<FichaPlanificador> extraerFichasPlanificador(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha;
        LocalTime horaInicio = null;
        LocalTime horaFin=null;
        String fechaString;
        String cifActual=null;
        String cifInicial=null;
        FichaPlanificador fichaPlanificador=null;
        List<FichaPlanificador> listaProfesores=new ArrayList<>();
        List<Plan> listaPlanesPorProfesor = new ArrayList<>();
        Profesor p=null;
        Asignatura asignatura=null;
        Aula aula=null;
        
        String secuencia="select profesores.cifdni,nombre,"
            + "apellidos,domicilio,poblacion,cp,provincia,fechanacimiento,telefono,"
            + "movil,ctabancaria,email,foto,"
            + "asignaturas.CodAsignatura,asignaturas.Descripcion,asignaturas.CargaHoras,"
            + "aulas.CodAula,aulas.Descripcion,aulas.Ubicacion,ocupar.DiaSemana,ocupar.De,ocupar.A "
            + "from profesores " 
            + "left join proveedores on proveedores.CifDni=profesores.CifDni "
            + "left join ensenyar on ensenyar.CifDni=profesores.CifDni "
            + "left join asignaturas on ensenyar.codAsignatura=asignaturas.codAsignatura "
            + "left join ocupar on (ocupar.CifDni=profesores.CifDni and ocupar.CodAsignatura=asignaturas.CodAsignatura) "
            + "left join aulas on aulas.CodAula=ocupar.codAula;";
            
        ResultSet resultado=ejecucionSentenciaDML(secuencia);
    
        if (resultado!=null){
            try {
                while(resultado.next()){
                    cifActual=resultado.getString(1);
                    if (!cifActual.equals(cifInicial)){
                        if (cifInicial!=null){
                            fichaPlanificador=new FichaPlanificador(p,listaPlanesPorProfesor);
                            listaProfesores.add(fichaPlanificador);
                            listaPlanesPorProfesor=new ArrayList<>();
                        }   
                        fecha=(resultado.getString(8)==null?null:Campos.stringToFecha(resultado.getString(8)));
                    
                        p=new Profesor(resultado.getString(1)!=null?resultado.getString(1):"",
                            resultado.getString(2)!=null?resultado.getString(2):"",
                            resultado.getString(3)!=null?resultado.getString(3):"",
                            resultado.getString(4)!=null?resultado.getString(4):"",
                            resultado.getString(5)!=null?resultado.getString(5):"",
                            resultado.getString(6)!=null?resultado.getString(6):"",
                            resultado.getString(7)!=null?resultado.getString(7):"",
                            fecha,
                            resultado.getString(9)!=null?resultado.getString(9):"",
                            resultado.getString(10)!=null?resultado.getString(10):"",
                            resultado.getString(11)!=null?resultado.getString(11):"",
                            resultado.getString(12)!=null?resultado.getString(12):"",
                            resultado.getBytes(13));
                        cifInicial=cifActual;
                    }
                    if (resultado.getString(14)!=null){
                        asignatura=new Asignatura(resultado.getString(14),
                            resultado.getString(15),resultado.getInt(16));
                    }
                    if (resultado.getString(17)!=null){
                        aula=new Aula(resultado.getString(17),
                            resultado.getString(18),resultado.getString(19));
                    }
                    if (asignatura!=null && aula!=null && resultado.getInt(20)>0){
                        listaPlanesPorProfesor.add(new Plan(asignatura,aula,
                            DayOfWeek.of(resultado.getInt(20)),LocalTime.parse(resultado.getString(21)),
                                    LocalTime.parse(resultado.getString(22))));
                        asignatura=null;
                        aula=null;
                    } 
                }
                if (listaProfesores!=null){
                    fichaPlanificador=new FichaPlanificador(p,listaPlanesPorProfesor);
                    listaProfesores.add(fichaPlanificador);
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaProfesores;
    }
    
    
    
    /**
     * Función que inserta un plan en la BBDD
     * @param   datosProfesor   Ficha del plan de un profesor
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarPlanes(FichaPlanificador datosProfesor) {
        boolean estado=true;
        String cifProfesor=datosProfesor.getProfesor().getCifDni();
        datosProfesor.getListaPlanes().sort((plan1,plan2)->plan1.getCodigoAsignatura()
                .compareTo(plan2.getCodigoAsignatura()));
        String codigoAsignaturaIni="";
        String codigoAsignaturaActual="";
        for (Plan p: datosProfesor.getListaPlanes()){
            codigoAsignaturaActual=p.getCodigoAsignatura();
            if (codigoAsignaturaActual.equals(codigoAsignaturaIni)){
                if (ejecucionSentenciaDDL("INSERT INTO ocupar VALUES ('"+
                p.getCodigoAsignatura()+"','"+cifProfesor+"','"+
                p.getCodigoAula()+"',"+p.getDiaSemana().getValue()+",'"+
                p.getDeHora()+"','"+p.getAHora()+
                "');") != 1)
                    estado=false;
            }else{
                if (ejecucionSentenciaDDL("INSERT INTO ensenyar VALUES ('"+
                    p.getCodigoAsignatura()+"','"+cifProfesor+"'"+
                    ");") != 1 ||
                    ejecucionSentenciaDDL("INSERT INTO ocupar VALUES ('"+
                    p.getCodigoAsignatura()+"','"+cifProfesor+"','"+
                    p.getCodigoAula()+"',"+p.getDiaSemana().getValue()+",'"+
                    p.getDeHora()+"','"+p.getAHora()+
                    "');") != 1)
                    estado=false;
            }
            codigoAsignaturaIni=codigoAsignaturaActual;
        }
        return estado;
    }
    
    /**
     * Función que borrar los planes de un profesor en la BBDD
     * @param   cifDni  CifDni del profesor
     * @return  Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarPlanes(String cifDni) {
        return ejecucionSentenciaDDL("DELETE FROM ensenyar WHERE CifDni='"+
                cifDni+"';")>-1 && 
                ejecucionSentenciaDDL("DELETE FROM ocupar WHERE CifDni='"+
                cifDni+"';")>-1;
    }
    
    /**
     * Función que extrae la lista de los exámenes de cada alumno existentes en la BBDD
     * @return  Devuelve la lista de los exámenes de cada alumno
     */
    public static List<FichaExamen> extraerFichasExamenes() {
        int codigoAlumnoActual=-1;
        int codigoAlumnoInicial=-1;
        Asignatura asignatura=null;
        Alumno a=null;
        Examen examen=null;
        FichaExamen fichaExamen=null;
        List<Examen> listaExamenes=new ArrayList<>();
        List<FichaExamen> listaExamenesPorAlumno = new ArrayList<>();
        LocalDate fecha=null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        ResultSet resultado=ejecucionSentenciaDML("select alumnos.CodAlumno,Nia,"
            + "Nombre,Apellidos,Domicilio,Poblacion,Cp,Provincia,FechaNacimiento,Telefono,"
            + "Movil,Padre,DniPadre,TelefonoPadre,Email1,Madre,DniMadre,TelefonoMadre,"
            + "Email2,CentroEstudio,Foto,asignaturas.CodAsignatura,"
            + "asignaturas.Descripcion,asignaturas.CargaHoras,examinar.Fecha,"
            + "examinar.Nota from Alumnos "
            + "left join examinar on alumnos.CodAlumno=examinar.CodAlumno "
            + "left join asignaturas on asignaturas.codasignatura=examinar.codasignatura "
            + "order by codalumno,codasignatura,fecha;");
    
        if (resultado!=null){
            try {
                while(resultado.next()){
                    codigoAlumnoActual=resultado.getInt(1);
                    if (codigoAlumnoInicial!=codigoAlumnoActual){
                        if (codigoAlumnoInicial>-1){
                            fichaExamen=new FichaExamen(a,listaExamenes);
                            listaExamenesPorAlumno.add(fichaExamen);
                            listaExamenes=new ArrayList<>();
                        }  
                        fecha=(resultado.getString(9)==null?null:Campos.stringToFecha(resultado.getString(9)));
                        a=new Alumno(
                            resultado.getString(2)!=null?resultado.getString(2):"",  //Nia
                            resultado.getString(3)!=null?resultado.getString(3):"",  //Nombre
                            resultado.getString(4)!=null?resultado.getString(4):"",  //Apellidos
                            resultado.getString(5)!=null?resultado.getString(5):"",  //Domicilio
                            resultado.getString(6)!=null?resultado.getString(6):"",  //Poblacion
                            resultado.getString(7)!=null?resultado.getString(7):"",  //Cp
                            resultado.getString(8)!=null?resultado.getString(8):"",  //Provincia
                            fecha,                                                   //FechaNacimiento
                            resultado.getString(10)!=null?resultado.getString(10):"",//Telefono
                            resultado.getString(11)!=null?resultado.getString(11):"",//Movil
                            resultado.getString(12)!=null?resultado.getString(12):"",//Padre
                            resultado.getString(13)!=null?resultado.getString(13):"",//DniPadre    
                            resultado.getString(14)!=null?resultado.getString(14):"",//TelefonoPadre
                            resultado.getString(15)!=null?resultado.getString(15):"",//Email1
                            resultado.getString(16)!=null?resultado.getString(16):"",//Madre
                            resultado.getString(17)!=null?resultado.getString(17):"",//DniMadre
                            resultado.getString(18)!=null?resultado.getString(18):"",//TelefonoMadre
                            resultado.getString(19)!=null?resultado.getString(19):"",//Email2
                            resultado.getString(20)!=null?resultado.getString(20):"",//CentroEstudios
                            "",                                                      //Observaciones
                            resultado.getBytes(21));                                 //Foto   
                            a.setCodigo(resultado.getInt(1));
                        
                    } 
                    if (resultado.getString(22)!=null && resultado.getString(25)!= null ){
                            asignatura=new Asignatura(resultado.getString(9),
                            resultado.getString(23),resultado.getInt(24));
                            fecha=LocalDate.parse(resultado.getString(25));
                            examen=new Examen(asignatura,fecha,resultado.getFloat(26));
                            listaExamenes.add(examen);
                    }
                    codigoAlumnoInicial=codigoAlumnoActual;
                } 
                if (listaExamenes!=null){
                    fichaExamen=new FichaExamen(a,listaExamenes);
                    listaExamenesPorAlumno.add(fichaExamen);
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaExamenesPorAlumno;
    }
    
    /**
     * Función que extra un listado de asignaturas matriculadas de un alumno
     * @param codigoAlumno  Código del alumno
     * @return              Devuelve un listado de asignaturas matriculadas por el alumno
     */
    public static List<Asignatura> extraerAsignaturasMatriculadasPorAlumno(int codigoAlumno) {
        List<Asignatura> listaAsignaturas=new ArrayList<>();
        ResultSet resultado=ejecucionSentenciaDML("select asignaturas.CodAsignatura,"+
            "asignaturas.Descripcion,asignaturas.CargaHoras from asignaturas " +
            "left join matricular on asignaturas.CodAsignatura=matricular.CodAsignatura " +
            "where matricular.CodAlumno="+codigoAlumno+" and fechafin>now() " +
            "order by asignaturas.CodAsignatura;;");
        if (resultado!=null){
            try {
                while(resultado.next()){
                   listaAsignaturas.add(new Asignatura(
                        resultado.getString(1)!=null?resultado.getString(1):"",
                        resultado.getString(2)!=null?resultado.getString(2):"",
                        resultado.getInt(3)));
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        } 
         return listaAsignaturas;
     }
    
    /**
     * Función que borra los exámenes de un alumno en la BBDD
     * @param codigoAlumno  Código del alumno
     * @return  Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarExamenes(int codigoAlumno) {
        return ejecucionSentenciaDDL("DELETE FROM examinar WHERE CodAlumno="+
                codigoAlumno+";")>-1;
    }

    /**
     * Función que inserta los exámenes de un alumno en la BBDD
     * @param datosAlumno   Datos del alumno con asignatura
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */ 
    public static boolean insertarExamenes(FichaExamen datosAlumno) {
        boolean estado=true;
        int codigoAlumno=datosAlumno.getAlumno().getCodigo();
        for (Examen e: datosAlumno.getListaExamenes()){
            if (ejecucionSentenciaDDL("INSERT INTO examinar VALUES ("+
                codigoAlumno+",'"+e.getCodigoAsignatura()+"',"+e.getNota()+","+
                (e.getFecha().equals("")?null:"'"+e.getFecha()+"'")+    
                ");") != 1){
                    estado=false;
            }
        }
        return estado;
    }
    
    /**
     * Función que extrae una lista con los gastos de cada proveedor de la BBDD
     * @return  Devuelve una lista con los gastos de cada proveedor
     */
    public static List<FichaGasto> extraerFichasGastos() {
        String codigoProveedorActual="";
        String codigoProveedorInicial="";
        Proveedor p=null;
        Gasto g=null;
        List<Gasto> listaGastos=new ArrayList<>();
        FichaGasto fichaGasto=null;
        List<FichaGasto> listaGastosPorProveedor = new ArrayList<>();
        String sentenciaSQL="select proveedores.cifdni,proveedores.nombre,"+
            "proveedores.apellidos,proveedores.telefono,proveedores.movil,"+
            "proveedores.ctabancaria,proveedores.email,gastos.numgasto," +
            "gastos.sufactura,gastos.fecha,gastos.importebase,gastos.iva " +
            "from proveedores " +
            "left join profesores on proveedores.cifdni=profesores.cifdni " +
            "left join gastos on proveedores.cifdni=gastos.cifdni " +
            "where profesores.cifdni is null " +
            "order by proveedores.cifdni, gastos.fecha;";
        ResultSet resultado=ejecucionSentenciaDML(sentenciaSQL);
        if (resultado!=null){
            try {
                while(resultado.next()){
                    codigoProveedorActual=resultado.getString(1);
                    if (!codigoProveedorInicial.equals(codigoProveedorActual)){
                        if (codigoProveedorInicial!=""){
                            fichaGasto=new FichaGasto(p,listaGastos);
                            listaGastosPorProveedor.add(fichaGasto);
                            listaGastos=new ArrayList<>();
                        }   
                        p=new Proveedor(
                            resultado.getString(1)!=null?resultado.getString(1):"",
                            resultado.getString(2)!=null?resultado.getString(2):"",
                            resultado.getString(3)!=null?resultado.getString(3):"",
                            resultado.getString(4)!=null?resultado.getString(4):"",
                            resultado.getString(5)!=null?resultado.getString(5):"",
                            resultado.getString(6)!=null?resultado.getString(6):"",
                            resultado.getString(7)!=null?resultado.getString(7):""
                            );
                    } 
                    
                    if (resultado.getString(8)!=null){
                            g=new Gasto(resultado.getString(9),
                                LocalDate.parse(resultado.getString(10)),
                                resultado.getDouble(11),resultado.getDouble(12));
                            g.setNumGasto(resultado.getString(8));
                            listaGastos.add(g);
                    }
                    
                    codigoProveedorInicial=codigoProveedorActual;
                } 
                if (listaGastos!=null){
                    fichaGasto=new FichaGasto(p,listaGastos);
                    listaGastosPorProveedor.add(fichaGasto);
                }
            } catch (SQLException ex) {
                System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
            }
        }
        return listaGastosPorProveedor;
    }

    /**
     * Función que borra todos los gastos de un proveedor
     * @param cifDni    CifDni del proveedor
     * @return Devuelve booleano (true=borrado OK, false=error borrado)
     */
    public static boolean borrarGastos(String cifDni) {
        return ejecucionSentenciaDDL("DELETE FROM gastos WHERE CifDni='"+
                cifDni+"';")>-1;
    }
    
    /**
     * Función que inserta los gastos de un proveedor determinado en la BBDD
     * @param datosProveedor    Datos del proveedor
     * @return  Devuelve booleano (true=inserción OK, false=error inserción)
     */
    public static boolean insertarGastos(FichaGasto datosProveedor) {
        boolean estado=true;
        String codigoProveedor=datosProveedor.getProveedor().getCifDni();
        for (Gasto g: datosProveedor.getListaGastos()){
            if (ejecucionSentenciaDDL("INSERT INTO gastos VALUES ('"+
                g.getNumGasto()+"','"+g.getConcepto()+"',"+
                g.getBase()+","+g.getIva()+","+  
                (g.getFecha().equals("")?null:"'"+g.getFecha()+"'")+",'"+
                codigoProveedor+
                "');") != 1){
                    estado=false;
            }
        }
        return estado;
    }

   
    
// ---------------------------------------------------------------------------------------
    
    /**
     * Método que cambia el valor del campo de la imagen en una tabla de la BBDD
     * @param sql       Sentencia SQL
     * @param imagen    Array de byte de la imagen
     */
    private static void cambiarImagen(String sql,byte[] imagen){
        BDPostgre postgre=new BDPostgre(ShareData.SERVER,ShareData.PORT,"dbacademia", "acagest", "clavel98");
        postgre.ejecutarDDLIMG(sql, imagen);
        postgre.cerrarConexion(postgre.getConexion());
    }
    
    /**
     * Función que ejecuta una sentencia DDL
     * @param sql   Sentencia SQL
     * @return      Devuelve el número de filas afectadas
     */
    private static int ejecucionSentenciaDDL (String sql){
        int valor=-1;
        BDPostgre postgre=new BDPostgre(ShareData.SERVER,ShareData.PORT,"dbacademia","acagest","clavel98");
        if (postgre.isEstadoConexion()){
            valor=postgre.ejecutarDDL(sql);
        }
        postgre.cerrarConexion(postgre.getConexion());
        return valor;
    }
    
    /**
     * Función que ejecuta una sentencia DML
     * @param sql   Sentencia SQL
     * @return      Devuelve los datos solicitados (ResultSet)
     */
    private static ResultSet ejecucionSentenciaDML (String sql){
        ResultSet rs=null;
        BDPostgre postgre=new BDPostgre(ShareData.SERVER,ShareData.PORT,"dbacademia","acagest","clavel98");
        if (postgre.isEstadoConexion()){
            rs=postgre.ejecutarDML(sql);
        }
        postgre.cerrarConexion(postgre.getConexion());
        return rs;
    }
    
    /**
     * Función que valida los parámetros de configuración de la aplicación
     * @return      Devuelve booleano (true=validación OK, false=error validación)
     */
    public static boolean validarParametros(){
        Properties propiedades=obtenerParametrosConfig();
        boolean validar=false;
        if (validarParametrosAccesoBD()){
            validar=true;
            ShareData.SERVER=propiedades.getProperty("servidor");
            ShareData.PORT=propiedades.getProperty("puerto");
        }
        return validar;
    }

    /**
     * Función que valida el acceso al servidor de la BBDD
     * @param servidor  Servidor de la BBDD (IP)
     * @param puerto    Puerto de la BBDD
     * @param clave     Clave de acceso a la BBDD
     * @return          Devuelve booleano (true=validación OK, false=error validación)
     */
    public static boolean validarAccesoServerBD(String servidor, String puerto, String clave){
        boolean validacion=false;
        BDPostgre postgre=new BDPostgre(servidor,puerto,"postgres","postgres",clave);
        if (postgre.isEstadoConexion()){
            guardarParametrosConfig(servidor, puerto, clave);
            ShareData.SERVER=servidor;
            ShareData.PORT=puerto;
            validacion=true;
            postgre.cerrarConexion(postgre.getConexion());
        }
        return validacion;
    }
    
    /**
     * Función que crea el usuario acagest en la BBDD y la BBDD dbacademia
     * @param servidor  Nombre del servidor donde está la BBDD
     * @param puerto    Puerto de acceso a la BBDD
     * @param clave     Password del usuario administrador de la BBDD
     * @return          Devuelve True si la creación ha sido satisfastorio o False
     *                  en caso contrario.
     */
    public static boolean crearUsuarioBD(String servidor, String puerto,String clave){
        boolean estado=false;
        BDPostgre postgre=new BDPostgre(servidor,puerto,"postgres","postgres",clave);
        if (postgre.isEstadoConexion()){
            if (postgre.ejecutarDDL("CREATE USER acagest PASSWORD 'clavel98';")==0 && 
                    postgre.ejecutarDDL("CREATE DATABASE dbacademia;")==0 && 
                    postgre.ejecutarDDL("GRANT ALL PRIVILEGES ON DATABASE dbacademia TO acagest;")==0){
                estado=true;
            }
        }
        postgre.cerrarConexion(postgre.getConexion());
        return estado;
    }
    
    /**
     * Función que crea las tablas en la BBDD
     * @param servidor  Nombre del servidor donde está la BBDD
     * @param puerto    Puerto de acceso a la BBDD
     * @param baseDatos Nombre de la BBDD
     * @param usuario   Usuario de acceso a la BBDD
     * @param clave     Passwor del usuario
     * @return          Devuelve True si la creación ha sido satisfastorio o False
     *                  en caso contrario.
     */
    public static boolean crearTablasBD(String servidor, String puerto,String baseDatos, String usuario, String clave){
        boolean estado=true;
        
        String[] tablas={"USUARIOS","PROVEEDORES","PROFESORES","GASTOS","ALUMNOS",
            "AULAS","ASIGNATURAS","CURSOS","CONTENER","MATRICULAR","EXAMINAR",
            "ENSENYAR","FACTURAS","RECIBOS","DETALLAR","OCUPAR","EMPRESAS"};
        String[] estructuras=new String[19];
        
        // USUARIOS
        estructuras[0]="CREATE TABLE "+tablas[0]+" ("+
                "Username VARCHAR(10) PRIMARY KEY," +
                "Password VARCHAR(64)," +
                "Nivel NUMERIC(1)" +
                ");";
        
        // PROVEEDORES
        estructuras[1]="CREATE TABLE "+tablas[1]+" (" +
                "CifDni VARCHAR(9) PRIMARY KEY," +
                "Nombre VARCHAR(25)," +
                "Apellidos VARCHAR(25),"+
                "Telefono VARCHAR(25)," +
                "Movil VARCHAR(25)," +
                "CtaBancaria VARCHAR(24)," +
                "Email VARCHAR(50)"+
                ");";
                
        // PROFESORES
        estructuras[2]="CREATE TABLE "+tablas[2]+" ("+
                "CifDni VARCHAR(9) PRIMARY KEY REFERENCES PROVEEDORES,"+
                "Domicilio VARCHAR(60),"+
                "Poblacion VARCHAR(45),"+
                "Cp VARCHAR(5),"+
                "Provincia VARCHAR(40),"+
                "FechaNacimiento DATE,"+
                "Foto BYTEA"+
                ");";

        // GASTOS
        estructuras[3]="CREATE TABLE "+tablas[3]+" ("+
                "NumGasto VARCHAR(35) PRIMARY KEY,"+
                "SuFactura VARCHAR(25),"+
                "ImporteBase NUMERIC(8,2),"+
                "Iva NUMERIC(8,2),"+
                "Fecha DATE,"+
                "CifDni VARCHAR(9) NOT NULL REFERENCES PROVEEDORES"+
                ");";
        
        // ALUMNOS
        estructuras[4]="CREATE TABLE "+tablas[4]+" ("+
                "CodAlumno SERIAL PRIMARY KEY,"+
                "Nia VARCHAR(8),"+
                "Nombre VARCHAR(25),"+
                "Apellidos VARCHAR(25),"+
                "Domicilio VARCHAR(60),"+
                "Poblacion VARCHAR(45),"+
                "Cp VARCHAR(5),"+
                "Provincia VARCHAR(40),"+
                "FechaNacimiento DATE,"+
                "Telefono VARCHAR(25),"+
                "Movil VARCHAR(25),"+
                "Padre VARCHAR(50),"+
                "DniPadre VARCHAR(9),"+
                "TelefonoPadre VARCHAR(25),"+
                "Email1 VARCHAR(50),"+
                "Madre VARCHAR(50),"+
                "DniMadre VARCHAR(9),"+
                "TelefonoMadre VARCHAR(25),"+
                "Email2 VARCHAR(50),"+
                "CentroEstudio VARCHAR(50),"+
                "Observaciones TEXT,"+
                "Foto BYTEA"+
                ");";

        // AULAS
        estructuras[5]="CREATE TABLE "+tablas[5]+" ("+
                "CodAula VARCHAR(9) PRIMARY KEY,"+
                "Descripcion VARCHAR(30),"+
                "Ubicacion VARCHAR(30)"+
                ");";

        // ASIGNATURAS
        estructuras[6]="CREATE TABLE "+tablas[6]+" ("+
                "CodAsignatura VARCHAR(9) PRIMARY KEY,"+
                "Descripcion VARCHAR(40) NOT NULL,"+
                "CargaHoras INTEGER NOT NULL"+
                ");";
        
        // CURSOS
        estructuras[7]="CREATE TABLE "+tablas[7]+" ("+
                "CodCurso VARCHAR(9) PRIMARY KEY,"+
                "Descripcion VARCHAR (30) NOT NULL,"+
                "ImporteHora NUMERIC(7,2) NOT NULL,"+
                "Pago VARCHAR(1) NOT NULL,"+
                "CifDni VARCHAR(9) NOT NULL UNIQUE REFERENCES PROFESORES"+
                ");";

        // CONTENER (CURSOS-ASIGNATURAS)
        estructuras[8]="CREATE TABLE "+tablas[8]+" ("+
                "CodCurso VARCHAR(9) NOT NULL REFERENCES CURSOS,"+
                "CodAsignatura VARCHAR(9) NOT NULL REFERENCES ASIGNATURAS,"+
                "PRIMARY KEY (CodCurso,CodAsignatura)"+
                ");";
        
        // MATRICULAR (ALUMNOS-CURSOS)
        estructuras[9]="CREATE TABLE "+tablas[9]+" ("+
                "CodAlumno INTEGER NOT NULL REFERENCES ALUMNOS,"+
                "CodCurso VARCHAR(9) NOT NULL REFERENCES CURSOS,"+
                "CodAsignatura VARCHAR(9) NOT NULL,"+
                "FechaInicio DATE NOT NULL,"+
                "FechaFin DATE NOT NULL,"+
                "PRIMARY KEY (CodAlumno,CodCurso,CodAsignatura,FechaInicio)"+
                ");";
        
        // EXAMINAR (ALUMNOS-ASIGNATURAS)
        estructuras[10]="CREATE TABLE "+tablas[10]+" ("+
                "CodAlumno INTEGER NOT NULL REFERENCES ALUMNOS,"+
                "CodAsignatura VARCHAR(9) NOT NULL REFERENCES ASIGNATURAS,"+
                "Nota NUMERIC(4,2),"+
                "Fecha DATE NOT NULL,"+
                "PRIMARY KEY (CodAlumno, CodAsignatura, Fecha)"+
                ");";
        
        
        //ENSENYAR (PROFESORES-ASIGNATURAS)
        estructuras[11]="CREATE TABLE "+tablas[11]+" ("+
                "CodAsignatura VARCHAR(9) NOT NULL REFERENCES ASIGNATURAS,"+
                "CifDni VARCHAR(9) NOT NULL REFERENCES PROFESORES,"+
                "PRIMARY KEY (CodAsignatura,CifDni)"+
                ");";
        
        // FACTURAS 
        estructuras[12]="CREATE TABLE "+tablas[12]+" ("+
                "NumFactura VARCHAR(12) PRIMARY KEY,"+
                "Fecha DATE"+
                ");";
        
        // RECIBOS
        estructuras[13]="CREATE TABLE "+tablas[13]+" ("+
                "NumRecibo SERIAL PRIMARY KEY,"+
                "FechaEmision DATE,"+
                "FechaPago DATE,"+
                "Descripcion TEXT,"+
                "Importe NUMERIC(8,2),"+
                "NumFactura VARCHAR(12) REFERENCES FACTURAS,"+
                "CodAlumno INTEGER NOT NULL REFERENCES ALUMNOS"+
                ");";
        
        // DETALLAR (CURSOS-RECIBOS)
        estructuras[14]="CREATE TABLE "+tablas[14]+" ("+
                "CodCurso VARCHAR(9) NOT NULL REFERENCES CURSOS,"+
                "NumRecibo INTEGER NOT NULL REFERENCES RECIBOS,"+
                "PRIMARY KEY (CodCurso,NumRecibo)"+
                ");";
        
        // OCUPAR (PROFESOR-ASIGNATURA-AULA)
	estructuras[15]="CREATE TABLE "+tablas[15]+" ("+
                "CodAsignatura VARCHAR(9) NOT NULL REFERENCES ASIGNATURAS,"+
                "CifDni VARCHAR(9) NOT NULL REFERENCES PROFESORES,"+
                "CodAula VARCHAR(9) NOT NULL REFERENCES AULAS,"+
                "DiaSemana INTEGER NOT NULL,"+
                "De TIME NOT NULL,"+
                "A TIME NOT NULL,"+
                "PRIMARY KEY (CodAsignatura, CifDni, CodAula, DiaSemana, De)"+
                ");";
        
        // EMPRESAS
        estructuras[16]="CREATE TABLE "+tablas[16]+" ("+
                "Codigo serial,"+
                "NifNie VARCHAR(9),"+
                "Nombre VARCHAR(40),"+
                "Domicilio VARCHAR(60),"+
                "Poblacion VARCHAR(45),"+
                "Cp VARCHAR(5),"+
                "Provincia VARCHAR(40),"+
                "Telefono VARCHAR(50),"+
                "Email VARCHAR(50),"+
                "Logo BYTEA"+
                ");";
        
        // INSERTAMOS USUARIO admin
        estructuras[17]="INSERT INTO USUARIOS VALUES "+
                    "('admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',1);";
        
        // INSERTAMOS REGISTRO EN BLANCO EN EMPRESAS
        estructuras[18]="INSERT INTO EMPRESAS (nombre) values ('');";
        
        // Ejecutamos las sentencias SQL que hemos creado
        BDPostgre postgre=new BDPostgre(servidor,puerto,baseDatos,usuario,clave);
        if (postgre.isEstadoConexion()){
            for (int i=0; i<estructuras.length; i++){
                if (postgre.ejecutarDDL(estructuras[i])<0){
                    estado=false;
                }
            }
        }
        postgre.cerrarConexion(postgre.getConexion());
        return estado;
    }

    public static List<Recibo> generarRecibos() {
        /*select alumnos.codalumno,alumnos.nombre,alumnos.apellidos,cursos.codcurso,cursos.descripcion,cursos.pago,asignaturas.codasignatura,
	cursos.importehora,asignaturas.cargahoras from matricular
	left join alumnos on matricular.codAlumno=alumnos.codAlumno
	left join cursos on matricular.codcurso=cursos.codcurso
	left join asignaturas on matricular.codasignatura=asignaturas.codasignatura
	where fechafin>=now() and fechainicio<=now();*/
        List<Recibo> listaRecibos=null;
        Alumno alumno=null;
        Curso curso=null;
        Asignatura asignatura=null;
        List<Curso> listaCursos=null;
        List<Asignatura> listaAsignaturas=null;
        int codigoAlumnoActual=-1;
        int codigoAlumnoInicial=-1;
        String codigoCursoActual="";
        String codigoCursoInicial="";
        String asignaturaActual="";
        String descripcion="";
        String sentencia="";
        ResultSet resultado=null;
        sentencia="select alumnos.codalumno,alumnos.nombre,alumnos.apellidos,"+
                "cursos.codcurso,cursos.descripcion,cursos.pago,asignaturas.codasignatura,"+
                "cursos.importehora,asignaturas.cargahoras from matricular " +
                "left join alumnos on matricular.codAlumno=alumnos.codAlumno " +
                "left join cursos on matricular.codcurso=cursos.codcurso " +
                "left join asignaturas on matricular.codasignatura=asignaturas.codasignatura " +
                "where fechafin>=now() and fechainicio<=now() "+
                "order by alumnos.codalumno,cursos.codcurso, asignaturas.codasignatura;";
        resultado=ejecucionSentenciaDML(sentencia);
        try {
            if (resultado.isBeforeFirst()){
                // Creación de clases
                listaRecibos=new ArrayList<>();
                while(resultado.next()){
                    codigoAlumnoActual=resultado.getInt(1);
                    codigoCursoActual=resultado.getString(4);
                    asignaturaActual=resultado.getString(7);
                    
                    if (codigoAlumnoInicial!=codigoAlumnoActual){
                        if (codigoAlumnoInicial>-1){
                            curso.setAsignaturas(listaAsignaturas);
                            listaCursos.add(curso);
                            Recibo recibo=new Recibo(alumno, listaCursos,LocalDate.now());
                            listaRecibos.add(recibo);                            
                            //Creo alumno nuevo, lista asignaturas y cursos
                            alumno=new Alumno("", resultado.getString(2),resultado.getString(3),"","","","",null,
                            "","","","","","","","","","","","",null);
                            alumno.setCodigo(resultado.getInt(1));
                            listaAsignaturas=new ArrayList<>();
                            listaCursos=new ArrayList<>();
                            curso=new Curso(resultado.getString(4),resultado.getString(5),
                                resultado.getDouble(8),resultado.getString(6),null);
                            
                            
                        }else{
                            //Creo alumno nuevo, lista asignaturas y cursos
                            //Recibo recibo=new Recibo(alumno, listaCursos,LocalDate.now(), null, anotaciones,0.0, 0.0);
                            
                            alumno=new Alumno("", resultado.getString(2),resultado.getString(3),"","","","",null,
                            "","","","","","","","","","","","",null); 
                            alumno.setCodigo(resultado.getInt(1));
                            listaAsignaturas=new ArrayList<>();
                            listaCursos=new ArrayList<>();
                            curso=new Curso(resultado.getString(4),resultado.getString(5),
                                resultado.getDouble(8),resultado.getString(6),null);
                            
                        }
                    }else{
                        if (!codigoCursoInicial.equals(codigoCursoActual)){
                            curso.setAsignaturas(listaAsignaturas);
                            listaCursos.add(curso);
                            listaAsignaturas=new ArrayList<>();
                            curso=new Curso(resultado.getString(4),resultado.getString(5),
                                resultado.getDouble(8),resultado.getString(6),null);
                        }
                    }
                    asignatura=new Asignatura(resultado.getString(7),"",resultado.getInt(9));
                    listaAsignaturas.add(asignatura);
                    codigoAlumnoInicial=codigoAlumnoActual;
                    codigoCursoInicial=codigoCursoActual;
                            
                    
                }
                curso.setAsignaturas(listaAsignaturas);
                listaCursos.add(curso);
                Recibo recibo=new Recibo(alumno, listaCursos,LocalDate.now());
                listaRecibos.add(recibo);
                
                
                //Comprobamos que los recibos a generar no están duplicados.

                for (Recibo r:listaRecibos){
                    for (Iterator<Curso> iter = r.getCursos().listIterator(); iter.hasNext(); ) {
                        Curso c = iter.next();
                        switch(c.getPago()){
                            case "A":   sentencia="select count(*) from recibos " +
                                        "left join detallar on detallar.numrecibo=recibos.numrecibo " +
                                        "where codalumno="+r.getAlumno().getCodigo()+" "+
                                        "and codcurso='"+c.getCodigo()+"' "+
                                        "and recibos.fechaemision>now()- interval '365 day';";
                                        break;
                            case "M":   sentencia="select count(*) from recibos " +
                                        "left join detallar on detallar.numrecibo=recibos.numrecibo " +
                                        "where codalumno="+r.getAlumno().getCodigo()+" "+
                                        "and codcurso='"+c.getCodigo()+"' "+
                                        "and recibos.fechaemision>now()- interval '1 month';";
                                        break;
                            case "S":   sentencia="select count(*) from recibos " +
                                        "left join detallar on detallar.numrecibo=recibos.numrecibo " +
                                        "where codalumno="+r.getAlumno().getCodigo()+" "+
                                        "and codcurso='"+c.getCodigo()+"' "+
                                        "and recibos.fechaemision>now()- interval '7 day';";
                                        break;
                        }
                        
                        
                            resultado=ejecucionSentenciaDML(sentencia);
                            if (resultado.next()){
                                if(resultado.getInt(1)>0){
                                    Mensajes.msgInfo("RECIBOS DUPLICADOS", "Para "+
                                        r.getAlumno().getApellidos()+", "+r.getAlumno().getNombre()+
                                        "Curso: "+c.getDescripcion()+" con pago "+c.getPago());
                                    iter.remove();
                                }
                            }
                        
                    }
                }
                
                // Creo los recibos 
                boolean estadoInsercion=false;
                Double importeBase=0.0;
                int ultimoId=0;
                for (Recibo r:listaRecibos){
                    importeBase=0.0;
                    descripcion=r.getAlumno().getNombre()+" "+r.getAlumno().getApellidos();
                    if (r.getCursos().size()>0){
                        for (Curso c:r.getCursos()) {
                            descripcion=descripcion+" Curso:"+c.getDescripcion()+" ";
                            for (Asignatura a:c.getAsignaturas()){
                                importeBase=importeBase+(a.getCargaHoras()*c.getImporteHora());
                            }
                        }
                        sentencia="INSERT INTO recibos VALUES (default,'"+
                            LocalDate.now()+"',null,'"+descripcion+
                            "',"+importeBase+",null,"+r.getAlumno().getCodigo()+
                            ") RETURNING numRecibo;";
                        resultado=ejecucionSentenciaDML(sentencia); 
                        ultimoId=extraerID(resultado);
                        if (ultimoId>0) estadoInsercion=true; 
                        for (Curso c: r.getCursos()){
                            sentencia="INSERT INTO detallar VALUES ('"+c.getCodigo()+"',"+
                                ultimoId+");";
                            if (ejecucionSentenciaDDL(sentencia)==1 && estadoInsercion){
                                estadoInsercion=true;
                            }else{
                                estadoInsercion=false; 
                            }
                            if (!estadoInsercion) Mensajes.msgError("INSERCION RECIBOS", "Algo ha fallado..");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("ERROR: Capa Negocio - OperativasBD.java "+ex.getMessage());
        }
        return listaRecibos;
    }
}
