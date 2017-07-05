package capaPresentacion;


import capaNegocio.DatePickerCell;
import capaNegocio.FichaRecibo;
import capaNegocio.OperativasBD;
import capaNegocio.ReciboGenerado;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Francisco José Sampedro Lujan
 */
public class FXMLRecibosController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaRecibo> alumnosListView;
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
    @FXML private HBox formularioDatosMatriculaciones;  
    
    @FXML private TableView<ReciboGenerado> recibosTable;
    @FXML private TableColumn<ReciboGenerado, String> numeroReciboCol;
    @FXML private TableColumn<ReciboGenerado, LocalDate> fechaEmisionReciboCol;
    @FXML private TableColumn<ReciboGenerado, LocalDate> fechaPagoReciboCol;
    @FXML private TableColumn<ReciboGenerado, String> descripcionReciboCol;
    @FXML private TableColumn<ReciboGenerado, String> importeReciboCol;
    @FXML private TableColumn<ReciboGenerado, String> facturaReciboCol;
    @FXML private Button addReciboBtn;
    @FXML private Button delReciboBtn;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private Button impReciboBtn;
    @FXML private Button editarReciboBtn;
    @FXML private Button editBtn;
    @FXML private HBox barraConfirmacion;
    @FXML private HBox barraOpciones;

  
    // Lista Observable de Alumnos para el ListView
    private ObservableList<FichaRecibo> alumnosObsList;
    // Lista Observable de Recibos de un alumno
    private ObservableList<ReciboGenerado> recibosObsList= FXCollections.observableArrayList();
    
    private byte[] imagen;
    private FichaRecibo datosAlumnoSeleccionado;
    private List<ReciboGenerado> recibosInicial;
    private boolean edicionStatus;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        cargarListaAlumnos();
        inicializarTablaRecibos();
        edicionStatus=false;
        // Listener Filtro
        FilteredList<FichaRecibo> listaFiltrada = new FilteredList<>(alumnosObsList, s -> true);
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
                recibosObsList=FXCollections.observableList(datosAlumnoSeleccionado.getListaRecibos());
                recibosInicial=new ArrayList<>();
                recibosInicial.addAll(recibosObsList); 
                recibosTable.setItems(recibosObsList);               
            }
        }); 
        
        recibosTable.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection ==null){
                if (recibosObsList.isEmpty()){
                    delReciboBtn.setDisable(true);
                    impReciboBtn.setDisable(true);
                    editarReciboBtn.setDisable(true);
                }
            }else{
                
            }
        }); 
        
        alumnosListView.getSelectionModel().selectFirst();
    }    

    @FXML
    private void clickAddReciboBtn(ActionEvent event) {
        recibosObsList.add(new ReciboGenerado(0, LocalDate.now(), null, "", 0.0, ""));
        btnAceptar.setDisable(false);
    }

    @FXML
    private void clickDelReciboBtn(ActionEvent event) {
       ReciboGenerado reciboSeleccionado = recibosTable.getSelectionModel().getSelectedItem();
        if (Mensajes.msgPregunta("Recibo número "+reciboSeleccionado.getReciboFormateado())){
            recibosObsList.remove(reciboSeleccionado);
            recibosTable.getSelectionModel().clearSelection();
            Mensajes.msgInfo("RECIBOS", "Recibo "+reciboSeleccionado.getReciboFormateado()+" borrado..");
            btnAceptar.setDisable(false);
        }
        recibosTable.getSelectionModel().selectFirst();
    }

    @FXML
    private void clickBtnAceptar(ActionEvent event) {
    }

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
        datosAlumnoSeleccionado.setListaRecibos(recibosInicial);
        recibosObsList=FXCollections.observableList(recibosInicial);
        alumnosObsList.set(indice, datosAlumnoSeleccionado);        
        habilitarCamposEdicion(false);
        alumnosListView.getSelectionModel().clearSelection(registroActual);
        alumnosListView.getSelectionModel().select(registroActual);
    }

       
    /**
     * Acción al ser pulsado botón para EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        habilitarCamposEdicion(true);
        recibosTable.getSelectionModel().selectFirst();
    }

    
    @FXML
    private void clickImpReciboBtn(ActionEvent event) {
        FileOutputStream ficheroPdf = null;
        try {
            // Se crea el documento tamaño A4 y margenes
            Document documento = new Document(PageSize.A5, 50, 50, 50, 50);
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
            pdf.GenerarHojaRecibo(recibosTable.getSelectionModel().getSelectedItem(),datosAlumnoSeleccionado);
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
        btnAceptar.setDisable(b);
        addReciboBtn.setDisable(!b);
        delReciboBtn.setDisable(!b);
        impReciboBtn.setDisable(!b);
        btnCancelar.setDisable(!b);
    }
    
    
      /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/edit.png",
            "resources/icons/addlittle.png","resources/icons/deletelittle.png",            
            "resources/icons/editlittle.png","resources/icons/imprimirlittle.png"};
        ImageView[] iv=new ImageView[icons.length];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        editBtn.setText("Editar");
        editBtn.setGraphic(iv[0]);
        editBtn.setContentDisplay(ContentDisplay.TOP);
        addReciboBtn.setGraphic(iv[1]);
        addReciboBtn.setContentDisplay(ContentDisplay.TOP);
        delReciboBtn.setGraphic(iv[2]);
        delReciboBtn.setContentDisplay(ContentDisplay.TOP); 
        editarReciboBtn.setGraphic(iv[3]);
        editarReciboBtn.setContentDisplay(ContentDisplay.TOP); 
        impReciboBtn.setGraphic(iv[4]);
        impReciboBtn.setContentDisplay(ContentDisplay.TOP); 
    }
    
    /**
     * Método que cargar la lista de alumnos en una lista observable
     */
    private void cargarListaAlumnos(){
        List<FichaRecibo> listaAlumnos=OperativasBD.extraerFichasRecibos();
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
     * Método que inicializa la tabla de recibos del alumno
     */
    private void inicializarTablaRecibos() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        recibosTable.setPlaceholder(new Label ("Alumno sin recibos...."));
        numeroReciboCol.setCellValueFactory(new PropertyValueFactory<>("reciboFormateado"));
        fechaEmisionReciboCol.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        fechaPagoReciboCol.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        fechaPagoReciboCol.setOnEditCommit(data -> { 
            data.getRowValue().setFechaPago(data.getNewValue()); 
            recibosTable.getSelectionModel().getSelectedItem().setFechaPago(data.getNewValue());
        });
        
        descripcionReciboCol.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        descripcionReciboCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //descripcionReciboCol.setEditable(true);
        descripcionReciboCol.setOnEditCommit(data -> { 
            data.getRowValue().setDescripcion(data.getNewValue()); 
        });
        importeReciboCol.setCellValueFactory(new PropertyValueFactory<>("importeFormateado"));
        importeReciboCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //importeReciboCol.setEditable(true);
        importeReciboCol.setOnEditCommit(data -> { 
            if (!data.getOldValue().equals(data.getNewValue())){
                btnAceptar.setDisable(false);
                Double valor;
                valor=convertirADoble(data.getNewValue());
                //Añadir el valor a la lista observable
                data.getRowValue().setImporte(valor); 
                recibosTable.getSelectionModel().getSelectedItem().setImporte(valor);
                recibosTable.refresh() ;
            } 
        });
        facturaReciboCol.setCellValueFactory(new PropertyValueFactory<>("factura"));
        fechaEmisionReciboCol.setStyle( "-fx-alignment: CENTER;");
        fechaEmisionReciboCol.setCellFactory(column -> {
            return new TableCell<ReciboGenerado, LocalDate>() {
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
        fechaPagoReciboCol.setStyle( "-fx-alignment: CENTER;");
        
        importeReciboCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        habilitarCamposTabla(false);
    }
    
      /**
     * Método que visualiza los alumnos en pantalla. Carga el ListView
     * @param lista     Lista de los alumnos
     */
    private void visualizaLista(ObservableList<FichaRecibo> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
        }
        alumnosListView.setItems(lista);
        alumnosListView.getSelectionModel().selectFirst();
    } 
    
     /**
     * Método que deshabilita los botones de Editar, Borrar e Imprimir
     * @param b     Booleano True=deshabilitar False=Habilitar
     */
    private void deshabilitarBotonesEdicion(boolean b) {
        editBtn.setDisable(b);
    }

    @FXML
    private void clickEditarReciboBtn(ActionEvent event) {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (edicionStatus){
            recibosTable.setEditable(false);
            addReciboBtn.setDisable(false);
            delReciboBtn.setDisable(false);
            impReciboBtn.setDisable(false);
            edicionStatus=false;
            habilitarCamposTabla(false);
            fechaPagoReciboCol.setCellFactory(column -> {
            return new TableCell<ReciboGenerado, LocalDate>() {
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
            recibosTable.refresh();
        }else{
            recibosTable.setEditable(true);
            addReciboBtn.setDisable(true);
            delReciboBtn.setDisable(true);
            impReciboBtn.setDisable(true);
            habilitarCamposTabla(true);
            fechaPagoReciboCol.setCellFactory(DatePickerCell::new);
            edicionStatus=true;
        }
    }


    private double convertirADoble(String valor) {
        Double v;
        try{
            v=Double.parseDouble(valor);
        }catch (Exception e){
            Mensajes.msgError("DATO INTRODUCIDO", "El valor introducido no es numérico");
            v=0.0;
        }
        return v;
    }

    private void habilitarCamposTabla(boolean b){
        fechaEmisionReciboCol.setEditable(b);
        descripcionReciboCol.setEditable(b);
        importeReciboCol.setEditable(b);
    }
   
}
