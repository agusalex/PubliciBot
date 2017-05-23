package com.PubliciBot.UI;

import com.PubliciBot.DM.Tag;
import com.PubliciBot.Services.ArbolTagsService;
import com.PubliciBot.Services.TreeService;
import com.vaadin.ui.*;

public class ABMTagsController extends VerticalLayout {
    Tree treeVaadin;

    public ABMTagsController(ABMTags abmtag){
        super();
        treeVaadin = new Tree();

        ArbolTagsService arbolTagService = new ArbolTagsService();
        TreeService treeService = new TreeService();

        TextField txtNuevoTag = new TextField("");
        txtNuevoTag.setMaxLength(30);
        Label Title=new Label("Administracion de Tags");
        Button btnAgregarTag = new Button("Agregar");
        Button btneliminarTag = new Button("Eliminar");

        arbolTagService.recuperarArbol();
        treeVaadin = arbolTagService.convertirArbolaTree(treeVaadin);


        btnAgregarTag.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Tag nuevo=null ;
                Tag temp=null;

                if (txtNuevoTag.getValue().trim() != "") {
                    nuevo=new Tag(txtNuevoTag.getValue());

                    txtNuevoTag.setValue("");
                }

                temp = (Tag) treeVaadin.getValue();

                if (temp != null && nuevo!=null) {
                    arbolTagService.agregarTag(nuevo);
                    arbolTagService.setearPadre(nuevo, temp);
                    treeService.agregarTag(treeVaadin,nuevo);
                    treeService.setearPadre(treeVaadin,nuevo,temp);
                }
                else if(nuevo != null) {
                    arbolTagService.agregarTag(nuevo);
                    treeService.agregarTag(treeVaadin,nuevo);
                }
                if(nuevo == null){
                    abmtag.showNotification("No es posible agregar un tag Vacio");
                }
                abmtag.showNotification(arbolTagService.getArbolTags().getTags().toString());
                arbolTagService.guardarArbol();

            }
        });

        btneliminarTag.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Tag temp = (Tag) treeVaadin.getValue();

                if (temp != null) {
                    treeService.quitarTagTree(treeVaadin, temp);
                    arbolTagService.quitarTagArbolTags(temp);
                }
                if(temp == null)
                    abmtag.showNotification("No se ha seleccionado ningun tag");
                abmtag.showNotification(arbolTagService.getArbolTags().getTags().toString());
                arbolTagService.guardarArbol();

            }
        });
        HorizontalLayout HL=new HorizontalLayout();
        HL.addComponent(txtNuevoTag);
        HL.addComponent(btnAgregarTag);
        HL.setComponentAlignment(txtNuevoTag,Alignment.MIDDLE_CENTER);
        HL.setComponentAlignment(btnAgregarTag,Alignment.BOTTOM_CENTER);
        this.addComponent(Title);
        this.setComponentAlignment(Title,Alignment.BOTTOM_CENTER);
        this.addComponent(HL);
        this.addComponent(treeVaadin);
        HorizontalLayout HL2=new HorizontalLayout();


        HL2.addComponent(btneliminarTag);
        HL2.setComponentAlignment(btneliminarTag,Alignment.BOTTOM_RIGHT);
        HL2.setSpacing(true);
        this.addComponent(HL2);


    }


}
