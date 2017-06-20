package capaNegocio;

import java.util.List;

/**
 * Clase FichaMatricula
 * Esta clase contiene los parámetros de las Fichas de Matriculas
 * 
 * Atributos:
 *  alumno          : Alumno
 *  listaMatriculas : Lista de matrículas del alumno
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Devuelve el nombre y apellidos del alumno
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FichaMatricula {
    private Alumno alumno;
    private List<Matricula> listaMatriculas;

    /**
     * Constructor parametrizado
     * @param alumno            Alumno
     * @param listaMatriculas   Lista de matriculaciones del alumno
     */
    public FichaMatricula(Alumno alumno, List<Matricula> listaMatriculas) {
        this.alumno = alumno;
        this.listaMatriculas = listaMatriculas;
    }

    /**
     * Getter alumno
     * @return  Devuelve el alumno matriculado
     */
    public Alumno getAlumno() {
        return alumno;
    }

    /**
     * Setter alumno 
     * @param alumno    Alumno a matricular 
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * Getter Lista de matrículas del alumno
     * @return  Devuelve la lista de matrículas del alumno
     */
    public List<Matricula> getListaMatriculas() {
        return listaMatriculas;
    }

    /**
     * Setter Lista de matriculas del alumno
     * @param listaMatriculas   Lista de matriculas del alumno
     */
    public void setListaMatriculas(List<Matricula> listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }
    
    /**
     * Método que devuelve la cadena con el nombre del alumno
     * @return  Devuelve el nombre completo de alumno
     */
    @Override
    public String toString(){
        return alumno.getApellidos()+", "+alumno.getNombre();
    }
}
