package capaPresentacion;

import capaNegocio.Aula;
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
 * Formulario para la gestión de aulas
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLAulasController implements Initializable {
    @FXML private TextField filtroTxt;
    @FXML private ListView<Aula> aulasListView;
    @FXML private VBox formularioDatos;
    @FXML private TextField codigoTxt;
    @FXML private TextField descripcionTxt;
    @FXML private TextField ubicacionTxt;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button printBtn;
    private ObservableList<Aula> aulasObsList;
    private boolean MODOEDICION=false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        mostrarListaAulas();

        // Listener Filtro
        FilteredList<Aula> listaFiltrada = new FilteredList<>(aulasObsList, s -> true);
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
        
        // Listener Selección Aula
        aulasListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                codigoTxt.setText(newSelection.getCodigo());
                descripcionTxt.setText(newSelection.getDescripcion());
                ubicacionTxt.setText(newSelection.getUbicacion());
                deshabilitarBotonesEdicion(false);
            }
        });        
        
        visualizaLista(aulasObsList);
        fijarTamanoMaximoConPatron(codigoTxt,9,"[0-9A-ZÑ]");
        fijarTamanoMaximoConPatron(descripcionTxt,30,"");
        fijarTamanoMaximoConPatron(ubicacionTxt,30,"");
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
     * Acción al ser pulsado el botón para EDITAR
     * @param event  Evento
     */
    @FXML
    private void clickEditBtn(ActionEvent event) {
        MODOEDICION=true;
        habilitarCamposEdicion(true);
    }
    
    /**
     * Acción al ser pulsado el botón para BORRAR
     * @param event  Evento
     */
    @FXML
    private void clickDeleteBtn(ActionEvent event) {
        if (aulasListView.getSelectionModel().getSelectedIndex()>-1){
            Aula aula=aulasListView.getSelectionModel().getSelectedItem();
            if (Mensajes.msgPregunta("Borrar elemento",aula.getDescripcion()+"("+aula.getCodigo()+") será borrada.","¿Quieres borrar el elemento?")) {
                if (OperativasBD.borrarAula(aula)){
                    Mensajes.msgInfo("ACCION:", "El borrado del Aula ha sido realizado");
                    aulasObsList.remove(aula);
                    if (aulasObsList.isEmpty()){
                        deshabilitarBotonesEdicion(true);
                        inicializarCampos();
                    }else{
                        aulasListView.getSelectionModel().clearAndSelect(0);
                    }
                }else{
                    Mensajes.msgError("ACCION:", "El borrado del Aula ha fallado");
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO AULAS", "No ha seleccionado ninguna aula");
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
            pdf.GenerarHojaAulas(aulasObsList);
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
     * Acción al ser pulsado el botón para ACEPTAR
     * @param event Evento 
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int indice, posicionActual;
        if (!codigoTxt.getText().equals("") && !descripcionTxt.getText().equals("")){
            Aula aula = new Aula(codigoTxt.getText(), descripcionTxt.getText(), ubicacionTxt.getText());
            if (!MODOEDICION){
                if(OperativasBD.insertarAula(aula)){
                    aulasObsList.add(aula);
                    ordenarListaAulas();
                    Mensajes.msgInfo("INSERCION AULA", "Aula añadida correctamente");
                    visualizaLista(aulasObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Aula");
                }
            }else{
                if(OperativasBD.modificarAula(aula)){
                    posicionActual=aulasListView.getSelectionModel().getSelectedIndex();
                    if (!filtroTxt.getText().isEmpty()){
                        indice=aulasObsList.indexOf(aulasListView.getSelectionModel().getSelectedItem());
                    }else{
                        indice=posicionActual;
                    }
                    if (aulasListView.getSelectionModel().getSelectedItem().getCodigo().equals(codigoTxt.getText())
                            && aulasListView.getSelectionModel().getSelectedItem().getDescripcion().equals(descripcionTxt.getText())){
                        aulasObsList.set(indice, aula);
                        aulasListView.getSelectionModel().clearSelection();
                        aulasListView.getSelectionModel().clearAndSelect(posicionActual);
                    }else{
                        aulasObsList.set(indice, aula);
                        ordenarListaAulas();
                        filtroTxt.setText("");
                    }
                    Mensajes.msgInfo("MODIFICAR AULAS", "Aula modificada correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Aula");
                }
            }
            habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Código aula o descripcion aula no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón de CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=aulasListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        aulasListView.getSelectionModel().clearSelection(registroActual); 
        aulasListView.getSelectionModel().select(registroActual);
    }

    /**
     * Método que carga la lista de las aulas en la lista observable
     */
    private void mostrarListaAulas() {
        List<Aula> listaAulas=OperativasBD.extraerAulas();
        aulasObsList = FXCollections.observableArrayList(listaAulas);
        ordenarListaAulas();
    }
    
    /**
     * Método que ordena la lista de aulas
     */
    private void ordenarListaAulas(){
        aulasObsList.sort((aula1,aula2)->aula1.toString().toLowerCase().compareTo(aula2.toString().toLowerCase()));
        aulasListView.setItems(aulasObsList);
    }
    
    /**
     * Método que inicializa los campos del formulario a blanco o nulos
     */
    private void inicializarCampos() {
        codigoTxt.setText("");
        descripcionTxt.setText("");
        ubicacionTxt.setText("");
    }
    
    /**
     * Método que habilita o deshabilita los campos del formulario
     * @param b     True=habilita campos False=deshabilita campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        codigoTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        aulasListView.setDisable(b);
        filtroTxt.setDisable(b);
        codigoTxt.setPromptText(b?"Código del Aula":"");
        descripcionTxt.setPromptText(b?"Descripción del Aula":"");
        ubicacionTxt.setPromptText(b?"Ubicación del Aula":"");
        codigoTxt.setTooltip(new Tooltip("Introduzca código del aula"));
        descripcionTxt.setTooltip(new Tooltip("Introduzca la descripción del aula"));
        ubicacionTxt.setTooltip(new Tooltip("Introduzca la ubicación del aula"));
    }

    /**
     * Método que visualiza la Lista de Aulas
     * @param lista     Lista de Aulas
     */
    private void visualizaLista(ObservableList<Aula> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        aulasListView.setItems(lista);
        aulasListView.getSelectionModel().select(0);
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
