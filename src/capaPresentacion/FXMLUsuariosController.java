package capaPresentacion;

import capaNegocio.OperativasBD;
import capaNegocio.Usuario;
import static capaPresentacion.resources.Campos.fijarTamanoMaximoConPatron;
import capaPresentacion.resources.Mensajes;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * Formulario gestión de usuarios
 *
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class FXMLUsuariosController implements Initializable {
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private TextField usuarioTxt;
    @FXML private ComboBox<String> nivelCombo;
    @FXML private PasswordField passwordTxt;
    @FXML private VBox formularioDatos;
    @FXML private HBox barraOpciones;
    @FXML private Button aceptarBtn;
    @FXML private Button cancelarBtn;
    @FXML private ListView<Usuario> usuariosListView;
    private ObservableList<Usuario> usuariosObsList; 
    private boolean MODOEDICION=false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitarCamposEdicion(false);
        nivelCombo.setItems(
            FXCollections.observableArrayList( "Nivel 1 - Administrador", "Nivel 2 - Gestor", "Nivel 3 - Usuario"));
        showIcons();
        mostrarListaUsuarios();
        // Listener Usuarios
        usuariosListView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection !=null){
                usuarioTxt.setText(newSelection.getUsername());
                passwordTxt.setText(newSelection.getPassword());
                nivelCombo.getSelectionModel().clearAndSelect(newSelection.getNivel()-1);
                deshabilitarBotonesEdicion(false);
            }
        });      
        visualizaLista(usuariosObsList);
        fijarTamanoMaximoConPatron(usuarioTxt,10,"[0-9a-z]");
        fijarTamanoMaximoConPatron(passwordTxt,64,"");
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
     * Acción al ser pulsado el botón EDICIÓN
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
        if (usuariosListView.getSelectionModel().getSelectedIndex()>-1){
            Usuario usuario=usuariosListView.getSelectionModel().getSelectedItem();
            if (usuario.getUsername().equals("admin")){
                Mensajes.msgInfo("ATENCION:", "El usuario administrador no puede ser borrado");
            }else{
                if (Mensajes.msgPregunta("Borrado elemento",usuario.getUsername()+" será borrado.","¿Quieres borrar el elemento?")) {
                    if (OperativasBD.borrarUsuario(usuario)){
                        Mensajes.msgInfo("ACCION:", "El borrado del Usuario ha sido realizado");
                        usuariosObsList.remove(usuario);
                        if(usuariosListView.getSelectionModel().isEmpty()){
                            deshabilitarBotonesEdicion(true);
                            inicializarCampos();
                        }else{
                            usuariosListView.getSelectionModel().clearAndSelect(0);
                        }
                    }else{
                        Mensajes.msgError("ACCION:", "El borrado del Usuario ha fallado");
                    }
                }
            }
        }else{
            Mensajes.msgInfo("BORRADO USUARIOS", "No ha seleccionado ningún usuario");
        }
    }
    
    /**
     * Acción al ser pulsado el botón ACEPTAR
     * @param event  Evento
     */
    @FXML
    private void clickAceptarBtn(ActionEvent event) {
        int indice;
        if (!usuarioTxt.getText().equals("") && !passwordTxt.getText().equals("") && nivelCombo.getValue()!=null){
            Usuario usuario = new Usuario(usuarioTxt.getText(), 
                    passwordTxt.getText(), nivelCombo.getSelectionModel().getSelectedIndex()+1);
            if (!MODOEDICION){
                if(OperativasBD.insertarUsuario(usuario)){
                    usuariosObsList.add(usuario);
                    ordenarListaUsuarios();
                    Mensajes.msgInfo("INSERCION USUARIOS", "Usuario añadido correctamente");
                    visualizaLista(usuariosObsList);
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al insertar Usuario");
                }
            }else{
                if(OperativasBD.modificarUsuario(usuario)){
                    indice=usuariosListView.getSelectionModel().getSelectedIndex();
                    usuariosObsList.set(indice, usuario);
                    ordenarListaUsuarios();
                    Mensajes.msgInfo("MODIFICAR USUARIOS", "Usuario modificado correctamente");
                }else{
                    Mensajes.msgError("ERROR SQL", "Error al modificar Usuario");
                }
            }
            habilitarCamposEdicion(false);
        }else{
            Mensajes.msgError("ERROR", "Usuario, contraseña o nivel no puede estar vacio");
        }
    }

    /**
     * Acción al ser pulsado el botón CANCELAR
     * @param event  Evento
     */
    @FXML
    private void clickCancelarbtn(ActionEvent event) {
        int registroActual=usuariosListView.getSelectionModel().getSelectedIndex();
        habilitarCamposEdicion(false);
        usuariosListView.getSelectionModel().select(0);   
        usuariosListView.getSelectionModel().select(registroActual);   
    }
    
    /**
     * Método que muestra en el list view del formulario la lista de usuarios
     */
    private void mostrarListaUsuarios() {
        List<Usuario> listaUsuarios=OperativasBD.extraerUsuarios();
        usuariosObsList = FXCollections.observableArrayList(listaUsuarios);
        ordenarListaUsuarios();
    }
    
    /**
     * Método que ordena la lista de usuarios
     */
    private void ordenarListaUsuarios() {
        usuariosObsList.sort((usuario1,usuario2)->usuario1.toString().toLowerCase()
            .compareTo(usuario2.toString().toLowerCase()));
        usuariosListView.setItems(usuariosObsList);
    }
    
    /**
     * Método que inicializa los campos del formulario
     */
    private void inicializarCampos() {
        usuarioTxt.setText("");
        passwordTxt.setText("");
        nivelCombo.setValue(null);
    }
    
    /**
     * Habilita/Deshabilita campos para su edición
     * @param b True=Habilita los Campos False=Deshabilita los campos
     */
    private void habilitarCamposEdicion(boolean b) {
        formularioDatos.setDisable(!b);
        usuarioTxt.setDisable(MODOEDICION);
        barraOpciones.setDisable(b);
        usuariosListView.setDisable(b);
        usuarioTxt.setPromptText(b?"Nombre de usuario":"");
        passwordTxt.setPromptText(b?"Password":"");
        usuarioTxt.setTooltip(new Tooltip("Introduzca el nombre de usuario"));
        passwordTxt.setTooltip(new Tooltip("Introduzca la contraseña de usuario"));
    }
    
    /**
     * Método que visualiza la Lista de usuarios
     * @param lista     Lista de usuarios
     */
    private void visualizaLista(ObservableList<Usuario> lista) {
        if (lista.size()>0){
            deshabilitarBotonesEdicion(false);
        }else{
            deshabilitarBotonesEdicion(true);
            inicializarCampos();
        }
        usuariosListView.setItems(lista);
        usuariosListView.getSelectionModel().select(0);
    }

    /**
     * Método que deshabilita los botones de Editar, Borrar e Imprimir
     * @param b     Booleano True=deshabilitar False=Habilitar
     */
    private void deshabilitarBotonesEdicion(boolean b) {
        editBtn.setDisable(b);
        deleteBtn.setDisable(b);
    }
    
    /**
     * Muestra las imágenes de los botones del formulario
     */
    private void showIcons() {
        String[] icons={"resources/icons/add.png","resources/icons/edit.png",
            "resources/icons/delete.png"            
            };
        ImageView[] iv=new ImageView[3];
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
    }
}
