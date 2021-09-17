package me.lensferno.dogename.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class GushiciPaneController extends VBox {

    @FXML
    public Text contentText;

    @FXML
    public Text contentType;

    @FXML
    public Text contentInfo;

    public GushiciPaneController(String content, String title, String author, String type) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/GushiciPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        contentText.setText("“" + content + "”");
        contentInfo.setText("《" + title + "》" + "——" + author);
        contentType.setText(type);
    }
}
