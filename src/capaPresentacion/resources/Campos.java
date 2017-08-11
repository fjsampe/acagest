package capaPresentacion.resources;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Clase Campos
 * Esta clase contiene las funciones para el uso de campos y conversiones
 * 
 * Atributos:
 * 
 * 
 * Métodos y funciones:
 *  fijarTamanoMaximo           : Fija el tamaño máximo de un campo
 *  fijarTamanoMaximoConPatron  : Fija el tamaño máximo de un campo y el patrón
 *  convertirImagen             : Convierte un array de bytes en una imagen
 *  fechaToString               : Convierte una fecha a cadena
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class Campos {
    
    /**
     * Método que fija el tamaño máximo de un campo de texto
     * @param campoTexto    Nombre del campo de texto
     * @param tamanoMaximo  Tamaño máximo del campo de texto
     */
    public static void fijarTamanoMaximo(TextField campoTexto, int tamanoMaximo) {
        campoTexto.lengthProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
            Number valorAnterior, Number valorActual) {
            if (valorActual.intValue() > valorAnterior.intValue()) {
                if (campoTexto.getText().length() >= tamanoMaximo) {
                        campoTexto.setText(campoTexto.getText().substring(0, tamanoMaximo));
                }
            }
        }
        });
   }
   
    /**
     * Método que fija el tamaño máximo y la máscara de un campo de texto
     * @param campoTexto    Nombre del campo de texto
     * @param tamanoMaximo  Tamaño máximo del campo de texto
     * @param patron        Patrón de la máscara
     */
    public static void fijarTamanoMaximoConPatron(TextField campoTexto, int tamanoMaximo, String patron) {
            campoTexto.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                Number valorAnterior, Number valorActual) {
                if (valorActual.intValue() > valorAnterior.intValue()) {
                    if (patron.length()>0){
                        Pattern permitido = Pattern.compile(patron);
                        Matcher mpermitido = permitido.matcher(campoTexto.getText().substring(valorAnterior.intValue(), valorActual.intValue()));
                        if (!mpermitido.find()) {
                            campoTexto.setText(campoTexto.getText().substring(0, valorAnterior.intValue()));
                        }
                    }
                    if (campoTexto.getText().length() >= tamanoMaximo) {
                            campoTexto.setText(campoTexto.getText().substring(0, tamanoMaximo));
                    }
                }
            }
            });
    }
    
    /***
     * Función que convierte un array de byte en una imagen
     * @param array     Array recibido
     * @return          Devuelve imagen
     */
    public static Image convertirImagen(byte[] array) {
        Image image=null;
        if (array!=null){
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(array);
                Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
                ImageReader reader = (ImageReader) readers.next();
                Object source = bis;
                ImageInputStream iis = ImageIO.createImageInputStream(source);
                reader.setInput(iis, true);
                ImageReadParam param = reader.getDefaultReadParam();
                BufferedImage img=reader.read(0, param);
                image = SwingFXUtils.toFXImage(img, null);
            } catch (IOException ex) {
                System.err.println("ERROR: Capa Presentacion | FXMLDatosEmpresaController "
                + "| byteArrayToImage "+ex.getMessage());
            }
        }else{
            image=new Image(Campos.class.getResource("icons/image.jpg").toExternalForm());
        }
        return image;
    }
    
    public static void validaCampoFecha(DatePicker campoFecha) {
        campoFecha.focusedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
            if(!arg2){
                if (campoFecha.getEditor().getText().equals("")){
                        campoFecha.setValue(null);
                }else{
                    try{
                        campoFecha.setValue(LocalDate.parse(campoFecha.getEditor().getText(),DateTimeFormatter.ofPattern("d/MM/yyyy")));
                        campoFecha.getEditor().setText(fechaToString(campoFecha.getValue()));
                    }catch(DateTimeParseException e){
                        Mensajes.msgError("ERROR CAMPO", "El dato introducido no es una fecha válida");
                        campoFecha.setValue(null);
                        campoFecha.getEditor().setText("");
                    }
                } 
            }
        }
        });
    }
    
    /**
     * Función que convierte una fecha en una cadena de texto
     * @param date  Fecha
     * @return      Devuelve la fecha en formato texto ("dd/MM/yyyy")
     */
    public static String fechaToString(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }
    
    public static LocalDate stringToFecha(String fecha){
        return (LocalDate.parse(fecha));
    }
    
    public static boolean validarCampoNumerico(String numero){
        double num;
        boolean validacion=true;
        try{
            num=Double.parseDouble(numero);
        }catch(NumberFormatException ex){
            Mensajes.msgError("DATO NUMÉRICO", "Error en el dato introducido. Revise por favor..");
            validacion=false;
        }
        return validacion;
    }
}
