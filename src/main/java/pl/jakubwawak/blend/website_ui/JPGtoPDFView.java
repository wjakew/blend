/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.website_ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
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
import pl.jakubwawak.blend.maintanance.ConvertEngine;
import pl.jakubwawak.blend.maintanance.FileObject;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@PageTitle("convert by Jakub Wawak")
@Route(value = "jpgconvert")
public class JPGtoPDFView extends VerticalLayout {

    MultiFileMemoryBuffer buffer;
    Upload uploadComponent;

    Button convert_button;

    Button clear_button;
    VerticalLayout uploadLayout;

    ArrayList<FileObject> fileCollection;

    /**
     * Constructor
     */
    public JPGtoPDFView(){
        this.getElement().setAttribute("theme", Lumo.LIGHT);

        prepareView();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
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
        uploadComponent.setAcceptedFileTypes("application/jpeg", ".jpg");
        uploadComponent.setMaxFiles(30);

        int maxFileSizeInBytes = 30 * 1024 * 1024; // 10MB
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
            fileCollection.add(new FileObject(fileName,fileData, LocalDateTime.now(ZoneId.of("Europe/Warsaw"))));
            Notification.show("Uploaded "+fileName+"photos!");

        });

        uploadComponent.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        clear_button = new Button("Clear Files",this::clearbutton_action);
        clear_button.setWidth("100%");clear_button.setHeight("50%");
        clear_button.getStyle().set("color","black");
        clear_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        clear_button.setIcon(VaadinIcon.TRASH.create());
        clear_button.getStyle().set("border-radius","25px");

        convert_button = new Button("Convert to PDF!",this::convertbutton_action);
        convert_button.setWidth("100%");convert_button.setHeight("50%");
        convert_button.getStyle().set("color","black");
        convert_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        convert_button.setIcon(VaadinIcon.FILE.create());
        convert_button.getStyle().set("border-radius","25px");

        uploadLayout = new VerticalLayout();
        uploadLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        uploadLayout.setWidth("70%");
        uploadLayout.setHeight("60%");
        uploadLayout.getStyle().set("text-align", "center");
        uploadLayout.getStyle().set("border-radius","25px");
        uploadLayout.getStyle().set("margin","75px");
        uploadLayout.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        uploadLayout.getStyle().set("--lumo-font-family","Monospace");
        uploadLayout.add(new H6("Drop here your JPG files!"),uploadComponent);


    }


    /**
     * Function for preparing view
     */
    void prepareView(){
        prepareComponents();
        this.removeAll();
        StreamResource res = new StreamResource("blend_icon.png", () -> {
            return MergeView.class.getClassLoader().getResourceAsStream("images/blend_icon.png");
        });
        Image logo = new Image(res,"bear_in_mind logo");
        logo.setHeight("10rem");
        logo.setWidth("10rem");

        logo.addClickListener(e->{
            UI.getCurrent().navigate("/blend");
        });

        VerticalLayout logoLayout = new VerticalLayout(logo);
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        logoLayout.setWidth("100%");

        add(logoLayout);
        add(new HorizontalLayout(new H6("blend by Jakub Wawak / "+ BlendApplication.version+" / "+BlendApplication.build)));

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();

        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        buttonLayout.getStyle().set("text-align", "center");
        buttonLayout.getStyle().set("--lumo-font-family","Monospace");

        buttonLayout.add(clear_button,convert_button);

        mainLayout.add(uploadLayout,buttonLayout);

        add(mainLayout);
    }

    /**
     * clearbutton_action
     * @param ex
     */
    private void clearbutton_action(ClickEvent ex){
        uploadComponent.clearFileList();
        Notification.show("File list cleared!");
    }

    /**
     * convert_button action
     * @param ex
     */
    private void convertbutton_action(ClickEvent ex){
        if ( fileCollection.size() > 0 ){
            try{
                ConvertEngine ce = new ConvertEngine(fileCollection,"test.pdf");
                File file = ce.convert();
                if ( file != null ){
                    FileDownloaderComponent fdc = new FileDownloaderComponent(file);
                    add(fdc.dialog);
                    fdc.dialog.open();
                }
                else{
                    Notification.show("Error converting jgps, contact application administrator!");
                }
            }catch(Exception e){
                Notification.show("Error, "+e.toString());
            }
        }
        else{
            Notification.show("Your file list is empty!");
        }
    }

}
