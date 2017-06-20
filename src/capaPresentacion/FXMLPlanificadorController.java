package capaPresentacion;

import capaNegocio.Asignatura;
import capaNegocio.Aula;
import capaNegocio.FichaPlanificador;
import capaNegocio.OperativasBD;
import capaNegocio.Plan;
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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Formulario para la gestión de planes de horarios/aulas/asignaturas
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.2
 */
public class FXMLPlanificadorController implements Initializable {

    @FXML private TextField filtroTxt;
    @FXML private ListView<FichaPlanificador> profesoresListView;
    @FXML private VBox formularioDatos;
    @FXML private HBox formularioDatosAlumno;
    @FXML private TextField nifTxt;
    @FXML private TextField nombreTxt;
    @FXML private TextField domicilioTxt;
    @FXML private TextField poblacionTxt;
    @FXML private TextField cpTxt;
    @FXML private TextField provinciaTxt;
    @FXML private TextField telefonosTxt;
    @FXML private TextField emailTxt;
    @FXML private ImageView fotoImg;
    @FXML private HBox formularioDatosMatriculaciones;
    @FXML private ComboBox<Asignatura> asignaturaCmb;
    @FXML private ComboBox<Aula> aulaCmb;
    @FXML private TableView<Plan> planificacionTable;
    @FXML private TableColumn<Asignatura, String> planificadorAsignaturaCol;
    @FXML private TableColumn<Asignatura, String> planificadorDescripcionAsignaturaCol;
    @FXML private TableColumn<Aula, String> planificadorAulaCol;
    @FXML private TableColumn<Aula, String> planificadorDescripcionAulaCol;
    @FXML private TableColumn<Plan, DayOfWeek> diaSemanaCol;
    @FXML private TableColumn<Plan, LocalTime> deHoraCol;
    @FXML private TableColumn<Plan, LocalTime> aHoraCol;
    @FXML private ComboBox<String> diaSemanaCmb;
    @FXML private Spinner<Integer> deHoraSpin;
    @FXML private Spinner<Integer> deMinutosSpin;
    @FXML private Spinner<Integer> aHoraSpin;
    @FXML private Spinner<Integer> aMinutosSpin;
    @FXML private Button addPlanBtn;
    @FXML private Button delPlanBtn;
    @FXML private HBox barraConfirmacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private HBox barraOpciones;
    @FXML private Button editBtn;
    @FXML private Button printBtn;
    @FXML private Button estadoBtn;

    // Lista observable de asignaturas para el ComboBOX
    private ObservableList<Asignatura> asignaturasObsList;
    // Lista Observable para las Aulas para el ComboBox
    private ObservableList<Aula> aulasObsList;
    // Lista Observable de Profesores para el ListView
    private ObservableList<FichaPlanificador> profesoresObsList;
    // Lista Observable de Matriculaciones de un alumno
    private ObservableList<Plan> planesObsList= FXCollections.observableArrayList();
    
    private byte[] imagen;
    private FichaPlanificador datosProfesorSeleccionado;
    private List<Plan> planesInicial;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        showIconos();
        cargarListaAsignaturas();
        cargarListaAulas();
        cargarListaProfesores();
        inicializarSpinersYComboSemanal();
        inicializarTablaPlanes();
        
        // Listener Filtro
        FilteredList<FichaPlanificador> listaFiltrada = new FilteredList<>(profesoresObsList, s -> true);
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
        
