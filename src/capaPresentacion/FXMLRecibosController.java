/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaPresentacion;

import capaNegocio.FichaRecibo;
import capaNegocio.Recibo;
import capaNegocio.ReciboGenerado;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Francisco José Sampedro Lujan
 */
public class FXMLRecibosController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaRecibo> alumnosListView;
    @FXML private VBox formularioDatos;
    @FXML private HBox formularioDatosAlumno;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private ImageView fotoImg;
    @FXML private HBox formularioDatosMatriculaciones;  
    
    @FXML private ComboBox<String> cursoCmb;
    @FXML private ComboBox<String> asignaturaCmb;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private TableView<ReciboGenerado> recibosTable;
    @FXML private TableColumn<Recibo, Integer> numeroReciboCol;
    @FXML private TableColumn<Recibo, LocalDate> fechaEmisionReciboCol;
    @FXML private TableColumn<Recibo, LocalDate> fechaPagoReciboCol;
    @FXML private TableColumn<Recibo, String> descripcionReciboCol;
    @FXML private TableColumn<Recibo, Double> importeReciboCol;
    @FXML private TableColumn<Recibo, String> facturaReciboCol;
    @FXML private Button addReciboBtn;
    @FXML private Button delReciboBtn;
    @FXML private HBox barraConfirmacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button editBtn;
    @FXML private Button printBtn;
    
  
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickAddReciboBtn(ActionEvent event) {
    }

    @FXML
    private void clickDelReciboBtn(ActionEvent event) {
    }

    @FXML
    private void clickBtnAceptar(ActionEvent event) {
    }

    @FXML
    private void clickBtnCancelar(ActionEvent event) {
    }

    @FXML
    private void clickEditBtn(ActionEvent event) {
    }

    @FXML
    private void clickPrintBtn(ActionEvent event) {
    }
    
}
