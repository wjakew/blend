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

import pl.jakubwawak.blend.maintanance.FileObject;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@PageTitle("merge by Jakub Wawak")
@Route(value = "merge")
public class MergeView extends VerticalLayout {

    MultiFileMemoryBuffer buffer;
    Upload uploadComponent;

    Button merge_button;

    Button clear_button;
    VerticalLayout uploadLayout;

    ArrayList<FileObject> fileCollection;

    /**
     * Constructor
     */
    public MergeView(){
        this.getElement().setAttribute("theme", Lumo.LIGHT);
        fileCollection = new ArrayList<>();

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
        uploadComponent.setAcceptedFileTypes("application/pdf", ".pdf");
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
            Notification.show("Uploaded "+fileName+"!");

        });

        uploadComponent.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        merge_button = new Button("Merge!",this::mergebutton_action);
        merge_button.setWidth("100%");merge_button.setHeight("20%");
        merge_button.getStyle().set("color","black");
        merge_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        merge_button.setIcon(VaadinIcon.FILE.create());
        merge_button.getStyle().set("border-radius","25px");

        clear_button = new Button("Clear Files",this::clearbutton_action);
        clear_button.setWidth("100%");clear_button.setHeight("20%");
        clear_button.getStyle().set("color","black");
        clear_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        clear_button.setIcon(VaadinIcon.TRASH.create());
        clear_button.getStyle().set("border-radius","25px");

        uploadLayout = new VerticalLayout();

        uploadLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        uploadLayout.setWidth("70%");
        uploadLayout.setHeight("60%");
        uploadLayout.getStyle().set("text-align", "center");
        uploadLayout.getStyle().set("border-radius","25px");
        uploadLayout.getStyle().set("margin","75px");
        uploadLayout.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        uploadLayout.getStyle().set("--lumo-font-family","Monospace");
        uploadLayout.add(clear_button,uploadComponent,merge_button);

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
        add(uploadLayout);
    }

    /**
     * merge_button action
     * @param ex
     */
    private void mergebutton_action(ClickEvent ex){
        if ( fileCollection.size() > 0 ){
            MergeOptionsWindow mow = new MergeOptionsWindow(fileCollection);
            add(mow.main_dialog);
            mow.main_dialog.open();
        }
        else{
            Notification.show("Your file list is empty!");
        }
    }

    /**
     * clearbutton_action
     * @param ex
     */
    private void clearbutton_action(ClickEvent ex){
        uploadComponent.clearFileList();
        Notification.show("File list cleared!");
    }
}
