package capaNegocio;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Clase Plan
 * Esta clase contiene los parámetros de la planificación de horario/asignatura/aula
 * 
 * Atributos:
 *  asignatura  : Asignatura del plan
 *  aula        : Aula del plan
 *  diaSemana   : Dia de la semana
 *  deHora      : Hora de inicio de la clase
 *  aHora       : Hora de finalización de la clase
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Devuelve la cadena formada por el valor del dia de la semana y
 *                la hora de inicio.
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.2
 */
public class Plan {
    private Asignatura asignatura;
    private Aula aula;
    private DayOfWeek diaSemana;
    private LocalTime deHora;
    private LocalTime aHora;
    

    /**
     * Constructor parametrizado
     * @param asignatura    Asignatura
     * @param aula          Aula
     * @param diaSemana     Dia de la semana
     * @param deHora        Hora de inicio
     * @param aHora         Hora de fin
     */
    public Plan(Asignatura asignatura, Aula aula, DayOfWeek diaSemana, LocalTime deHora, LocalTime aHora) {
        this.asignatura = asignatura;
        this.aula = aula;
        this.diaSemana = diaSemana;
        this.deHora = deHora;
        this.aHora = aHora;
    }

    /**
     * Getter de la asignatura del plan
     * @return  Devuelve la asignatura del plan
     */
    public Asignatura getAsignatura() {
        return asignatura;
    }
    
    /**
     * Getter del código de la asignatura del plan
     * @return  Devuelve el código de la asignatura del plan
     */
    public String getCodigoAsignatura(){
        return asignatura.getCodigo();
    }
    
    /**
     * Getter del nombre de la asignatura del plan
     * @return  Devuelve el nombre de la asignatura del plan
     */
    public String getDescripcionAsignatura(){
        return asignatura.getNombre();
    }

    /**
     * Setter de la asignatura del plan
     * @param asignatura    Asignatura del plan
     */
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    /**
     * Getter del aula del plan
     * @return  Devuelve el aula del plan
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * Setter del aula del plan
     * @param aula  Aula del plan
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    /**
     * Getter del código del aula del plan
     * @return  Devuelve el código del aula del plan
     */
    public String getCodigoAula(){
        return aula.getCodigo();
    }
    
    /**
     * Getter de la descripción del aula del plan
     * @return  Devuelve la descripción del aula del plan
     */
    public String getDescripcionAula(){
        return aula.getDescripcion();
    }

    /**
     * Getter del dia de la Semana
     * @return  Devuelve el dia de la semana 
     */
    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    /**
     * Getter del dia de la Semana
     * @return Devuelve el dia de la semana en Español
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
     * @param diaSemana     Dia de la semana.
     */
    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    /**
     * Getter Hora de inicio clase
     * @return  Devuelve la hora de inicio de la clase
     */
    public LocalTime getDeHora() {
        return deHora;
    }

    /**
     * Setter de la hora de inicio de la clase
     * @param deHora    Hora de inicio de la clase
     */
    public void setDeHora(LocalTime deHora) {
        this.deHora = deHora;
    }

    /**
     * Getter de la hora de finalización de la clase
     * @return  Devuelve la hora de finalización de la clase
     */
    public LocalTime getAHora() {
        return aHora;
    }

    /**
     * Setter de la hora de fin de la clase
     * @param aHora     Hora de fin de la clase
     */
    public void setAHora(LocalTime aHora) {
        this.aHora = aHora;
    }
    
    /**
     * Método que devuelve el dia de la semana y la hora de inicio
     * @return  Devuelve la cadena formada por el valor del dia de la semana y la hora de inicio
     */
    @Override
    public String toString() {
         return diaSemana.getValue()+""+deHora+"";
    }
}
