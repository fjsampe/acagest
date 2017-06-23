/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import java.time.LocalDate;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class ReciboGenerado {
    private int recibo;
    private LocalDate fechaEmision;
    private LocalDate fechaPago;
    private String descripcion;
    private double importe;
    private String factura;

    public ReciboGenerado(int recibo, LocalDate fechaEmision, LocalDate fechaPago, String descripcion, double importe, String factura) {
        this.recibo = recibo;
        this.fechaEmision = fechaEmision;
        this.fechaPago = fechaPago;
        this.descripcion = descripcion;
        this.importe = importe;
        this.factura = factura;
    }

    public int getRecibo() {
        return recibo;
    }

    public void setRecibo(int recibo) {
        this.recibo = recibo;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }
    
    
}
