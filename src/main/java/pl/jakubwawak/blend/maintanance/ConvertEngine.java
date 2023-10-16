/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.maintanance;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.notification.Notification;
import org.apache.commons.compress.utils.IOUtils;
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
public class ConvertEngine {

    public ArrayList<FileObject> fileCollection;
    String fileName;

    /**
     * Constructor
     */
    public ConvertEngine(ArrayList<FileObject> fileCollection,String fileName){
        this.fileCollection = fileCollection;
        this.fileName = fileName;
    }

    /**
     * Function for merge files
     * @return Integer
     */
    public File convert(){
        if ( fileCollection.size() > 0 ){
            try{
                List<InputStream> inputJPGList = new ArrayList<>();
                for(FileObject fileObject : fileCollection){
                    inputJPGList.add(fileObject.fileStream);
                }
                String outputFileName = fileName+".pdf";
                OutputStream outputStream = new FileOutputStream(outputFileName);
                convertJPGFiles(inputJPGList,outputStream);
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

    /**
     * Function for converting jpg files to one pdf
     * @param inputJPGPhotos
     * @param outputStream
     * @throws Exception
     */
    void convertJPGFiles(List<InputStream> inputJPGPhotos,
                       OutputStream outputStream) throws Exception{
        //Create document and pdfReader objects.
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        for(InputStream jpgPhoto : inputJPGPhotos){
            byte[] bytes= IOUtils.toByteArray(jpgPhoto);
            Image pdfImg=Image.getInstance(bytes);
            document.add(pdfImg);
        }
    }
}
