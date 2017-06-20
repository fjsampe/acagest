package capaPresentacion;

import capaNegocio.Alumno;
import capaNegocio.OperativasBD;
import capaPresentacion.resources.Campos;
import static capaPresentacion.resources.Campos.convertirImagen;
import static capaPresentacion.resources.Campos.fijarTamanoMaximoConPatron;
import capaPresentacion.resources.HeaderFooterPDF;
import capaPresentacion.resources.Mensajes;
import capaPresentacion.resources.MostrarPDF;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * FXML Controller class
 * Formulario para la gestión de alumnos
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLAlumnosController implements Initializable {
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private ListView<Alumno> alumnosListView;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private DatePicker fechaDatePicker;
    @FXML private TextField telefonoTxt;
    @FXML private TextField centroTxt;
    @FXML private TextField movilTxt;
    @FXML private TextField padreTxt;
    @FXML private TextField madreTxt;
    @FXML private TextField dniPadreTxt;
    @FXML private TextField dniMadreTxt;
    @FXML private TextField telefonoPadreTxt;
    @FXML private TextField telefonoMadreTxt;
    @FXML private TextField emailPadreTxt;
    @FXML private TextField emailMadreTxt;
    @FXML private TextArea observacionesTxt;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private ImageView fotoImg;
    @FXML private VBox formularioDatos;
    @FXML private HBox barraOpciones;
    @FXML private TextField filtroTxt;
    @FXML private Button fotoBtn;
    @FXML private Button printBtn;
    private ObservableList<Alumno> alumnosObsList;
    private boolean MODOEDICION=false;
    private byte[] imagen;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        mostrarListaAlumnos();
        
        // Listener Filtro
        FilteredList<Alumno> listaFiltrada = new FilteredList<>(alumnosObsList, s -> true);
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
        
        
        
        
        // Listener Selección Alumno
        alumnosListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                niaTxt.setText(newSelection.getNia());
                nombreTxt.setText(newSelection.getNombre());
                apellidosTxt.setText(newSelection.getApellidos());
                domicilioTxt.setText(newSelection.getDomicilio());
                poblacionTxt.setText(newSelection.getPoblacion());
                cpTxt.setText(newSelection.getCp());
                provinciaTxt.setText(newSelection.getProvincia());
                telefonoTxt.setText(newSelection.getTelefono());
                centroTxt.setText(newSelection.getCentro());
                movilTxt.setText(newSelection.getMovil());
                padreTxt.setText(newSelection.getPadre());
                madreTxt.setText(newSelection.getMadre());
                dniPadreTxt.setText(newSelection.getDniPadre());
                dniMadreTxt.setText(newSelection.getDniMadre());
                telefonoPadreTxt.setText(newSelection.getTelefonoPadre());
                telefonoMadreTxt.setText(newSelection.getTelefonoMadre());
                emailPadreTxt.setText(newSelection.getEmailPadre());
                emailMadreTxt.setText(newSelection.getEmailMadre());
                observacionesTxt.setText(newSelection.getObservaciones());
                imagen=newSelection.getFoto();
                fotoImg.setImage(convertirImagen(imagen));
                fotoImg.setFitHeight(150);
                fotoImg.setFitWidth(150);
                fotoImg.setPreserveRatio(false);
                fotoImg.setSmooth(true);
                fechaDatePicker.setValue(newSelection.getNacimiento());
                deshabilitarBotonesEdicion(false);
            }
        });    
        
        visualizaLista(alumnosObsList);
        fijarTamanoMaximoConPatron(niaTxt,8,"[0-9]");
        fijarTamanoMaximoConPatron(nombreTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(apellidosTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(domicilioTxt,60,"");
        fijarTamanoMaximoConPatron(poblacionTxt,45,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(cpTxt,5,"[0-9]");
        fijarTamanoMaximoConPatron(provinciaTxt,40,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(telefonoTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(movilTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(padreTxt,50,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ, ]");
        fijarTamanoMaximoConPatron(dniPadreTxt,9,"[0-9A-Z]");
        fijarTamanoMaximoConPatron(telefonoPadreTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(emailPadreTxt,50,"[0-9A-Za-zçÇ@\\-\\_\\.]");
        fijarTamanoMaximoConPatron(madreTxt,50,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ, ]");
        fijarTamanoMaximoConPatron(dniMadreTxt,9,"[0-9A-Z]");
        fijarTamanoMaximoConPatron(telefonoMadreTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(emailMadreTxt,50,"[0-9A-Za-zçÇ@\\-\\_\\.]");
        fijarTamanoMaximoConPatron(centroTxt,50,"");
        Campos.validaCampoFecha(fechaDatePicker);   
    }    

    /**
     * Acción al ser pulsado botón para AÑADIR
     * @param event Evento
     */
    @FXML
    private void clickAddBtn(ActionEvent event) {
        MODOEDICION=false;
        habilitarCamposEdicion(true);
        inicializarCampos();
    }

    /**
     * Acción al ser pulsado botón para EDITAR
     * @param event Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        MODOEDICION=true;
        habilitarCamposEdicion(true);
    }

    /**
     * Acción al ser pulsado el botón para BORRAR
     * @param event Evento
     */
    @FXML
    private void clickDeleteBtn(ActionEvent event) {
        if (alumnosListView.getSelectionModel().getSelectedIndex()>-1){
            Alumno alumno=alumnosListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta(alumno.getNombre()+" "+alumno.getApellidos()+" será borrado.")) {
                if (OperativasBD.borrarAlumno(alumno)){
                    Mensajes.msgInfo("ACCION:", "El borrado del Alumno ha sido realizado");
                    alumnosObsList.remove(alumno);
                    if (alumnosListView.getSelectionModel().isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarCampos();
                    }else{
                        alumnosListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado del Alumno ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO ALUMNOS", "No ha seleccionado ningún alumno");
        }
    }
    
    /**
     * Acción al ser pulsado el botón para IMPRIMIR
     * @param event Evento 
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
            pdf.GenerarHojaAlumno(alumnosListView.getSelectionModel().getSelectedItem());
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
     * Acción al ser pulsado el botón de ACEPTAR
     * @param event Evento
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int indice,posicionActual,ultimoId;
        ultimoId=0;
        if (!nombreTxt.getText().equals("") && !apellidosTxt.getText().equals("")){
            Alumno alumno = new Alumno(niaTxt.getText(),nombreTxt.getText(), 
                apellidosTxt.getText(), domicilioTxt.getText(), poblacionTxt.getText(),
                cpTxt.getText(), provinciaTxt.getText(), fechaDatePicker.getValue(),
                telefonoTxt.getText(), movilTxt.getText(), padreTxt.getText(), 
                dniPadreTxt.getText(), telefonoPadreTxt.getText(), emailPadreTxt.getText(), 
                madreTxt.getText(), dniMadreTxt.getText(), telefonoMadreTxt.getText(), 
                emailMadreTxt.getText(), centroTxt.getText(), observacionesTxt.getText(), 
                imagen); 
            if (!MODOEDICION){
                ultimoId=OperativasBD.insertarAlumno(alumno);
                if(ultimoId>0){
                    alumno.setCodigo(ultimoId);
                    alumnosObsList.add(alumno);
                    ordenarListaAlumnos();
                    Mensajes.msgInfo("INSERCION ALUMNOS", "Alumno añadido correctamente");
                    visualizaLista(alumnosObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Alumno");
                }
            }else{
                alumno.setCodigo(alumnosListView.getSelectionModel().getSelectedItem().getCodigo());
                if(OperativasBD.modificarAlumno(alumno)){
                    posicionActual=alumnosListView.getSelectionModel().getSelectedIndex();
                    if (!filtroTxt.getText().isEmpty()){
                        indice=alumnosObsList.indexOf(alumnosListView.getSelectionModel().getSelectedItem());
                    }else{
                        indice=posicionActual;
                    }
                    if (alumnosListView.getSelectionModel().getSelectedItem().getApellidos().equals(apellidosTxt.getText())
                        && alumnosListView.getSelectionModel().getSelectedItem().getNombre().equals(nombreTxt.getText())){
                        alumnosObsList.set(indice, alumno);
                        alumnosListView.getSelectionModel().clearSelection();
                        alumnosListView.getSelectionModel().clearAndSelect(posicionActual);
                    }else{
                        alumnosObsList.set(indice, alumno);
                        ordenarListaAlumnos();
                        filtroTxt.setText("");
                    }
                    Mensajes.msgInfo("MODIFICAR ALUMNOS", "Alumno modificado correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Alumno");
                }
            }
            habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Nombre o apellidos del alumno no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón de CANCELAR
     * @param event Evento 
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=alumnosListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        alumnosListView.getSelectionModel().clearSelection(registroActual); 
        alumnosListView.getSelectionModel().select(registroActual);
    }

    /**
     * Acción al ser pulsado el botón para añadir FOTO
     * @param event Evento 
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
     * Método que carga la lista de los alumnos en la lista observable
     */
    private void mostrarListaAlumnos() {
        List<Alumno> listaAlumnos=OperativasBD.extraerAlumnos();
        alumnosObsList = FXCollections.observableArrayList(listaAlumnos);
        ordenarListaAlumnos();
    }
    
    /**
     * Método que ordena la lista de alumnos
     */
    private void ordenarListaAlumnos(){
        alumnosObsList.sort((alumno1,alumno2)->alumno1.toString().toLowerCase()
                .compareTo(alumno2.toString().toLowerCase()));
        alumnosListView.setItems(alumnosObsList);
    }  
    
    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        niaTxt.setText("");
        nombreTxt.setText("");
        apellidosTxt.setText("");
        domicilioTxt.setText("");
        poblacionTxt.setText("");
        cpTxt.setText("");
        provinciaTxt.setText("");
        fechaDatePicker.setValue(null);
        telefonoTxt.setText("");
        centroTxt.setText("");
        movilTxt.setText("");
        padreTxt.setText("");
        madreTxt.setText("");
        dniPadreTxt.setText("");
        dniMadreTxt.setText("");
        telefonoPadreTxt.setText("");
        telefonoMadreTxt.setText("");
        emailPadreTxt.setText("");
        emailMadreTxt.setText("");
        observacionesTxt.setText("");
        fotoImg.setImage(null);
        imagen=null;
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        barraOpciones.setDisable(b);
        alumnosListView.setDisable(b);
        filtroTxt.setDisable(b);
        niaTxt.setPromptText(b?"NIA":"");
        nombreTxt.setPromptText(b?"Nombre":"");
        apellidosTxt.setPromptText(b?"Apellidos":"");
        domicilioTxt.setPromptText(b?"Domicilio":"");
        poblacionTxt.setPromptText(b?"Población":"");
        cpTxt.setPromptText(b?"CP":"");
        provinciaTxt.setPromptText(b?"Província":"");
        fechaDatePicker.setPromptText(b?"Fecha Nacimiento":"");
        telefonoTxt.setPromptText(b?"Teléfono Fijo":"");
        centroTxt.setPromptText(b?"Centro de Estudios":"");
        movilTxt.setPromptText(b?"Teléfono Móvil":"");
        padreTxt.setPromptText(b?"Nombre del Padre":"");
        madreTxt.setPromptText(b?"Nombre de la Madre":"");
        dniPadreTxt.setPromptText(b?"DNI Padre":"");
        dniMadreTxt.setPromptText(b?"DNI Madre":"");
        telefonoPadreTxt.setPromptText(b?"Teléfono del Padre":"");
        telefonoMadreTxt.setPromptText(b?"Teléfono de la Madre":"");
        emailPadreTxt.setPromptText(b?"Correo del Padre":"");
        emailMadreTxt.setPromptText(b?"Correo de la Madre":"");
        observacionesTxt.setPromptText(b?"Observaciones":"");
    }
   
    /**
     * Método que visualiza la Lista de Alumnos
     * @param lista     Lista de Alumnos
     */
    private void visualizaLista(ObservableList<Alumno> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        alumnosListView.setItems(lista);
        alumnosListView.getSelectionModel().select(0);
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