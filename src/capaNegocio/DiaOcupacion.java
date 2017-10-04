package capaNegocio;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Clase DiaOcupacion
 * Esta clase contiene los parámetros de los dias de ocupación
 * Atributos:
 *  diaSemana   : Dia de la semana
 *  deHora      : Hora de inicio
 *  aHora       : Hora de finalización
 *  asignatura  : Asignatura
 *  profesor    : Profesor
 *
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  getDiaSemanaSpanish    : Devuelve el dia de la semana en español
 * 
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.2
 */
public class DiaOcupacion {
    private DayOfWeek diaSemana;
    private LocalTime deHora;
    private LocalTime aHora;
    private String asignatura;
    private String profesor;

    /**
     * Constructor parametrizado
     * @param diaSemana     Dia de la semana
     * @param deHora        Hora de inicio
     * @param aHora         Hora de finalización
     * @param asignatura    Asignatura
     * @param profesor      Profesor
     */
    public DiaOcupacion(DayOfWeek diaSemana, LocalTime deHora, LocalTime aHora, String asignatura, String profesor) {
        this.diaSemana = diaSemana;
        this.deHora = deHora;
        this.aHora = aHora;
        this.asignatura = asignatura;
        this.profesor = profesor;
    }

    /**
     * Getter dia de la semana
     * @return  Devuelve el dia de la semana
     */
    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }
    
    /**
     * Devuelve el día de la semana en español
     * @return  Devuelve el dia de la semana en español
     */
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

    /**
     * Setter dia de la semana
     * @param diaSemana     Dia de la semana
     */
    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    /**
     * Getter hora de inicio
     * @return  Devuelve la hora de inicio
     */
    public LocalTime getDeHora() {
        return deHora;
    }

    /**
     * Setter hora de inicio
     * @param deHora    Hora de inicio
     */
    public void setDeHora(LocalTime deHora) {
        this.deHora = deHora;
    }

    /**
     * Getter hora de finalización
     * @return  Devuelve la hora de finalización
     */
    public LocalTime getaHora() {
        return aHora;
    }

    /**
     * Setter hora de finalización
     * @param aHora     Hora de finalización
     */
    public void setaHora(LocalTime aHora) {
        this.aHora = aHora;
    }

    /**
     * Getter asignatura
     * @return  Devuelve la asignatura
     */
    public String getAsignatura() {
        return asignatura;
    }

    /**
     * Setter asignatura
     * @param asignatura    Asignatura 
     */
    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    /**
     * Getter profesor
     * @return  Devuelve el profesor
     */
    public String getProfesor() {
        return profesor;
    }

    /**
     * Setter profesor
     * @param profesor  Profesor 
     */
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
