package capaNegocio;

import java.util.Objects;

/**
 * Clase Asignatura
 * Esta clase contiene los parámetros de Asignaturas
 * 
 * Atributos:
 *  codigo      : Código de la asignatura
 *  nombre      : Asignatura
 *  cargaHoras  : Carga de Horas por asignatura
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre de la asignatura
 *  hashCode    : Devuelve código hash
 *  equals      : Devuelve true si la comparación de objetos es igual
 *
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Asignatura {
    private String codigo;
    private String nombre;
    private int cargaHoras;
    
    
    /**
     * Constructor parametrizado
     * @param codigo        Código de la asignatura
     * @param nombre        Asignatura
     * @param cargaHoras    Carga de Horas de la asignatura
     */
    public Asignatura(String codigo, String nombre, int cargaHoras) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cargaHoras = cargaHoras;
    }

    /**
     * Getter código de la asignatura
     * @return  Devuelve el código de la asignatura
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Setter código de la asignatura
     * @param codigo    Código de la asignatura
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter asignatura
     * @return  Devuelve el nombre de la asignatura
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter nombre de la asignatura
     * @param nombre    Nombre de la asignatura
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** 
     * Getter carga de horas de la asignatura
     * @return  Devuelve la carga de horas de la asignatura
     */
    public int getCargaHoras() {
        return cargaHoras;
    }

    /**
     * Setter carga de horas de la asignatura
     * @param cargaHoras    Carga de horas de la asignatura
     */
    public void setCargaHoras(int cargaHoras) {
        this.cargaHoras = cargaHoras;
    }
    
    /**
     * HasCode
     * @return  Devuelve código hash
     */
    @Override
    public int hashCode(){
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.codigo);
        hash = 89 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    /**
     * Método equals
     * @param obj   Objeto a Comparar
     * @return      Devuelve True=si son iguales los objetos a comparar, en caso 
     *              contrario devuelve false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Asignatura other = (Asignatura) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    /**
     * Método que devuelve la cadena con el nombre del alumno
     * @return  Devuelve el nombre completo de alumno
     */
    @Override
    public String toString() {
        return "("+codigo+") "+nombre;
    }
}

