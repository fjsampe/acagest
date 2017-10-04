package capaNegocio;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase Factura
 * Esta clase contiene los valores de la factura
 * 
 * Atributos:
 *  numFactura      : Número de la factura
 *  fechaFactura    : Fecha de la factura
 *  descripcion     : Descripción de la factura
 *  importeFactura  : Importe de la factura (IVA incluido)  
 * @author Francisco José Sampedro Lujan
 */
public class Factura {
    private int numFactura;
    private LocalDate fechaFactura;
    private String descripcion;
    private Double importeFactura;
    private List<ReciboGenerado> recibos;

    /**
     * Constructor parametrizado
     * @param numFactura        Número de factura
     * @param fechaFactura      Fecha de la factura
     * @param descripcion       Descripción de la factura
     * @param importeFactura    Importe de la factura
     */
    public Factura(int numFactura, LocalDate fechaFactura, String descripcion, Double importeFactura) {
        this.numFactura = numFactura;
        this.fechaFactura = fechaFactura;
        this.descripcion = descripcion;
        this.importeFactura = importeFactura;
    }

    /**
     * Getter número de factura
     * @return  Devuelve el número de factura
     */
    public int getNumFactura() {
        return numFactura;
    }

    /**
     * Geter número de factura con formato
     * @return  Devuelve el número de factura formateado (yyyy/xxxxx)
     */
    public String getFacturaFormateada() {
        String facturaFormateada="";
        if (fechaFactura!=null && numFactura>0){
            facturaFormateada=fechaFactura.getYear()+"/"+String.format("%05d", numFactura);
        }
        return facturaFormateada;
    }

    /**
     * Setter número de factura
     * @param numFactura    Número de factura
     */
    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    /**
     * Getter fecha de factura
     * @return  Devuelve la fecha de factura
     */
    public LocalDate getFechaFactura() {
        return fechaFactura;
    }

    /**
     * Setter fecha de factura
     * @param fechaFactura  Fecha de factura
     */
    public void setFechaFactura(LocalDate fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    /**
     * Getter descripción de la factura
     * @return  Devuelve la descripción de la factura
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter descripción de la factura
     * @param descripcion   Descripción de la factura
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Getter importe de la factura
     * @return  Devuelve el importe de la factura
     */
    public Double getImporteFactura() {
        return importeFactura;
    }

    /**
     * Getter importe de la factura con formato
     * @return  Devuelve el importe de la factura con 2 decimales y moneda
     */
    public String getImporteFormateado(){
        return String.format("%.2f€", importeFactura);
    }
    
    /**
     * Setter importe de la factura
     * @param importeFactura    Importe de la factura
     */
    public void setImporteFactura(Double importeFactura) {
        this.importeFactura = importeFactura;
    }

    /**
     * Getter recibos que contiene la factura
     * @return  Devuelve los recibos de la factura
     */
    public List<ReciboGenerado> getRecibos() {
        return recibos;
    }

    /**
     * Setter recibos que contienen la factura
     * @param recibos   Recibos que contiene la factura
     */
    public void setRecibos(List<ReciboGenerado> recibos) {
        this.recibos = recibos;
    }
}
