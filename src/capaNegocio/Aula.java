package capaNegocio;

import java.util.Objects;

/**
 * Clase Aula
 * Esta clase contiene los parámetros de Aulas
 * 
 * Atributos:
 *  codigo      : Código del aula
 *  descripcion : Descripción o nombre del aula
 *  ubicacion   : Ubicación dentro del centro
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre de la asignatura
 *  hashCode    : Devuelve código hash
 *  equals      : Devuelve true si la comparación de objetos es igual
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Aula {
    private String codigo;
    private String descripcion;
    private String ubicacion;

    /**
     * Constructor parametrizado
     * @param codigo        Código del aula
     * @param descripcion   Descripción o nombre del aula
     * @param ubicacion     Ubicación dentro del centro
     */
    public Aula(String codigo, String descripcion, String ubicacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    /**
     * Getter código del aula
     * @return  Devuelve el código del aula
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Setter código del aula
     * @param codigo    Código del aula
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter descripción del aula
     * @return  Devuelve la descripción del aula
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter descripción del aula
     * @param descripcion   Descripción del aula
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Getter ubicación del aula
     * @return  Devuelve la ubicación del aula
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Setter ubicación del aula
     * @param ubicacion     Ubicación del aula
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * HasCode
     * @return  Devuelve código hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.codigo);
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
        final Aula other = (Aula) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }
    
    /**
     * Método que devuelve la cadena con el codigo y la descripción del aula
     * @return  Devuelve la cadena formada por código y descripción del aula
     */
    @Override
    public String toString() {
         return "("+codigo+") "+descripcion;
    }
}


