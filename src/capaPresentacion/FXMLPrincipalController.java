package capaPresentacion;

import capaNegocio.OperativasBD;
import capaNegocio.ShareData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Pantalla Principal de la Aplicación (MENU DE OPCIONES)
 *
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLPrincipalController implements Initializable {
    @FXML private Button configuracionBtn;
    @FXML private Button usuariosBtn;
    @FXML private Button alumnosBtn;
    @FXML private Button profesoresBtn;
    @FXML private Button cursosBtn;
    @FXML private Button asignaturasBtn;
    @FXML private Button aulaBtn;
    @FXML private Button proveedorBtn;
    @FXML private Button matricularBtn;
    @FXML private HBox lineaMenu;
    @FXML private HBox grupoConfig;
    @FXML private HBox grupoDatos;
    @FXML private HBox grupoAcciones;
    @FXML private Label usernameLbl;
    @FXML private Label levelLbl;
    @FXML private Label empresaLbl;
    @FXML private Button planificadorBtn;
    @FXML private Button examinarBtn;
    @FXML private Button acercaDeBtn;
    @FXML private Button gastosBtn;
    @FXML private Button recibosBtn;
    @FXML private Button utilidadesBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ShareData.EMPRESA=OperativasBD.extraerEmpresa();
        showIcons();
        habilitarBloques();
    }  

    /**
     * Acción al ser pulsado el botón USUARIOS
     * @param event  Evento
     */
    @FXML
    private void clickUsuariosBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLUsuarios.fxml", "Usuarios", new Stage(),"/capaPresentacion/resources/icons/usuarios.png",true);
    }
    
    /**
     * Acción al ser pulsado el botón ALUMNOS
     * @param event  Evento
     */
    @FXML
    private void clickAlumnosBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLAlumnos.fxml", "Alumnos", new Stage(),"/capaPresentacion/resources/icons/alumnos.png",true);
    }
    
    /**
     * Acción al ser pulsado el botón CONFIGURACION
     * @param event  Evento
     */
    @FXML
    private void clickConfiguracionBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLEmpresa.fxml", "Datos Empresa", new Stage(),"/capaPresentacion/resources/icons/colegio.png",true);
    }

    /**
     * Acción al ser pulsado el botón PROFESORES
     * @param event  Evento
     */
    @FXML
    private void clickProfesoresBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLProfesores.fxml", "Profesores", new Stage(),"/capaPresentacion/resources/icons/profesores.png",true);
    }

    /**
     * Acción al ser pulsado el botón CURSOS
     * @param event  Evento
     */
    @FXML
    private void clickCursosBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLCursos.fxml", "Cursos", new Stage(),"/capaPresentacion/resources/icons/cursos.png",true);
    }

    /**
     * Acción al ser pulsado el botón ASIGNATURAS
     * @param event  Evento
     */
    @FXML
    private void clickAsignaturasBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLAsignaturas.fxml", "Asignaturas", new Stage(),"/capaPresentacion/resources/icons/asignaturas.png",true);
    }

    /**
     * Acción al ser pulsado el botón AULAS
     * @param event  Evento
     */ 
    @FXML
    private void clickAulaBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLAulas.fxml", "Aulas", new Stage(),"/capaPresentacion/resources/icons/pizarra.png",true);
    }


    /**
     * Acción al ser pulsado el botón PROVEEDORES
     * @param event  Evento
     */
    @FXML
    private void clickProveedorBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLProveedores.fxml", "Proveedores", new Stage(),"/capaPresentacion/resources/icons/proveedores.png",true);
    }

    /**
     * Acción al ser pulsado el botón MATRICULACIONES
     * @param event  Evento
     */
    @FXML
    private void clickMatricularBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLMatriculaciones.fxml", "Matricular", new Stage(),"/capaPresentacion/resources/icons/matricular.png",true);
    }

    /**
     * Acción al ser pulsado el botón PLANIFICADOR
     * @param event  Evento
     */
    @FXML
    private void clickPlanificadorBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLPlanificador.fxml", "Planificador", new Stage(),"/capaPresentacion/resources/icons/planificador.png",true);
    }    
    
    /**
     * Acción al ser pulsado el botón EXAMINAR
     * @param event  Evento
     */
    @FXML
    private void clickExaminarBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLExaminar.fxml", "Examinar", new Stage(),"/capaPresentacion/resources/icons/examinar.png",true);
    }

    /**
     * Acción al ser pulsado el botón ACERCA DE
     * @param event  Evento
     */
    @FXML
    private void clickAcercaDeBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLAcercaDe.fxml", "Acerca De", new Stage(),"/capaPresentacion/resources/icons/acerca.png",true);
    }
    
    /**
     * Acción al ser pulsado el botón GASTOS
     * @param event  Evento
     */
    @FXML
    private void clickGastosBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLGastos.fxml", "Gastos", new Stage(),"/capaPresentacion/resources/icons/gastos.png",true);
    }
    
    
    @FXML
    private void clickRecibosBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLRecibos.fxml", "Recibos", new Stage(),"/capaPresentacion/resources/icons/recibos.png",true);
    
    }
    
    
    @FXML
    private void clickUtilidadesBtn(ActionEvent event) {
        cargadorPantallas.cargarPantalla("resources/fxml/FXMLUtilidades.fxml", "Gastos", new Stage(),"/capaPresentacion/resources/icons/utilidades.png",true);
    }
    
    /**
     * Método que coloca las imágenes en los botones correspondientes
     */
    private void showIcons() {
        String[] icons={"resources/icons/colegio.png","resources/icons/usuarios.png",
            "resources/icons/alumnos.png","resources/icons/profesores.png",
            "resources/icons/cursos.png","resources/icons/asignaturas.png",
            "resources/icons/pizarra.png",
            "resources/icons/proveedores.png","resources/icons/matricular.png",
            "resources/icons/planificador.png","resources/icons/examinar.png",
            "resources/icons/acerca.png","resources/icons/gastos.png",
             "resources/icons/recibos.png","resources/icons/utilidades.png"
            };
        ImageView[] iv=new ImageView[15];
        for (int i=0; i<icons.length; i++){
            iv[i]=new ImageView();
            iv[i].setImage(new Image(getClass().getResource(icons[i]).toExternalForm()));
        }
        configuracionBtn.setText("Configuración");
        configuracionBtn.setGraphic(iv[0]);
        configuracionBtn.setContentDisplay(ContentDisplay.TOP);
        usuariosBtn.setText("Usuarios");
        usuariosBtn.setGraphic(iv[1]);
        usuariosBtn.setContentDisplay(ContentDisplay.TOP);
        alumnosBtn.setText("Alumnos");
        alumnosBtn.setGraphic(iv[2]);
        alumnosBtn.setContentDisplay(ContentDisplay.TOP);
        profesoresBtn.setText("Profesores");
        profesoresBtn.setGraphic(iv[3]);
        profesoresBtn.setContentDisplay(ContentDisplay.TOP);
        cursosBtn.setText("Cursos");
        cursosBtn.setGraphic(iv[4]);
        cursosBtn.setContentDisplay(ContentDisplay.TOP);
        asignaturasBtn.setText("Asignaturas");
        asignaturasBtn.setGraphic(iv[5]);
        asignaturasBtn.setContentDisplay(ContentDisplay.TOP);
        aulaBtn.setText("Aulas");
        aulaBtn.setGraphic(iv[6]);
        aulaBtn.setContentDisplay(ContentDisplay.TOP);
        proveedorBtn.setText("Proveedor");
        proveedorBtn.setGraphic(iv[7]);
        proveedorBtn.setContentDisplay(ContentDisplay.TOP);
        matricularBtn.setText("Matricular");
        matricularBtn.setGraphic(iv[8]);
        matricularBtn.setContentDisplay(ContentDisplay.TOP);
        planificadorBtn.setText("Planificador");
        planificadorBtn.setGraphic(iv[9]);
        planificadorBtn.setContentDisplay(ContentDisplay.TOP);
        examinarBtn.setText("Examinar");
        examinarBtn.setGraphic(iv[10]);
        examinarBtn.setContentDisplay(ContentDisplay.TOP);
        acercaDeBtn.setText("Acerca De");
        acercaDeBtn.setGraphic(iv[11]);
        acercaDeBtn.setContentDisplay(ContentDisplay.TOP);
        gastosBtn.setText("Gastos");
        gastosBtn.setGraphic(iv[12]);
        gastosBtn.setContentDisplay(ContentDisplay.TOP);
        recibosBtn.setText("Recibos");
        recibosBtn.setGraphic(iv[13]);
        recibosBtn.setContentDisplay(ContentDisplay.TOP);
        utilidadesBtn.setText("Utilidades");
        utilidadesBtn.setGraphic(iv[14]);
        utilidadesBtn.setContentDisplay(ContentDisplay.TOP);
        
        
        empresaLbl.setText(ShareData.EMPRESA.getNombre());
        usernameLbl.setText(ShareData.USER.getUsername());
        levelLbl.setText(ShareData.USER.getNivel()+"");
    }

    /**
     * Método que habilita los bloques de acciones según nivel de acceso del usuario
     */
    private void habilitarBloques() {
        switch (ShareData.USER.getNivel()){
            case 1: grupoConfig.setDisable(false);
                    grupoDatos.setDisable(true);
                    grupoAcciones.setDisable(true);
                    break;
            case 2: grupoConfig.setDisable(true);
                    grupoDatos.setDisable(false);
                    grupoAcciones.setDisable(false);
                    break;
            case 3: grupoConfig.setDisable(true);
                    grupoDatos.setDisable(false);
                    grupoAcciones.setDisable(true);
                    break;
            default: break;
        }
    }

    

    
}
