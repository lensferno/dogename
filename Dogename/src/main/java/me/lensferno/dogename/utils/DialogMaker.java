package me.lensferno.dogename.utils;

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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class DialogMaker {
    Pane rootPane;
    JFXDialog dialog;

    public DialogMaker(@NamedArg("rootPane") Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void createMessageDialog(@NamedArg("title") String title, @NamedArg("message") String message) {
        JFXButton OKButton = new JFXButton("了解！");
        OKButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);

        Text messageText = new Text(message);
        messageText.setFont(Font.font("Microsoft YaHei", 14));

        createDialog(title, messageText, OKButton);
    }

    //创建只有一个按钮的dialog
    public void createDialogWithOneBtn(@NamedArg("title") String title, @NamedArg("theBody") Node body, @NamedArg("OKEvent") EventHandler<JFXDialogEvent> event) {
        //dialog.setPrefHeight(rootPane.getPrefHeight());
        //dialog.setPrefWidth(rootPane.getPrefWidth());

        JFXButton OKButton = new JFXButton("好的！");
        OKButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.addEventHandler(ActionEvent.ACTION, e -> dialog.close());

        createDialog(title, body, OKButton);

        if (event != null) {
            dialog.setOnDialogClosed(event);
        }

        dialog.show();
    }

    public void createDialogWithOneBtn(@NamedArg("title") String title, @NamedArg("theBody") Node body) {
        createDialogWithOneBtn(title, body, null);
    }

    //创建有OK和cancel按钮的dialog
    public void createDialogWithOKAndCancel(@NamedArg("title") String title, @NamedArg("message") String message, @NamedArg("OKEvent") EventHandler<ActionEvent> OKEvent) {
        //dialog.setPrefHeight(rootPane.getPrefHeight());
        //dialog.setPrefWidth(rootPane.getPrefWidth());

        JFXButton CancelButton = new JFXButton("手滑了");
        CancelButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 12));
        CancelButton.setPrefWidth(60);
        CancelButton.setPrefHeight(30);

        JFXButton OKButton = new JFXButton("是！");
        OKButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 12));
        OKButton.setPrefWidth(60);
        OKButton.setPrefHeight(30);
        OKButton.setTextFill(Paint.valueOf("red"));
        OKButton.addEventHandler(ActionEvent.ACTION, e -> {
            dialog.close();
        });
        OKButton.addEventHandler(ActionEvent.ACTION, OKEvent);

        Text messageText = new Text(message);
        messageText.setFont(Font.font("Microsoft YaHei", 14));

        createDialog(title, messageText, CancelButton, OKButton);

        dialog.show();
    }

    public void createDialog(@NamedArg("title") String title, @NamedArg("theBody") Node body, @NamedArg("buttons") JFXButton... buttons) {

        JFXDialogLayout content = new JFXDialogLayout();

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD, 20));
        content.setHeading(titleLabel);

        content.setBody(body);
        content.setAlignment(Pos.CENTER);

        StackPane tempPane = new StackPane();
        tempPane.setPrefHeight(rootPane.getPrefHeight());
        tempPane.setPrefWidth(rootPane.getPrefWidth());
        rootPane.getChildren().add(tempPane);

        dialog = new JFXDialog(tempPane, content, JFXDialog.DialogTransition.TOP);

        dialog.setOnDialogClosed(event -> rootPane.getChildren().remove(tempPane));

        for (JFXButton button : buttons) {
            button.addEventHandler(ActionEvent.ACTION, e -> {
                dialog.close();
            });
        }

        content.setActions(buttons);

        dialog.show();
    }
}
