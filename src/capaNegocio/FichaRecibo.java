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
public class FichaRecibo {
    private Alumno alumno;
    private List<ReciboGenerado> listaRecibos;

    public FichaRecibo(Alumno alumno, List<ReciboGenerado> listaRecibos) {
        this.alumno = alumno;
        this.listaRecibos = listaRecibos;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public List<ReciboGenerado> getListaRecibos() {
        return listaRecibos;
    }

    public void setListaRecibos(List<ReciboGenerado> listaRecibos) {
        this.listaRecibos = listaRecibos;
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
