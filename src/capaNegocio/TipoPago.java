package capaNegocio;

/**
 * Clase TipoPago
 * Esta clase contiene los parámetros de Tipos de Pagos
 * 
 * Atributos:
 *  codigo      : Código del Tipo de Pago
 *  descripcion : Descripción del Tipo de Pago
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre de la asignatura
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class TipoPago {
    private String codigo;
    private String descripcion;

    /**
     * Constructor parametrizado
     * @param codigo        Código del tipo de pago
     * @param descripcion   Descripción del tipo de pago
     */
    public TipoPago(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    /**
     * Getter código del tipo de pago
     * @return  Devuelve el código del tipo de pago
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Getter descripción del tipo de pago
     * @return  Devuelve la descripción del tipo de pago
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método que devuelve la cadena con el codigo y la descripción del tipo de pago
     * @return  Devuelve la cadena formada por código y descripción del tipo de pago
     */
    @Override
    public String toString() {
        return descripcion;
    }
    
    
}
