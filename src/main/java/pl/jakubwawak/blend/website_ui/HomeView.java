package pl.jakubwawak.blend.website_ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.jakubwawak.blend.BlendApplication;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@PageTitle("blend by Jakub Wawak")
@Route(value = "blend")
@RouteAlias(value = "/")
public class HomeView extends VerticalLayout {

    MultiFileMemoryBuffer buffer;
    Upload uploadComponent;

    Button merge_button;
    HorizontalLayout uploadLayout;

    ArrayList<InputStream> fileCollection;

    /**
     * Constructor
     */
    public HomeView(){
        this.getElement().setAttribute("theme", Lumo.LIGHT);
        fileCollection = new ArrayList<>();

        prepareView();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().set("background-image","linear-gradient(#e38ad9, #FFFFFF)");
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        buffer = new MultiFileMemoryBuffer();
        uploadComponent = new Upload(buffer);
        uploadComponent.setWidth("97%");uploadComponent.setHeight("97%");
        uploadComponent.setDropAllowed(true);
        uploadComponent.setAutoUpload(true);
        uploadComponent.setAcceptedFileTypes("application/pdf", ".pdf");
        uploadComponent.setMaxFiles(10);

        int maxFileSizeInBytes = 20 * 1024 * 1024; // 10MB
        uploadComponent.setMaxFileSize(maxFileSizeInBytes);

        uploadComponent.addSucceededListener(event -> {
            // Determine which file was uploaded
            String fileName = event.getFileName();

            // Get input stream specifically for the finished file
            InputStream fileData = buffer
                    .getInputStream(fileName);
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();

            // Do something with the file data
            // processFile(fileData, fileName, contentLength, mimeType);
            fileCollection.add(fileData);
            Notification.show("Uploaded "+fileName+"!");

        });

        uploadComponent.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        merge_button = new Button("Merge!",this::mergebutton_action);
        merge_button.setWidth("40%");merge_button.setHeight("10%");
        merge_button.getStyle().set("color","black");
        merge_button.getStyle().set("background-image","linear-gradient(#e38ad9, #FFFFFF)");
        merge_button.setIcon(VaadinIcon.FILE.create());
        merge_button.getStyle().set("border-radius","25px");

        uploadLayout = new HorizontalLayout();

        uploadLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        uploadLayout.setWidth("100%");
        uploadLayout.setHeight("100%");
        uploadLayout.getStyle().set("text-align", "center");
        uploadLayout.getStyle().set("border-radius","25px");
        uploadLayout.getStyle().set("margin","75px");
        uploadLayout.getStyle().set("background-image","linear-gradient(#e38ad9, #FFFFFF)");
        uploadLayout.getStyle().set("--lumo-font-family","Monospace");

    }


    /**
     * Function for preparing view
     */
    void prepareView(){
        prepareComponents();
        this.removeAll();
        StreamResource res = new StreamResource("blend_icon.png", () -> {
            return HomeView.class.getClassLoader().getResourceAsStream("images/blend_icon.png");
        });
        Image logo = new Image(res,"bear_in_mind logo");
        logo.setHeight("512px");
        logo.setWidth("512px");

        HorizontalLayout centerLayout = new HorizontalLayout();

        uploadLayout.add(uploadComponent);

        VerticalLayout rightlayout = new VerticalLayout();

        rightlayout.setSizeFull();
        rightlayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rightlayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        rightlayout.getStyle().set("text-align", "center");
        rightlayout.getStyle().set("--lumo-font-family","Monospace");
        rightlayout.add(uploadLayout,merge_button);

        centerLayout.add(logo,rightlayout);

        centerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centerLayout.setWidth("512px");
        centerLayout.setHeight("512px");
        centerLayout.getStyle().set("text-align", "center");
        centerLayout.getStyle().set("margin","75px");
        centerLayout.getStyle().set("--lumo-font-family","Monospace");

        add(new H6("blend by Jakub Wawak / "+ BlendApplication.version+" / "+BlendApplication.build));
        add(centerLayout);
    }

    /**
     * merge_button action
     * @param ex
     */
    private void mergebutton_action(ClickEvent ex){
        if ( fileCollection.size() > 0 ){
            try{
                List<InputStream> inputPdfList = fileCollection;
                String outputFileName = "blend_merge"+BlendApplication.globalFileIndex+".pdf";
                OutputStream outputStream = new FileOutputStream(outputFileName);
                mergePdfFiles(inputPdfList,outputStream);
                File fileToDownload = new File(outputFileName);
                if ( fileToDownload.exists()){
                    FileDownloaderComponent fdc = new FileDownloaderComponent(fileToDownload);
                    add(fdc.dialog);
                    fdc.dialog.open();
                }
                else{
                    Notification.show("Error - cannot download file!");
                }
            }catch(Exception e){
                System.out.println(e.toString());
                Notification.show("Error ("+e.toString()+")");
            }
            BlendApplication.globalFileIndex++;
        }
        else{
            Notification.show("Number of files is 0!");
        }
    }

    void mergePdfFiles(List<InputStream> inputPdfList,
                              OutputStream outputStream) throws Exception{
        //Create document and pdfReader objects.
        Document document = new Document();
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
