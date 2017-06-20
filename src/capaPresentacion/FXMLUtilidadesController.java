/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaPresentacion;

import capaNegocio.OperativasBD;
import capaNegocio.Recibo;
import capaPresentacion.resources.Mensajes;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Francisco José Sampedro Lujan
 */
public class FXMLUtilidadesController implements Initializable {

    @FXML private VBox formularioDatos;
    @FXML private ComboBox<String> funcionCmb;
    @FXML private ComboBox<String> accionCmb;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitar(false);
        cargarFunciones();
        // Listener Selección Función ComboBox
        funcionCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
        if (newSelection!=null) cargarListaAcciones(newSelection);
        });
    }    

    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        List<Recibo> listaRecibos;
        switch (funcionCmb.getValue().toLowerCase()){
            case "generar recibos": listaRecibos=OperativasBD.generarRecibos(); 
                                    break;
            case "listados":        break;
        }
    }

    @FXML
    private void clickBtnCancelar(ActionEvent event) {
    }

    private void habilitar(boolean b) {
        btnAceptar.setDisable(!b);
        btnCancelar.setDisable(!b);
    }

    private void cargarFunciones() {
        funcionCmb.setItems(
            FXCollections.observableArrayList("Generar Recibos","Listados"));    
    }

    private void cargarListaAcciones(String seleccionFuncion) {
        habilitar(true);
        switch(seleccionFuncion.toLowerCase()){
            case "generar recibos": cargarAccionesRecibos();
                                    break;
            case "listados":        cargarAccionesListados();
                                    break;
            default:            Mensajes.msgError("ERROR CODIGO", 
                                    "Opción no implementada. Revise aplicación módulo Utilidades Controller");
                                break;
        }
    }

    private void cargarAccionesRecibos() {
        accionCmb.setItems(FXCollections.observableArrayList("Enero","Febrero"));
    }

    private void cargarAccionesListados() {
        accionCmb.setItems(FXCollections.observableArrayList("Balance","Ocupación Aulas"));
    }

    private void generarRecibos() {
    /*    select * from matricular
	left join alumnos on matricular.codAlumno=alumnos.codAlumno
	left join cursos on matricular.codcurso=cursos.codcurso
	where fechafin>=now() and fechainicio<=now();
    */
    }
    
}
