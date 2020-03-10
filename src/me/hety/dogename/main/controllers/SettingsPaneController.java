package me.hety.dogename.main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class SettingsPaneController extends VBox {
    public SettingsPaneController(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/SettingsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){

        }
    }
}
