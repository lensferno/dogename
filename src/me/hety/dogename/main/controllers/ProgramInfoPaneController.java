package me.hety.dogename.main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.DataReleaser;

public class ProgramInfoPaneController extends VBox {

    @FXML
    public ImageView dogeView;

    public ProgramInfoPaneController(){
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
    }

}
