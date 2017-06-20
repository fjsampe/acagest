package capaNegocio;

import java.util.List;

/**
 * Clase FichaPlanificador
 * Esta clase contiene los parámetros de las fichas de los planes 
 * de horas-aulas-profesores
 * 
 * Atributos:
 *  profesor    : Profesor
 *  listaPlanes : Lista de planes del profesor
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre del profesor
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FichaPlanificador {
    private Profesor profesor;
    private List<Plan> listaPlanes;

    /**
     * Contructor parametrizado
     * @param profesor      Profesor del plan
     * @param listaPlanes   Lista de planes del profesor
     */
    public FichaPlanificador(Profesor profesor, List<Plan> listaPlanes) {
        this.profesor = profesor;
        this.listaPlanes = listaPlanes;
    }

    /**
     * Getter del profesor del plan
     * @return  Devuelve el profesor del plan
     */
    public Profesor getProfesor() {
        return profesor;
    }

    /**
     * Setter del profesor del plan
     * @param profesor  Profesor del plan
     */
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    /**
     * Getter de la lista de planes del profesor
     * @return  Devuelve la lista de planes de profesor
     */
    public List<Plan> getListaPlanes() {
        return listaPlanes;
    }

    /**
     * Setter de la lista de planes del profesor
     * @param listaPlanes   Lista de planes del profesor
     */
    public void setListaPlanes(List<Plan> listaPlanes) {
        this.listaPlanes = listaPlanes;
    }

    /**
     * Método que devuelve la cadena con el nombre del profesor
     * @return  Devuelve el nombre completo del profesor
     */
    @Override
    public String toString(){
        return profesor.getApellidos()+", "+profesor.getNombre();
    }
}
