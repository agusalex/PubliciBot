package com.PubliciBot.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Hugo on 14/05/2017.
 */
@Theme("mytheme")
public class ABMTags extends UI {

    Tree treeVaadin;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        ABMTagsController ABCTRL=new ABMTagsController(this);
        setContent(ABCTRL);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ABMTags.class, productionMode = false)
    public static class ABMTagsServlet extends VaadinServlet {

    }
}
