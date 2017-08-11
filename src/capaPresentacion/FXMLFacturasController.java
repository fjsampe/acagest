/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaPresentacion;

import capaNegocio.Factura;
import capaNegocio.FichaFactura;
import capaNegocio.FichaRecibo;
import capaNegocio.OperativasBD;
import capaNegocio.ReciboGenerado;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Francisco José Sampedro Lujan
 */
public class FXMLFacturasController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaFactura> alumnosListView;
    @FXML private VBox formularioDatos;
    @FXML private HBox formularioDatosAlumno;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField telefonoTxt;
    @FXML private TextField movilTxt;
    @FXML private TextField fechaNacimientoTxt;
    @FXML private ImageView fotoImg;
    @FXML private HBox formularioDatosMatriculaciones;
    @FXML private TableView<Factura> facturasTable;
    @FXML private TableColumn<Factura, String> numeroFacturaCol;
    @FXML private TableColumn<Factura, LocalDate> fechaEmisionFacturaCol;
    @FXML private TableColumn<Factura, String> descripcionFacturaCol;
    @FXML private TableColumn<Factura, Double> importeFacturaCol;
    @FXML private HBox barraConfirmacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button editBtn;
    @FXML private Button impFacturaBtn;
    
    
    // Lista Observable de Alumnos para el ListView
    private ObservableList<FichaFactura> alumnosObsList;
    // Lista Observable de Facturas de un alumno
    private ObservableList<Factura> facturasObsList= FXCollections.observableArrayList();
    private byte[] imagen;
    private FichaFactura datosAlumnoSeleccionado;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fechaEmisionFacturaCol.setStyle( "-fx-alignment: CENTER;");
        habilitarCamposEdicion(false);
        showIconos();
        cargarListaAlumnos();
        inicializarTablaFacturas();
        
        // Listener Filtro
        FilteredList<FichaFactura> listaFiltrada = new FilteredList<>(alumnosObsList, s -> true);
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
                nombreTxt.setText(datosAlumnoSeleccionado.getAlumno().getNombre()+" "+
                        datosAlumnoSeleccionado.getAlumno().getApellidos());
                domicilioTxt.setText(datosAlumnoSeleccionado.getAlumno().getDomicilio());
                poblacionTxt.setText(datosAlumnoSeleccionado.getAlumno().getPoblacion());
                cpTxt.setText(datosAlumnoSeleccionado.getAlumno().getCp());
                provinciaTxt.setText(datosAlumnoSeleccionado.getAlumno().getProvincia());
                telefonoTxt.setText(datosAlumnoSeleccionado.getAlumno().getTelefono());
                movilTxt.setText(datosAlumnoSeleccionado.getAlumno().getMovil());
                fechaNacimientoTxt.setText(Campos.fechaToString(datosAlumnoSeleccionado.getAlumno().getNacimiento()));
                imagen=datosAlumnoSeleccionado.getAlumno().getFoto();
                fotoImg.setImage(convertirImagen(imagen));
                fotoImg.setFitHeight(100);
                fotoImg.setFitWidth(100);
                fotoImg.setPreserveRatio(false);
                fotoImg.setSmooth(true); 
                facturasObsList=FXCollections.observableList(datosAlumnoSeleccionado.getLista());
                facturasTable.setItems(facturasObsList);
            }
        }); 
        
        
        alumnosListView.getSelectionModel().selectFirst();
        
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
        alumnosObsList.set(indice, datosAlumnoSeleccionado);        
        habilitarCamposEdicion(false);
        alumnosListView.getSelectionModel().clearSelection();
        alumnosListView.getSelectionModel().select(registroActual);
    }
    
    

    @FXML
    private void clickEditBtn(ActionEvent event) {
        habilitarCamposEdicion(true);
        //verificaSizeFactura();
        facturasTable.getSelectionModel().selectFirst();
    }


    @FXML
    private void clickImpFacturaBtn(ActionEvent event) {
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
            pdf.GenerarHojaFactura(facturasTable.getSelectionModel().getSelectedItem(),datosAlumnoSeleccionado);
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
        impFacturaBtn.setDisable(!b);
        btnCancelar.setDisable(!b);
    }
    
    /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/edit.png",
            "resources/icons/imprimirlittle.png"};
        ImageView[] iv=new ImageView[icons.length];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        editBtn.setText("Editar");
        editBtn.setGraphic(iv[0]);
        editBtn.setContentDisplay(ContentDisplay.TOP);
        impFacturaBtn.setGraphic(iv[1]);
        impFacturaBtn.setContentDisplay(ContentDisplay.TOP); 
    }
    
    /**
     * Método que cargar la lista de alumnos en una lista observable
     */
    private void cargarListaAlumnos(){
        List<FichaFactura> listaAlumnos=OperativasBD.extraerFichasFacturas();
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
    private void inicializarTablaFacturas() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        facturasTable.setPlaceholder(new Label ("Alumno sin Facturas...."));
        numeroFacturaCol.setCellValueFactory(new PropertyValueFactory<>("FacturaFormateada"));
        fechaEmisionFacturaCol.setCellValueFactory(new PropertyValueFactory<>("FechaFactura"));
        descripcionFacturaCol.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        importeFacturaCol.setCellValueFactory(new PropertyValueFactory<>("ImporteFormateado"));
        importeFacturaCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
    }
 
    /**
     * Método que visualiza los alumnos en pantalla. Carga el ListView
     * @param lista     Lista de los alumnos
     */
    private void visualizaLista(ObservableList<FichaFactura> lista){
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
    
}
