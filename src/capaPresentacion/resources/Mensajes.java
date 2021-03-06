package capaPresentacion.resources;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Clase Mensajes
 * Muestra mensajes de tipo error, informativo o preguntas
 *  
 * 
 * Métodos y funciones:
 *  msgError    : Muestra un mensaje de error
 *  msgInfo     : Muestra un mensaje de información
 *  msgPregunta : Muestra una mensaje con pregunta
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Mensajes {
    
    /**
     * Método que muestra una alerta con mensaje de error
     * @param message1  Mensaje línea 1
     * @param message2  Mensaje línea 2
     */
    public static void msgError(String message1, String message2) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message1);
        alert.setContentText(message2);
        alert.show();
    }
    
    /**
     * Método que muestra una alerta con mensaje de información
     * @param message1  Mensaje línea 1
     * @param message2  Mensaje línea 2
     */
    public static void msgInfo(String message1, String message2) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(message1);
        alert.setContentText(message2);
        alert.show();
    }
    
    /**
     * /**
     * Función que muestra una alerta con mensaje de pregunta
     * @param mensaje1  Título de la alerta
     * @param mensaje2  Cabecera de la alerta
     * @param mensaje3  Mensaje de la alerta
     * @return          Devuelve booleano (True=Aceptar, False=Cancelar)
     */
    public static boolean msgPregunta(String mensaje1, String mensaje2, String mensaje3) {
        boolean deleteItem = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(mensaje1);
        alert.setHeaderText(mensaje2);
        alert.setContentText(mensaje3);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteItem = true;
        }
        return deleteItem;
    }    
}
