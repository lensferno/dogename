package me.hety.dogename.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


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
        messageText.setFont(Font.font("Microsoft YaHei",14));

        creatDialog(title,messageText,OKButton);
    }

    //创建只有一个按钮的dialog
    public void creatDialogWithOneBtn(@NamedArg("title") String title, @NamedArg("theBody") Node body){
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

    //创建只有一个按钮的dialog
    public void creatDialogWithOKAndCancel(@NamedArg("title") String title, @NamedArg("message") String message,@NamedArg("OKEvent") EventHandler<ActionEvent> OKEvent){
        //dialog.setPrefHeight(rootPane.getPrefHeight());
        //dialog.setPrefWidth(rootPane.getPrefWidth());

        JFXButton CancelButton = new JFXButton("手滑了");
        CancelButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));
        CancelButton.setPrefWidth(60);
        CancelButton.setPrefHeight(30);
        CancelButton.setOnAction(e -> dialog.close());

        JFXButton OKButton = new JFXButton("是！");
        OKButton.setFont(Font.font("Microsoft YaHei",FontWeight.BOLD,12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.setTextFill(Paint.valueOf("red"));
        OKButton.addEventHandler(ActionEvent.ACTION,e -> {dialog.close();});
        OKButton.addEventHandler(ActionEvent.ACTION,OKEvent);

        OKButton.setOnAction(OKEvent);

        Text messageText=new Text(message);
        messageText.setFont(Font.font("Microsoft YaHei",14));

        creatDialog(title,messageText,CancelButton,OKButton);

        dialog.show();
    }

    public void creatDialog(@NamedArg("title") String title,@NamedArg("theBody") Node body,@NamedArg("buttons") JFXButton...buttons){

        JFXDialogLayout content = new JFXDialogLayout();

        Label titleLabel=new Label(title);
        titleLabel.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,20));
        content.setHeading(titleLabel);

        content.setBody(body);
        content.setAlignment(Pos.CENTER);

        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(rootPane.getPrefHeight());
        tempPane.setPrefWidth(rootPane.getPrefWidth());
        rootPane.getChildren().add(tempPane);

        dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);

        dialog.setOnDialogClosed(event -> rootPane.getChildren().remove(tempPane));

        content.setActions(buttons);

        dialog.show();
    }
}