        // Listener Selección Profesor
        profesoresListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                datosProfesorSeleccionado=newSelection;
                nifTxt.setText(datosProfesorSeleccionado.getProfesor().getCifDni());
                nombreTxt.setText(datosProfesorSeleccionado.getProfesor().getApellidos()+", "+
                        datosProfesorSeleccionado.getProfesor().getNombre());
                domicilioTxt.setText(datosProfesorSeleccionado.getProfesor().getDomicilio());
                poblacionTxt.setText(datosProfesorSeleccionado.getProfesor().getPoblacion());
                cpTxt.setText(datosProfesorSeleccionado.getProfesor().getCp());
                provinciaTxt.setText(datosProfesorSeleccionado.getProfesor().getProvincia());
                telefonosTxt.setText(datosProfesorSeleccionado.getProfesor().getTelefono()+" / "+
                        datosProfesorSeleccionado.getProfesor().getMovil());
                emailTxt.setText(datosProfesorSeleccionado.getProfesor().getEmail());
                imagen=datosProfesorSeleccionado.getProfesor().getFoto();
                fotoImg.setImage(convertirImagen(imagen));
                fotoImg.setFitHeight(100);
                fotoImg.setFitWidth(100);
                fotoImg.setPreserveRatio(false);
                fotoImg.setSmooth(true);
                planesObsList=FXCollections.observableList(datosProfesorSeleccionado.getListaPlanes());
                planesInicial=new ArrayList<>();
                planesInicial.addAll(planesObsList); 
                planificacionTable.setItems(planesObsList);               
            }
        }); 
        profesoresListView.getSelectionModel().selectFirst();
    }    

    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        int posicionActual,indice;
        posicionActual=profesoresListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=profesoresObsList.indexOf(profesoresListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosProfesorSeleccionado.setListaPlanes(planesObsList);
        profesoresObsList.set(indice, datosProfesorSeleccionado);
        planesInicial.clear();
        planesInicial.addAll(planesObsList);
        if (OperativasBD.borrarPlanes(datosProfesorSeleccionado.getProfesor().getCifDni()) && 
            OperativasBD.insertarPlanes(datosProfesorSeleccionado)){
                Mensajes.msgInfo("MODIFICACION PLANES", "Los planes se han grabado correctamente");
        }else{
                Mensajes.msgError("MODIFICACION PLANES", "Los planes no se han grabado correctamente");
        }
        habilitarCamposEdicion(false);
        limpiarCombos();
        visualizaLista(profesoresObsList);
        profesoresListView.getSelectionModel().select(indice);
    }

    /**
     * Acción al ser pulsado el botón de CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickBtnCancelar(ActionEvent event) {
        int registroActual=profesoresListView.getSelectionModel().getSelectedIndex();
        int posicionActual,indice;
        posicionActual=profesoresListView.getSelectionModel().getSelectedIndex();
        if (!filtroTxt.getText().isEmpty()){
            indice=profesoresObsList.indexOf(profesoresListView.getSelectionModel().getSelectedItem());
        }else{
            indice=posicionActual;
        }
        datosProfesorSeleccionado.setListaPlanes(planesInicial);
        planesObsList=FXCollections.observableList(planesInicial);
        profesoresObsList.set(indice, datosProfesorSeleccionado);        
        habilitarCamposEdicion(false);
        profesoresListView.getSelectionModel().clearSelection(registroActual);
        profesoresListView.getSelectionModel().select(registroActual);
        limpiarCombos();
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
            pdf.GenerarHojaPlanes(profesoresListView.getSelectionModel().getSelectedItem());
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
     * Acción al ser pulsado el botón ESTADO 
     * @param event Evento
     */
    @FXML
    private void clickEstadoBtn(ActionEvent event) {
        double cant1,cant2;
        List<Plan> listaOrdenadaDias=new ArrayList<>();
        listaOrdenadaDias.addAll(planesObsList);
        listaOrdenadaDias.sort((plan1,plan2)->plan1.toString().compareTo(plan2.toString()));
        LocalTime inicio;
        ArrayList<Double>[] group = new ArrayList[7];
        int numeroSeries=0;
        for (int i=1; i<8; i++){
            group[i-1]=new ArrayList<Double>();
            inicio=LocalTime.parse("00:00");
            for (Plan p:listaOrdenadaDias){
                if (p.getDiaSemana().getValue()==i){
                    if(inicio.isBefore(p.getDeHora())){
                        group[i-1].add(((int) ChronoUnit.MINUTES.between(inicio,p.getDeHora()))/60.00);
                    }else{
                        group[i-1].add(0.0);
                    }
                    cant1=p.getAHora().getHour()+(p.getAHora().getMinute()/60);
                    cant2=p.getDeHora().getHour()+(p.getDeHora().getMinute()/60);
                    group[i-1].add(((int) ChronoUnit.MINUTES.between(p.getDeHora(),p.getAHora()))/60.00);
                    inicio=p.getAHora();
                }
            }
            if (inicio==LocalTime.parse("00:00")){
                group[i-1].add(0.0);
            }//else{
              //  group[i-1].add(((int) ChronoUnit.MINUTES.between(LocalTime.parse("23:59"),inicio))/60.00);
                    
            //}
        }
        
        for (int i=0;i<7;i++){
            numeroSeries=(group[i].size()>numeroSeries?group[i].size():numeroSeries); 
        }
        
        Stage stage=new Stage();
        Scene scene=new Scene(new Group());
        stage.setTitle("ESTADO HORARIO");
        stage.setWidth(550);
        stage.setHeight(450);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dias de la Semana");
        NumberAxis yAxis = new NumberAxis("Y Axis", 0.0d, 24.0d, 1.0d);
        
        yAxis.setLabel("Hora");
        StackedBarChart stackedBarChart = new StackedBarChart(xAxis, yAxis);
        
        XYChart.Series[] datasSeries=new XYChart.Series[numeroSeries];
        stackedBarChart.setLegendVisible(false);
        for(int i=0; i<numeroSeries; i++){
            datasSeries[i]=new XYChart.Series();
            //datasSeries[i].setName("XYChart.Series "+i+1);
            for (int j=0; j<7; j++){
                if(group[j].size()>i)
                    datasSeries[i].getData().add(new XYChart.Data(convierteDia(j+1),group[j].get(i)));
            }
            stackedBarChart.getData().add(datasSeries[i]);
        }
        ((Group)scene.getRoot()).getChildren().add(stackedBarChart);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        scene.getStylesheets().add("capaPresentacion/resources/css/styleBar.css");
        stage.show();
    }
    
    
    /**
     * Acción al ser pulsado el botón AÑADIR plan
     * @param event  Evento
     */
    @FXML
    private void clickAddPlanBtn(ActionEvent event) {
        LocalTime de=LocalTime.parse(String.format("%02d", deHoraSpin.getValue())+
                ":"+String.format("%02d", deMinutosSpin.getValue()));
        LocalTime a=LocalTime.parse(String.format("%02d", aHoraSpin.getValue())+
                ":"+String.format("%02d", aMinutosSpin.getValue()));
        if (asignaturaCmb.getSelectionModel().isEmpty() ||
            aulaCmb.getSelectionModel().isEmpty() ||
            diaSemanaCmb.getSelectionModel().isEmpty() ||
            a.compareTo(de)<=0){
                Mensajes.msgInfo("VALIDACION PLAN", "Los campos Asignatura "
                    + "Aula y Día Semana deben estar rellenos y las Horas correctas.");
        }else{
            if (validaHorario(aulaCmb.getSelectionModel().getSelectedItem(), DayOfWeek.of(diaSemanaCmb.getSelectionModel().getSelectedIndex()+1),de,a)){
                Plan p=new Plan(asignaturaCmb.getSelectionModel().getSelectedItem(),
                    aulaCmb.getSelectionModel().getSelectedItem(),
                    DayOfWeek.of(diaSemanaCmb.getSelectionModel().getSelectedIndex()+1),
                    de,a);
                planesObsList.add(p);
                planificacionTable.getSelectionModel().select(p);
                planificacionTable.scrollTo(p);
                limpiarCombos();
                if (btnAceptar.isDisable()) btnAceptar.setDisable(false);
            }
        }
    }
    
    /**
     * Acción al ser pulsado el botón para QUITAR planes de horarios
     * @param event  Evento
     */
    @FXML
    private void clickDelPlanBtn(ActionEvent event) {
        Plan p=planificacionTable.getSelectionModel().getSelectedItem();
        planesObsList.remove(p);
        planificacionTable.setItems(planesObsList);
        if (btnAceptar.isDisable()) btnAceptar.setDisable(false);
    }
    
    /**
     * Método que cargar la lista de profesores en una lista observable
     */
    private void cargarListaProfesores(){
        List<FichaPlanificador> listaProfesores=OperativasBD.extraerFichasPlanificador();
        profesoresObsList = FXCollections.observableArrayList(listaProfesores);
        ordenarListaProfesores();
    }
    
    /**
     * Método que ordena la lista de profesores
     */
    private void ordenarListaProfesores(){
        profesoresObsList.sort((profesor1,profesor2)->profesor1.toString().compareTo(profesor2.toString()));
        profesoresListView.setItems(profesoresObsList);
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
     * Método que carga la lista de aulas
     */
    private void cargarListaAulas() {
        aulasObsList=FXCollections.observableArrayList(OperativasBD.extraerAulas());
        ordenarListaAulas();
    }
    
    /**
     * Ordena la lista de aulas
     */
    private void ordenarListaAulas(){
        aulasObsList.sort((aula1,aula2)->aula1.toString().compareTo(aula2.toString()));
        aulaCmb.setItems(aulasObsList);
    }
    
    /**
     * Muestra los iconos en los botones del formulario
     */
    private void showIconos() {
        String[] icons={"resources/icons/edit.png","resources/icons/print.png",
            "resources/icons/planner.png",
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
        estadoBtn.setText("Estado");
        estadoBtn.setGraphic(iv[2]);
        estadoBtn.setContentDisplay(ContentDisplay.TOP);
        addPlanBtn.setGraphic(iv[3]);
        addPlanBtn.setContentDisplay(ContentDisplay.TOP);
        delPlanBtn.setGraphic(iv[4]);
        delPlanBtn.setContentDisplay(ContentDisplay.TOP); 
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
        profesoresListView.setDisable(b);
        barraOpciones.setDisable(b);
        formularioDatosAlumno.setDisable(true);
        formularioDatosMatriculaciones.setDisable(!b);
        barraConfirmacion.setDisable(!b);
        btnAceptar.setDisable(true);
        filtroTxt.setDisable(b);
    }
    
    /**
     * Método que visualiza los alumnos en pantalla. Carga el ListView
     * @param lista     Lista de los alumnos
     */
    private void visualizaLista(ObservableList<FichaPlanificador> lista){
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
        }
        profesoresListView.setItems(lista);
        profesoresListView.getSelectionModel().selectFirst();
    }
    
    /**
     * Método que inicializa la tabla de matriculaciones del alumno
     */
    private void inicializarTablaPlanes() {
        planificacionTable.setPlaceholder(new Label ("Profesor sin planificación...."));
        planificadorAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("CodigoAsignatura"));
        planificadorDescripcionAsignaturaCol.setCellValueFactory(new PropertyValueFactory<>("DescripcionAsignatura"));
        planificadorAulaCol.setCellValueFactory(new PropertyValueFactory<>("CodigoAula"));
        planificadorDescripcionAulaCol.setCellValueFactory(new PropertyValueFactory<>("DescripcionAula"));
        diaSemanaCol.setCellValueFactory(new PropertyValueFactory<>("DiaSemanaSpanish"));
        deHoraCol.setCellValueFactory(new PropertyValueFactory<>("DeHora"));
        aHoraCol.setCellValueFactory(new PropertyValueFactory<>("AHora"));
    }

    /**
     * Método que limpia los combos y los deja sin nada selecciondo
     */
    private void limpiarCombos(){
        asignaturaCmb.setValue(null);
        aulaCmb.setValue(null);
        diaSemanaCmb.setValue(null);
        deHoraSpin.getValueFactory().setValue(Integer.MIN_VALUE);
        aHoraSpin.getValueFactory().setValue(Integer.MIN_VALUE);
        deMinutosSpin.getValueFactory().setValue(Integer.MIN_VALUE);
        aMinutosSpin.getValueFactory().setValue(Integer.MIN_VALUE);
    }
    
    /**
     * Función que valida un horario seleccionado con los horarios introducidos
     * @param aula      Aula    
     * @param diaSemana Dia de la semana
     * @param de        Hora inicio
     * @param hasta     Hora fín
     * @return Devuelve True=horario válido, False=horario no válido
     */
    private boolean validaHorario(Aula aula,DayOfWeek diaSemana, LocalTime de, LocalTime hasta) {
        String cadenaMensaje="Este horario ya se está utilizando por: \n";
        boolean estado=true;
        if (de.isBefore(hasta)){
            for (Plan plan: planesObsList){
                if(diaSemana.equals(plan.getDiaSemana()) &&
                    (de.isBefore(plan.getAHora()) 
                        && hasta.isAfter(plan.getDeHora()))){
                    estado=false;
                    cadenaMensaje="El rango de hora ya está siendo utilizado en este profesor.";
                }
            }
        }else{
            estado=false;
            cadenaMensaje="Hora de finalización debe ser mayor que la hora de inicio.";
        }
        
        // Valida con otros profesores
        // Si hasta ahora es correcto hay que verificar con todos los profesores
        // que el aula no esté ocupada en ese horario.
        if (estado){
            for (FichaPlanificador f:profesoresObsList){
                for (Plan p:f.getListaPlanes()){
                    if(p.getAula().equals(aula) && diaSemana.equals(p.getDiaSemana()) &&
                        (de.isBefore(p.getAHora()) && hasta.isAfter(p.getDeHora()))){
                        estado=false;
                        cadenaMensaje=cadenaMensaje+"Profesor: "+f.getProfesor().getApellidos()+
                            ", "+f.getProfesor().getNombre()+", en el aula: "+
                            p.getDescripcionAula()+", dia de la semana "+p.getDiaSemanaSpanish()+
                            " y en horario de "+p.getDeHora()+" a "+p.getAHora()+"\n";
                    }
                }
            }
        }
        
        if (!estado){
            Mensajes.msgError("PROBLEMA CON HORARIOS", cadenaMensaje);
        }
        return estado;
    }

    /**
     * Método para inicializar los Spinners y el combo con los dias de la semana
     */
    private void inicializarSpinersYComboSemanal() {
        diaSemanaCmb.setItems(
            FXCollections.observableArrayList("Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"));
        SpinnerValueFactory<Integer>[] valueFactoryHoras=new SpinnerValueFactory[2];
        SpinnerValueFactory<Integer>[] valueFactoryMinutos=new SpinnerValueFactory[2]; 
        for (int i=0; i<2; i++){
            valueFactoryHoras[i] = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
            valueFactoryMinutos[i] = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        }
        deHoraSpin.setValueFactory(valueFactoryHoras[0]);
        aHoraSpin.setValueFactory(valueFactoryHoras[1]);
        deMinutosSpin.setValueFactory(valueFactoryMinutos[0]);
        aMinutosSpin.setValueFactory(valueFactoryMinutos[1]);
    }

    /**
     * Función que convierte el número del día de la semana en el texto del dia
     * @param dia   Número del día de la semana
     * @return      Devuelve el texto del día de la semana
     */
    private String convierteDia(int dia) {
        String cadena="";
        switch(dia){
            case 1: cadena="Lunes";
                    break;
            case 2: cadena="Martes";
                    break;
            case 3: cadena="Miércoles";
                    break;
            case 4: cadena="Jueves";
                    break;
            case 5: cadena="Viernes";
                    break;
            case 6: cadena="Sábado";
                    break;
            case 7: cadena="Domingo";
                    break;
            default: cadena="Error Dia";
                    break;
        }
        return cadena;
    }
}
