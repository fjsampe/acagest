/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import capaPresentacion.resources.Campos;
import java.time.LocalDate;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class Movimiento {
    private LocalDate fecha;
    private String clienteProveedor;
    private String concepto;
    private double baseImponible;
    private double iva;
    private String tipo;

    public Movimiento(LocalDate fecha, String clienteProveedor, String concepto, double baseImponible, double iva, String tipo) {
        this.fecha = fecha;
        this.clienteProveedor = clienteProveedor;
        this.concepto = concepto;
        this.baseImponible = baseImponible;
        this.iva=iva;
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public String getFechaFormateado(){
        return Campos.fechaToString(fecha);
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getClienteProveedor() {
        return clienteProveedor;
    }

    public void setClienteProveedor(String clienteProveedor) {
        this.clienteProveedor = clienteProveedor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getBaseImponible() {
        return baseImponible;
    }
    
    public String getBaseImpobibleFormateado(){
        return String.format("%.2f€", baseImponible);
    }

    public String getImporteFormateado(){
        return String.format("%.2f€", baseImponible+iva);
    }

    
    public void setBaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public double getIva() {
        return iva;
    }
    
    public String getIvaFormateado(){
        return String.format("%.2f€", iva);
    }

    public void setIva(double iva) {
        this.iva = iva;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    
}
