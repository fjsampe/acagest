/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class Factura {
    private int numFactura;
    private LocalDate fechaFactura;
    private String descripcion;
    private Double importeFactura;
    private List<ReciboGenerado> recibos;

    public Factura(int numFactura, LocalDate fechaFactura, String descripcion, Double importeFactura) {
        this.numFactura = numFactura;
        this.fechaFactura = fechaFactura;
        this.descripcion = descripcion;
        this.importeFactura = importeFactura;
    }

    public int getNumFactura() {
        return numFactura;
    }

    public String getFacturaFormateada() {
        String facturaFormateada="";
        if (fechaFactura!=null && numFactura>0){
            facturaFormateada=fechaFactura.getYear()+"/"+String.format("%05d", numFactura);
        }
        return facturaFormateada;
    }

    
    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public LocalDate getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(LocalDate fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getImporteFactura() {
        return importeFactura;
    }

    public String getImporteFormateado(){
        return String.format("%.2f€", importeFactura);
    }
    
    public void setImporteFactura(Double importeFactura) {
        this.importeFactura = importeFactura;
    }

    public List<ReciboGenerado> getRecibos() {
        return recibos;
    }

    public void setRecibos(List<ReciboGenerado> recibos) {
        this.recibos = recibos;
    }
    
    
    
}
