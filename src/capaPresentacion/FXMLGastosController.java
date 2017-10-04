package capaPresentacion;

import capaNegocio.FichaGasto;
import capaNegocio.Gasto;
import capaNegocio.OperativasBD;
import capaPresentacion.resources.Campos;
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
import java.time.LocalDateTime;
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
 * Formulario gestión de gastos
 *
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLGastosController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaGasto> proveedoresListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField cifTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField telefonosTxt;
    @FXML private TextField ctaBancariaTxt;
    @FXML private TextField suFacturaTxt;
    @FXML private DatePicker fechaPicker;
    @FXML private TextField baseTxt;
    @FXML private TextField ivaTxt;
    @FXML private TableView<Gasto> gastosTable;
    @FXML private TableColumn<Gasto,String> gastoConceptoCol;
    @FXML private TableColumn<Gasto, LocalDate> gastoFechaCol;
    @FXML private TableColumn<Gasto,Double> gastoBaseCol;
    @FXML private TableColumn<Gasto,Double> gastoIvaCol;
    @FXML private Button addGastoBtn;
    @FXML private Button delGastoBtn;
    @FXML private HBox barraConfirmacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button editBtn;
    @FXML private Button printBtn;
    @FXML private HBox formularioDatosGastos;
    @FXML private HBox formularioDatosProveedor;

    
    // Lista Observable de Proveedores para el ListView
    private ObservableList<FichaGasto> proveedoresObsList;
    // Lista Observable de Gastos de un proveedor
    private ObservableList<Gasto> gastosObsList= FXCollections.observableArrayList();
    
    private FichaGasto datosProveedorSeleccionado;
    private List<Gasto> gastosInicial;
   
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        cargarListaProveedores();
        inicializarTablaGastos();
        
        // Listener Filtro
        FilteredList<FichaGasto> listaFiltrada = new FilteredList<>(proveedoresObsList, s -> true);
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
        
        // Listener Selección Proveedor
        proveedoresListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                datosProveedorSeleccionado=newSelection;
                cifTxt.setText(datosProveedorSeleccionado.getProveedor().getCifDni());
                nombreTxt.setText(datosProveedorSeleccionado.getProveedor().getNombre()+
                        " "+datosProveedorSeleccionado.getProveedor().getApellidos());
                telefonosTxt.setText(datosProveedorSeleccionado.getProveedor().getTelefono()+
                        " / "+datosProveedorSeleccionado.getProveedor().getMovil());
                ctaBancariaTxt.setText(datosProveedorSeleccionado.getProveedor().getCtaBanco());
                gastosObsList=FXCollections.observableList(datosProveedorSeleccionado.getListaGastos());
                gastosInicial=new ArrayList<>();
                gastosInicial.addAll(gastosObsList); 
                gastosTable.setItems(gastosObsList);               
            }
        }); 
        proveedoresListView.getSelectionModel().selectFirst();
        fijarTamanoMaximoConPatron(suFacturaTxt,25,"");
        fijarTamanoMaximoConPatron(baseTxt,8,"[0-9//.]");
        fijarTamanoMaximoConPatron(ivaTxt,8,"[0-9//.]");
        Campos.validaCampoFecha(fechaPicker);  
    }    

    /**
     * Acción al ser pulsado el botón AÑADIR
     * @param event Evento 
     */
    @FXML
    private void clickAddGastoBtn(ActionEvent event) {
        if (suFacturaTxt.getText().equals("") || fechaPicker.getValue()==null ||
                baseTxt.getText().equals("") ||
                ivaTxt.getText().equals("")){
            Mensajes.msgInfo("VALIDACION GASTOS", "Los campos Concepto, "
                    + "Fecha, Base e Iva deben estar rellenos.");
        }else{
            if (Double.parseDouble(baseTxt.getText())>Double.parseDouble(ivaTxt.getText())){
                Gasto g=new Gasto(suFacturaTxt.getText(), fechaPicker.getValue(),
                    Double.parseDouble(baseTxt.getText()),Double.parseDouble(ivaTxt.getText()));
                g.setNumGasto(LocalDateTime.now()+cifTxt.getText().trim());
                gastosObsList.add(g);
                gastosTable.getSelectionModel().select(g);
                gastosTable.scrollTo(g);
                limpiarCombos();
            }else{
                Mensajes.msgInfo("VALIDACION GASTOS", "El IVA no puede ser mayor "
                    + "que la Base.");
            }
        }
    }

    /**
     * Acción al ser pulsado el botón BORRAR
     * @param event  Evento
     */
    @FXML
    private void clickDelGastoBtn(ActionEvent event) {
        Gasto g=gastosTable.getSelectionModel().getSelectedItem();
        gastosObsList.remove(g);
        gastosTable.setItems(gastosObsList);
    }

    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int posicionActual,indice;
        posicionActual=proveedoresListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=proveedoresObsList.indexOf(proveedoresListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosProveedorSeleccionado.setListaGastos(gastosObsList);
        proveedoresObsList.set(indice, datosProveedorSeleccionado);
        gastosInicial.clear();
        gastosInicial.addAll(gastosObsList);
        if (OperativasBD.borrarGastos(datosProveedorSeleccionado.getProveedor().getCifDni()) && 
            OperativasBD.insertarGastos(datosProveedorSeleccionado)){
                Mensajes.msgInfo("MODIFICACION GASTOS", "Los gastos se han grabado correctamente");
        }else{
                Mensajes.msgError("MODIFICACION GASTOS", "Los gastos no se han grabado correctamente");
        }
        habilitarCamposEdicion(false);
        limpiarCombos();
        visualizaLista(proveedoresObsList);
        proveedoresListView.getSelectionModel().select(indice);
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=proveedoresListView.getSelectionModel().getSelectedIndex();
        int posicionActual,indice;
        posicionActual=proveedoresListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=proveedoresObsList.indexOf(proveedoresListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosProveedorSeleccionado.setListaGastos(gastosInicial);
        gastosObsList=FXCollections.observableList(gastosInicial);
        proveedoresObsList.set(indice, datosProveedorSeleccionado);        
        habilitarCamposEdicion(false);
        proveedoresListView.getSelectionModel().clearSelection(registroActual);
        proveedoresListView.getSelectionModel().select(registroActual);
        limpiarCombos();
    }

    /**
     * Acción al ser pulsado el botón EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
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
            pdf.GenerarHojaGastos(proveedoresListView.getSelectionModel().getSelectedItem());
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
        proveedoresListView.setDisable(b);
        barraOpciones.setDisable(!b);
        formularioDatosProveedor.setDisable(true);
        formularioDatosGastos.setDisable(!b);
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
        addGastoBtn.setGraphic(iv[2]);
        addGastoBtn.setContentDisplay(ContentDisplay.TOP);
        delGastoBtn.setGraphic(iv[3]);
        delGastoBtn.setContentDisplay(ContentDisplay.TOP); 
    }
    
     /**
     * Método que cargar la lista de proveedores en una lista observable
     */
    private void cargarListaProveedores(){
        List<FichaGasto> listaProveedores=OperativasBD.extraerFichasGastos();
        proveedoresObsList = FXCollections.observableArrayList(listaProveedores);
        ordenarListaProveedores();
    }
    
    /**
     * Método que ordena la lista de proveedores
     */
    private void ordenarListaProveedores(){
        proveedoresObsList.sort((proveedor1,proveedor2)->proveedor1.toString().compareTo(proveedor2.toString()));
        proveedoresListView.setItems(proveedoresObsList);
    }
    
    
    /**
     * Método que inicializa la tabla de gastos del proveedor
     */
    private void inicializarTablaGastos() {
        gastosTable.setPlaceholder(new Label ("Proveedor sin gastos...."));
        gastoConceptoCol.setCellValueFactory(new PropertyValueFactory<>("Concepto"));
        gastoFechaCol.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
        gastoBaseCol.setCellValueFactory(new PropertyValueFactory<>("Base"));
        gastoIvaCol.setCellValueFactory(new PropertyValueFactory<>("Iva"));
        gastoFechaCol.setStyle( "-fx-alignment: CENTER;");
        gastoFechaCol.setCellFactory(column -> {
            return new TableCell<Gasto, LocalDate>() {
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
     * Método que visualiza los proveedores en pantalla. Carga el ListView
     * @param lista     Lista de los proveedores
     */
    private void visualizaLista(ObservableList<FichaGasto> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
        }
        proveedoresListView.setItems(lista);
        proveedoresListView.getSelectionModel().selectFirst();
    }
    
    /**
     * Método que limpia los combos y los deja sin nada selecciondo
     */
    private void limpiarCombos(){
        suFacturaTxt.setText("");
        fechaPicker.setValue(null);
        baseTxt.setText("");
        ivaTxt.setText("");
    }
}
