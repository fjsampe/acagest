package capaPresentacion;

import capaNegocio.OperativasBD;
import capaNegocio.Profesor;
import capaPresentacion.resources.Campos;
import static capaPresentacion.resources.Campos.convertirImagen;
import static capaPresentacion.resources.Campos.fechaToString;
import static capaPresentacion.resources.Campos.fijarTamanoMaximoConPatron;
import capaPresentacion.resources.HeaderFooterPDF;
import capaPresentacion.resources.Mensajes;
import capaPresentacion.resources.MostrarPDF;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 * Formulario para la gestión de profesores
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLProfesoresController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private VBox formularioDatos;
    @FXML private TextField nifTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private TextField emailTxt;
    @FXML private ImageView fotoImg;
    @FXML private DatePicker fechaDatePicker;
    @FXML private TextField telefonoTxt;
    @FXML private TextField cuentaTxt;
    @FXML private TextField movilTxt;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button printBtn;    
    @FXML private Button fotoBtn;
    @FXML private ListView<Profesor> profesoresListView;
    private ObservableList<Profesor> profesoresObsList;
    private boolean MODOEDICION=false;
    private byte[] imagen;

    
    
    /**
     * Inicializes the controler class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        mostrarListaProfesores();
        
        // Listener Filtro
        FilteredList<Profesor> listaFiltrada = new FilteredList<>(profesoresObsList, s -> true);
        filtroTxt.textProperty().addListener(obs->{
            if(filtroTxt.getText().length() == 0) {
                listaFiltrada.setPredicate(s -> true);
            }
            else {
                listaFiltrada.setPredicate(s -> s.toString().toLowerCase()
                        .contains(filtroTxt.getText().toLowerCase()));
            }
            visualizaLista(listaFiltrada);
        });
        
        // Listener Seleccion Profesor
        profesoresListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                nifTxt.setText(newSelection.getCifDni());
                nombreTxt.setText(newSelection.getNombre());
                apellidosTxt.setText(newSelection.getApellidos());
                domicilioTxt.setText(newSelection.getDomicilio());
                poblacionTxt.setText(newSelection.getPoblacion());
                cpTxt.setText(newSelection.getCp());
                provinciaTxt.setText(newSelection.getProvincia());
                emailTxt.setText(newSelection.getEmail());
                telefonoTxt.setText(newSelection.getTelefono());
                cuentaTxt.setText(newSelection.getCtaBanco());
                movilTxt.setText(newSelection.getMovil());
                imagen=newSelection.getFoto();
                fotoImg.setImage(convertirImagen(imagen));
                fotoImg.setFitHeight(150);
                fotoImg.setFitWidth(150);
                fotoImg.setPreserveRatio(false);
                fotoImg.setSmooth(true);
                fechaDatePicker.setValue(newSelection.getFechaNacimiento());
                deshabilitarBotonesEdicion(false);
            }
        });        
        visualizaLista(profesoresObsList);
        fijarTamanoMaximoConPatron(nifTxt,9,"[0-9A-Z]");
        fijarTamanoMaximoConPatron(nombreTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(apellidosTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(domicilioTxt,60,"");
        fijarTamanoMaximoConPatron(poblacionTxt,45,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(cpTxt,5,"[0-9]");
        fijarTamanoMaximoConPatron(provinciaTxt,40,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(emailTxt,50,"[0-9A-Za-zçÇ@\\-\\_\\.]");
        fijarTamanoMaximoConPatron(telefonoTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(movilTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(cuentaTxt,24,"");
        Campos.validaCampoFecha(fechaDatePicker);  
    }    

    /**
     * Acción al ser pulsado el botón AÑADIR
     * @param event  Evento
     */
    @FXML
    private void clickAddBtn(ActionEvent event) {
        MODOEDICION=false;
        habilitarCamposEdicion(true);
        inicializarCampos();
    }

    /**
     * Acción al ser pulsado el botón EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        MODOEDICION=true;
        habilitarCamposEdicion(true);
    }

    /**
     * Acción al ser pulsado el botón BORRAR
     * @param event  Evento
     */
    @FXML
    private void clickDeleteBtn(ActionEvent event) {
        if (profesoresListView.getSelectionModel().getSelectedIndex()>-1){
            Profesor profesor=profesoresListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta("Borrado elemento",profesor.getNombre()+" "+profesor.getApellidos()+" será borrado.","¿Quieres borrar el elemento?")) {
                if (OperativasBD.borrarProfesor(profesor)){
                    Mensajes.msgInfo("ACCION:", "El borrado del Profesor ha sido realizado");
                    profesoresObsList.remove(profesor);
                    if (profesoresListView.getSelectionModel().isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarCampos();
                    }else{
                        profesoresListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado del Profesor ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO PROFESORES", "No ha seleccionado ningún profesor");
        }
    }
    
    /**
     * Acción al ser pulsado el botón IMPRIMIR
     * @param event  Evento
     */
    @FXML
    private void clickPrintBtn(ActionEvent event) {
        FileOutputStream ficheroPdf = null;
        try {
            // Se crea el documento tamaño A4 y margenes
            Document documento = new Document(PageSize.A4, 50, 50, 50, 50);
            // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
            ficheroPdf = new FileOutputStream("fichero.pdf");
            // Se asocia el documento al OutputStream y se indica que el espaciado entre
            // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
            PdfWriter writer=PdfWriter.getInstance(documento,ficheroPdf);
            writer.setInitialLeading(20);
            HeaderFooterPDF evento = new HeaderFooterPDF();
            writer.setPageEvent(evento);
            
            // Se abre el documento y lo genero.
            documento.open();
            MostrarPDF pdf=new MostrarPDF(documento);
            pdf.GenerarHojaProfesor(profesoresListView.getSelectionModel().getSelectedItem());
            documento=pdf.getDocumento();
            // Se cierra documento
            documento.close();
        } catch (FileNotFoundException | DocumentException ex) {
            Mensajes.msgError("GENERACION PDF", "Documento abierto o error en la generación");
        } finally {
            try {
                if (ficheroPdf!=null){
                    ficheroPdf.close();
                    File path = new File ("fichero.pdf");
                    Desktop.getDesktop().open(path);
                }
            } catch (IOException ex) {
                Mensajes.msgError("GENERACION PDF", "Documento no puede ser cerrado");
            }
        }
    }

    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int indice, posicionActual;
        try{
            if (!nifTxt.getText().equals("") && !nombreTxt.getText().equals("")){
                Profesor profesor = new Profesor(nifTxt.getText(), nombreTxt.getText(), 
                        apellidosTxt.getText(), domicilioTxt.getText(), poblacionTxt.getText(),
                        cpTxt.getText(), provinciaTxt.getText(), fechaDatePicker.getValue(),
                        telefonoTxt.getText(), movilTxt.getText(), cuentaTxt.getText(), 
                        emailTxt.getText(), imagen); 
                if (!MODOEDICION){
                    if(OperativasBD.insertarProfesor(profesor)){
                        profesoresObsList.add(profesor);
                        ordenarListaProfesores();
                        Mensajes.msgInfo("INSERCION PROFESORES", "Profesor añadido correctamente");
                        visualizaLista(profesoresObsList);
                    }else{
                        Mensajes.msgError("ERROR SQL", "Error al insertar Profesor");
                    }
                }else{
                    if(OperativasBD.modificarProfesor(profesor)){
                        posicionActual=profesoresListView.getSelectionModel().getSelectedIndex();
                        if (!filtroTxt.getText().isEmpty()){
                            indice=profesoresObsList.indexOf(profesoresListView.getSelectionModel().getSelectedItem());
                        }else{
                            indice=posicionActual;
                        }
                        if (profesoresListView.getSelectionModel().getSelectedItem().getApellidos().equals(apellidosTxt.getText())
                                && profesoresListView.getSelectionModel().getSelectedItem().getNombre().equals(nombreTxt.getText())){
                            profesoresObsList.set(indice, profesor);
                            profesoresListView.getSelectionModel().clearSelection();
                            profesoresListView.getSelectionModel().clearAndSelect(posicionActual);
                        }else{
                            profesoresObsList.set(indice, profesor);
                            ordenarListaProfesores();
                            filtroTxt.setText("");
                        }
                        Mensajes.msgInfo("MODIFICAR PROFESORES", "Profesor modificado correctamente");
                    }else{
                        Mensajes.msgError("ERROR SQL", "Error al modificar Profesor");
                    }
                }
                  habilitarCamposEdicion(false);
            }else{
                Mensajes.msgError("ERROR", "Nif profesor o nombre no puede estar vacio");
            }
        }catch(DateTimeParseException ex){
            fechaDatePicker.getEditor().setText("");
            Mensajes.msgError("ERROR FORMATO", "La fecha debe tener el formato dd/mm/aaaa");
        }  
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=profesoresListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        profesoresListView.getSelectionModel().clearSelection(registroActual);
        profesoresListView.getSelectionModel().select(registroActual);
    }

    /**
     * Acción al ser pulsado el botón para añadir FOTO
     * @param event  Evento
     */
    @FXML
    private void clickFotoBtn(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "JPEG images", Arrays.asList("*.jpg")));
        File file=fileChooser.showOpenDialog(null);
        if (file!=null){
            Image img=new Image("file:"+file.getAbsolutePath(),150,150,false,true);
            fotoImg.setImage(img);
            try {
                imagen = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                System.err.println("ERROR: Capa Presentacion | FXMLDatosEmpresaController"
                        + " | "+ex.getMessage());
            }
        }
    }
    
    /**
     * Método que carga la lista de los profesores en la lista observable
     */
    private void mostrarListaProfesores() {
        List<Profesor> listaProfesores=OperativasBD.extraerProfesores();
        profesoresObsList = FXCollections.observableArrayList(listaProfesores);
        ordenarListaProfesores();
    }
    
    /**
     * Método que ordena la lista de profesores
     */
    private void ordenarListaProfesores(){
        profesoresObsList.sort((profesor1,profesor2)->profesor1.toString()
                .toLowerCase().compareTo(profesor2.toString().toLowerCase()));
        profesoresListView.setItems(profesoresObsList);
    }
    
    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        nifTxt.setText("");
        nombreTxt.setText("");
        apellidosTxt.setText("");
        domicilioTxt.setText("");
        poblacionTxt.setText("");
        cpTxt.setText("");
        provinciaTxt.setText("");
        fechaDatePicker.setValue(null);
        telefonoTxt.setText("");
        cuentaTxt.setText("");
        movilTxt.setText("");
        emailTxt.setText("");
        fotoImg.setImage(null);
        imagen=null;
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        nifTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        profesoresListView.setDisable(b);
        filtroTxt.setDisable(b);
        nifTxt.setPromptText(b?"Nif":"");
        nombreTxt.setPromptText(b?"Nombre":"");
        apellidosTxt.setPromptText(b?"Apellidos":"");
        domicilioTxt.setPromptText(b?"Domicilio":"");
        poblacionTxt.setPromptText(b?"Población":"");
        cpTxt.setPromptText(b?"CP":"");
        provinciaTxt.setPromptText(b?"Província":"");
        fechaDatePicker.setPromptText(b?"Fecha Nacimiento":"");
        telefonoTxt.setPromptText(b?"Teléfono Fijo":"");
        cuentaTxt.setPromptText(b?"Cuenta Bancaria":"");
        movilTxt.setPromptText(b?"Teléfono Móvil":"");
        emailTxt.setPromptText(b?"Email":"");
    }
   
    /**
     * Método que visualiza la Lista de Alumnos
     * @param lista     Lista de Alumnos
     */
    private void visualizaLista(ObservableList<Profesor> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        profesoresListView.setItems(lista);
        profesoresListView.getSelectionModel().select(0);
    }
    
    /**
     * Método que deshabilita los botones de Editar, Borrar e Imprimir
     * @param b     Booleano True=deshabilitar False=Habilitar
     */
    private void deshabilitarBotonesEdicion(boolean b) {
        editBtn.setDisable(b);
        deleteBtn.setDisable(b);
        printBtn.setDisable(b);
    }
    
    /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/add.png","resources/icons/edit.png",
            "resources/icons/delete.png","resources/icons/print.png"            
            };
        ImageView[] iv=new ImageView[4];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        addBtn.setText("Añadir");
        addBtn.setGraphic(iv[0]);
        addBtn.setContentDisplay(ContentDisplay.TOP);
        editBtn.setText("Editar");
        editBtn.setGraphic(iv[1]);
        editBtn.setContentDisplay(ContentDisplay.TOP);
        deleteBtn.setText("Borrar");
        deleteBtn.setGraphic(iv[2]);
        deleteBtn.setContentDisplay(ContentDisplay.TOP);
        printBtn.setText("Imprimir");
        printBtn.setGraphic(iv[3]);
        printBtn.setContentDisplay(ContentDisplay.TOP);
    }
}
