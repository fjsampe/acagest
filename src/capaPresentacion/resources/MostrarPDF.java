package capaPresentacion.resources;

import capaNegocio.Alumno;
import capaNegocio.Asignatura;
import capaNegocio.Aula;
import capaNegocio.Curso;
import capaNegocio.DiaOcupacion;
import capaNegocio.Examen;
import capaNegocio.Factura;
import capaNegocio.FichaExamen;
import capaNegocio.FichaFactura;
import capaNegocio.FichaGasto;
import capaNegocio.FichaMatricula;
import capaNegocio.FichaPlanificador;
import capaNegocio.FichaRecibo;
import capaNegocio.Gasto;
import capaNegocio.Matricula;
import capaNegocio.Movimiento;
import capaNegocio.OcupacionAula;
import capaNegocio.Plan;
import capaNegocio.Profesor;
import capaNegocio.Proveedor;
import capaNegocio.ReciboGenerado;
import capaNegocio.ShareData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;

/**
 * Clase MostratPDF
 * Genera un documento pdf imprimible según la información solicitada
 *  
 * Atributos:
 *  documento   : Documento
 * 
 * Constructor, Getter y Setter
 * 
 * Métodos y funciones:
 *  GenerarHojaAlumno       : Genera la hoja del alumno indicado
 *  GenerarHojaProfesor     : Genera la hoja del profesor indicado
 *  GenerarHojaAsignaturas  : Genera la hoja de las asignaturas
 *  GenerarHojaCurso        : Genera la hoja del curso indicado
 *  GenerarHojaAulas        : Genera la hoja con las aulas
 *  GeneraHojaHorarios      : Genera la hoja con los horarios
 *  GeneraHojaProveedores   : Genera la hoja de los proveedores
 *  GeneraHojaMatricula     : Genera la hoja del alumno con sus matriculaciones
 *  GenerarHojaPlanes       : Genera la hoja de planes de horarios/aulas/asignaturas de un profesor
 *  devuelveCadenaDias      : Devuelve una cadena con los dias y horas de un horario
 *  GenerarHojaExamenes     : Genera la hoja de examenes de un alumno
 *  GenerarHojaGastos       : Genera la hoja de gastos de un proveedor
 *  generaCabecera          : Genera la cabecera de la hoja
 *  generaCabeceraReferencia: Genera la subcabecera de la hoja
 *  generaCuerpo1           : Genera el cuerpo de la hoja
 *  generaCuerpo2Alumno     : Genera otro cuerpo de la hoja alumno
 *  generaCuerpo3Alumno     : Genera otro cuerpo más de la hoja alumno
 *  generaTabla             : Genera una tabla según los datos facilitados
 * 
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
public class MostrarPDF {
    
    private Document documento;

    /**
     * Contructor parametrizado
     * @param documento     Documento
     */
    public MostrarPDF(Document documento) {
        this.documento = documento;
    }

    /**
     * Getter Documento
     * @return  Devuelve el documento generado
     */
    public Document getDocumento() {
        return documento;
    }
    
    /**
     * Método que genera la hoja del alumno indicado
     * @param alumno    Alumno
     */
    public void GenerarHojaAlumno(Alumno alumno){
        try {
            documento.open();
            documento.add(generaCabecera("** DATOS ALUMNO **"));
            String[] referencias={"NIA:","NOMBRE:"};
            String[] contenidos=new String[2];
            contenidos[0]=alumno.getNia()+"";
            contenidos[1]=alumno.getApellidos()+", "+alumno.getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    alumno.getFoto()));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DOMICILIO:","L",false));
            datos.add(new CamposTablaPDF(alumno.getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("POBLACION:","L",false));
            datos.add(new CamposTablaPDF(alumno.getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("PROVINCIA:","L",false));
            datos.add(new CamposTablaPDF(alumno.getCp()+" - "+alumno.getProvincia(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONOS:","L",false));
            datos.add(new CamposTablaPDF(alumno.getTelefono().trim()+" / "+alumno.getMovil(),"L",false));
            datos.add(new CamposTablaPDF("FECHA NACIMIENTO:","L",false));
            datos.add(new CamposTablaPDF(Campos.fechaToString(alumno.getNacimiento()),"L",false));
            datos.add(new CamposTablaPDF("CENTRO ESTUDIOS:","L",false));
            datos.add(new CamposTablaPDF(alumno.getCentro(),"L",false));
            documento.add(generaCuerpo1(datos));
            documento.add(generaCuerpo2Alumno(alumno));
            documento.add(generaCuerpo3Alumno(alumno));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Alumno)");
        }
    }
    
    /**
     * Método que genera la hoja del profesor indicado
     * @param profesor  Profesor
     */
    public void GenerarHojaProfesor(Profesor profesor){
        try {
            documento.open();
            documento.add(generaCabecera("** DATOS PROFESOR **"));
            String[] referencias={"REFERENCIA:","NOMBRE:"};
            String[] contenidos=new String[2];
            contenidos[0]=profesor.getCifDni();
            contenidos[1]=profesor.getApellidos()+", "+profesor.getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    profesor.getFoto()));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DOMICILIO:","L",false));
            datos.add(new CamposTablaPDF(profesor.getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("POBLACION:","L",false));
            datos.add(new CamposTablaPDF(profesor.getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("PROVINCIA:","L",false));
            datos.add(new CamposTablaPDF(profesor.getCp()+" - "+profesor.getProvincia(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONOS:","L",false));
            datos.add(new CamposTablaPDF(profesor.getTelefono().trim()+" / "+profesor.getMovil(),"L",false));
            datos.add(new CamposTablaPDF("FECHA NACIMIENTO:","L",false));
            datos.add(new CamposTablaPDF(Campos.fechaToString(profesor.getFechaNacimiento()),"L",false));
            datos.add(new CamposTablaPDF("CTA.BANCARIA:","L",false));
            datos.add(new CamposTablaPDF(profesor.getCtaBanco(),"L",false));
            datos.add(new CamposTablaPDF("EMAIL:","L",false));
            datos.add(new CamposTablaPDF(profesor.getEmail(),"L",false));
            documento.add(generaCuerpo1(datos));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
    }
    
    /**
     * Método que genera la hoja de asignaturas
     * @param asignaturasObsList    Lista asignaturas
     */
    public void GenerarHojaAsignaturas(ObservableList<Asignatura> asignaturasObsList) {
        try {
            float[] medidaCeldas = {1.00f, 5.00f,2.00f};
            documento.open();
            documento.add(generaCabecera("** ASIGNATURAS **"));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("CLAVE","L",true));
            datos.add(new CamposTablaPDF("ASIGNATURA","L",true));
            datos.add(new CamposTablaPDF("HORAS","L",true));
            for (Asignatura a: asignaturasObsList){
                datos.add(new CamposTablaPDF(a.getCodigo(),"L",false));
                datos.add(new CamposTablaPDF(a.getNombre(),"L",false));
                datos.add(new CamposTablaPDF(a.getCargaHoras()+" horas","R",false));
            }
            documento.add(generaTabla(3, medidaCeldas , 4, 1, 12, datos, false));
            

            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Asignaturas)");
        }
    }
    
    /**
     * Método que genera la hoja del curso indicado
     * @param curso Curso
     */
    public void GenerarHojaCurso(Curso curso) {
        try {
            documento.open();
            documento.add(generaCabecera("** DATOS CURSO **"));
            String[] referencias={"REFERENCIA:","NOMBRE:","TUTOR:"};
            String[] contenidos=new String[3];
            contenidos[0]=curso.getCodigo();
            contenidos[1]=curso.getDescripcion();
            contenidos[2]=curso.getTutor().getApellidos()+", "+
                    curso.getTutor().getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    curso.getTutor().getFoto()));
            
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("IMPORTE/HORA:","L",false));
            String tipoPago;
            switch(curso.getPago()){
                case "A": tipoPago="Anual";
                            break;
                case "M": tipoPago="Mensual";
                            break;
                case "S": tipoPago="Semanal";
                            break;
                default:  tipoPago="No definido";
                            break;
            }
            datos.add(new CamposTablaPDF(curso.getImporteHora()+" €  "+tipoPago,"L",false));
            documento.add(generaCuerpo1(datos));
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100.0f);
            float[] medidaCeldas = {3.00f,8.20f};
            PdfPCell celdaIzquierda = new PdfPCell();
            celdaIzquierda.setBorder(0);
            celdaIzquierda.setPadding(0);
            celdaIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell celdaDerecha = new PdfPCell();
            celdaDerecha.setBorder(0);
            celdaDerecha.setPadding(0);
            celdaDerecha.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.setWidths(medidaCeldas); 
            celdaIzquierda.addElement(new Paragraph("ASIGNATURAS:"));
            List<CamposTablaPDF> asignaturas=new ArrayList<>();
            for (Asignatura a: curso.getAsignaturas()){
                asignaturas.add(new CamposTablaPDF(a.getCodigo(),"L",false));
                asignaturas.add(new CamposTablaPDF(a.getNombre(),"L",false));
            }
            celdaDerecha.addElement(generaTabla(2,medidaCeldas,1,0,12,asignaturas, false));
            tabla.addCell(celdaIzquierda);
            tabla.addCell(celdaDerecha);
            documento.add(tabla);
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
    }
    
    /**
     * Método que genera la hoja de aulas
     * @param aulas Aulas
     */
    public void GenerarHojaAulas(ObservableList<Aula> aulas) {
        try {
            float[] medidaCeldas = {2.00f, 3.30f, 4.70f};
            documento.open();
            documento.add(generaCabecera("** AULAS **"));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("CLAVE","L",true));
            datos.add(new CamposTablaPDF("AULA","L",true));
            datos.add(new CamposTablaPDF("UBICACION","L",true));
            for (Aula a: aulas){
                datos.add(new CamposTablaPDF(a.getCodigo(),"L",false));
                datos.add(new CamposTablaPDF(a.getDescripcion(),"L",false));
                datos.add(new CamposTablaPDF(a.getUbicacion(),"L",false));
            }
            documento.add(generaTabla(3, medidaCeldas , 4, 1, 12, datos, false));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Aulas)");
        }
    }
    
   
    /**
     * Método que genera la hoja de proveedores
     * @param proveedores  Proveedores
     */
    public void GenerarHojaProveedores(ObservableList<Proveedor> proveedores) {
        try {
            int j=0;
            float[] medidaCeldas = {0.65f, 1.60f, 1.60f, 1.40f, 1.60f};
            documento.open();
            documento.add(generaCabecera("** PROVEEDORES **"));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("CIF/DNI","L",true));
            datos.add(new CamposTablaPDF("NOMBRE","L",true));
            datos.add(new CamposTablaPDF("TELEFONOS","L",true));
            datos.add(new CamposTablaPDF("CTA.BANCO","L",true));
            datos.add(new CamposTablaPDF("EMAIL","L",true));
            for (Proveedor proveedor: proveedores){
                datos.add(new CamposTablaPDF(proveedor.getCifDni(),"L",false));
                datos.add(new CamposTablaPDF(proveedor.getNombre()+" "+proveedor.getApellidos(),"L",false));
                datos.add(new CamposTablaPDF(proveedor.getTelefono()+" - "+proveedor.getMovil(),"R",false));
                datos.add(new CamposTablaPDF(proveedor.getCtaBanco(),"R",false));
                datos.add(new CamposTablaPDF(proveedor.getEmail(),"L",false));
            }
            documento.add(generaTabla(5, medidaCeldas , 4, 1, 12, datos, false));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Horarios)");
        }
    }
    
    /**
     * Método que genera la hoja del alumno con sus matriculas
     * @param matricula     Matricula
     */
    public void GenerarHojaMatriculas(FichaMatricula matricula) {
        try {
            documento.open();
            documento.add(generaCabecera("** DATOS ALUMNO **"));
            String[] referencias={"NIA:","NOMBRE:"};
            String[] contenidos=new String[2];
            contenidos[0]=matricula.getAlumno().getNia()+"";
            contenidos[1]=matricula.getAlumno().getApellidos()+", "+matricula.getAlumno().getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    matricula.getAlumno().getFoto()));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DOMICILIO:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("POBLACION:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("PROVINCIA:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getCp()+" - "+matricula.getAlumno().getProvincia(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONOS:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getTelefono().trim()+" / "+matricula.getAlumno().getMovil(),"L",false));
            datos.add(new CamposTablaPDF("FECHA NACIMIENTO:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getNacimiento()!=null?matricula.getAlumno().getNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")):" ","L",false));
            datos.add(new CamposTablaPDF("CENTRO ESTUDIOS:","L",false));
            datos.add(new CamposTablaPDF(matricula.getAlumno().getCentro(),"L",false));
            documento.add(generaCuerpo1(datos));
            documento.add(generaCuerpo2Alumno(matricula.getAlumno()));
            documento.add(generaCuerpo3Alumno(matricula.getAlumno()));
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100.0f);
            float[] medidaCeldas1 = {3.00f,8.20f};
            float[] medidaCeldas2 = {2.10f,4.80f,1.90f,1.90f};
            PdfPCell celdaIzquierda = new PdfPCell();
            celdaIzquierda.setBorder(0);
            celdaIzquierda.setPadding(0);
            celdaIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell celdaDerecha = new PdfPCell();
            celdaDerecha.setBorder(0);
            celdaDerecha.setPadding(0);
            celdaDerecha.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.setWidths(medidaCeldas1); 
            celdaIzquierda.addElement(new Paragraph("MATRICULACIONES:"));
            List<CamposTablaPDF> matriculas=new ArrayList<>();
            for (Matricula m: matricula.getListaMatriculas() ){
                matriculas.add(new CamposTablaPDF(m.getCodCurso(),"L",false));
                matriculas.add(new CamposTablaPDF(m.getDescripcionAsignatura(),"L",false));
                matriculas.add(new CamposTablaPDF(m.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),"L",false));
                matriculas.add(new CamposTablaPDF(m.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),"L",false));
            }
            celdaDerecha.addElement(generaTabla(4,medidaCeldas2,1,0,12,matriculas, true));
            tabla.addCell(celdaIzquierda);
            tabla.addCell(celdaDerecha);
            documento.add(tabla);
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Alumno)");
        }
    }

    /**
     * Método que genera la hoja de planes de horarios/aulas/asignaturas de un profesor
     * @param fichaPlan     Plan
     */
    public void GenerarHojaPlanes(FichaPlanificador fichaPlan) {
        try {
            documento.open();
            documento.add(generaCabecera("** DATOS PROFESOR **"));
            String[] referencias={"REFERENCIA:","NOMBRE:"};
            String[] contenidos=new String[2];
            contenidos[0]=fichaPlan.getProfesor().getCifDni();
            contenidos[1]=fichaPlan.getProfesor().getApellidos()+", "+fichaPlan.getProfesor().getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    fichaPlan.getProfesor().getFoto()));
            documento.add(new Paragraph(" "));
            Font fuente= new Font();
            fuente.setSize(8);
            float[] medidaCeldas = {1.20f, 4.30f, 1.20f, 3.30f, 2.20f, 2.20f};
            
            
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("CÓDIGO","L",true));
            datos.add(new CamposTablaPDF("ASIGNATURA","L",true));
            datos.add(new CamposTablaPDF("CÓDIGO","L",true));
            datos.add(new CamposTablaPDF("AULA","L",true));
            datos.add(new CamposTablaPDF("DIA","L",true));
            datos.add(new CamposTablaPDF("HORAS","L",true));
            for (Plan plan: fichaPlan.getListaPlanes()){
                datos.add(new CamposTablaPDF(plan.getCodigoAsignatura(),"L",false));
                datos.add(new CamposTablaPDF(plan.getDescripcionAsignatura(),"L",false));
                datos.add(new CamposTablaPDF(plan.getCodigoAula(),"L",false));
                datos.add(new CamposTablaPDF(plan.getDescripcionAula(),"L",false));
                datos.add(new CamposTablaPDF(plan.getDiaSemanaSpanish(),"L",false));
                datos.add(new CamposTablaPDF(plan.getDeHora()+" - "+plan.getAHora(),"L",false));
            }
            documento.add(generaTabla(6, medidaCeldas , 4, 1, 8, datos, false));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
        
    }
    
    
   
    
    /**
     * Método que genera la hoja de exámenes de un alumno
     * @param ficha Ficha Alumno
     */
    public void GenerarHojaExamenes(FichaExamen ficha) {
        try{
            documento.open();
            documento.add(generaCabecera("** DATOS ALUMNO **"));
            String[] referencias={"NIA:","NOMBRE:"};
            String[] contenidos=new String[2];
            contenidos[0]=ficha.getAlumno().getNia()+"";
            contenidos[1]=ficha.getAlumno().getApellidos()+", "+ficha.getAlumno().getNombre();
            documento.add(generaCabeceraReferencia(referencias,contenidos,
                    ficha.getAlumno().getFoto()));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DOMICILIO:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("POBLACION:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("PROVINCIA:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getCp()+" - "+ficha.getAlumno().getProvincia(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONOS:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getTelefono().trim()+" / "+ficha.getAlumno().getMovil(),"L",false));
            datos.add(new CamposTablaPDF("FECHA NACIMIENTO:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getNacimiento()!=null?ficha.getAlumno().getNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")):" ","L",false));
            datos.add(new CamposTablaPDF("CENTRO ESTUDIOS:","L",false));
            datos.add(new CamposTablaPDF(ficha.getAlumno().getCentro(),"L",false));
            documento.add(generaCuerpo1(datos));
            documento.add(new Paragraph(" "));
            float[] medidaCeldas = {1.20f, 3.00f, 1.20f, 1.20f};
            datos.clear();
            datos.add(new CamposTablaPDF("CÓDIGO","L",true));
            datos.add(new CamposTablaPDF("ASIGNATURA","L",true));
            datos.add(new CamposTablaPDF("FECHA","L",true));
            datos.add(new CamposTablaPDF("NOTA","L",true));
            for (Examen examen: ficha.getListaExamenes()){
                datos.add(new CamposTablaPDF(examen.getCodigoAsignatura(),"L",false));
                datos.add(new CamposTablaPDF(examen.getDescripcionAsignatura(),"L",false));
                datos.add(new CamposTablaPDF(examen.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),"R",false));
                datos.add(new CamposTablaPDF(String.format("%.2f",examen.getNota()),"R",false));
            }
            documento.add(generaTabla(4, medidaCeldas , 4, 1, 12, datos, false));
            documento.close();
            
            } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Alumno)");
        }
    }

    /**
     * Método que genera la hoja de gasto del proveedor
     * @param proveedor Proveedor
     */
    public void GenerarHojaGastos(FichaGasto proveedor) {
        try{
            documento.open();
            documento.add(generaCabecera("GASTOS PROVEEDOR"));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("NIF:","L",false));
            datos.add(new CamposTablaPDF(proveedor.getProveedor().getCifDni(),"L",false));
            datos.add(new CamposTablaPDF("NOMBRE:","L",false));
            datos.add(new CamposTablaPDF(proveedor.getProveedor().getNombre()+" "+proveedor.getProveedor().getApellidos(),"L",false));
            documento.add(generaCuerpo1(datos));
            documento.add(new Paragraph(" "));
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100.0f);
            float[] medidaCeldas1 = {3.00f,8.20f};
            float[] medidaCeldas2 = {4.90f,1.90f,1.90f,1.90f};
            PdfPCell celdaIzquierda = new PdfPCell();
            celdaIzquierda.setBorder(0);
            celdaIzquierda.setPadding(0);
            celdaIzquierda.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell celdaDerecha = new PdfPCell();
            celdaDerecha.setBorder(0);
            celdaDerecha.setPadding(0);
            celdaDerecha.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.setWidths(medidaCeldas1); 
            celdaIzquierda.addElement(new Paragraph("RECIBOS:"));
            List<CamposTablaPDF> gastos=new ArrayList<>();
            gastos.add(new CamposTablaPDF("CONCEPTO","L",true));
            gastos.add(new CamposTablaPDF("FECHA","L",true));
            gastos.add(new CamposTablaPDF("IMPORTE","L",true));
            gastos.add(new CamposTablaPDF(ShareData.EMPRESA.getImpuesto(),"L",true));
            Double total=0.0;
            Double iva=0.0;
            for (Gasto g: proveedor.getListaGastos() ){
                gastos.add(new CamposTablaPDF(g.getConcepto(),"L",false));
                gastos.add(new CamposTablaPDF(Campos.fechaToString(g.getFecha()),"L",false));
                gastos.add(new CamposTablaPDF(String.format("%10.2f",g.getBase()),"R",false));
                gastos.add(new CamposTablaPDF(String.format("%10.2f",g.getIva()),"R",false));
                total=total+g.getBase();
                iva=iva+g.getIva();
            }
            
            gastos.add(new CamposTablaPDF("","L",false));
            gastos.add(new CamposTablaPDF("TOTAL:","L",false));
            gastos.add(new CamposTablaPDF(String.format("%10.2f",total),"R",false));
            gastos.add(new CamposTablaPDF(String.format("%10.2f",iva),"R",false));
                                
            celdaDerecha.addElement(generaTabla(4,medidaCeldas2,1,0,12,gastos, false));
            tabla.addCell(celdaIzquierda);
            tabla.addCell(celdaDerecha);
            documento.add(tabla);
            documento.close();
            } catch (DocumentException ex) {
                Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Alumno)");
        }    
    
    }
    
    
    /**
     * Función que genera la cabecera de la hoja
     * @param nombreListado Texto cabecera del Listado
     * @return              Devuelve la tabla Generada 
     */
    private PdfPTable generaCabecera(String nombreListado) {
        int columnas=(nombreListado==null)?2:3;
        PdfPTable tablaCabecera = new PdfPTable(columnas);
        tablaCabecera.setWidthPercentage(100.0f);
        float[] medidaCeldas;
        medidaCeldas = new float[columnas];
        medidaCeldas[0]=1.50f;
        medidaCeldas[1]=4.10f;
        if (columnas==3) medidaCeldas[2]=4.40f;
        try {
            tablaCabecera.setWidths(medidaCeldas);
            PdfPCell celdaLogo = new PdfPCell();
            celdaLogo.setPadding(0);
            celdaLogo.setBorder(0);
            celdaLogo.setFixedHeight(120);
            byte[] imagen =ShareData.EMPRESA.getLogo();
            if (imagen!=null){
                celdaLogo.addElement(Image.getInstance(imagen));
            }else{
                celdaLogo.addElement(new Paragraph("LOGO"));
            }
            PdfPCell celdaDatosEmpresa = new PdfPCell();
            celdaDatosEmpresa.setPadding(0); 
            celdaDatosEmpresa.setBorder(0);
            celdaDatosEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getNombre(),"L",false));
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getCp()+" - "+ShareData.EMPRESA.getProvincia(),"L",false));
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getTelefono(),"L",false));
            float[] medidaCeldasEmpresa = {2.00f};
            PdfPTable bloqueDatosEmpresa = generaTabla(1,medidaCeldasEmpresa,1,0,8,datos, false);
            celdaDatosEmpresa.addElement(bloqueDatosEmpresa); 
            tablaCabecera.addCell(celdaLogo);
            tablaCabecera.addCell(celdaDatosEmpresa);
            if (columnas==3){
                PdfPCell celdaNombreListado = new PdfPCell();
                celdaNombreListado.setPaddingLeft(8);
                celdaNombreListado.setBorderWidth(0);      
                celdaNombreListado.addElement(new Paragraph(nombreListado,
                            FontFactory.getFont(FontFactory.HELVETICA,18, 
                            Font.BOLDITALIC, new CMYKColor(0, 255, 255,17))));
                tablaCabecera.addCell(celdaNombreListado);
            }
        } catch (DocumentException | IOException ex) {
            Mensajes.msgError("GENERACION PDF", "Error en la definición del PDF. No se puede generar. "+ex.getMessage());
        }
        return tablaCabecera;
    }

        
   

    
    
    /**
     * Función que genera una subcabecera con los datos facilitados
     * @param referencias   Etiquetas Campos
     * @param contenidos    Contanido Campos
     * @param imagen        Imagen
     * @return              Devuelve la tabla generada
     */
    private PdfPTable generaCabeceraReferencia(String[] referencias, String[] contenidos, byte[] imagen) {
        PdfPTable tablaCabecera = new PdfPTable(2);
        tablaCabecera.setWidthPercentage(100.0f);
        float[] medidaCeldas = {8.00f, 2.00f};
        try {
            tablaCabecera.setWidths(medidaCeldas);
            PdfPCell celdaDatosAlumno = new PdfPCell();
            celdaDatosAlumno.setPadding(0); 
            celdaDatosAlumno.setBorder(0);
            celdaDatosAlumno.setHorizontalAlignment(Element.ALIGN_LEFT);
            List<CamposTablaPDF> datos=new ArrayList<>();
            for (int i=0; i<referencias.length;i++){
                datos.add(new CamposTablaPDF(referencias[i],"L",false));
                datos.add(new CamposTablaPDF(contenidos[i],"L",false));
            }
            float[] medidaCeldasEmpresa = {3.31f,6.49f};
            PdfPTable bloqueCabeceraAlumno = generaTabla(2,medidaCeldasEmpresa,1,0,12,datos, false);
            celdaDatosAlumno.addElement(bloqueCabeceraAlumno);
            PdfPCell celdaFoto = new PdfPCell();
            if (imagen!=null){
                Image foto=Image.getInstance(imagen);
                celdaFoto.addElement(foto); 

            }else{
                celdaFoto.setPadding(2);
                celdaFoto.setFixedHeight(130);
                celdaFoto.setBackgroundColor(BaseColor.LIGHT_GRAY);
                celdaFoto.addElement(new Paragraph("FOTO"));
            }
            tablaCabecera.addCell(celdaDatosAlumno);
            tablaCabecera.addCell(celdaFoto);
        } catch (DocumentException | IOException ex) {
            Mensajes.msgError("GENERACION PDF", "Error en la definición del PDF. No se puede generar. "+ex.getMessage());
        }
        return tablaCabecera;
    }

    /**
     * Función que genera el cuerpo del documento
     * @param datos Lista de datos a visualizar
     * @return      Devuelve la tabla generada
     */
    private PdfPTable generaCuerpo1(List<CamposTablaPDF> datos) {
        float[] medidaCeldas = {2.70f, 7.30f};
        PdfPTable bloqueDatos = generaTabla(2,medidaCeldas,1,0,12,datos, false);
        return bloqueDatos;
    }
    
    /**
     * Función que genera otro cuerpo mas en el documento de Alumno
     * @param alumno    Alumno
     * @return          Devuelve la tabla generada
     */
    private PdfPTable generaCuerpo2Alumno(Alumno alumno) {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100.0f);
        float[] medidaCeldas = {2.70f, 7.30f};
        PdfPCell celdaIzquierdaSup = new PdfPCell();
        celdaIzquierdaSup.setBorder(0);
        celdaIzquierdaSup.setPadding(0);
        celdaIzquierdaSup.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell celdaDerechaSup = new PdfPCell();
        celdaDerechaSup.setBorder(0);
        celdaDerechaSup.setPadding(0);
        celdaDerechaSup.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell celdaIzquierdaInf = new PdfPCell();
        celdaIzquierdaInf.setBorder(0); 
        celdaIzquierdaInf.setPadding(0); 
        celdaIzquierdaInf.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell celdaDerechaInf = new PdfPCell();
        celdaDerechaInf.setBorder(0);
        celdaDerechaInf.setPadding(0); 
        celdaDerechaInf.setHorizontalAlignment(Element.ALIGN_LEFT);
        try {
            tabla.setWidths(medidaCeldas); 
            celdaIzquierdaSup.addElement(new Paragraph("PADRE:"));
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DNI:","L",false));
            datos.add(new CamposTablaPDF(alumno.getDniPadre(),"L",false));
            datos.add(new CamposTablaPDF("NOMBRE:","L",false));
            datos.add(new CamposTablaPDF(alumno.getPadre(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONO:","L",false));
            datos.add(new CamposTablaPDF(alumno.getTelefonoPadre(),"L",false));
            datos.add(new CamposTablaPDF("EMAIL:","L",false));
            datos.add(new CamposTablaPDF(alumno.getEmailPadre(),"L",false));
            PdfPTable bloqueDatosPadre = generaTabla(2,medidaCeldas,1,0,12,datos, false);
            celdaDerechaSup.addElement(bloqueDatosPadre);
            celdaIzquierdaInf.addElement(new Paragraph("MADRE:"));
            datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("DNI:","L",false));
            datos.add(new CamposTablaPDF(alumno.getDniMadre(),"L",false));
            datos.add(new CamposTablaPDF("NOMBRE:","L",false));
            datos.add(new CamposTablaPDF(alumno.getMadre(),"L",false));
            datos.add(new CamposTablaPDF("TELEFONO:","L",false));
            datos.add(new CamposTablaPDF(alumno.getTelefonoMadre(),"L",false));
            datos.add(new CamposTablaPDF("EMAIL:","L",false));
            datos.add(new CamposTablaPDF(alumno.getEmailMadre(),"L",false));
            PdfPTable bloqueDatosMadre = generaTabla(2,medidaCeldas,1,0,12,datos, false);
            celdaDerechaInf.addElement(bloqueDatosMadre);
        } catch (DocumentException ex) {
            Mensajes.msgError("GENERACION PDF", "Error en la definición del PDF. No se puede generar. "+ex.getMessage());
        }
        tabla.addCell(celdaIzquierdaSup);
        tabla.addCell(celdaDerechaSup);
        tabla.addCell(celdaIzquierdaInf);
        tabla.addCell(celdaDerechaInf);
        return tabla;
    }

    /**
     * Función que genera otro cuerpo más en el documento de alumno
     * @param alumno    Alumno
     * @return          Devuelve la tabla generada
     */
    private PdfPTable generaCuerpo3Alumno(Alumno alumno) {
        List<CamposTablaPDF> datos=new ArrayList<>();
        datos.add(new CamposTablaPDF("OBSERVACIONES:","L",false));
        datos.add(new CamposTablaPDF(alumno.getObservaciones(),"L",false));
        float[] medidaCeldas = {2.70f, 7.30f};
        PdfPTable bloqueDatosAlumno = generaTabla(2,medidaCeldas,1,0,12,datos, false);
        return bloqueDatosAlumno;
    }
    
    /**
     * Función que genera una tabla con los datos facilitados
     * @param columnas      Número de columnas
     * @param sizeColumnas  Tamaño de las columnas
     * @param padding       Padding
     * @param bordes        Tamaño del Borde
     * @param sizeFuente    Tamaño de la letra
     * @param datos         Datos a visualizar
     * @param bordesTabla   True=Con bordes  False=Sin bordes
     * @return              Devuelve la tabla generada
     */
    private PdfPTable generaTabla(int columnas, float[] sizeColumnas, int padding, int bordes, int sizeFuente, List<CamposTablaPDF> datos, boolean bordesTabla) {
        Font fuente= new Font();
        fuente.setSize(sizeFuente);
        PdfPTable tabla=new PdfPTable(columnas);
        tabla.setWidthPercentage(100.0f);
        int filasTotales=datos.size()/columnas;
        try {
            tabla.setWidths(sizeColumnas);
            int numeroColumna=1;
            int filas=1;
            for (CamposTablaPDF s: datos){
                PdfPCell celda = new PdfPCell();
                celda.setBorderWidth(bordes);
                if (bordesTabla){
                    if (filas==1){
                        celda.setBorderWidthTop(1f);
                    }
                    if (numeroColumna==1){
                        celda.setBorderWidthLeft(1f);
                    } 
                    if (filas==filasTotales){
                        celda.setBorderWidthBottom   (1f);
                    }
                    if (numeroColumna==columnas){
                        celda.setBorderWidthRight  (1f);
                    } 
                }
                
                
                
                if (s.isSombreado()) celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                
                
                
                //celda.setPadding(padding);
                celda.setPaddingLeft(padding);
                celda.setPaddingRight(padding);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                
                Paragraph p=new Paragraph(s.getCadena(),fuente);
                if (filas==filasTotales){
                    celda.setPaddingBottom(padding);
                }
                if (s.getAlineamiento().equals("L")){
                    p.setAlignment(Element.ALIGN_LEFT);
                }else if (s.getAlineamiento().equals("R")){
                    p.setAlignment(Element.ALIGN_RIGHT);
                }else{
                    p.setAlignment(Element.ALIGN_CENTER);
                }
                
                celda.addElement(p);
                tabla.addCell(celda);
                numeroColumna++;
                if (numeroColumna>columnas){
                    numeroColumna=1;
                    filas++;
                }
            } 
        } catch (DocumentException ex) {
            Mensajes.msgError("GENERACION PDF", "Error en la definición del PDF. No se puede generar. "+ex.getMessage());
        }
        return tabla;
    }

    public void GenerarHojaRecibo(ReciboGenerado recibo,FichaRecibo datosAlumnoSeleccionado) {
         try {
            documento.open();
            documento.add(generaCabecera(null));
            float[] medidaSubCabecera={0.80f,1.00f,0.80f,1.00f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("Recibo:","L",false));
            datos.add(new CamposTablaPDF(String.format("%07d", recibo.getRecibo()),"L",false));
            datos.add(new CamposTablaPDF("Fecha:","L",false));
            datos.add(new CamposTablaPDF(Campos.fechaToString(recibo.getFechaEmision()),"L",false));
            documento.add(generaTabla(4, medidaSubCabecera, 3, 0, 12, datos, false));
            documento.add(new Paragraph(" "));
            float[] medidas={1.00f,3.70f};
            float[] medidaFirma={1.00f};
            datos.clear();
            datos.add(new CamposTablaPDF("Nombre:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getNombre()+
                    " "+datosAlumnoSeleccionado.getAlumno().getApellidos(),"L",false));
            datos.add(new CamposTablaPDF("Domicilio:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("Población:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("Provincia:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getCp()+" - "+
                    datosAlumnoSeleccionado.getAlumno().getProvincia(),"L",false));
            documento.add(generaTabla(2, medidas , 4, 0, 12, datos, true));
            documento.add(new Paragraph(" "));
            float[] medidaTotales = {3.70f, 1.00f};
            datos.clear();
            datos.add(new CamposTablaPDF("DESCRIPCION","L",true));
            datos.add(new CamposTablaPDF("IMPORTE","L",true));
            documento.add(generaTabla(2, medidaTotales , 4, 1, 12, datos, false));
            datos.clear();
            datos.add(new CamposTablaPDF(recibo.getDescripcion(),"L",false));
            datos.add(new CamposTablaPDF(recibo.getImporteFormateado(),"R",false));
            documento.add(generaTabla(2, medidaTotales , 4, 1, 12, datos, false));
            documento.add(new Paragraph(" "));
            datos.clear();
            datos.add(new CamposTablaPDF("Firma/Sello:","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            documento.add(generaTabla(1, medidaFirma , 4, 0, 12, datos, true));
            documento.close();
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Asignaturas)");
        }
    }
    
    public void GenerarHojaFactura(Factura factura, FichaFactura datosAlumnoSeleccionado) {
        try {
            documento.open();
            documento.add(generaCabecera(null));
            float[] medidaSubCabecera={0.80f,1.00f,0.80f,1.00f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("Factura:","L",false));
            datos.add(new CamposTablaPDF(factura.getFacturaFormateada(),"L",false));
            datos.add(new CamposTablaPDF("Fecha:","L",false));
            datos.add(new CamposTablaPDF(Campos.fechaToString(factura.getFechaFactura()),"L",false));
            documento.add(generaTabla(4, medidaSubCabecera, 3, 0, 12, datos, false));
            documento.add(new Paragraph(" "));
            float[] medidas={0.80f,3.70f};
            float[] medidaFirma={1.00f};
            datos.clear();
            datos.add(new CamposTablaPDF("Nombre:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getNombre()+
                    " "+datosAlumnoSeleccionado.getAlumno().getApellidos(),"L",false));
            datos.add(new CamposTablaPDF("Domicilio:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getDomicilio(),"L",false));
            datos.add(new CamposTablaPDF("Población:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getPoblacion(),"L",false));
            datos.add(new CamposTablaPDF("Provincia:","L",true));
            datos.add(new CamposTablaPDF(datosAlumnoSeleccionado.getAlumno().getCp()+" - "+
                    datosAlumnoSeleccionado.getAlumno().getProvincia(),"L",false));
            documento.add(generaTabla(2, medidas , 4, 0, 12, datos, true));
            documento.add(new Paragraph(" "));
            float[] medidaCeldas = {3.70f, 1.00f};
            List<ReciboGenerado> listaRecibos=factura.getRecibos();
            float[] medidaTotales = {3.70f, 1.00f};
            datos.clear();
            datos.add(new CamposTablaPDF("DESCRIPCION","L",true));
            datos.add(new CamposTablaPDF("IMPORTE","L",true));
            documento.add(generaTabla(2, medidaTotales , 4, 1, 12, datos, false));
           
            datos.clear();
            for (ReciboGenerado recibo: listaRecibos){
                datos.add(new CamposTablaPDF(recibo.getDescripcion(),"L",false));
                datos.add(new CamposTablaPDF(recibo.getImporteFormateado(),"R",false));
            }
            documento.add(generaTabla(2, medidaTotales , 4, 1, 12, datos, false));
            
           
            Double baseImponible,iva,pago;
            pago=factura.getImporteFactura();
            baseImponible=(double)Math.round((pago*100/(100+ShareData.EMPRESA.getPorcentajeImpuesto()))*100d)/100d;
            iva=pago-baseImponible;
            datos.clear();
            datos.add(new CamposTablaPDF("BASE IMPONIBLE:","L",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€", baseImponible),"R",false));
            datos.add(new CamposTablaPDF(ShareData.EMPRESA.getImpuesto()+":","L",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",iva),"R",false));
            datos.add(new CamposTablaPDF("TOTAL A PAGAR:","L",false));
            datos.add(new CamposTablaPDF(factura.getImporteFormateado(),"R",false));
            
            documento.add(generaTabla(2, medidaTotales , 4, 0, 12, datos, true));
            
            documento.add(new Paragraph(" "));
            datos.clear();
            datos.add(new CamposTablaPDF("Firma/Sello:","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            datos.add(new CamposTablaPDF(" ","L",false));
            documento.add(generaTabla(1, medidaFirma , 4, 0, 12, datos, true));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Asignaturas)");
        }    
    
    }

    public void GenerarHojaOcupacionAulas(List<OcupacionAula> ocupacionAulas) {
        try{
            documento.open();
            documento.add(generaCabecera("* OCUPACIÓN AULAS *"));
            documento.add(new Paragraph(" "));
            Font fuente= new Font();
            fuente.setSize(8);
            float[] medidas = {1.20f, 3.30f, 2.20f, 2.30f, 3.20f, 2.20f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("CÓDIGO","L",true));
            datos.add(new CamposTablaPDF("AULA","L",true));
            datos.add(new CamposTablaPDF("DIA","L",true));
            datos.add(new CamposTablaPDF("HORARIO","L",true));
            datos.add(new CamposTablaPDF("PROFESOR","L",true));
            datos.add(new CamposTablaPDF("ASIGNATURA","L",true));
            documento.add(generaTabla(6, medidas , 4, 1, 8, datos, false));
            datos.clear();
            boolean inicio=true;
            for (OcupacionAula ocupacion: ocupacionAulas){
                datos.add(new CamposTablaPDF(ocupacion.getAula().getCodigo(),"L",false));
                datos.add(new CamposTablaPDF(ocupacion.getAula().getDescripcion(),"L",false));
                for (DiaOcupacion dia:ocupacion.getDiasOcupacion()){
                    if (!inicio){
                        datos.add(new CamposTablaPDF(" ","L",false));
                        datos.add(new CamposTablaPDF(" ","L",false));
                    }
                    datos.add(new CamposTablaPDF(dia.getDiaSemanaSpanish(),"L",false));
                    datos.add(new CamposTablaPDF(dia.getDeHora()+" - "+dia.getaHora(),"L",false));
                    datos.add(new CamposTablaPDF(dia.getProfesor(),"L",false));
                    datos.add(new CamposTablaPDF(dia.getAsignatura(),"L",false));
                    inicio=false;
                }
                inicio=true;
            }
            documento.add(generaTabla(6, medidas , 4, 1, 8, datos, false));
            documento.close();
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
    }

    public void GenerarHojaRecibosPendientes(List<FichaRecibo> recibosPendientes) {
        try{
            documento.open();
            documento.add(generaCabecera("RECIBOS PENDIENTES"));
            documento.add(new Paragraph(" "));
            float[] medidas = {2.80f, 1.20f, 1.20f, 3.80f, 1.20f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("ALUMNO","L",true));
            datos.add(new CamposTablaPDF("RECIBO","L",true));
            datos.add(new CamposTablaPDF("FECHA","L",true));
            datos.add(new CamposTablaPDF("DESCRIPCION","L",true));
            datos.add(new CamposTablaPDF("IMPORTE","L",true));
            documento.add(generaTabla(5, medidas , 4, 1, 10, datos, false));
            double total=0.0;
            datos.clear();
            for (FichaRecibo fichaRecibo: recibosPendientes){
                datos.add(new CamposTablaPDF(fichaRecibo.getAlumno().getApellidos()+", "+fichaRecibo.getAlumno().getNombre(),"L",false));
                for (ReciboGenerado recibo:fichaRecibo.getListaRecibos()){
                    datos.add(new CamposTablaPDF(recibo.getReciboFormateado(),"L",false));
                    datos.add(new CamposTablaPDF(recibo.getFechaEmisionFormateado(),"L",false));
                    
                    datos.add(new CamposTablaPDF(recibo.getDescripcion(),"L",false));
                    datos.add(new CamposTablaPDF(recibo.getImporteFormateado(),"R",false));
                    total=total+recibo.getImporte();
                }
            }
            documento.add(generaTabla(5, medidas , 4, 1, 10, datos, false));

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            datos.clear();
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("TOTAL","L",true));
            datos.add(new CamposTablaPDF(String.format("%.2f€",total),"R",false));
            documento.add(generaTabla(5, medidas , 4, 0, 10, datos, true));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
    }

    public void GenerarHojaBalance(List<Movimiento> listaMovimientos,LocalDate inicio, LocalDate fin) {
        try{
            documento.open();
            documento.add(generaCabecera("** BALANCE **"));
            documento.add(new Paragraph("Del: "+Campos.fechaToString(inicio)+
                    "  al: "+Campos.fechaToString(fin)));
            documento.add(new Paragraph(" "));
            double ingresos=0.0;
            double gastos=0.0;
            String gasto,ingreso;
            float[] medidas = {1.20f, 3.00f, 3.30f, 1.10f, 1.10f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("FECHA","L",true));
            datos.add(new CamposTablaPDF("CLIENTE/PROVEEDOR","L",true));
            datos.add(new CamposTablaPDF("CONCEPTO","L",true));
            datos.add(new CamposTablaPDF("GASTO","L",true));
            datos.add(new CamposTablaPDF("INGRESO","L",true));
            documento.add(generaTabla(5, medidas , 4, 1, 9, datos, false));
            datos.clear();
            for (Movimiento mov: listaMovimientos){
                if (mov.getTipo().equals("G")){
                    gasto=mov.getImporteFormateado();
                    ingreso="";
                    gastos=gastos+(mov.getBaseImponible()+mov.getIva());
                }else{
                    ingreso=mov.getImporteFormateado();
                    gasto="";
                    ingresos=ingresos+(mov.getBaseImponible()+mov.getIva());
                }  
                datos.add(new CamposTablaPDF(mov.getFechaFormateado(),"L",false));
                datos.add(new CamposTablaPDF(mov.getClienteProveedor(),"L",false));
                datos.add(new CamposTablaPDF(mov.getConcepto(),"L",false));
                datos.add(new CamposTablaPDF(gasto,"R",false));
                datos.add(new CamposTablaPDF(ingreso,"R",false));
            }
            documento.add(generaTabla(5, medidas , 4, 1, 9, datos, false));
            datos.clear();
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",gastos),"R",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",ingresos),"R",false));
            documento.add(generaTabla(5, medidas , 4, 0, 9, datos, true));
            datos.clear();
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("TOTAL:","L",true));
            datos.add(new CamposTablaPDF(String.format("%.2f€",ingresos-gastos),"R",true));
            documento.add(generaTabla(5, medidas , 4, 0, 9, datos, true));
            documento.close();
            
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }
    }

    public void GenerarHojaIva(List<Movimiento> listaMovimientos,LocalDate inicio, LocalDate fin) {
        try{
            documento.open();
            documento.add(generaCabecera("** IVA **"));
             documento.add(new Paragraph("Del: "+Campos.fechaToString(inicio)+
                    "  al: "+Campos.fechaToString(fin)));
            documento.add(new Paragraph(" "));double ingresos=0.0;
            double ingresosIva=0.0;
            double gastos=0.0;
            double gastosIva=0.0;
            String gasto,ingreso,gastoIva,ingresoIva;
            float[] medidas = {1.20f, 3.00f, 3.30f, 1.10f, 1.10f, 1.10f, 1.10f};
            List<CamposTablaPDF> datos=new ArrayList<>();
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("","L",true));
            datos.add(new CamposTablaPDF("GASTO","C",true));
            datos.add(new CamposTablaPDF("GASTO","C",true));
            datos.add(new CamposTablaPDF("INGRESO","C",true));
            datos.add(new CamposTablaPDF("INGRESO","C",true));
            documento.add(generaTabla(7, medidas , 4, 0, 8, datos, true));
            datos.clear();
            datos.add(new CamposTablaPDF("FECHA","L",true));
            datos.add(new CamposTablaPDF("CLIENTE/PROVEEDOR","L",true));
            datos.add(new CamposTablaPDF("CONCEPTO","L",true));
            datos.add(new CamposTablaPDF("BASE","L",true));
            datos.add(new CamposTablaPDF("IVA","L",true));
            datos.add(new CamposTablaPDF("BASE","L",true));
            datos.add(new CamposTablaPDF("IVA","L",true));
            documento.add(generaTabla(7, medidas , 4, 1, 8, datos, false));
            datos.clear();
            for (Movimiento mov: listaMovimientos){
                if (mov.getTipo().equals("G")){
                    gasto=mov.getBaseImpobibleFormateado();
                    gastoIva=mov.getIvaFormateado();
                    ingreso="";
                    ingresoIva="";
                    gastos=gastos+mov.getBaseImponible();
                    gastosIva=gastosIva+mov.getIva();
                }else{
                    ingreso=mov.getBaseImpobibleFormateado();
                    ingresoIva=mov.getIvaFormateado();
                    gasto="";
                    gastoIva="";
                    ingresos=ingresos+mov.getBaseImponible();
                    ingresosIva=ingresosIva+mov.getIva();
                }  
                datos.add(new CamposTablaPDF(mov.getFechaFormateado(),"L",false));
                datos.add(new CamposTablaPDF(mov.getClienteProveedor(),"L",false));
                datos.add(new CamposTablaPDF(mov.getConcepto(),"L",false));
                datos.add(new CamposTablaPDF(gasto,"R",false));
                datos.add(new CamposTablaPDF(gastoIva,"R",false));
                datos.add(new CamposTablaPDF(ingreso,"R",false));
                datos.add(new CamposTablaPDF(ingresoIva,"R",false));
            }
            documento.add(generaTabla(7, medidas , 4, 1, 8, datos, false));
            datos.clear();
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("","L",false));
            datos.add(new CamposTablaPDF("TOTALES","L",true));
            datos.add(new CamposTablaPDF(String.format("%.2f€",gastos),"R",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",gastosIva),"R",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",ingresos),"R",false));
            datos.add(new CamposTablaPDF(String.format("%.2f€",ingresosIva),"R",false));
            documento.add(generaTabla(7, medidas , 4, 0, 8, datos, true));
            documento.close();
            
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Ficha Profesor)");
        }}

    
 
}
