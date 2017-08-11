package capaPresentacion;

import capaNegocio.Asignatura;
import capaNegocio.Curso;
import capaNegocio.OperativasBD;
import capaNegocio.Profesor;
import capaNegocio.ShareData;
import capaNegocio.TipoPago;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * Formulario para la gestión de cursos
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLCursosController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<Curso> cursosListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField codigoTxt;
    @FXML private TextField descripcionTxt;
    @FXML private TextField importeTxt;
    @FXML private ComboBox<TipoPago> formaPagoCmb;
    @FXML private ComboBox<Profesor> tutorCmb;
    @FXML private ComboBox<Asignatura> asignaturaCmb;
    @FXML private ListView<Asignatura> asignaturasListView;
    @FXML private Button addAsignaturaBtn;
    @FXML private Button delAsignaturaBtn;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar; 
    @FXML private HBox barraOpciones;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button printBtn;
    // Lista observable de asignaturas para el ComboBOX
    private ObservableList<Asignatura> asignaturasObsList;
    // Lista Observable de asignaturas para la ListVIEW
    private ObservableList<Asignatura> asignaturasSeleccionadasObsList;
    // Lista Observable de Profesores para el ComboBOX
    private ObservableList<Profesor> profesoresObsList;
    // Lista Observable para los Cursos para la ListView
    private ObservableList<Curso> cursosObsList;
    // Lista tipos de Pago para ComboBOX
    private ObservableList<TipoPago> pagosObsList;
    // Lista de tipos de pagos
    private List<TipoPago> pagos;
    // Lista de asignaturas seleccionadas
    private List<Asignatura> asignaturasSeleccionadas;
    // Lista de todas las asignaturas existentes.
    private List<Asignatura> listaAsignaturas;
    // Lista de todos los profesores existentes.
    private List<Profesor> listaProfesores;
    private boolean MODOEDICION=false;
    
    private String pago;
    private Profesor tutor;
    private Asignatura asignatura;
    private Asignatura asignaturaSeleccionadaEnLista;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        inicializarVariables();
        cargarListaAsignaturas();
        cargarListaProfesores();
        mostrarProfesoresNoUsados(false);
        mostrarListaPago();
        mostrarListaCursos();
        
        // Listener Filtro
        FilteredList<Curso> listaFiltrada = new FilteredList<>(cursosObsList, s -> true);
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

        // Listener Selección Forma de Pago
        formaPagoCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null) pago=newSelection.getCodigo();
        }); 
        
        // Listener Selección Tutor
        tutorCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null) tutor=newSelection;
        }); 
        
        // Listener Selección Asignatura ComboBox
        asignaturaCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null) asignatura=newSelection;                
        });         
        
        // Listener Selección Asignatura ListView
        asignaturasListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null) asignaturaSeleccionadaEnLista=newSelection;                
        }); 
        
        //Listener Selección Curso
        cursosListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                codigoTxt.setText(newSelection.getCodigo());
                descripcionTxt.setText(newSelection.getDescripcion());
                importeTxt.setText(newSelection.getImporteHora()+"");
                for(TipoPago tp:pagos){
                    if (tp.getCodigo().equals(newSelection.getPago()))
                        formaPagoCmb.setValue(tp);
                }
                tutor=newSelection.getTutor();
                tutorCmb.setValue(newSelection.getTutor());
                asignaturasSeleccionadas=newSelection.getAsignaturas();
                asignaturasListView.setItems(FXCollections.observableArrayList(
                    asignaturasSeleccionadas));
            }                
        }); 
        
        visualizaLista(cursosObsList);
        fijarTamanoMaximoConPatron(codigoTxt,9,"[0-9A-Za-z\\-\\_\\.]");
        fijarTamanoMaximoConPatron(descripcionTxt,30,"");
        fijarTamanoMaximoConPatron(importeTxt,7,"[0-9\\.]");
    }    
    
    /**
     * Acción al ser pulsado botón AÑADIR
     * @param event  Evento
     */
    @FXML
    private void clickAddBtn(ActionEvent event) {
        MODOEDICION=false;
        inicializarVariables();
        //mostrarProfesoresNoUsados(true);
        mostrarProfesoresNoUsados(ShareData.EMPRESA.isTutorPorCurso());
        habilitarCamposEdicion(true);
        inicializarCampos();
        mostrarListaAsignaturasNoUsadas(false);
    }

    /**
     * Acción al ser pulsado botón EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        MODOEDICION=true;
        //inicializarVariables();
        //mostrarProfesoresNoUsados(true);
        mostrarProfesoresNoUsados(ShareData.EMPRESA.isTutorPorCurso());
        profesoresObsList.add(cursosListView.getSelectionModel().getSelectedItem().getTutor());
        // añado el profesor actual en esta linea
        habilitarCamposEdicion(true);
        //inicializarCampos();
        asignaturasSeleccionadasObsList=asignaturasListView.getItems();
        mostrarListaAsignaturasNoUsadas(true);
    }

    /**
     * Acción al ser pulsado el botón BORRAR
     * @param event  Evento
     */
    @FXML
    private void clickDeleteBtn(ActionEvent event) {
        if (cursosListView.getSelectionModel().getSelectedIndex()>-1){
            Curso curso=cursosListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta("Borrado elemento",curso.getDescripcion()+"("+curso.getCodigo()+") será borrado.","¿Quieres borrar el elemento?")) {
                if (OperativasBD.borrarCurso(curso)){
                    Mensajes.msgInfo("ACCION:", "El borrado del Curso ha sido realizado");
                    cursosObsList.remove(curso);
                    if (cursosObsList.isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarVariables();
                        inicializarCampos();
                    }else{
                        cursosListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado del Curso ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO CURSOS", "No ha seleccionado ninguna curso");
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
            pdf.GenerarHojaCurso(cursosListView.getSelectionModel().getSelectedItem());
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
        int indice, posicionActual;
        if (!codigoTxt.getText().equals("") && !descripcionTxt.getText().equals("")
                && formaPagoCmb.getValue()!=null && tutorCmb.getValue()!=null
                && Double.parseDouble(importeTxt.getText().equals("")?"0.0":importeTxt.getText())>0){   
            Curso curso=new Curso(
                codigoTxt.getText(),
                descripcionTxt.getText(),
                Double.parseDouble(importeTxt.getText().equals("")?"0":importeTxt.getText()),
                pago,
                tutor);
            curso.setAsignaturas(asignaturasSeleccionadas);
            
            if (!MODOEDICION){
                if(OperativasBD.insertarCurso(curso)){
                    cursosObsList.add(curso);
                    ordenarListaCursos();
                    Mensajes.msgInfo("INSERCION CURSOS", "Curso añadido correctamente");
                    visualizaLista(cursosObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Curso");
                }
            }else{
                if(OperativasBD.modificarCurso(curso)){
                    posicionActual=cursosListView.getSelectionModel().getSelectedIndex();
                    if (!filtroTxt.getText().isEmpty()){
                        indice=cursosObsList.indexOf(cursosListView.getSelectionModel().getSelectedItem());
                    }else{
                        indice=posicionActual;
                    }
                    
                    if (cursosListView.getSelectionModel().getSelectedItem().getCodigo().equals(codigoTxt.getText())
                            && cursosListView.getSelectionModel().getSelectedItem().getDescripcion().equals(descripcionTxt.getText())){
                        cursosObsList.set(indice, curso);
                        cursosListView.getSelectionModel().clearSelection();
                        cursosListView.getSelectionModel().clearAndSelect(posicionActual);
                    }else{
                        cursosObsList.set(indice, curso);
                        ordenarListaCursos();
                        filtroTxt.setText("");
                    }
                    Mensajes.msgInfo("MODIFICAR CURSOS", "Curso modificado correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Curso");
                }
            }
            mostrarProfesoresNoUsados(false);
            tutorCmb.setValue(tutor);
            habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Código curso, descripción, tipo de pago, importe hora o tutor no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=cursosListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        cursosListView.getSelectionModel().clearSelection(registroActual); 
        cursosListView.getSelectionModel().select(registroActual); 
        mostrarProfesoresNoUsados(false);
        tutorCmb.setValue(tutor);
    }
    
    /**
     * Método que añade una asignatura a la lista de asignaturas seleccionadas
     * @param event  Evento
     */
    @FXML
    private void clickAddAsignaturaBtn(ActionEvent event) {
        if(asignatura!=null){
            asignaturasSeleccionadas.add(asignatura);
            asignaturasSeleccionadasObsList=FXCollections.observableArrayList(asignaturasSeleccionadas);
            asignaturasListView.setItems(asignaturasSeleccionadasObsList);
            asignaturasObsList.remove(asignatura);
            ordenarListaAsignaturas();
            asignatura=null;
        }
    }

    /**
     * Método que quita una asignatura de la lista de asignaturas 
     * @param event  Evento
     */
    @FXML
    private void clickDelAsignaturaBtn(ActionEvent event) {
        if(asignaturaSeleccionadaEnLista!=null){
            asignaturasSeleccionadas.remove(asignaturaSeleccionadaEnLista);
            asignaturasSeleccionadasObsList=FXCollections.observableArrayList(asignaturasSeleccionadas);
            asignaturasListView.setItems(asignaturasSeleccionadasObsList);
            asignaturasObsList.add(asignaturaSeleccionadaEnLista);
            ordenarListaAsignaturas();
            asignaturaSeleccionadaEnLista=null;
        }
    }

    /**
     * Método que cargar la lista de asignaturas en una lista
     */
    private void cargarListaAsignaturas(){
        listaAsignaturas=new ArrayList<>();
        listaAsignaturas=OperativasBD.extraerAsignaturas(null);
        mostrarListaAsignaturasNoUsadas(false);
    }
    
    /**
     * Método que carga las lista de asignaturas en una lista observable
     * @param noUsadas  True=Asignaturas No usadas
     */
    private void mostrarListaAsignaturasNoUsadas(boolean noUsadas) {
        asignaturasObsList = FXCollections.observableArrayList(listaAsignaturas);
        if (noUsadas){
            for (Asignatura a: asignaturasSeleccionadas){
                asignaturasObsList.remove(a);
            }
        }
        ordenarListaAsignaturas();
    }
    
    /**
     * Ordena la lista de asignaturas
     */
    private void ordenarListaAsignaturas(){
        asignaturasObsList.sort((asignatura1,asignatura2)->asignatura1.toString().compareTo(asignatura2.toString()));
        asignaturaCmb.setItems(asignaturasObsList);
        asignaturaCmb.getSelectionModel().select(null);
    }

    /**
     * Método que carga los profesores en una lista
     */
    private void cargarListaProfesores(){
        listaProfesores=new ArrayList<>();
        listaProfesores=OperativasBD.extraerProfesores();
        mostrarProfesoresNoUsados(false);    
    }
    
    /**
     * Método que carga la lista de  profesores disponibles en una lista observable
     * @param noUsados True= Profesores no usados
     */
    private void mostrarProfesoresNoUsados(boolean noUsados) {
        if (noUsados){
            profesoresObsList=FXCollections.
                    observableArrayList(OperativasBD.extraerProfesoresNoUsadosEnCursos());
        }else{
            profesoresObsList = FXCollections.observableArrayList(listaProfesores);
        }   
        ordenarListaProfesores();
    }
    
    /**
     * Método que ordena la lista de profesores
     */
    private void ordenarListaProfesores(){
        profesoresObsList.sort((profesor1,profesor2)->profesor1.toString().compareTo(profesor2.toString()));
        tutorCmb.setItems(profesoresObsList);
    }
    
    /**
     * Método que muestra la lista de pagos
     */
    private void mostrarListaPago() {
        pagos=new ArrayList<>();
        pagos.add(new TipoPago("A","Anual"));
        pagos.add(new TipoPago("M","Mensual"));
        pagos.add(new TipoPago("S","Semanal"));
        pagosObsList = FXCollections.observableArrayList(pagos);
        formaPagoCmb.setItems(pagosObsList);
    }

    /**
     * Método que carga la lista de cursos en la lista observable
     */
    private void mostrarListaCursos() {
        List<Curso> listaCursos=OperativasBD.extraerCursos();
        cursosObsList = FXCollections.observableArrayList(listaCursos);
        ordenarListaCursos();
    }
    
    /**
     * Método que ordena la lista de cursos
     */
    private void ordenarListaCursos(){
        cursosObsList.sort((curso1,curso2)->curso1.toString().compareTo(curso2.toString()));
        cursosListView.setItems(cursosObsList);
    }
    
    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        codigoTxt.setText("");
        descripcionTxt.setText("");
        importeTxt.setText("0.0");       
        formaPagoCmb.setValue(null);
        tutorCmb.setValue(null);
        asignaturasListView.setItems(null);
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        codigoTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        cursosListView.setDisable(b);
        filtroTxt.setDisable(b);
        codigoTxt.setPromptText(b?"Código del Curso":"");
        descripcionTxt.setPromptText(b?"Nombre del Curso":"");
        importeTxt.setPromptText(b?"Importe del Curso":"");
        formaPagoCmb.setPromptText(b?"Forma de pago":"");
        tutorCmb.setPromptText(b?"Tutor del curso":"");
        codigoTxt.setTooltip(new Tooltip("Introduzca código del curso"));
        descripcionTxt.setTooltip(new Tooltip("Introduzca el nombre del curso"));
        importeTxt.setTooltip(new Tooltip("Introduzca el importe del curso"));
        formaPagoCmb.setTooltip(new Tooltip("Anual - Mensual - Semanal"));
        tutorCmb.setTooltip(new Tooltip("Introduzca el nombre del tutor del curso"));
    }
    
    /**
     * Método que visualiza los cursos en pantalla
     * @param lista     Lista de los cursos
     */
    private void visualizaLista(ObservableList<Curso> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        cursosListView.setItems(lista);
        cursosListView.getSelectionModel().select(0);
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
            "resources/icons/delete.png","resources/icons/print.png",
            "resources/icons/addlittle.png","resources/icons/deletelittle.png"            
            };
        ImageView[] iv=new ImageView[icons.length];
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
        addAsignaturaBtn.setGraphic(iv[4]);
        addAsignaturaBtn.setContentDisplay(ContentDisplay.TOP);
        delAsignaturaBtn.setGraphic(iv[5]);
        delAsignaturaBtn.setContentDisplay(ContentDisplay.TOP);
    }
    
    /**
     * Método que inicializa variables
     */
    private void inicializarVariables() {
        pago=null;
        tutor=null;
        asignatura=null;
        asignaturaSeleccionadaEnLista=null;
        if (!MODOEDICION){
            asignaturasSeleccionadas=new ArrayList<>();
        }
    }
}


