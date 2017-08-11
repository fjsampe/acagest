
package capaNegocio;

import java.util.List;

/**
 *
 * @author Francisco José Sampedro Lujan
 */
public class OcupacionAula {
    private Aula aula;
    private List<DiaOcupacion> diasOcupacion;

    public OcupacionAula(Aula aula) {
        this.aula = aula;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public List<DiaOcupacion> getDiasOcupacion() {
        return diasOcupacion;
    }

    public void setDiasOcupacion(List<DiaOcupacion> diasOcupacion) {
        this.diasOcupacion = diasOcupacion;
    }
    
    
   
    
}
