package capaNegocio;

import java.util.List;

/**
 * Clase FichaExamen
 * Esta clase contiene los parámetros de las fichas de examen del alumno
 * 
 * Atributos:
 *  alumno          : Alumno
 *  listaExamenes   : Lista de exámenes del alumno
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el código y la descripción del curso
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FichaExamen {
    private Alumno alumno;
    private List<Examen> listaExamenes;

    /**
     * Constructor parametrizado
     * @param alumno        Alumno
     * @param listaExamenes Lista de exámenes del alumno
     */
    public FichaExamen(Alumno alumno, List<Examen> listaExamenes) {
        this.alumno = alumno;
        this.listaExamenes = listaExamenes;
    }

    /**
     * Getter del alumno
     * @return  Devuelve el alumno
     */
    public Alumno getAlumno() {
        return alumno;
    }

    /**
     * Setter del alumno
     * @param alumno    Alumno
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * Getter de la lista de exámenes del alumno
     * @return  Devuelve la lista de exámenes del alumno
     */
    public List<Examen> getListaExamenes() {
        return listaExamenes;
    }

    /**
     * Setter de la lista de exámenes del alumno
     * @param listaExamenes Lista de exámenes del alumno     
     */
    public void setListaExamenes(List<Examen> listaExamenes) {
        this.listaExamenes = listaExamenes;
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
