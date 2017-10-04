package capaNegocio;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase Examen
 * Esta clase contiene los parámetros de Examenes
 * 
 * Atributos:
 *  asignatura  : Asignatura del examen
 *  fecha       : Fecha del examen
 *  nota        : Nota del examen
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Examen {
    private Asignatura asignatura;
    private LocalDate fecha;
    private float nota;

    /**
     * Constructor parametrizado
     * @param asignatura    Asignatura del examen
     * @param fecha         Fecha del examen
     * @param nota          Nota del examen
     */
    public Examen(Asignatura asignatura, LocalDate fecha, float nota) {
        this.asignatura = asignatura;
        this.fecha = fecha;
        this.nota = nota;
    }

    /**
     * Getter asignatura del examen
     * @return  Devuelve la asignatura del examen
     */
    public Asignatura getAsignatura() {
        return asignatura;
    }

    /**
     * Getter del código de la asignatura del examen
     * @return  Devuelve el código de la asignatura del examen
     */
    public String getCodigoAsignatura(){
        return asignatura.getCodigo();
    }
    
    /**
     * Getter de la descripción de la asignatura del examen
     * @return  Devielve la descripción de la asignatura del examen
     */
    public String getDescripcionAsignatura(){
        return asignatura.getNombre();
    }
    
    /**
     * Setter de la asignatura del examen.
     * @param asignatura    Asignatura del examen
     */
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    /**
     * Getter de la fecha del examen
     * @return  Devuelve la fecha del examen
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Setter de la fecha del examen
     * @param fecha     Fecha del examen
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Getter nota del examen
     * @return  Devuelve la nota del examen
     */
    public float getNota() {
        return nota;
    }

    /**
     * Setter de la nota del examen
     * @param nota  Nota del examen
     */
    public void setNota(float nota) {
        this.nota = nota;
    }
    
    /**
     * HasCode
     * @return  Devuelve código hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.asignatura);
        hash = 73 * hash + Objects.hashCode(this.fecha);
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
        final Examen other = (Examen) obj;
        if (!Objects.equals(this.asignatura, other.asignatura)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        return true;
    }
}
