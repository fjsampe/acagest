package capaPresentacion;

import capaNegocio.Asignatura;
import capaNegocio.OperativasBD;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * Formulario para la gestión de asignaturas
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLAsignaturasController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<Asignatura> asignaturasListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField codigoTxt;
    @FXML private TextField descripcionTxt;
    @FXML private TextField cargaHorasTxt;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button printBtn;
    private ObservableList<Asignatura> asignaturasObsList;
    private boolean MODOEDICION=false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        mostrarListaAsignaturas();

        // Listener Filtro
        FilteredList<Asignatura> listaFiltrada = new FilteredList<>(asignaturasObsList, s -> true);
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
        
        // Listener Selección Asignaturas
        asignaturasListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                codigoTxt.setText(newSelection.getCodigo());
                descripcionTxt.setText(newSelection.getNombre());
                cargaHorasTxt.setText(newSelection.getCargaHoras()+"");
                deshabilitarBotonesEdicion(false);
            }
        });        
        
        visualizaLista(asignaturasObsList);
        fijarTamanoMaximoConPatron(codigoTxt,9,"[0-9A-ZÑÇ\\-\\_]");
        fijarTamanoMaximoConPatron(descripcionTxt,40,"");
        fijarTamanoMaximoConPatron(cargaHorasTxt,4,"[0-9]");
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
     * @param event Evento 
     */
    @FXML
    private void clickDeleteBtn(ActionEvent event) {
        if (asignaturasListView.getSelectionModel().getSelectedIndex()>-1){
            Asignatura asignatura=asignaturasListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta("Borrado elemento", asignatura.getNombre()+"("+asignatura.getCodigo()+") será borrado.","¿Quieres borrar el elemento?")) {
                if (OperativasBD.borrarAsignatura(asignatura)){
                    Mensajes.msgInfo("ACCION:", "El borrado de la Asignatura ha sido realizado");
                    asignaturasObsList.remove(asignatura);
                    if (asignaturasListView.getSelectionModel().isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarCampos();
                    }else{
                        asignaturasListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado de la Asignatura ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO ASIGNATURAS", "No ha seleccionado ninguna asignatura");
        }
    }
    
    /**
     * Acción al ser pulsado el botón IMPRIMIR
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
            pdf.GenerarHojaAsignaturas(asignaturasObsList);
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
     * @param event Evento 
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int indice, posicionActual;
        if (cargaHorasTxt.getText().equals("")) cargaHorasTxt.setText("0");
        if (!codigoTxt.getText().equals("") && !descripcionTxt.getText().equals("")
                && Integer.parseInt(cargaHorasTxt.getText())>0){
            Asignatura asignatura = new Asignatura(codigoTxt.getText(), 
                    descripcionTxt.getText(),Integer.parseInt(cargaHorasTxt.getText()));
            if (!MODOEDICION){
                if(OperativasBD.insertarAsignatura(asignatura)){
                    asignaturasObsList.add(asignatura);
                    ordenarListaAsignaturas();
                    Mensajes.msgInfo("INSERCION ASIGNATURA", "Asignatura añadida correctamente");
                    visualizaLista(asignaturasObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Asignatura");
                }
            }else{
                if(OperativasBD.modificarAsignatura(asignatura)){
                    posicionActual=asignaturasListView.getSelectionModel().getSelectedIndex();
                    if (!filtroTxt.getText().isEmpty()){
                        indice=asignaturasObsList.indexOf(asignaturasListView.getSelectionModel().getSelectedItem());
                    }else{
                        indice=posicionActual;
                    }
                    if (asignaturasListView.getSelectionModel().getSelectedItem().getCodigo().equals(codigoTxt.getText())
                            && asignaturasListView.getSelectionModel().getSelectedItem().getNombre().equals(descripcionTxt.getText())){
                        asignaturasObsList.set(indice, asignatura);
                        asignaturasListView.getSelectionModel().clearSelection();
                        asignaturasListView.getSelectionModel().clearAndSelect(posicionActual);
                    }else{
                        asignaturasObsList.set(indice, asignatura);
                        ordenarListaAsignaturas();
                        filtroTxt.setText("");
                    }
                    Mensajes.msgInfo("MODIFICAR ASIGNATURAS", "Asignatura modificada correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Asignatura");
                }
            }
            habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Código asignatura, nombre asignatura o carga de horas no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=asignaturasListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        asignaturasListView.getSelectionModel().clearSelection(registroActual); 
        asignaturasListView.getSelectionModel().select(registroActual);
    }

    /**
     * Método que carga la lista de las asignaturas en la lista observable
     */
    private void mostrarListaAsignaturas() {
        List<Asignatura> listaAsignaturas=OperativasBD.extraerAsignaturas(null);
        asignaturasObsList = FXCollections.observableArrayList(listaAsignaturas);
        ordenarListaAsignaturas();
    }
    
    /**
     * Método que ordena la lista de asignaturas
     */
    private void ordenarListaAsignaturas(){
        asignaturasObsList.sort((asignatura1,asignatura2)->asignatura1.toString().toLowerCase().compareTo(asignatura2.toString().toLowerCase()));
        asignaturasListView.setItems(asignaturasObsList);
    }
    
    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        codigoTxt.setText("");
        descripcionTxt.setText("");
        cargaHorasTxt.setText("0");
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        codigoTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        asignaturasListView.setDisable(b);
        filtroTxt.setDisable(b);
        codigoTxt.setPromptText(b?"Código de la Asignatura":"");
        descripcionTxt.setPromptText(b?"Nombre de la Asignatura":"");
        cargaHorasTxt.setPromptText(b?"Carga Horas de la Asignatura":"0");
        codigoTxt.setTooltip(new Tooltip("Introduzca código de asignatura"));
        descripcionTxt.setTooltip(new Tooltip("Introduzca el nombre de la asignatura"));
        cargaHorasTxt.setTooltip(new Tooltip("Introduzca la carga de horas de la asignatura"));
    }
    
    /**
     * Método que visualiza la Lista de Asignaturas
     * @param lista     Lista de Asignaturas
     */
    private void visualizaLista(ObservableList<Asignatura> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        asignaturasListView.setItems(lista);
        asignaturasListView.getSelectionModel().select(0);
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
