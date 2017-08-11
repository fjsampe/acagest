/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaPresentacion.resources;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class CamposTablaPDF {
    private String cadena;
    private String alineamiento;
    private boolean sombreado;

    public CamposTablaPDF(String cadena, String alineamiento, boolean sombreado) {
        this.cadena = cadena;
        this.alineamiento = alineamiento;
        this.sombreado=sombreado;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getAlineamiento() {
        return alineamiento;
    }

    public void setAlineamiento(String alineamiento) {
        this.alineamiento = alineamiento;
    }

    public boolean isSombreado() {
        return sombreado;
    }

    public void setSombreado(boolean sombreado) {
        this.sombreado = sombreado;
    }
    
    
    
}
