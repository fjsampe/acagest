/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import java.util.List;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class FichaFactura {
    
    private Alumno alumno;
    private List<Factura> lista;

    public FichaFactura(Alumno alumno, List<Factura> lista) {
        this.alumno = alumno;
        this.lista = lista;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public List<Factura> getLista() {
        return lista;
    }

    public void setLista(List<Factura> lista) {
        this.lista = lista;
    }
    
   
    
    /**
     * Método que devuelve la cadena con el nombre del alumno
     * @return  Devuelve el nombre completo de alumno
     */
    @Override
    public String toString(){
        return alumno.getApellidos()+", "+alumno.getNombre();
    }
    
    
}
