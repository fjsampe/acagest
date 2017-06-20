package capaNegocio;

import java.util.List;

/**
 * Clase FichaGasto
 * Esta clase contiene los parámetros de las fichas de gasto del proveedor
 * 
 * Atributos:
 *  proveedor       : Proveedor
 *  listaGastos     : Lista de gastos del proveedor
 * 
 * Constructor, Getters y Setters
 * 
 * Métodos:
 *  toString    : Return el nombre del proveedor
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FichaGasto {
    private Proveedor proveedor;
    private List<Gasto> listaGastos;

    /**
     * Constructor parametrizado
     * @param proveedor     Proveedor
     * @param listaGastos   Lista de gastos del proveedor
     */
    public FichaGasto(Proveedor proveedor, List<Gasto> listaGastos) {
        this.proveedor = proveedor;
        this.listaGastos = listaGastos;
    }

    /**
     * Getter del proveedor
     * @return  Devuelve el proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Setter del proveedor
     * @param proveedor Proveedor
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Getter de la lista de gastos del proveedor
     * @return  Devuelve la lista de gastos del proveedor
     */
    public List<Gasto> getListaGastos() {
        return listaGastos;
    }

    /**
     * Setter de la lista de gastos del proveedor
     * @param listaGastos   Lista de gastos del proveedor
     */
    public void setListaGastos(List<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }
    
    /**
     * Método que devuelve la cadena con el nombre del proveedor
     * @return  Devuelve el nombre completo del proveedor
     */
    @Override
    public String toString(){
        return proveedor.getApellidos()+", "+proveedor.getNombre();
    }
}
