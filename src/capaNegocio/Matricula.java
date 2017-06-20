package capaNegocio;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Clase Matricula
 * Esta clase contiene la matrícula del alumno
 * 
 * Atributos:
 *  codigo      : Código del horario
 *  descripcion : Descrición del horario
 *  horas       : Lista de horas
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Devuelve cadena con el código y descripción del horario
 *  hashCode    : Devuelve código hash
 *  equals      : Devuelve true si la comparación de objetos es igual
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Matricula {
    private String codCurso;
    private String descripcionCurso;
    private String codAsignatura;
    private String descripcionAsignatura;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /**
     * Constructor parametrizado
     * @param codCurso              : Código del curso matriculado
     * @param descripcionCurso      : Descripción del curso matriculado
     * @param codAsignatura         : Código de la asignatura matriculada
     * @param descripcionAsignatura : Descripción de la asignatura matriculada
     * @param fechaInicio           : Fecha inicio curso/asignatura
     * @param fechaFin              : Fecha finalización curso/asignatura
     */
    public Matricula(String codCurso, String descripcionCurso, String codAsignatura, String descripcionAsignatura, LocalDate fechaInicio, LocalDate fechaFin) {
        this.codCurso = codCurso;
        this.descripcionCurso = descripcionCurso;
        this.codAsignatura = codAsignatura;
        this.descripcionAsignatura = descripcionAsignatura;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /**
     * Getter código del curso matriculado
     * @return  Devuelve el código del curso matriculado
     */
    public String getCodCurso() {
        return codCurso;
    }

    /**
     * Setter código del curso matriculado
     * @param codCurso  Código de Curso
     */
    public void setCodCurso(String codCurso) {
        this.codCurso = codCurso;
    }

    /**
     * Getter descripción del curso matriculado
     * @return  Devuelve la descripción del curso matriculado
     */
    public String getDescripcionCurso() {
        return descripcionCurso;
    }

    /**
     * Setter de la descripción del curso matriculado
     * @param descripcionCurso  Descripción del curso matriculado
     */
    public void setDescripcionCurso(String descripcionCurso) {
        this.descripcionCurso = descripcionCurso;
    }

    /**
     * Getter código de asignatura matriculada
     * @return  Devuelve la asignatura matriculada
     */
    public String getCodAsignatura() {
        return codAsignatura;
    }

    /**
     * Setter del código de asignatura matriculada
     * @param codAsignatura     Código asignatura matriculada
     */
    public void setCodAsignatura(String codAsignatura) {
        this.codAsignatura = codAsignatura;
    }
    
    /**
     * Getter de la descripción de la asignatura matriculada
     * @return  Devuelve la descripción de la asignatura matriculada
     */
    public String getDescripcionAsignatura() {
        return descripcionAsignatura;
    }

    /**
     * Setter de la descripción de la asignatura matriculada
     * @param descripcionAsignatura Descripción de la Asignatura
     */
    public void setDescripcionAsignatura(String descripcionAsignatura) {
        this.descripcionAsignatura = descripcionAsignatura;
    }
    
    /**
     * Getter de la fecha de inicio del curso/asignatura matriculada
     * @return  Devuelve la fecha de inicio
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Setter de la fecha de inicio del curso/asignatura matriculada
     * @param fechaInicio   Fecha de inicio
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Getter de la fecha de fin del curso/asignatura matriculada
     * @return  Devuelve la fecha de fin
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Setter de la fecha del fin del curso/asignatura matriculada
     * @param fechaFin  Fecha de finalización
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Método hascode
     * @return  Devuelve hascode
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.codCurso);
        hash = 83 * hash + Objects.hashCode(this.codAsignatura);
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
        final Matricula other = (Matricula) obj;
        if (!Objects.equals(this.codCurso, other.codCurso)) {
            return false;
        }
        if (!Objects.equals(this.codAsignatura, other.codAsignatura)) {
            return false;
        }
        return true;
    }
}
