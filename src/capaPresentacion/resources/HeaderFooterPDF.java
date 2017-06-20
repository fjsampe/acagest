package capaPresentacion.resources;

import capaNegocio.ShareData;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Clase HeaderFooterPDF
 * Genera el pie de nuestro documentos PDF's
 *  
 * 
 * Métodos y funciones:
 *  onEndPage    : Muestra un mensaje de error
 * 
 * @author Francisco José Sampedro Lujan
 * @version 0.1
 */
 public class HeaderFooterPDF extends PdfPageEventHelper{
    
    /**
      * Método que genera el pié de página de los documentos PDF's
      * @param writer   Writer
      * @param document Documento    
      */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Font fuente= new Font();
        fuente.setSize(8);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(ShareData.EMPRESA.getNombre()+" "+ShareData.EMPRESA.getEmail(),fuente), 140, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Página: " + (document.getPageNumber()-1),fuente), 540, 30, 0);
    }
}