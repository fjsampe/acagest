package capaPresentacion;


import capaNegocio.DatePickerCell;
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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
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
 * Formulario para la gestión de recibos
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLRecibosController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaRecibo> alumnosListView;
    @FXML private VBox formularioDatos;
    @FXML private HBox formularioDatosAlumno;
    @FXML private TextField niaTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private TextField telefonoTxt;
    @FXML private TextField movilTxt;
    @FXML private TextField fechaNacimientoTxt;
    @FXML private ImageView fotoImg;
    @FXML private HBox formularioDatosMatriculaciones;  
    
    @FXML private TableView<ReciboGenerado> recibosTable;
    @FXML private TableColumn<ReciboGenerado, String> numeroReciboCol;
    @FXML private TableColumn<ReciboGenerado, LocalDate> fechaEmisionReciboCol;
    @FXML private TableColumn<ReciboGenerado, LocalDate> fechaPagoReciboCol;
    @FXML private TableColumn<ReciboGenerado, String> descripcionReciboCol;
    @FXML private TableColumn<ReciboGenerado, String> importeReciboCol;
    @FXML private TableColumn<ReciboGenerado, Boolean> checkCol;
    @FXML private Button delReciboBtn;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private Button impReciboBtn;
    @FXML private Button editarReciboBtn;
    @FXML private Button editBtn; 
    @FXML private Button facturarBtn;
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
    private boolean algunoAModificar;
    private boolean edicionRecibo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaEmisionReciboCol.setStyle( "-fx-alignment: CENTER;");
        fechaPagoReciboCol.setStyle( "-fx-alignment: CENTER;");
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
                algunoAModificar=false;
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
                recibosInicial=new ArrayList<>();
                for (ReciboGenerado r: datosAlumnoSeleccionado.getListaRecibos()){
                    recibosInicial.add(new ReciboGenerado(r.getRecibo(),r.getFechaEmision(),r.getFechaPago(),
                        r.getDescripcion(),r.getImporte(),r.getFactura()));
                    if (r.getFactura()==null) algunoAModificar=true;
                }
                recibosObsList=FXCollections.observableList(datosAlumnoSeleccionado.getListaRecibos());
                recibosTable.setItems(recibosObsList);
                if (algunoAModificar){
                    editBtn.setDisable(false);
                }else{
                    editBtn.setDisable(true);
                }
            }
        }); 
        
        recibosTable.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection!=null){
                if (newSelection.getFactura()!=null){
                    habilitarBotonesDerecha(false);       
                }else{
                    habilitarBotonesDerecha(true);
                }
            }
        }); 
        alumnosListView.getSelectionModel().selectFirst();
    }    

   
    // -----------  Métodos click ---------------
    
    /**
     * Acción al ser pulsado botón para EDITAR TABLEVIEW
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        habilitarCamposEdicion(true);
        recibosTable.getSelectionModel().selectFirst();
    }
    
    /**
     * Acción al ser pulsado el botón para BORRAR
     * @param event 
     */
    @FXML
    private void clickDelReciboBtn(ActionEvent event) {
        ReciboGenerado reciboSeleccionado = recibosTable.getSelectionModel().getSelectedItem();
        if (Mensajes.msgPregunta("Borrado elemento","Recibo número "+reciboSeleccionado.getReciboFormateado(),"¿Quieres borrar el elemento?")){
            recibosObsList.remove(reciboSeleccionado);
            recibosTable.getSelectionModel().clearSelection();
            Mensajes.msgInfo("RECIBOS", "Recibo "+reciboSeleccionado.getReciboFormateado()+" borrado..");
            btnAceptar.setDisable(false);
        }
        recibosTable.getSelectionModel().selectFirst();
    }

    /**
     * Acción al ser pulsado el botón para EDITAR UN RECIBO
     * @param event 
     */
    @FXML
    private void clickEditarReciboBtn(ActionEvent event) {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (edicionStatus){
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
            habilitaBotonesEdicionRecibo(false);
            recibosTable.refresh();
        }else{
            habilitaBotonesEdicionRecibo(true);
            fechaPagoReciboCol.setCellFactory(DatePickerCell::new);
        }
    }
    
    /**
     * Acción al ser pulsado el botón para IMPRIMIR UN RECIBO
     * @param event 
     */
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
     * Acción al ser pulsado el botón para FACTURAR
     * @param event 
     */
    @FXML
    private void clickFacturarBtn(ActionEvent event) {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (edicionStatus){
            habilitaBotonesFacturarRecibos(false);
            checkCol.setEditable(false);
            editarReciboBtn.setDisable(false);
            recibosTable.refresh();
        }else{
            habilitaBotonesFacturarRecibos(true);
            checkCol.setEditable(true);
            editarReciboBtn.setDisable(true);
            facturarBtn.setDisable(false);
            
           
            //deshabilitar las lineas 
            
        }
    }
    
    
    
    
    
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        realizamosCambios();
        int posicionActual,indice;
        posicionActual=alumnosListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=alumnosObsList.indexOf(alumnosListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosAlumnoSeleccionado.setListaRecibos(recibosObsList);
        alumnosObsList.set(indice,datosAlumnoSeleccionado);
        recibosInicial.clear();
        recibosInicial.addAll(recibosObsList);
        habilitarCamposEdicion(false);
        visualizaLista(alumnosObsList);
        alumnosListView.getSelectionModel().select(indice);
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
        alumnosListView.getSelectionModel().clearSelection();
        alumnosListView.getSelectionModel().select(registroActual);
    }

       
   

    
    
    
    
    
 
    
    
    
    
     /**
     * Método que deshabilita los botones de Editar, Borrar e Imprimir
     * @param b     Booleano True=deshabilitar False=Habilitar
     */
    private void deshabilitarBotonesEdicion(boolean b) {
        editBtn.setDisable(b);
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

    

    private void realizamosCambios() {
        List<ReciboGenerado> recibosFacturados=new ArrayList<>();
        boolean encontrado;
        boolean quiereGenerarFactura=false;
        boolean preguntado=false;
        ReciboGenerado reciboEncontrado;
        int numeroFacturaNuevo=0;
        for (ReciboGenerado r:recibosObsList){
            reciboEncontrado=null;
            encontrado=false;
            for (ReciboGenerado inicial: recibosInicial){
                if (r.getRecibo()==inicial.getRecibo()){
                    if (r.getCheck()){
                        if (!preguntado){
                            if (Mensajes.msgPregunta("Gestión de Recibos","Facturación","¿Facturamos?")){
                                quiereGenerarFactura=true;
                                // creo la factura y recojo el número nuevo de factura
                                numeroFacturaNuevo=OperativasBD.insertarFactura();
                            }
                            preguntado=true;
                        }
                        if (quiereGenerarFactura){
                            System.out.println("GENERAR FACTURA NUEVA: \n"+
                            "RECIBO: "+r.getReciboFormateado()+"\n"+
                            "DESCRIPCION: "+r.getDescripcion()+"\n"+
                            "IMPORTE: "+r.getImporte()+"\n");
                            r.setFactura(numeroFacturaNuevo+"");
                            r.setFechaPago(LocalDate.now());
                            recibosFacturados.add(r);
                        }
                    }
                    if (!r.equals(inicial) ){
                        System.out.println("Modificado: \n"+
                                "RECIBO: "+r.getReciboFormateado()+"\n"+
                                "DESCRIPCION: "+r.getDescripcion()+"\n"+
                                "IMPORTE: "+r.getImporte()+"\n");
                        //debo modificar
                        if (!OperativasBD.modificarRecibo(r))
                            Mensajes.msgError("MODIFICACIÓN RECIBOS", "El recibo número: "+r.getRecibo()+
                        " no puede ser modificado");
                    }
                    reciboEncontrado=inicial;
                    encontrado=true;
                    break;
                }
            }
            if (encontrado) recibosInicial.remove(reciboEncontrado);
        }
        
        
        // No deben de estar en la tabla los siguientes.
        System.out.println("No debe de estar");
        for (ReciboGenerado r:recibosInicial){
            if (!OperativasBD.borrarRecibo(r.getRecibo())) 
                Mensajes.msgError("BORRADO RECIBOS", "El recibo número: "+r.getRecibo()+
                        " no puede ser borrado");
        }
        
        // quito de la lista observable los recibos facturados
        
        for (ReciboGenerado r:recibosFacturados){
            recibosObsList.remove(r);
        }
        
        
        /*
        // Se deben añadir los siguientes 
        System.out.println("Se deben añadir");
        for (ReciboGenerado r:recibosObsList){
            if (r.getRecibo()==0){
                
                //if (!OperativasBD.insertarRecibo(r,datosAlumnoSeleccionado.getAlumno().getCodigo()))
                //    Mensajes.msgError("INSERCION RECIBOS", "Error inserción recibo "+r.getReciboFormateado());
                System.out.println("Recibo: "+r.getRecibo()+" "+r.getDescripcion());
            }
        }*/
        
        
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
        delReciboBtn.setDisable(!b);
        impReciboBtn.setDisable(!b);
        btnCancelar.setDisable(!b);
        editarReciboBtn.setDisable(!b);
        facturarBtn.setDisable(!b);
    }
    
    /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/edit.png",
            "resources/icons/addlittle.png","resources/icons/deletelittle.png",            
            "resources/icons/editlittle.png","resources/icons/imprimirlittle.png",
            "resources/icons/facturalittle.png"};
        ImageView[] iv=new ImageView[icons.length];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        editBtn.setText("Editar");
        editBtn.setGraphic(iv[0]);
        editBtn.setContentDisplay(ContentDisplay.TOP);
        delReciboBtn.setGraphic(iv[2]);
        delReciboBtn.setContentDisplay(ContentDisplay.TOP); 
        editarReciboBtn.setGraphic(iv[3]);
        editarReciboBtn.setContentDisplay(ContentDisplay.TOP); 
        impReciboBtn.setGraphic(iv[4]);
        impReciboBtn.setContentDisplay(ContentDisplay.TOP); 
        facturarBtn.setGraphic(iv[5]);
        facturarBtn.setContentDisplay(ContentDisplay.TOP); 
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
     * Método que inicializa la tabla de recibos del alumno
     */
    private void inicializarTablaRecibos() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        recibosTable.setPlaceholder(new Label ("Alumno sin recibos...."));
        numeroReciboCol.setCellValueFactory(new PropertyValueFactory<>("ReciboFormateado"));
        fechaEmisionReciboCol.setCellValueFactory(new PropertyValueFactory<>("FechaEmision"));
        fechaPagoReciboCol.setCellValueFactory(new PropertyValueFactory<>("FechaPago"));
        fechaPagoReciboCol.setOnEditCommit(data -> { 
            data.getRowValue().setFechaPago(data.getNewValue()); 
            recibosTable.getSelectionModel().getSelectedItem().setFechaPago(data.getNewValue());
        });
        descripcionReciboCol.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        descripcionReciboCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descripcionReciboCol.setOnEditCommit(data -> { 
            data.getRowValue().setDescripcion(data.getNewValue()); 
        });
        importeReciboCol.setCellValueFactory(new PropertyValueFactory<>("ImporteFormateado"));
        importeReciboCol.setCellFactory(TextFieldTableCell.forTableColumn());
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
        
        //checkCol.setCellValueFactory(new PropertyValueFactory<>("Check"));
        checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));
        checkCol.setCellValueFactory(new Callback<CellDataFeatures<ReciboGenerado, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<ReciboGenerado, Boolean> param) {
                ReciboGenerado recibo = param.getValue();
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(recibo.getCheck());
                // Note: singleCol.setOnEditCommit(): Not work for
                // CheckBoxTableCell.
                    booleanProp.addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                            recibo.setCheck(newValue);
                         }
                    });
                return booleanProp;
            }
        });
        importeReciboCol.setStyle( "-fx-alignment: CENTER-RIGHT;");
        habilitarCamposTabla(false);
    }
    
    /**
     * Método que habilita los campos en la tableview que van a ser editados
     * @param b     True=habilitar  False=Deshabilitado
     */
    private void habilitarCamposTabla(boolean b){
        fechaEmisionReciboCol.setEditable(b);
        descripcionReciboCol.setEditable(b);
        importeReciboCol.setEditable(b); 
    }
    
    /**
     * Método que deshabilita los botones de borrar, editar,imprimir y facturas si no existen recibos
     */
    private void verificarEdicionPosible() {
        if (algunoAModificar){
            habilitarBotonesDerecha(true);
            
            recibosTable.setDisable(false);
        }else{
            habilitarBotonesDerecha(false);
            
            recibosTable.setDisable(true);
        }
    }

    /**
     * Método que habilita los botones de edición del recibo
     * @param b True=habilita False=deshabilita
     */
    private void habilitaBotonesEdicionRecibo(boolean b) {
        habilitarBotones(b);
        facturarBtn.setDisable(b);
        habilitarCamposTabla(b);
    }
    
    /**
     * Método que habilita los botones de facturar recibos
     * @param b True=habilita False=deshabilita
     */
    private void habilitaBotonesFacturarRecibos(boolean b) {
        habilitarBotones(b);
        editarReciboBtn.setDisable(b);
        checkCol.setEditable(b);
    }
    
    /**
     * Método que habilita los botones 
     * @param b True=habilita False=deshabilita
     */
    private void habilitarBotones(boolean b){
        edicionStatus=b;
        delReciboBtn.setDisable(b);
        impReciboBtn.setDisable(b);
        recibosTable.setEditable(b);
        btnAceptar.setDisable(b);
        btnCancelar.setDisable(b);
    }

    private void habilitarBotonesDerecha(boolean b) {
        delReciboBtn.setDisable(!b);
        impReciboBtn.setDisable(!b);
        editarReciboBtn.setDisable(!b);
        facturarBtn.setDisable(!b);
    }

}  

            