package capaPresentacion;

import capaNegocio.Asignatura;
import capaNegocio.Curso;
import capaNegocio.FichaMatricula;
import capaNegocio.Matricula;
import capaNegocio.OperativasBD;
import capaPresentacion.resources.Campos;
import static capaPresentacion.resources.Campos.convertirImagen;
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
import java.time.format.DateTimeParseException;
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
 * Formulario para la gestión de matriculaciones
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLMatriculacionesController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaMatricula> alumnosListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private ImageView fotoImg;
    @FXML private ComboBox<Curso> cursoCmb;
    @FXML private ComboBox<Asignatura> asignaturaCmb;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private TableView<Matricula> matriculacionesTable;
    @FXML private TableColumn<Matricula, String> matriculaCursoCol;
    @FXML private TableColumn<Matricula, String> matriculaAsignaturaCol;
    @FXML private TableColumn<Matricula, LocalDate> matriculaFechaInicioCol;
    @FXML private TableColumn<Matricula, LocalDate> matriculaFechaFinCol;
    @FXML private TableColumn<Matricula, String> matriculaDescripcionCursoCol;
    @FXML private TableColumn<Matricula, String> matriculaDescripcionAsignaturaCol;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private Button addAsignaturaBtn;
    @FXML private Button delAsignaturaBtn;
    @FXML private HBox barraOpciones;
    @FXML private HBox formularioDatosAlumno;
    @FXML private HBox formularioDatosMatriculaciones;
    @FXML private HBox barraConfirmacion;
    @FXML private Button editBtn;
    @FXML private Button printBtn;
    
    // Lista observable de asignaturas para el ComboBOX
    private ObservableList<Asignatura> asignaturasObsList;
    // Lista Observable para los Cursos para el ComboBox
    private ObservableList<Curso> cursosObsList;
    // Lista Observable de Alumnos para el ListView
    private ObservableList<FichaMatricula> alumnosObsList;
    // Lista Observable de Matriculaciones de un alumno
    private ObservableList<Matricula> matriculasObsList= FXCollections.observableArrayList();
    
    private byte[] imagen;
    private FichaMatricula datosAlumnoSeleccionado;
    private List<Matricula> asignaturaMatriculaInicial;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        cargarListaAlumnos();
        cargarListaCursos();
        inicializarTablaMatriculaciones();
        
        
        // Listener Filtro
        FilteredList<FichaMatricula> listaFiltrada = new FilteredList<>(alumnosObsList, s -> true);
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
                matriculasObsList=FXCollections.observableList(datosAlumnoSeleccionado.getListaMatriculas());
                asignaturaMatriculaInicial=new ArrayList<>();
                asignaturaMatriculaInicial.addAll(matriculasObsList); 
                matriculacionesTable.setItems(matriculasObsList);               
            }
        }); 
        
        // Listener Selección Curso ComboBox
        cursoCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
        if (newSelection!=null) cargarListaAsignaturas(newSelection.getCodigo());
        });
        
        Campos.validaCampoFecha(fechaInicioPicker);
        Campos.validaCampoFecha(fechaFinPicker);  
        alumnosListView.getSelectionModel().selectFirst();

    }    

    /**
     * Acción al ser pulsado botón para EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        habilitarCamposEdicion(true);
    }
    
    /**
     * Acción al ser pulsado el botón para IMPRIMIR
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
            pdf.GenerarHojaMatriculas(alumnosListView.getSelectionModel().getSelectedItem());
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
        datosAlumnoSeleccionado.setListaMatriculas(matriculasObsList);
        alumnosObsList.set(indice, datosAlumnoSeleccionado);
        asignaturaMatriculaInicial.clear();
        asignaturaMatriculaInicial.addAll(matriculasObsList);
        if (OperativasBD.borrarMatriculaciones(datosAlumnoSeleccionado.getAlumno().getCodigo()) && 
            OperativasBD.insertarMatriculaciones(datosAlumnoSeleccionado)){
                Mensajes.msgInfo("MODIFICACION MATRICULAS", "Las matrículas se han grabado correctamente");
        }else{
                Mensajes.msgError("MODIFICACION MATRICULAS", "Las matrículas no se han grabado correctamente");
        }
        habilitarCamposEdicion(false);
        limpiarCombos();
        visualizaLista(alumnosObsList);
        alumnosListView.getSelectionModel().select(indice);
    }
    
    /**
     * Acción al ser pulsado el botón de CANCELAR
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
        datosAlumnoSeleccionado.setListaMatriculas(asignaturaMatriculaInicial);
        matriculasObsList=FXCollections.observableList(asignaturaMatriculaInicial);
        alumnosObsList.set(indice, datosAlumnoSeleccionado);        
        habilitarCamposEdicion(false);
        alumnosListView.getSelectionModel().clearSelection(registroActual);
        alumnosListView.getSelectionModel().select(registroActual);
        limpiarCombos();
    }
    
    /**
     * Acción al ser pulsado el botón para añadir matriculaciones de asignaturas
     * @param event  Evento
     */
    @FXML
    private void clickAddAsignaturaBtn(ActionEvent event) {
        try{
            LocalDate dateInicio, dateFin;
            dateInicio=fechaInicioPicker.getValue();
            dateFin=fechaFinPicker.getValue();
            if (cursoCmb.getSelectionModel().isEmpty() || 
                asignaturaCmb.getSelectionModel().isEmpty() ||
                fechaInicioPicker.getValue()==null ||
                fechaFinPicker.getValue()==null){
                Mensajes.msgInfo("VALIDACION MATRICULA", "Los campos Curso,Asignatura "
                    + "y fechas deben estar rellenos");
            }else{
                Matricula m=new Matricula(cursoCmb.getSelectionModel().getSelectedItem().getCodigo(),
                cursoCmb.getSelectionModel().getSelectedItem().getDescripcion(),
                asignaturaCmb.getSelectionModel().getSelectedItem().getCodigo(),
                asignaturaCmb.getSelectionModel().getSelectedItem().getNombre(),
                fechaInicioPicker.getValue(),fechaFinPicker.getValue());
                if (validaMatricula(m)){
                    matriculasObsList.add(m);
                    matriculacionesTable.getSelectionModel().select(m);
                    matriculacionesTable.scrollTo(m);
                    limpiarComboAsignatura();
                }else{
                    Mensajes.msgError("ERROR MATRICULACION", "Verifica fechas y asignaturas");
                }
            }
        }catch(DateTimeParseException ex){
            fechaInicioPicker.getEditor().setText("");
            fechaFinPicker.getEditor().setText("");
            Mensajes.msgError("ERROR FORMATO", "Las fechas deben tener el formato dd/mm/aaaa");
        }
    }
    
    /**
     * Acción al ser pulsado el botón para quitar matriculaciones de asignaturas
     * @param event  Evento
     */
    @FXML
    private void clickDelAsignaturaBtn(ActionEvent event) {
        Matricula m=matriculacionesTable.getSelectionModel().getSelectedItem();
        matriculasObsList.remove(m);
        matriculacionesTable.setItems(matriculasObsList);
    }
    
    /**
     * Método que cargar la lista de alumnos en una lista observable
     */
    private void cargarListaAlumnos(){
        List<FichaMatricula> listaAlumnos=OperativasBD.extraerFichasMatriculas();
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
     * Método que cargar la lista de asignaturas en una lista
     * @param codigo    Código del Curso
     */
    private void cargarListaAsignaturas(String codigo){
        asignaturasObsList=FXCollections.observableArrayList(OperativasBD.extraerAsignaturas(codigo));
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
     * Método que cargar la lista de cursos en una lista
     */
    private void cargarListaCursos(){
        cursosObsList=FXCollections.observableArrayList(OperativasBD.extraerCursos());
        ordenarListaCursos();
    }
    
    /**
     * Ordena la lista de cursos
     */
    private void ordenarListaCursos(){
        cursosObsList.sort((curso1,curso2)->curso1.toString().compareTo(curso2.toString()));
        cursoCmb.setItems(cursosObsList);
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
        addAsignaturaBtn.setGraphic(iv[2]);
        addAsignaturaBtn.setContentDisplay(ContentDisplay.TOP);
        delAsignaturaBtn.setGraphic(iv[3]);
        delAsignaturaBtn.setContentDisplay(ContentDisplay.TOP); 
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
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        alumnosListView.setDisable(b);
        barraOpciones.setDisable(b);
        formularioDatosAlumno.setDisable(true);
        formularioDatosMatriculaciones.setDisable(!b);
        barraConfirmacion.setDisable(!b);
        filtroTxt.setDisable(b);
    }
    
    
    /**
     * Método que visualiza los alumnos en pantalla. Carga el ListView
     * @param lista     Lista de los alumnos
     */
    private void visualizaLista(ObservableList<FichaMatricula> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
        }
        alumnosListView.setItems(lista);
        alumnosListView.getSelectionModel().selectFirst();
    } 
    
    /**
     * Método que inicializa la tabla de matriculaciones del alumno
     */
    private void inicializarTablaMatriculaciones() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        matriculacionesTable.setPlaceholder(new Label ("Alumno no matriculado...."));
        matriculaCursoCol.setCellValueFactory(new PropertyValueFactory<>("codCurso"));
        matriculaDescripcionCursoCol.setCellValueFactory(new PropertyValueFactory<>("descripcionCurso"));
        matriculaAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("codAsignatura"));
        matriculaDescripcionAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("descripcionAsignatura"));
        matriculaFechaInicioCol.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        matriculaFechaFinCol.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        
        matriculaFechaInicioCol.setStyle( "-fx-alignment: CENTER;");
        matriculaFechaInicioCol.setCellFactory(column -> {
            return new TableCell<Matricula, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(myDateFormatter.format(item));
                }
            }
            };
        });
        matriculaFechaFinCol.setStyle( "-fx-alignment: CENTER;");
        matriculaFechaFinCol.setCellFactory(column -> {
            return new TableCell<Matricula, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(myDateFormatter.format(item));
                }
            }
            };
        });
    }

    
    /**
     * Función que valida una matriculación con las ya existentes
     * @param matricula Matrícula a añadir
     * @return          Devuelve True=se puede añadir. False=no se puede añadir
     */
    private boolean validaMatricula(Matricula matricula) {
        boolean estado=true;
        if (matricula.getFechaFin().compareTo(matricula.getFechaInicio())<=0) {
            estado=false;
        }else{
            if (matriculasObsList.size()>0){
                for(Matricula m: matriculasObsList){
                    if (m.equals(matricula)){
                        if ((matricula.getFechaInicio().compareTo(m.getFechaFin())<=0 &&
                                matricula.getFechaFin().compareTo(m.getFechaInicio())>=0)){
                            estado=false;
                        }
                    }
                }
            }else{
                estado=true;
            }    
        }
        return estado;
    }
    
    /**
     * Método que limpia los combos y los deja sin nada selecciondo
     */
    private void limpiarCombos(){
        cursoCmb.setValue(null);
        asignaturaCmb.setValue(null);
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        fechaInicioPicker.getEditor().setText("");
        fechaFinPicker.getEditor().setText("");
        matriculacionesTable.getSelectionModel().clearSelection();
    }
    
    /**
     * Método que limpia los combos y los deja sin nada seleccionado 
     * menos el comboBox del curso
     */
    private void limpiarComboAsignatura(){
        asignaturaCmb.setValue(null);
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        matriculacionesTable.getSelectionModel().clearSelection();
    }

}



        