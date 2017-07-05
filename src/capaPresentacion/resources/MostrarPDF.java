package capaPresentacion.resources;

import capaNegocio.Alumno;
import capaNegocio.Asignatura;
import capaNegocio.Aula;
import capaNegocio.Curso;
import capaNegocio.Examen;
import capaNegocio.FichaExamen;
import capaNegocio.FichaGasto;
import capaNegocio.FichaMatricula;
import capaNegocio.FichaPlanificador;
import capaNegocio.FichaRecibo;
import capaNegocio.Gasto;
import capaNegocio.Matricula;
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
            List<String> datos=new ArrayList<>();
            datos.add("DOMICILIO:");
            datos.add(alumno.getDomicilio());
            datos.add("POBLACION:");
            datos.add(alumno.getPoblacion());
            datos.add("PROVINCIA:");
            datos.add(alumno.getCp()+" - "+alumno.getProvincia());
            datos.add("TELEFONOS:");
            datos.add(alumno.getTelefono().trim()+" / "+alumno.getMovil());
            datos.add("FECHA NACIMIENTO:");
            datos.add(Campos.fechaToString(alumno.getNacimiento()));
            datos.add("CENTRO ESTUDIOS:");
            datos.add(alumno.getCentro());
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
            List<String> datos=new ArrayList<>();
            datos.add("DOMICILIO:");
            datos.add(profesor.getDomicilio());
            datos.add("POBLACION:");
            datos.add(profesor.getPoblacion());
            datos.add("PROVINCIA:");
            datos.add(profesor.getCp()+" - "+profesor.getProvincia());
            datos.add("TELEFONOS:");
            datos.add(profesor.getTelefono().trim()+" / "+profesor.getMovil());
            datos.add("FECHA NACIMIENTO:");
            datos.add(Campos.fechaToString(profesor.getFechaNacimiento()));
            datos.add("CTA.BANCARIA:");
            datos.add(profesor.getCtaBanco());
            datos.add("EMAIL:");
            datos.add(profesor.getEmail());
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
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            for (Asignatura a: asignaturasObsList){
                String[] campos=new String[3];
                campos[0]=a.getCodigo();
                campos[1]=a.getNombre();
                campos[2]=a.getCargaHoras()+" horas";
                for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
                }
            }
            documento.add(tabla);
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
            
            List<String> datos=new ArrayList<>();
            datos.add("IMPORTE/HORA:");
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
            datos.add(curso.getImporteHora()+" €  "+tipoPago);
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
            List<String> asignaturas=new ArrayList<>();
            for (Asignatura a: curso.getAsignaturas()){
                asignaturas.add(a.getCodigo());
                asignaturas.add(a.getNombre());
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
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            for (Aula a: aulas){
                String[] campos=new String[3];
                campos[0]=a.getCodigo();
                campos[1]=a.getDescripcion();
                campos[2]=a.getUbicacion();
                for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
                }
            }
            documento.add(tabla);
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
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            String[] campos=new String[5];
            campos[0]="CIF/DNI";
            campos[1]="NOMBRE";
            campos[2]="TELEFONOS";
            campos[3]="CTA.BANCO";
            campos[4]="EMAIL";
            for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
            }
            for (Proveedor proveedor: proveedores){
                campos[0]=proveedor.getCifDni();
                campos[1]=proveedor.getNombre()+" "+proveedor.getApellidos();
                campos[2]=proveedor.getTelefono()+" - "+proveedor.getMovil();
                campos[3]=proveedor.getCtaBanco();
                campos[4]=proveedor.getEmail();
                for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
                }
            }
            documento.add(tabla);
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
            List<String> datos=new ArrayList<>();
            datos.add("DOMICILIO:");
            datos.add(matricula.getAlumno().getDomicilio());
            datos.add("POBLACION:");
            datos.add(matricula.getAlumno().getPoblacion());
            datos.add("PROVINCIA:");
            datos.add(matricula.getAlumno().getCp()+" - "+matricula.getAlumno().getProvincia());
            datos.add("TELEFONOS:");
            datos.add(matricula.getAlumno().getTelefono().trim()+" / "+matricula.getAlumno().getMovil());
            datos.add("FECHA NACIMIENTO:");
            datos.add(matricula.getAlumno().getNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            datos.add("CENTRO ESTUDIOS:");
            datos.add(matricula.getAlumno().getCentro());
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
            List<String> matriculas=new ArrayList<>();
            for (Matricula m: matricula.getListaMatriculas() ){
                matriculas.add(m.getCodCurso());
                matriculas.add(m.getDescripcionAsignatura());
                matriculas.add(m.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                matriculas.add(m.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            String[] campos=new String[6];
            campos[0]="CÓDIGO";
            campos[1]="ASIGNATURA";
            campos[2]="CÓDIGO";
            campos[3]="AULA";
            campos[4]="DIA";
            campos[5]="HORAS";
            for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i],fuente));
                    tabla.addCell(celda);
            }   
            for (Plan plan: fichaPlan.getListaPlanes()){
                campos[0]=plan.getCodigoAsignatura();
                campos[1]=plan.getDescripcionAsignatura();
                campos[2]=plan.getCodigoAula();
                campos[3]=plan.getDescripcionAula();
                campos[4]=plan.getDiaSemanaSpanish();
                campos[5]=plan.getDeHora()+" - "+plan.getAHora();
                
                
                for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i],fuente));
                    tabla.addCell(celda);
                }
            }
            documento.add(tabla);
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
            List<String> datos=new ArrayList<>();
            datos.add("DOMICILIO:");
            datos.add(ficha.getAlumno().getDomicilio());
            datos.add("POBLACION:");
            datos.add(ficha.getAlumno().getPoblacion());
            datos.add("PROVINCIA:");
            datos.add(ficha.getAlumno().getCp()+" - "+ficha.getAlumno().getProvincia());
            datos.add("TELEFONOS:");
            datos.add(ficha.getAlumno().getTelefono().trim()+" / "+ficha.getAlumno().getMovil());
            datos.add("FECHA NACIMIENTO:");
            datos.add(ficha.getAlumno().getNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            datos.add("CENTRO ESTUDIOS:");
            datos.add(ficha.getAlumno().getCentro());
            documento.add(generaCuerpo1(datos));
            documento.add(new Paragraph(" "));
            float[] medidaCeldas = {1.20f, 3.00f, 1.20f, 1.20f};
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            String[] campos=new String[4];
            campos[0]="CÓDIGO";
            campos[1]="ASIGNATURA";
            campos[2]="FECHA";
            campos[3]="NOTA";
            for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
            }   
            for (Examen examen: ficha.getListaExamenes()){
                campos[0]=examen.getCodigoAsignatura();
                campos[1]=examen.getDescripcionAsignatura();
                campos[2]=examen.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                campos[3]=String.format("%.2f",examen.getNota());
                for (int i=0; i<medidaCeldas.length; i++){
                    PdfPCell celda = new PdfPCell();
                    celda.addElement(new Paragraph(campos[i]));
                    tabla.addCell(celda);
                }
            }
            documento.add(tabla);
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
            List<String> datos=new ArrayList<>();
            datos.add("NIF:");
            datos.add(proveedor.getProveedor().getCifDni());
            datos.add("NOMBRE:");
            datos.add(proveedor.getProveedor().getNombre()+" "+proveedor.getProveedor().getApellidos());
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
            List<String> gastos=new ArrayList<>();
            gastos.add("CONCEPTO");
            gastos.add("FECHA");
            gastos.add("IMPORTE");
            gastos.add("IVA");
            Double total=0.0;
            Double iva=0.0;
            for (Gasto g: proveedor.getListaGastos() ){
                gastos.add(g.getConcepto());
                gastos.add(Campos.fechaToString(g.getFecha()));
                gastos.add(String.format("%10.2f",g.getBase()));
                gastos.add(String.format("%10.2f",g.getIva()));
                total=total+g.getBase();
                iva=iva+g.getIva();
            }
            gastos.add("TOTAL:");
            gastos.add("");
            gastos.add(String.format("%10.2f",total));
            gastos.add(String.format("%10.2f",iva));
                                
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
            List<String> datos=new ArrayList<>();
            datos.add(ShareData.EMPRESA.getNombre());
            datos.add(ShareData.EMPRESA.getDomicilio());
            datos.add(ShareData.EMPRESA.getPoblacion());
            datos.add(ShareData.EMPRESA.getCp()+" - "+ShareData.EMPRESA.getProvincia());
            datos.add(ShareData.EMPRESA.getTelefono());
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
            List<String> datos=new ArrayList<>();
            for (int i=0; i<referencias.length;i++){
                datos.add(referencias[i]);
                datos.add(contenidos[i]);
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
    private PdfPTable generaCuerpo1(List<String> datos) {
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
            List<String> datos=new ArrayList<>();
            datos.add("DNI:");
            datos.add(alumno.getDniPadre());
            datos.add("NOMBRE:");
            datos.add(alumno.getPadre());
            datos.add("TELEFONO:");
            datos.add(alumno.getTelefonoPadre());
            datos.add("EMAIL:");
            datos.add(alumno.getEmailPadre());
            PdfPTable bloqueDatosPadre = generaTabla(2,medidaCeldas,1,0,12,datos, false);
            celdaDerechaSup.addElement(bloqueDatosPadre);
            celdaIzquierdaInf.addElement(new Paragraph("MADRE:"));
            datos=new ArrayList<>();
            datos.add("DNI:");
            datos.add(alumno.getDniMadre());
            datos.add("NOMBRE:");
            datos.add(alumno.getMadre());
            datos.add("TELEFONO:");
            datos.add(alumno.getTelefonoMadre());
            datos.add("EMAIL:");
            datos.add(alumno.getEmailMadre());
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
        List<String> datos=new ArrayList<>();
        datos.add("OBSERVACIONES:");
        datos.add(alumno.getObservaciones());
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
     * @return              Devuelve la tabla generada
     */
    private PdfPTable generaTabla(int columnas, float[] sizeColumnas, int padding, int bordes, int sizeFuente, List<String> datos, boolean bordesTabla) {
        Font fuente= new Font();
        fuente.setSize(sizeFuente);
        PdfPTable tabla=new PdfPTable(columnas);
        tabla.setWidthPercentage(100.0f);
        int filasTotales=datos.size()/columnas;
        try {
            tabla.setWidths(sizeColumnas);
            int numeroColumna=1;
            int filas=1;
            for (String s: datos){
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
                //celda.setPadding(padding);
                celda.setPaddingLeft(padding);
                celda.setPaddingRight(padding);
                if (filas==filasTotales){
                    celda.setPaddingBottom(padding);
                }
                
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.addElement(new Paragraph(s,fuente));
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
            List<String> datos=new ArrayList<>();
            datos.add("Recibo:");
            datos.add(String.format("%07d", recibo.getRecibo()));
            datos.add("Fecha:");
            datos.add(Campos.fechaToString(recibo.getFechaEmision()));
            documento.add(generaTabla(4, medidaSubCabecera, 3, 0, 12, datos, false));
            documento.add(new Paragraph(" "));
            float[] medidas={1.00f};
            datos.clear();
            datos.add("Nombre: "+datosAlumnoSeleccionado.getAlumno().getNombre()+
                    " "+datosAlumnoSeleccionado.getAlumno().getApellidos()+"\n"+
            "Domicilio: "+datosAlumnoSeleccionado.getAlumno().getDomicilio()+"\n"+
            "Población: "+datosAlumnoSeleccionado.getAlumno().getPoblacion()+"\n"+
            "Provincia: "+datosAlumnoSeleccionado.getAlumno().getCp()+" - "+
                    datosAlumnoSeleccionado.getAlumno().getProvincia());
            documento.add(generaTabla(1, medidas , 4, 0, 12, datos, true));
            documento.add(new Paragraph(" "));
            float[] medidaCeldas = {4.00f, 1.00f};
            PdfPTable tabla=new PdfPTable(medidaCeldas.length);
            tabla.setWidthPercentage(100.0f);
            tabla.setWidths(medidaCeldas);
            String[] campos=new String[6];
            campos[0]="DESCRIPCION";
            campos[1]="IMPORTE";
            campos[2]=recibo.getDescripcion();
            campos[3]=String.format("%.2f", recibo.getImporte());
            
            
            PdfPCell columnHeader;
            
            for (int column = 0; column < 2; column++) {
                columnHeader = new PdfPCell(new Phrase(campos[column]));
                columnHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
                columnHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);  
                columnHeader.setBorderWidth(0);
                columnHeader.setPadding(1);
                tabla.addCell(columnHeader);
            }
            tabla.setHeaderRows(1);
            tabla.addCell(campos[2]);
            PdfPCell celdaDinero=new PdfPCell();
            
            //celdaDinero.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph preface = new Paragraph(campos[3]); 
            preface.setAlignment(Element.ALIGN_RIGHT);
            celdaDinero.addElement(preface);
            celdaDinero.setVerticalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celdaDinero);
            documento.add(tabla);
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));
            datos.clear();
            datos.add("Firma/Sello:");
            datos.add(" ");
            datos.add(" ");
            datos.add(" ");
            datos.add(" ");
            documento.add(generaTabla(1, medidas , 4, 0, 12, datos, true));
            documento.close();
        } catch (DocumentException ex) {
            Mensajes.msgError("ERROR PDF", "Error generando documento PDF (Listado Asignaturas)");
        }
    }

  

    
    
}
