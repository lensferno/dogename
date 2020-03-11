package me.hety.dogename.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    //创建只有一个按钮的dialog
    public void creatDialongWithOneBtn(@NamedArg("title") String header,@NamedArg("theBody") Pane body){

        JFXDialogLayout content = new JFXDialogLayout();

        Label title=new Label(header);
        title.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,15));
        content.setHeading(title);

        content.setBody(body);

        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(rootPane.getPrefHeight());
        tempPane.setPrefWidth(rootPane.getPrefWidth());
        rootPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(rootPane.getPrefHeight());
        dialog.setPrefWidth(rootPane.getPrefWidth());

        JFXButton OKButton = new JFXButton("好的！");
        OKButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));
        dialog.setOnDialogClosed(event -> rootPane.getChildren().remove(tempPane));
        OKButton.setPrefWidth(100);
        OKButton.setPrefHeight(30);
        OKButton.setOnAction(e -> dialog.close());

        content.setActions(OKButton);

        dialog.show();
    }
}
