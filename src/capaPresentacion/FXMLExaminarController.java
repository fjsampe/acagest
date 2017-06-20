package capaPresentacion;

import capaNegocio.Asignatura;
import capaNegocio.Examen;
import capaNegocio.FichaExamen;
import capaNegocio.OperativasBD;
import capaPresentacion.resources.Campos;
import static capaPresentacion.resources.Campos.convertirImagen;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * Formulario para la gestión de examenes
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLExaminarController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaExamen> alumnosListView;
    @FXML private VBox formularioDatos;
    @FXML private HBox formularioDatosAlumno;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private ImageView fotoImg;
    @FXML private HBox formularioDatosExamenes;
    @FXML private ComboBox<Asignatura> asignaturaCmb;
    @FXML private TableView<Examen> examenesTable;
    @FXML private HBox barraConfirmacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button editBtn;
    @FXML private Button printBtn;
    @FXML private DatePicker fechaPicker;
    @FXML private TextField notaTxt;
    @FXML private TableColumn<Asignatura,String> examenAsignaturaCol;
    @FXML private TableColumn<Asignatura,String> examenDescripcionAsignaturaCol;
    @FXML private TableColumn<Examen, LocalDate> examenFechaCol;
    @FXML private TableColumn<Examen, Float> examenNotaCol;
    @FXML private Button addExamenBtn;
    @FXML private Button delExamenBtn;
    
    // Lista observable de asignaturas para el ComboBOX
    private ObservableList<Asignatura> asignaturasObsList;
    // Lista Observable de Alumnos para el ListView
    private ObservableList<FichaExamen> alumnosObsList;
    // Lista Observable de Examenes de un alumno
    private ObservableList<Examen> examenesObsList= FXCollections.observableArrayList();
    
    private byte[] imagen;
    private FichaExamen datosAlumnoSeleccionado;
    private List<Examen> examenesInicial;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        //cargarListaAsignaturas();
        cargarListaAlumnos();
        inicializarTablaExamenes();
        
        // Listener Filtro
        FilteredList<FichaExamen> listaFiltrada = new FilteredList<>(alumnosObsList, s -> true);
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
                datosAlumnoSeleccionado=newSelection;
                niaTxt.setText(datosAlumnoSeleccionado.getAlumno().getNia());
                nombreTxt.setText(datosAlumnoSeleccionado.getAlumno().getNombre());
                apellidosTxt.setText(datosAlumnoSeleccionado.getAlumno().getApellidos());
                domicilioTxt.setText(datosAlumnoSeleccionado.getAlumno().getDomicilio());
                poblacionTxt.setText(datosAlumnoSeleccionado.getAlumno().getPoblacion());
                cpTxt.setText(datosAlumnoSeleccionado.getAlumno().getCp());
                provinciaTxt.setText(datosAlumnoSeleccionado.getAlumno().getProvincia());
                imagen=datosAlumnoSeleccionado.getAlumno().getFoto();
                fotoImg.setImage(convertirImagen(imagen));
                fotoImg.setFitHeight(150);
                fotoImg.setFitWidth(150);
                fotoImg.setPreserveRatio(false);
                fotoImg.setSmooth(true);
                cargarListaAsignaturas(datosAlumnoSeleccionado.getAlumno().getCodigo());
                examenesObsList=FXCollections.observableList(datosAlumnoSeleccionado.getListaExamenes());
                examenesInicial=new ArrayList<>();
                examenesInicial.addAll(examenesObsList); 
                examenesTable.setItems(examenesObsList);               
            }
        }); 
        alumnosListView.getSelectionModel().selectFirst();
        fijarTamanoMaximoConPatron(notaTxt,5,"[0-9\\.]");
        Campos.validaCampoFecha(fechaPicker);  
    }    


    /**
     * Acción al ser pulsado botón para ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int posicionActual,indice;
        posicionActual=alumnosListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=alumnosObsList.indexOf(alumnosListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosAlumnoSeleccionado.setListaExamenes(examenesObsList);
        alumnosObsList.set(indice, datosAlumnoSeleccionado);
        examenesInicial.clear();
        examenesInicial.addAll(examenesObsList);
        if (OperativasBD.borrarExamenes(datosAlumnoSeleccionado.getAlumno().getCodigo()) && 
            OperativasBD.insertarExamenes(datosAlumnoSeleccionado)){
                Mensajes.msgInfo("MODIFICACION EXAMENES", "Los exámenes se han grabado correctamente");
        }else{
                Mensajes.msgError("MODIFICACION EXAMENES", "Los exámenes no se han grabado correctamente");
        }
        habilitarCamposEdicion(false);
        limpiarCombos();
        visualizaLista(alumnosObsList);
        alumnosListView.getSelectionModel().select(indice);
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=alumnosListView.getSelectionModel().getSelectedIndex();
        int posicionActual,indice;
        posicionActual=alumnosListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=alumnosObsList.indexOf(alumnosListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosAlumnoSeleccionado.setListaExamenes(examenesInicial);
        examenesObsList=FXCollections.observableList(examenesInicial);
        alumnosObsList.set(indice, datosAlumnoSeleccionado);        
        habilitarCamposEdicion(false);
        alumnosListView.getSelectionModel().clearSelection(registroActual);
        alumnosListView.getSelectionModel().select(registroActual);
        limpiarCombos();
    }

    /**
     * Acción al ser pulsado el botón EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        cargarListaAsignaturas(datosAlumnoSeleccionado.getAlumno().getCodigo());
        habilitarCamposEdicion(true);
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
            pdf.GenerarHojaExamenes(alumnosListView.getSelectionModel().getSelectedItem());
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
     * Acción al ser pulsado el botón AÑADIR
     * @param event  Evento
     */
    @FXML
    private void clickAddExamenBtn(ActionEvent event) {
        if (asignaturaCmb.getSelectionModel().isEmpty() ||
                fechaPicker.getValue()==null ||
                notaTxt.getText().equals("")){
            Mensajes.msgInfo("VALIDACION EXAMEN", "Los campos Asignatura, "
                    + "Fecha y Nota deben estar rellenos");
        }else{
            if (Float.parseFloat(notaTxt.getText())<=12.0){
                Examen e=new Examen(asignaturaCmb.getSelectionModel().getSelectedItem(),
                    fechaPicker.getValue(),Float.parseFloat(notaTxt.getText()));
                if (examenesObsList.contains(e)){
                    Mensajes.msgInfo("EXAMENES", "Ya ha realizado ese examen en esa fecha");
                }else{
                    examenesObsList.add(e);
                    examenesTable.getSelectionModel().select(e);
                    examenesTable.scrollTo(e);
                    limpiarCombos();
                }
                
            }else{
                Mensajes.msgInfo("VALIDACIÓN EXAMEN", "La nota debe ser un datos entre 0.00 y 12.00");
            }
        }
    }
    
    /**
     * Acción al ser pulsado el botón BORRAR
     * @param event  Evento
     */
    @FXML
    private void clickDelExamenBtn(ActionEvent event) {
        Examen e=examenesTable.getSelectionModel().getSelectedItem();
        examenesObsList.remove(e);
        examenesTable.setItems(examenesObsList);
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        alumnosListView.setDisable(b);
        barraOpciones.setDisable(b);
        formularioDatosAlumno.setDisable(true);
        formularioDatosExamenes.setDisable(!b);
        barraConfirmacion.setDisable(!b);
        filtroTxt.setDisable(b);
    }
    
    /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/edit.png","resources/icons/print.png",
            "resources/icons/addlittle.png","resources/icons/deletelittle.png"            
            };
        ImageView[] iv=new ImageView[icons.length];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        editBtn.setText("Editar");
        editBtn.setGraphic(iv[0]);
        editBtn.setContentDisplay(ContentDisplay.TOP);
        printBtn.setText("Imprimir");
        printBtn.setGraphic(iv[1]);
        printBtn.setContentDisplay(ContentDisplay.TOP);
        addExamenBtn.setGraphic(iv[2]);
        addExamenBtn.setContentDisplay(ContentDisplay.TOP);
        delExamenBtn.setGraphic(iv[3]);
        delExamenBtn.setContentDisplay(ContentDisplay.TOP); 
    }
    
    /**
     * Método que cargar la lista de asignaturas en una lista
     */
    private void cargarListaAsignaturas(){
        asignaturasObsList=FXCollections.observableArrayList(OperativasBD.extraerAsignaturas(null));
        asignaturaCmb.setItems(asignaturasObsList);
        ordenarListaAsignaturas();
    }
    
     /**
     * Ordena la lista de asignaturas
     */
    private void ordenarListaAsignaturas(){
        asignaturasObsList.sort((asignatura1,asignatura2)->asignatura1.toString().compareTo(asignatura2.toString()));
        asignaturaCmb.setItems(asignaturasObsList);
        //asignaturaCmb.getSelectionModel().select(null);
    }
    
     /**
     * Método que cargar la lista de alumnos en una lista observable
     */
    private void cargarListaAlumnos(){
        List<FichaExamen> listaAlumnos=OperativasBD.extraerFichasExamenes();
        alumnosObsList = FXCollections.observableArrayList(listaAlumnos);
        ordenarListaAlumnos();
    }
    
    /**
     * Método que ordena la lista de alumnos
     */
    private void ordenarListaAlumnos(){
        alumnosObsList.sort((alumno1,alumno2)->alumno1.toString().compareTo(alumno2.toString()));
        alumnosListView.setItems(alumnosObsList);
    }
    
    /**
     * Método que deshabilita los botones de Editar, Borrar e Imprimir
     * @param b     Booleano True=deshabilitar False=Habilitar
     */
    private void deshabilitarBotonesEdicion(boolean b) {
        editBtn.setDisable(b);
        printBtn.setDisable(b);
    }
    
    /**
     * Método que visualiza los alumnos en pantalla. Carga el ListView
     * @param lista     Lista de los alumnos
     */
    private void visualizaLista(ObservableList<FichaExamen> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
        }
        alumnosListView.setItems(lista);
        alumnosListView.getSelectionModel().selectFirst();
    }
    
    /**
     * Método que inicializa la tabla de examenes del alumno
     */
    private void inicializarTablaExamenes() {
        examenesTable.setPlaceholder(new Label ("Alumno sin exámenes...."));
        examenAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("CodigoAsignatura"));
        examenDescripcionAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("DescripcionAsignatura"));
        examenFechaCol.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        examenNotaCol.setCellValueFactory(new PropertyValueFactory<>("Nota"));
        examenFechaCol.setStyle( "-fx-alignment: CENTER;");
        examenFechaCol.setCellFactory(column -> {
            return new TableCell<Examen, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(item));
                }
            }
            };
        });
        examenNotaCol.setStyle( "-fx-alignment: CENTER;");
        examenNotaCol.setCellFactory(column -> { 
            return new TableCell<Examen, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item)); // product's price format
                    }
                }
            };
        });
        
        
    }
    
    /**
     * Método que cargar la lista de asignaturas en una lista
     * @param codigoAlumno  Código del Alumno 
     */
    private void cargarListaAsignaturas(int codigoAlumno){
        asignaturasObsList=FXCollections.observableArrayList(OperativasBD.
                extraerAsignaturasMatriculadasPorAlumno(codigoAlumno));
        asignaturaCmb.setItems(asignaturasObsList);
        ordenarListaAsignaturas();
    }

    /**
     * Método que limpia los combos y los deja sin nada selecciondo
     */
    private void limpiarCombos(){
        asignaturaCmb.setValue(null);
        fechaPicker.setValue(null);
        notaTxt.setText("");  
    }
}
