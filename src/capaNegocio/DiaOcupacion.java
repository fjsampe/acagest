/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaNegocio;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class DiaOcupacion {
    private DayOfWeek diaSemana;
    private LocalTime deHora;
    private LocalTime aHora;
    private String asignatura;
    private String profesor;

    public DiaOcupacion(DayOfWeek diaSemana, LocalTime deHora, LocalTime aHora, String asignatura, String profesor) {
        this.diaSemana = diaSemana;
        this.deHora = deHora;
        this.aHora = aHora;
        this.asignatura = asignatura;
        this.profesor = profesor;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }
    
    public String getDiaSemanaSpanish(){
        String cadena="";
        switch (diaSemana){
            case MONDAY:    cadena="LUNES";
                            break;
            case TUESDAY:   cadena="MARTES";
                            break;
            case WEDNESDAY: cadena="MIÉRCOLES";
                            break;
            case THURSDAY:  cadena="JUEVES";
                            break;
            case FRIDAY:    cadena="VIERNES";
                            break;
            case SATURDAY:  cadena="SÁBADO";
                            break;
            case SUNDAY:    cadena="DOMINGO";
                            break;
        }
        return cadena;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getDeHora() {
        return deHora;
    }

    public void setDeHora(LocalTime deHora) {
        this.deHora = deHora;
    }

    public LocalTime getaHora() {
        return aHora;
    }

    public void setaHora(LocalTime aHora) {
        this.aHora = aHora;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

   
}
