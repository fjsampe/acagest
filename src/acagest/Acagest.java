package acagest;


import static capaNegocio.OperativasBD.validarParametros;
import capaPresentacion.resources.Mensajes;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase Acagest
 * Esta clase arranca la aplicación de Gestión de Centros de estudio
 * 
 * Atributos:
 *  
 * Métodos:
 *  start   : Arranca aplicación.
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Acagest extends Application {
    
    /**
     * Método de aranque de aplicación
     * @param stage     Stage
     */
    @Override
    public void start(Stage stage) {
        try {
            Class.forName("org.postgresql.Driver");
            Parent root=FXMLLoader.load(getClass().getResource("/capaPresentacion/resources/fxml/FXMLConfig.fxml"));
            String nombreForm="Configuración";
            String icono="/capaPresentacion/resources/icons/configuracion.png";
            if (validarParametros()){
                root = FXMLLoader.load(getClass().getResource("/capaPresentacion/resources/fxml/FXMLLogin.fxml"));
                nombreForm="Login";
                icono="/capaPresentacion/resources/icons/candado.png";
            }
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/capaPresentacion/resources/css/loginStyleSheet.css").toExternalForm()); // CSS
            stage.getIcons().add(new Image(getClass().getResource(icono).toExternalForm()));
            stage.setTitle(nombreForm);
            stage.setScene(scene);
            stage.show();
        } catch (ClassNotFoundException e) {
            Mensajes.msgError("POSTGRESQL","PostgreSQL JDBC Driver no incluido en el proyecto");
        } catch (IOException ex) {
            Mensajes.msgError("FORMULARIO","Formulario no existente");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
