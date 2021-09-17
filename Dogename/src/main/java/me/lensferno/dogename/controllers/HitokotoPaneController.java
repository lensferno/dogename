package me.lensferno.dogename.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class HitokotoPaneController extends VBox {

    @FXML
    private Text hitokotoContent;

    @FXML
    private Text contentType;

    @FXML
    private Text contentInfo;

    public HitokotoPaneController(String hitokoto, String from, String author, String creator, String type) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/HitokotoPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hitokotoContent.setText("『 " + hitokoto + " 』");
        contentInfo.setText("出自：" + from + "　　|　　作者：" + author + "　　|　　上传者：" + creator);
        contentType.setText(type);
    }
}
