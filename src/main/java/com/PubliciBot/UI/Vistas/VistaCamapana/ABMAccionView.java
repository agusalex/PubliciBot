package com.PubliciBot.UI.Vistas.VistaCamapana;

import com.PubliciBot.UI.Vistas.VistaCamapana.AccionView;
import com.vaadin.ui.Window;

/**
 * Created by Hugo on 10/06/2017.
 */
public class ABMAccionView  extends Window {

    public static final String VIEW_NAME = "Creación de Acciones";

    public ABMAccionView(AccionView accionView) {
        this.setContent(accionView);
        this.setSizeFull();

		//commmnbet
    }


}
