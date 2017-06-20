package capaPresentacion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 * Formulario Acerca de
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLAcercaDeController implements Initializable {

    @FXML private Label notaTxt;
    @FXML private Label imageLbl;
    @FXML private Label textoImagenesLbl;
    @FXML private Label linea1Lbl2;
    @FXML private Label linea1Lbl1;
    @FXML private Label linea2Lbl1;
    @FXML private Label linea2Lbl2;
    @FXML private Label linea1Lbl3;
    @FXML private Label linea2Lbl3;
    @FXML private Label linea3Lbl1;
    @FXML private Label linea3Lbl2;
    @FXML private Label linea3Lbl3;
    @FXML private Label linea4Lbl1;
    @FXML private Label linea4Lbl2;
    @FXML private Label linea4Lbl3;
    @FXML private Label linea5Lbl1;
    @FXML private Label linea5Lbl2;
    @FXML private Label linea5Lbl3;
    @FXML private Label linea6Lbl1;
    @FXML private Label linea6Lbl2;
    @FXML private Label linea6Lbl3;
    @FXML private Label linea7Lbl1;
    @FXML private Label linea7Lbl2;
    @FXML private Label linea7Lbl3;
    @FXML private Label linea8Lbl1;
    @FXML private Label linea8Lbl2;
    @FXML private Label linea8Lbl3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notaTxt.setWrapText(true);
        notaTxt.setText("Acagest es una aplicación que nos permite la gestión de "+
        "centros de estudios y/o académias. Esta aplicación nos permitirá controlar alumnos, profesores, "+
        "aulas, asignaturas, horarios, matriculaciones, exámenes, recibos, facturas, etc.\n"+
        "Acagest está distribuida bajo licencia Creative Commons.\n"+
        "Usted es libre para:\n" +
        "Compartir— copiar y redistribuir el material en cualquier medio o formato\n" +
        "Adaptar— remezclar, transformar y crear a partir del material con fines no comerciales.\n" +
        "El licenciador no puede revocar estas libertades mientras cumpla con los términos de la licencia.\n" +
        "Bajo las condiciones siguientes:\n" +
        "Reconocimiento — Debe reconocer adecuadamente la autoría, proporcionar un enlace a la licencia e indicar si se han realizado cambios. Puede hacerlo de cualquier manera razonable, pero no de una manera que sugiera que tiene el apoyo del licenciador o lo recibe por el uso que hace.\n" +
        "CompartirIgual— Si remezcla, transforma o crea a partir del material, deberá difundir sus contribuciones bajo la misma licencia que el original.\n" +
        "No hay restricciones adicionales — No puede aplicar términos legales o medidas tecnológicas que legalmente restrinjan realizar aquello que la licencia permite..\n" +
        "Desarrollado por: Francisco José Sampedro Luján  fcosampedro.com  \n");
        textoImagenesLbl.setWrapText(true);
        textoImagenesLbl.setText("Los icónos utilizados en la aplicación están descargados de la web iconfinder.com y distribuidos bajo las siguientes licencias.\n");
        linea1Lbl1.setText("Alexandre Moore");
        linea1Lbl2.setText("Licencia LGPL");
        linea1Lbl3.setText("http://sa-ki.deviantart.com/");
        linea2Lbl1.setText("Oxigen Team");
        linea2Lbl2.setText("Creative Commons");
        linea2Lbl3.setText("http://www.oxygen-icons.org/");
        linea3Lbl1.setText("Marco Martin");
        linea3Lbl2.setText("Licencia LGPL");
        linea3Lbl3.setText("http://notmart.org/blog/");
        linea4Lbl1.setText("Pavel InFeRnODeMoN");
        linea4Lbl2.setText("Licencia GPL");
        linea4Lbl3.setText("-");
        linea5Lbl1.setText("Sergio Sánchez López");
        linea5Lbl2.setText("Licencia GPL");
        linea5Lbl3.setText("Sephiroth6779");
        linea6Lbl1.setText("Yellow Icon");
        linea6Lbl2.setText("Licencia GPL");
        linea6Lbl3.setText("http://www.yellowicon.com/");  
        linea7Lbl1.setText("Aha-Soft");
        linea7Lbl2.setText("Creative Commons");
        linea7Lbl3.setText("http://aha-soft.com/");  
        linea8Lbl1.setText("Design Contest");
        linea8Lbl2.setText("Creative Commons");
        linea8Lbl3.setText("https://designcontest.com/");  
        Image image = new Image(getClass().getResourceAsStream("resources/icons/creativecommons.png"));
        imageLbl.setGraphic(new ImageView(image));
    }    
}
