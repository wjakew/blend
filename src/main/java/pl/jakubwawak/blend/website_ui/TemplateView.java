/**
 * by Jakub Wawak
 * j.wawak@usp.pl/kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.blend.website_ui;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.jakubwawak.blend.BlendApplication;

@PageTitle("blend by Jakub Wawak")
@Route(value = "template-viewadsfasdfsa")
public class TemplateView extends VerticalLayout {


    /**
     * Constructor
     */
    public TemplateView(){
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

    }

}
