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
        hitokotoContent.setText(String.format("『 %s 』", hitokoto));
        contentInfo.setText(String.format("出自：%s　　|　　作者：%s　　|　　上传者：%s", from, author, creator));
        contentType.setText(type);
    }
}
