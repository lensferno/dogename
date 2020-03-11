package me.hety.dogename.main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.logging.Logger;

public class GushiciPaneController extends VBox {

    Logger log=Logger.getLogger("GushiciPaneLogger");
    @FXML
    public Text contentText;

    @FXML
    public Text contentType;

    @FXML
    public Text contentInfo;

    public GushiciPaneController (String content,String works,String author,String type){

        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/GushiciPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            log.warning("Error to load Gushici pane FXML: "+e.toString());
            //e.printStackTrace();
        }
        contentText.setText("“"+content+"”");
        contentType.setText("《"+works+"》"+"——"+author);
        contentInfo.setText(type);
    }
}
