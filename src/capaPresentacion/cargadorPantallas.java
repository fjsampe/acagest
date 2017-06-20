package capaPresentacion;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase cargador de pantallas
 * Esta clase se encarga de gestionar la apertura de formularios
 * 
 * Métodos y funciones
 *  cargarPantalla  : Método que carga la pantalla
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class cargadorPantallas {
    
    /***
     * Método que carga la pantalla
     * @param vistaPath         Path de la vista        
     * @param nombreFormulario  Nombre del formulario
     * @param stage             Stage
     * @param iconoPath         Path donde se encuentra el icono del formulario
     * @param newStage          Nueva stage
     */
    public static void cargarPantalla(String vistaPath, String nombreFormulario, Stage stage, String iconoPath, boolean newStage){
        try {
            Parent vista=FXMLLoader.load(cargadorPantallas.class.getResource(vistaPath));
            Scene scene = new Scene(vista);
            if (newStage){
                Stage stage1=new Stage();
                stage1.initModality(Modality.APPLICATION_MODAL);
                stage1.getIcons().add(new Image(iconoPath));
                stage1.setTitle(nombreFormulario);
                stage1.setScene(scene);
                stage1.show();
            }else{
                stage.hide();
                stage.getIcons().add(new Image(iconoPath));
                stage.setTitle(nombreFormulario);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException ex) {
            System.err.println("ERROR Carga Formularios | Error en cargadorPantallas.java "+ex.getMessage());
        }     
    }
}


