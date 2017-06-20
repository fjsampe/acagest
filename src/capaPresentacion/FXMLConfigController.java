package capaPresentacion;


import static capaNegocio.OperativasBD.crearTablasBD;
import static capaNegocio.OperativasBD.crearUsuarioBD;
import static capaNegocio.OperativasBD.validarAccesoServerBD;
import capaPresentacion.resources.Mensajes;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Formulario para la gestión de la conexión a PostgreSQL 
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLConfigController implements Initializable {
    @FXML private Label cabecera;
    @FXML private TextField servidorTxt;
    @FXML private TextField puertoTxt;
    @FXML private TextField claveTxt;
    @FXML private Button aceptarBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickAceptarBtn(ActionEvent event) {
        if (!servidorTxt.getText().equals("") && !puertoTxt.getText().equals("") 
                && !claveTxt.getText().equals("")){
            if(validarAccesoServerBD(servidorTxt.getText(), puertoTxt.getText(), claveTxt.getText())){
                if (crearUsuarioBD(servidorTxt.getText(), puertoTxt.getText(),claveTxt.getText())){
                    if (crearTablasBD(servidorTxt.getText(), puertoTxt.getText(),"dbacademia", "acagest", "clavel98")){
                        try {
                            Parent root=FXMLLoader.load(getClass().getResource("resources/fxml/FXMLLogin.fxml"));
                            Scene scene = new Scene(root);
                            Stage stage=(Stage)aceptarBtn.getScene().getWindow();
                            scene.getStylesheets().add(getClass().getResource("resources/css/loginStyleSheet.css").toExternalForm()); // CSS
                            stage.getIcons().add(new Image(getClass().getResource("/capaPresentacion/resources/icons/candado.png").toExternalForm()));
                            stage.setTitle("Login");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            System.err.println("ERROR: Carga Formulario Login");
                        }
                    }else{
                        Mensajes.msgError("ERROR POSTGRESQL", "No se ha podido crear las tablas dela BBDD acagest");
                    }
                }else{
                    Mensajes.msgError("ERROR POSTGRESQL", "No se ha podido crear usuario/BBDD/Tablas revise mensajes log");
                }
            }else{
                Mensajes.msgError("ERROR PARÁMETROS", "No se puede acceder al Servidor POSTGRESQL");
            }
        }else{
            Mensajes.msgError("ERROR PARÁMETROS", "Uno o más campos están vacios. "
                    + "Por favor rellene todos los campos");
        }
    }
    
}
