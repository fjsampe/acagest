package capaNegocio;

import java.util.List;

/**
 * Clase Curso
 * Esta clase contiene los parámetros de Aulas
 * 
 * Atributos:
 *  codigo      : Código del curso
 *  descripcion : Descripción del curso
 *  importe     : Coste del curso
 *  fechaInicio : Fecha inicio del curso
 *  fechaFin    : Fecha fin del curso
 *  pago        : Tipo de pago (Anual-Mensual-Semanal)
 *  tutor       : Tutor del curso
 *  asignaturas : Lista de asignaturas del curso
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el código y la descripción del curso
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Curso {
    
    private String codigo;
    private String descripcion;
    private double importeHora;
    private String pago;
    private Profesor tutor;
    private List<Asignatura> asignaturas;

    /**
     * Constructor parametrizado
     * @param codigo        Código del curso
     * @param descripcion   Descripción del curso
     * @param importeHora   Importe por hora
     * @param pago          Tipo de pago del curso (Anual - Mensual - Semanal)
     * @param tutor         Tutor del curso
     */
    public Curso(String codigo, String descripcion, double importeHora,String pago, Profesor tutor) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.importeHora = importeHora;
        this.pago = pago;
        this.tutor = tutor;
    }

    /**
     * Getter código del curso
     * @return  Devuelve el código del aula
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Setter código del curso
     * @param codigo    Código del curso
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Getter descripción del curso
     * @return  Devuelve la descripción del curso
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter descripción del curso
     * @param descripcion   Descripción del curso
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Getter importe por hora del curso
     * @return  Devuelve el importe por hora del curso
     */
    public double getImporteHora() {
        return importeHora;
    }

    /**
     * Setter importe por hora del curso
     * @param importeHora   Importe por hora del curso
     */
    public void setImporteHora(double importeHora) {
        this.importeHora = importeHora;
    }

    /**
     * Getter tipo de pago del curso
     * @return  Devuelve el tipo de pago del curso (A)nual - (M)ensual - (S)emanal
     */
    public String getPago() {
        return pago;
    }

    /**
     * Setter tipo de pago del curso
     * @param pago  Tipo de pago del curso (A)nual - (M)ensual - (S)emanal
     */
    public void setPago(String pago) {
        this.pago = pago;
    }

    /**
     * Getter del tutor del curso
     * @return  Devuelve el tutor del curso
     */
    public Profesor getTutor() {
        return tutor;
    }

    /**
     * Setter del tutor del curso
     * @param tutor Tutor del curso
     */
    public void setTutor(Profesor tutor) {
        this.tutor = tutor;
    }

    /**
     * Getter de las asignaturas del curso
     * @return  Devuelve las asignaturas del curso
     */
    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    /**
     * Setter de las asignaturas del curso
     * @param asignaturas   Lista de asignaturas del curso
     */
    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    /**
     * Método que devuelve la cadena con el código y la descripción del curso
     * @return  Devuelve la cadena formada por código y descripción del curso
     */
    @Override
    public String toString() {
        return "("+codigo+") "+descripcion;
    }
}
