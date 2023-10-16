/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.maintanance;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.notification.Notification;
import pl.jakubwawak.blend.BlendApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Object for marge pdf files
 */
public class MergeEngine {

    public ArrayList<FileObject> fileCollection;
    String fileName;

    /**
     * Constructor
     */
    public MergeEngine(ArrayList<FileObject> fileCollection,String fileName){
        this.fileCollection = fileCollection;
        this.fileName = fileName;
    }

    /**
     * Function for merge files
     * @return Integer
     */
    public File merge(){
        if ( fileCollection.size() > 0 ){
            try{
                List<InputStream> inputPdfList = new ArrayList<>();
                for(FileObject fileObject : fileCollection){
                    inputPdfList.add(fileObject.fileStream);
                }
                String outputFileName = fileName+".pdf";
                OutputStream outputStream = new FileOutputStream(outputFileName);
                mergePdfFiles(inputPdfList,outputStream);
                File fileToDownload = new File(outputFileName);
                return fileToDownload;
            }catch(Exception e){
                System.out.println(e.toString());
            }
        }
        else{
            Notification.show("Number of files is 0!");
        }
        return null;
    }

    void mergePdfFiles(List<InputStream> inputPdfList,
                       OutputStream outputStream) throws Exception{
        //Create document and pdfReader objects.
        Document document = new Document();
        document.addAuthor("blend by Jakub Wawak");
        document.addCreator("blend by Jakub Wawak");
        List<PdfReader> readers =
                new ArrayList<PdfReader>();
        int totalPages = 0;

        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator =
                inputPdfList.iterator();

        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(
                        pdfReader,currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }
        //Close document and outputStream.
        outputStream.flush();
        document.close();
        outputStream.close();
        Notification.show("Pdf files merged successfully.");
    }
}
