/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import capaPresentacion.resources.Campos;
import java.time.LocalDate;
import java.util.Objects;

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
    private boolean check;

    
 

    public ReciboGenerado(int recibo, LocalDate fechaEmision, LocalDate fechaPago, 
        String descripcion, double importe, String factura) {
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
    
    public String getReciboFormateado(){
        return String.format("%07d", recibo);
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }
    
    public String getFechaEmisionFormateado(){
        return Campos.fechaToString(fechaEmision);
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }
    
    public String getFechaPagoFormateado(){
        return Campos.fechaToString(fechaPago);
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
    
    public String getImporteFormateado(){
        return String.format("%.2f€", importe);
    }

    public String getFactura() {
        return factura;
    }
    
    public String getFacturaFormateado() {
        String facturaFormateada="";
        if (fechaPago!=null && factura!=null){
            facturaFormateada=fechaPago.getYear()+"/"+String.format("%05d", Integer.parseInt(factura));
        }
        return facturaFormateada;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }
    
    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.recibo;
        hash = 67 * hash + Objects.hashCode(this.fechaEmision);
        hash = 67 * hash + Objects.hashCode(this.fechaPago);
        hash = 67 * hash + Objects.hashCode(this.descripcion);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.importe) ^ (Double.doubleToLongBits(this.importe) >>> 32));
        hash = 67 * hash + Objects.hashCode(this.factura);
        return hash;
    }

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
        final ReciboGenerado other = (ReciboGenerado) obj;
        if (this.recibo != other.recibo) {
            return false;
        }
        if (Double.doubleToLongBits(this.importe) != Double.doubleToLongBits(other.importe)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.factura, other.factura)) {
            return false;
        }
        if (!Objects.equals(this.fechaEmision, other.fechaEmision)) {
            return false;
        }
        if (!Objects.equals(this.fechaPago, other.fechaPago)) {
            return false;
        }
        return true;
    }

    
    
    
}
