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
public class Recibo {
    private Alumno alumno;
    private List<Curso> cursos;
    private LocalDate fechaEmision;

    public Recibo(Alumno alumno, List<Curso> cursos, LocalDate fechaEmision) {
        this.alumno = alumno;
        this.cursos = cursos;
        this.fechaEmision = fechaEmision;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }


    
}
