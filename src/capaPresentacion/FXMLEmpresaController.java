package capaPresentacion;

import capaNegocio.Empresa;
import capaNegocio.OperativasBD;
import capaNegocio.ShareData;
import capaPresentacion.resources.Campos;
import static capaPresentacion.resources.Campos.convertirImagen;
import capaPresentacion.resources.Mensajes;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 * Pantalla configuración empresa
 *
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLEmpresaController implements Initializable {

    @FXML private TextField nifTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField domicilioTxt;
    @FXML private Button aceptarBtn;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private TextField telefonoTxt;
    @FXML private Button cambiarLogoBtn;
    @FXML private TextField emailTxt;
    @FXML private ImageView logoImg; 
    @FXML private TextField impuestoTxt;
    @FXML private TextField porcentajeTxt;
    @FXML private CheckBox tutorCheck;
    private byte[] imagen;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nifTxt.setText(ShareData.EMPRESA.getNif());
        nombreTxt.setText(ShareData.EMPRESA.getNombre());
        domicilioTxt.setText(ShareData.EMPRESA.getDomicilio());
        poblacionTxt.setText(ShareData.EMPRESA.getPoblacion());
        cpTxt.setText(ShareData.EMPRESA.getCp());
        provinciaTxt.setText(ShareData.EMPRESA.getProvincia());
        telefonoTxt.setText(ShareData.EMPRESA.getTelefono());
        emailTxt.setText(ShareData.EMPRESA.getEmail());
        impuestoTxt.setText(ShareData.EMPRESA.getImpuesto());
        porcentajeTxt.setText(ShareData.EMPRESA.getPorcentajeImpuesto()+"");
        tutorCheck.setSelected(ShareData.EMPRESA.isTutorPorCurso());
        imagen=ShareData.EMPRESA.getLogo();
        logoImg.setImage(convertirImagen(ShareData.EMPRESA.getLogo()));
        logoImg.setFitHeight(180);
        logoImg.setFitWidth(180);
        logoImg.setPreserveRatio(false);
        logoImg.setSmooth(true);
        Campos.fijarTamanoMaximoConPatron(nifTxt,9,"[0-9A-Z]");
        Campos.fijarTamanoMaximoConPatron(nombreTxt,40,"");
        Campos.fijarTamanoMaximoConPatron(domicilioTxt,60,"");
        Campos.fijarTamanoMaximoConPatron(poblacionTxt,45,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        Campos.fijarTamanoMaximoConPatron(cpTxt,5,"[0-9]");
        Campos.fijarTamanoMaximoConPatron(provinciaTxt,40,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        Campos.fijarTamanoMaximoConPatron(telefonoTxt,50,"[0-9+()\\-\\. ]");
        Campos.fijarTamanoMaximoConPatron(emailTxt,50,"[0-9A-Za-zçÇ@\\-\\_\\.]");
        Campos.fijarTamanoMaximoConPatron(impuestoTxt,20,"[A-Za-z\\-\\_\\.]");
        Campos.fijarTamanoMaximoConPatron(porcentajeTxt,5,"[0-9\\.]");
    }    

    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickAceptarBtn(ActionEvent event) {
        if (Campos.validarCampoNumerico(porcentajeTxt.getText())){
            if (OperativasBD.cambiarEmpresa(new Empresa(nifTxt.getText(),nombreTxt.getText(),
                domicilioTxt.getText(),poblacionTxt.getText(), cpTxt.getText(),
                provinciaTxt.getText(), telefonoTxt.getText(),emailTxt.getText(),
                impuestoTxt.getText(), Double.parseDouble(porcentajeTxt.getText()),
                tutorCheck.selectedProperty().getValue(),imagen
                ))){
                Mensajes.msgInfo("DATOS EMPRESA", "Los datos de la empresa han sido actualizados");  
            }else{
                Mensajes.msgError("DATOS EMPRESA","Los datos de empresa no han sido actualizados");
            }
        }
    }

    /**
     * Acción al ser pulsado el botón CAMBIAR LOGO
     * @param event  Evento
     */
    @FXML
    private void clickCambiarLogoBtn(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "JPEG images", Arrays.asList("*.jpg")));
        File file=fileChooser.showOpenDialog(null);
        if (file!=null){
            Image img=new Image("file:"+file.getAbsolutePath(),180,180,false,true);
            logoImg.setImage(img);
            try {
                imagen = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                System.err.println("ERROR: Capa Presentacion | FXMLDatosEmpresaController"
                        + " | "+ex.getMessage());
            }
        }
    }
}
