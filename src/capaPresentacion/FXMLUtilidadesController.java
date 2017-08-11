/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capaPresentacion;

import capaNegocio.FichaRecibo;
import capaNegocio.Movimiento;
import capaNegocio.OcupacionAula;
import capaNegocio.OperativasBD;
import capaNegocio.Recibo;
import capaNegocio.ReciboGenerado;
import capaPresentacion.resources.Campos;
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Francisco José Sampedro Lujan
 */
public class FXMLUtilidadesController implements Initializable {

    @FXML private VBox formularioDatos;
    @FXML private ComboBox<String> funcionCmb;
    @FXML private ComboBox<String> accionCmb;
    @FXML private Button btnAceptar;
    @FXML private DatePicker inicioDate;
    @FXML private DatePicker finDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        habilitar(false);
        cargarFunciones();
        // Listener Selección Función ComboBox
        funcionCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            habilitar(false);
            if (newSelection!=null) {
                switch (newSelection.toLowerCase()){
                    case "generar recibos": btnAceptar.setDisable(false);
                                            break;
                    case "listados":    accionCmb.setDisable(false);
                                        cargarListaAcciones(newSelection);
                                        break;
                }
            }
        });
        
        // Listener Selección Acción ComboBox
        accionCmb.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->{
            if (newSelection!=null) {
                switch (newSelection.toLowerCase()){
                    case "ocupación aulas": btnAceptar.setDisable(false);
                                            inicioDate.setDisable(true);
                                            finDate.setDisable(true);
                                            break;
                                            
                    case "recibos pendientes":  btnAceptar.setDisable(false);
                                                inicioDate.setDisable(true);
                                                finDate.setDisable(true);
                                                break;
                                            
                    case "balance":         inicioDate.setDisable(false);
                                            finDate.setDisable(false);
                                            btnAceptar.setDisable(false);
                                            break;
                    case "iva":             inicioDate.setDisable(false);
                                            finDate.setDisable(false);
                                            btnAceptar.setDisable(false);
                                            break;
                    default: break;                            
                }
            }
        });
        Campos.validaCampoFecha(inicioDate);
        Campos.validaCampoFecha(finDate);
    }    

    @FXML
    private void clickBtnAceptar(ActionEvent event) {
        boolean primerBloque=false;
        List<Recibo> listaRecibos;
        switch (funcionCmb.getValue().toLowerCase()){
            case "generar recibos": listaRecibos=OperativasBD.generarRecibos(); 
                                    primerBloque=true;
                                    break;
            default:    break;
        }
        if (!primerBloque){
            switch (accionCmb.getValue().toLowerCase()){
                case "recibos pendientes" : listarRecibosPendientes(OperativasBD.extraerRecibosPendientes());
                                            break;
                
                case "ocupación aulas" : listarAulasOcupadas(OperativasBD.extraerAulasOcupadas());
                                        break;
                case "balance"         : listarBalance();
                                        break;
                case "iva"         : listarIva();
                                        break;
                default                : break;
            }
        }
    }


    private void habilitar(boolean b) {
        btnAceptar.setDisable(!b);
        accionCmb.setDisable(!b);
        inicioDate.setDisable(!b);
        finDate.setDisable(!b);
    }

    private void cargarFunciones() {
        funcionCmb.setItems(
            FXCollections.observableArrayList("Generar Recibos","Listados"));    
    }

    private void cargarListaAcciones(String seleccionFuncion) {
        switch(seleccionFuncion.toLowerCase()){
            case "generar recibos": cargarAccionesRecibos();
                                    break;
            case "listados":        cargarAccionesListados();
                                    break;
            default:            Mensajes.msgError("ERROR CODIGO", 
                                    "Opción no implementada. Revise aplicación módulo Utilidades Controller");
                                break;
        }
    }

    private void cargarAccionesRecibos() {
        accionCmb.setItems(FXCollections.observableArrayList(""));
    }

    private void cargarAccionesListados() {
        accionCmb.setItems(FXCollections.observableArrayList("Recibos Pendientes", "Balance","IVA","Ocupación Aulas"));
    }

    private void generarRecibos() {
    /*    select * from matricular
	left join alumnos on matricular.codAlumno=alumnos.codAlumno
	left join cursos on matricular.codcurso=cursos.codcurso
	where fechafin>=now() and fechainicio<=now();
    */
    }

    private void listarAulasOcupadas(List<OcupacionAula> ocupacionAulas) {
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
            pdf.GenerarHojaOcupacionAulas(ocupacionAulas);
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

    private void listarBalance() {
        
        if (inicioDate.getValue()==null || finDate.getValue()==null || inicioDate.getValue().isAfter(finDate.getValue())  ){
            Mensajes.msgError("ERROR FECHAS", "Rellene correctamente las fechas de búsqueda");
        }else{
            List<Movimiento> listaMovimientos=OperativasBD.extraerMovimientos(inicioDate.getValue(),finDate.getValue());
            List<Movimiento> listaRecibos=OperativasBD.extraerAbonos(inicioDate.getValue(),finDate.getValue(),false);
            List<Movimiento> listaFacturas=OperativasBD.extraerAbonos(inicioDate.getValue(),finDate.getValue(),true);
            listaMovimientos.addAll(listaRecibos);
            listaMovimientos.addAll(listaFacturas);
            listaMovimientos.sort((mov1,mov2)->mov1.getFecha().compareTo(mov2.getFecha()));
            listarMovimientos(listaMovimientos);
        }
    }
    
    
    private void listarIva() {
        
        if (inicioDate.getValue()==null || finDate.getValue()==null || inicioDate.getValue().isAfter(finDate.getValue())  ){
            Mensajes.msgError("ERROR FECHAS", "Rellene correctamente las fechas de búsqueda");
        }else{
            List<Movimiento> listaMovimientos=OperativasBD.extraerMovimientos(inicioDate.getValue(),finDate.getValue());
            List<Movimiento> listaFacturas=OperativasBD.extraerAbonos(inicioDate.getValue(),finDate.getValue(),true);
            listaMovimientos.addAll(listaFacturas);
            listaMovimientos.sort((mov1,mov2)->mov1.getFecha().compareTo(mov2.getFecha()));
            listarIva(listaMovimientos);
        }
    }
    

    private void listarRecibosPendientes(List<FichaRecibo> extraerRecibosPendientes) {
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
            pdf.GenerarHojaRecibosPendientes(extraerRecibosPendientes);
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

    private void listarMovimientos(List<Movimiento> listaMovimientos) {
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
            pdf.GenerarHojaBalance(listaMovimientos,inicioDate.getValue(),finDate.getValue());
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
    
    
    private void listarIva(List<Movimiento> listaMovimientos) {
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
            pdf.GenerarHojaIva(listaMovimientos,inicioDate.getValue(),finDate.getValue());
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
    
    
}
