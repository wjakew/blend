/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.website_ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.jakubwawak.blend.BlendApplication;

@PageTitle("blend by Jakub Wawak")
@Route(value = "blend")
@RouteAlias(value = "/")
public class WelcomeView extends VerticalLayout {

    Button merge_button;

    Button jpgtopdf_button;

    /**
     * Constructor
     */
    public WelcomeView(){
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
        merge_button = new Button("Merge PDFs",VaadinIcon.FILE_PROCESS.create(),this::mergebutton_action);
        merge_button.setWidth("80%");merge_button.setHeight("40%");
        merge_button.getStyle().set("color","black");
        merge_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        merge_button.getStyle().set("border-radius","25px");

        jpgtopdf_button = new Button("JPG to PDF",VaadinIcon.PICTURE.create(),this::convertbutton_action);
        jpgtopdf_button.setWidth("80%");jpgtopdf_button.setHeight("40%");
        jpgtopdf_button.getStyle().set("color","black");
        jpgtopdf_button.getStyle().set("background-image","linear-gradient("+BlendApplication.hexMainColor+", "+BlendApplication.hexSecondaryColor+")");
        jpgtopdf_button.getStyle().set("border-radius","25px");
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

        VerticalLayout logoLayout = new VerticalLayout(logo);
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        logoLayout.setWidth("100%");

        add(logoLayout);
        add(new HorizontalLayout(new H6("blend by Jakub Wawak / "+ BlendApplication.version+" / "+BlendApplication.build)));
        add(merge_button);
        add(jpgtopdf_button);
    }

    /**
     * merge_button action
     * @param ex
     */
    private void mergebutton_action(ClickEvent ex){
        merge_button.getUI().ifPresent(ui ->
                ui.navigate("/merge"));
    }

    /**
     * convert_button action
     * @param ex
     */
    private void convertbutton_action(ClickEvent ex){
        jpgtopdf_button.getUI().ifPresent(ui ->
                ui.navigate("/jpgconvert"));
    }

}
