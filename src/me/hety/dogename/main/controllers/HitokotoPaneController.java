package me.hety.dogename.main.controllers;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.hety.dogename.main.Common;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.sayings.Gushici;

import java.util.logging.Logger;

public class HitokotoPaneController extends VBox {

    Logger log=Logger.getLogger("HitokotoPaneLogger");

    @FXML
    private Text hitokotoContent;

    @FXML
    private Text contentType;

    @FXML
    private Text contentInfo;

    public HitokotoPaneController(String hitokoto, String from,String author,String creator,String type){

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/HitokotoPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            log.warning("Error to load Gushici pane FXML: "+e.toString());
            //e.printStackTrace();
        }
        hitokotoContent.setText("『 "+hitokoto+" 』");
        contentInfo.setText("出自："+from+"　　|　　作者："+author+"　　|　　上传者："+creator);
        contentType.setText(type);
    }
}
