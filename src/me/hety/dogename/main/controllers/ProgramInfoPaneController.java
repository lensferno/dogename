package me.hety.dogename.main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.hety.dogename.main.DataReleaser;
import me.hety.dogename.main.DialogMaker;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProgramInfoPaneController extends VBox {

    @FXML
    public ImageView dogeView;

    Pane rootPane;

    public ProgramInfoPaneController(Pane rootPane){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/ProgramInfoPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
        Image dogeImage=new Image(DataReleaser.getDogenameStream());
        dogeView.setImage(dogeImage);
        this.rootPane=rootPane;
    }


    @FXML
    void showLicense(ActionEvent event) {
        TextArea textArea=new TextArea(LicenseText.text);
        textArea.setFont(Font.font("Microsoft YaHei",14));
        textArea.setMinWidth(600);
        textArea.setPrefHeight(400);
        textArea.setEditable(false);
        new DialogMaker(rootPane).creatDialogWithOneBtn("开源协议（LGPL v3）",textArea);
    }

    @FXML
    void showHelp(ActionEvent event) {

    }

    @FXML
    void viewCode(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/eatenid/dogename"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
