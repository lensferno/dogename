package me.hety.dogename.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.hety.dogename.main.controllers.ProgramInfoPaneController;


public class DialogMaker {
    Pane rootPane;

    public DialogMaker(@NamedArg("rootPane") Pane rootPane){
        this.rootPane=rootPane;
    }


    JFXDialog dialog;

    public void creatMessageDialog(@NamedArg("title") String title, @NamedArg("message") String message){
        JFXButton OKButton = new JFXButton("了解！");
        OKButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.setOnAction(e -> dialog.close());

        Text messageText=new Text(message);
        messageText.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,15));

        creatDialog(title,messageText,OKButton);
    }

    //创建只有一个按钮的dialog
    public void creatDialogWithOneBtn(@NamedArg("title") String title, @NamedArg("theBody") Pane body){
       //dialog.setPrefHeight(rootPane.getPrefHeight());
        //dialog.setPrefWidth(rootPane.getPrefWidth());

        JFXButton OKButton = new JFXButton("好的！");
        OKButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.setOnAction(e -> dialog.close());

        creatDialog(title,body,OKButton);

        dialog.show();
    }

    public void creatDialog(@NamedArg("title") String title,@NamedArg("theBody") Node body,@NamedArg("buttons") JFXButton...buttons){

        JFXDialogLayout content = new JFXDialogLayout();


        Label titleLabel=new Label(title);
        titleLabel.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,15));
        content.setHeading(titleLabel);

        content.setBody(body);
        content.setAlignment(Pos.CENTER);

        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(rootPane.getPrefHeight());
        tempPane.setPrefWidth(rootPane.getPrefWidth());
        rootPane.getChildren().add(tempPane);

        dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);

        //dialog.setPrefHeight(rootPane.getPrefHeight());
        //dialog.setPrefWidth(rootPane.getPrefWidth());

        /*
        JFXButton OKButton = new JFXButton("好的！");
        OKButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));

        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.setOnAction(e -> dialog.close());
*/
        /*
        if(buttons.length==buttonEventHandlers.length){
            for(int i=0;i<buttons.length;i++){
                buttons[i].setOnAction(buttonEventHandlers[i]);
            }
        }*/

        dialog.setOnDialogClosed(event -> rootPane.getChildren().remove(tempPane));

        content.setActions(buttons);

        dialog.show();
    }
}
