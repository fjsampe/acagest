package capaPresentacion;

import static capaNegocio.OperativasBD.validarLogin;
import capaNegocio.Usuario;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * FXML Controller class
 * Pantalla Login de usuarios
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLLoginController implements Initializable {
    @FXML private Label cabecera;
    @FXML private Button loginBtn;
    @FXML private TextField usuarioTxt;
    @FXML private PasswordField passwordTxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Acción al ser pulsado el botón LOGIN
     * @param event  Evento
     */
    @FXML
    private void clickLoginBtn(ActionEvent event) {
        if (!usuarioTxt.getText().equals("") && !passwordTxt.getText().equals("")){
            if (validarLogin(new Usuario(usuarioTxt.getText(),passwordTxt.getText()))){
                try {
                    Parent root=FXMLLoader.load(getClass().getResource("resources/fxml/FXMLPrincipal.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage=(Stage)loginBtn.getScene().getWindow();
                    stage.setTitle("Formulario Principal");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.err.println("ERROR: Carga Formulario Principal");
                }
            }else{
                Mensajes.msgInfo("ACCESO", "Usuario/contraseña no correctos. Vuelva a intentarlo");
                passwordTxt.setText("");
            }
        }else{
            Mensajes.msgError("ERROR", "Usuario o contraseña no puede/n estar vacios");
        }
    }
}
