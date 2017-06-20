package capaNegocio;

import java.time.LocalDate;

/**
 * Clase Gasto
 * Esta clase contiene los parámetros de los gastos del centro
 * 
 * Atributos:
 *  numGasto    : Número del gasto
 *  concepto    : Concepto del gasto
 *  fecha       : Fecha del gasto
 *  base        : Importe base del gasto
 *  iva         : Iva del gasto
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Gasto {
    private String numGasto;
    private String concepto;
    private LocalDate fecha;
    private double base;
    private double iva;

    /**
     * Constructor parametrizado
     * @param concepto  Concepto del gasto
     * @param fecha     Fecha del gasto
     * @param base      Base imponible del gasto
     * @param iva       Iva del gasto
     */
    public Gasto(String concepto, LocalDate fecha, double base, double iva) {
        this.concepto = concepto;
        this.fecha = fecha;
        this.base = base;
        this.iva = iva;
    }

    /**
     * Getter del número de gasto
     * @return  Devuelve el número de gasto
     */
    public String getNumGasto() {
        return numGasto;
    }

    /**
     * Setter del número de gasto
     * @param numGasto  Número de gasto
     */
    public void setNumGasto(String numGasto) {
        this.numGasto = numGasto;
    }
    
    /**
     * Getter del concepto del gasto
     * @return  Devuelve el concepto del gasto
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Setter del concepto del gasto
     * @param concepto  Concepto del gasto
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * Getter de la fecha del gasto
     * @return  Devuelve la fecha del gasto
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Setter de la fecha del gasto
     * @param fecha     Fecha del gasto
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Getter de la base imponible del gasto
     * @return  Devuelve la base imponible del gasto
     */
    public double getBase() {
        return base;
    }

    /**
     * Setter de la base imponible del gasto
     * @param base  Base imponible del gasto
     */
    public void setBase(double base) {
        this.base = base;
    }

    /**
     * Getter del iva del gasto
     * @return  Devuelve el iva del gasto
     */
    public double getIva() {
        return iva;
    }

    /**
     * Setter del iva del gasto
     * @param iva   Iva del gasto
     */
    public void setIva(double iva) {
        this.iva = iva;
    }
}
