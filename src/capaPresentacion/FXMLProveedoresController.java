package capaPresentacion;

import capaNegocio.OperativasBD;
import capaNegocio.Proveedor;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * Formulario para la gestión de proveedores
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLProveedoresController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<Proveedor> proveedoresListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField nifTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField apellidosTxt;
    @FXML private TextField telefonoTxt;
    @FXML private TextField movilTxt;
    @FXML private TextField ctaTxt;
    @FXML private TextField emailTxt;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button printBtn;
    private ObservableList<Proveedor> proveedoresObsList;
    private boolean MODOEDICION=false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        mostrarListaProveedores();
        
        // Listener Filtro
        FilteredList<Proveedor> listaFiltrada = new FilteredList<>(proveedoresObsList, s -> true);
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
        
        // Listener Seleccion Proveedor
        proveedoresListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                nifTxt.setText(newSelection.getCifDni());
                nombreTxt.setText(newSelection.getNombre());
                apellidosTxt.setText(newSelection.getApellidos());
                telefonoTxt.setText(newSelection.getTelefono());
                movilTxt.setText(newSelection.getMovil());
                ctaTxt.setText(newSelection.getCtaBanco());
                emailTxt.setText(newSelection.getEmail());
                deshabilitarBotonesEdicion(false);
            }
        });        
        visualizaLista(proveedoresObsList);
        fijarTamanoMaximoConPatron(nifTxt,9,"[0-9A-Z]");
        fijarTamanoMaximoConPatron(nombreTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(apellidosTxt,25,"[A-Za-zñáéíóúàèìòùüçÑÁÉÍÓÚÀÈÌÒÙÜÇ ]");
        fijarTamanoMaximoConPatron(telefonoTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(movilTxt,25,"[0-9+() \\-\\.]");
        fijarTamanoMaximoConPatron(ctaTxt,24,"");
        fijarTamanoMaximoConPatron(emailTxt,50,"[0-9A-Za-zçÇ@\\-\\_\\.]");
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
        if (proveedoresListView.getSelectionModel().getSelectedIndex()>-1){
            Proveedor proveedor=proveedoresListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta(proveedor.getNombre()+" "+proveedor.getApellidos()+" será borrado.")) {
                if (OperativasBD.borrarProveedor(proveedor)){
                    Mensajes.msgInfo("ACCION:", "El borrado del Proveedor ha sido realizado");
                    proveedoresObsList.remove(proveedor);
                    if (proveedoresListView.getSelectionModel().isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarCampos();
                    }else{
                        proveedoresListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado del Proveedor ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO PROVEEDORES", "No ha seleccionado ningún proveedor");
        }
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
            Document documento = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
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
            pdf.GenerarHojaProveedores(proveedoresObsList);
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
          if (!nifTxt.getText().equals("") && !nombreTxt.getText().equals("")){
            Proveedor proveedor = new Proveedor(nifTxt.getText(), nombreTxt.getText(), 
                    apellidosTxt.getText(), telefonoTxt.getText(), movilTxt.getText(), 
                    ctaTxt.getText(),emailTxt.getText()); 
            if (!MODOEDICION){
                if(OperativasBD.insertarProveedor(proveedor)){
                    proveedoresObsList.add(proveedor);
                    ordenarListaProveedores();
                    Mensajes.msgInfo("INSERCION PROVEEDORES", "Proveedor añadido correctamente");
                    visualizaLista(proveedoresObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Proveedor");
                }
            }else{
                if(OperativasBD.modificarProveedor(proveedor)){
                    posicionActual=proveedoresListView.getSelectionModel().getSelectedIndex();
                    if (!filtroTxt.getText().isEmpty()){
                        indice=proveedoresObsList.indexOf(proveedoresListView.getSelectionModel().getSelectedItem());
                    }else{
                        indice=posicionActual;
                    }
                    if (proveedoresListView.getSelectionModel().getSelectedItem().getApellidos().equals(apellidosTxt.getText())
                            && proveedoresListView.getSelectionModel().getSelectedItem().getNombre().equals(nombreTxt.getText())){
                        proveedoresObsList.set(indice, proveedor);
                        proveedoresListView.getSelectionModel().clearSelection();
                        proveedoresListView.getSelectionModel().clearAndSelect(posicionActual);
                    }else{
                        proveedoresObsList.set(indice, proveedor);
                        ordenarListaProveedores();
                        filtroTxt.setText("");
                    }
                    Mensajes.msgInfo("MODIFICAR PROVEEDORES", "Proveedor modificado correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Proveedor");
                }
            }
              habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Nif proveedor o nombre no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=proveedoresListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        proveedoresListView.getSelectionModel().clearSelection(registroActual); 
        proveedoresListView.getSelectionModel().select(registroActual);
    }
    
    /**
     * Método que carga la lista de los proveedores en la lista observable
     */
    private void mostrarListaProveedores() {
        List<Proveedor> listaProveedores=OperativasBD.extraerProveedores();
        proveedoresObsList = FXCollections.observableArrayList(listaProveedores);
        ordenarListaProveedores();
    }
    
    /**
     * Método que ordena la lista de proveedores
     */
    private void ordenarListaProveedores(){
        proveedoresObsList.sort((proveedor1,proveedor2)->proveedor1.toString()
                .toLowerCase().compareTo(proveedor2.toString().toLowerCase()));
        proveedoresListView.setItems(proveedoresObsList);
    }

    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        nifTxt.setText("");
        nombreTxt.setText("");
        apellidosTxt.setText("");
        telefonoTxt.setText("");
        movilTxt.setText("");
        ctaTxt.setText("");
        emailTxt.setText("");
    }
   
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        nifTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        proveedoresListView.setDisable(b);
        filtroTxt.setDisable(b);
        nifTxt.setPromptText(b?"Nif":"");
        nombreTxt.setPromptText(b?"Nombre":"");
        apellidosTxt.setPromptText(b?"Apellidos":"");
        telefonoTxt.setPromptText(b?"Teléfono Fijo":"");
        movilTxt.setPromptText(b?"Teléfono Móvil":"");
        ctaTxt.setPromptText(b?"Cuenta Bancaria":"");
        emailTxt.setPromptText(b?"Email":"");
    }

    /**
     * Método que visualiza la Lista de Proveedores
     * @param lista     Lista de Proveedores
     */
    private void visualizaLista(ObservableList<Proveedor> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        proveedoresListView.setItems(lista);
        proveedoresListView.getSelectionModel().select(0);
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