package me.hety.dogename.main.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.configs.MainConfig;

public class NumberSettingsPaneController extends VBox {
    public NumberSettingsPaneController(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/NumberSettingPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private JFXTextField minValueField;

    @FXML
    private JFXTextField maxValueField;

    public void bindProperties(MainConfig mainConfig){
        minValueField.textProperty().bindBidirectional(mainConfig.minNumberPropertyProperty());
        maxValueField.textProperty().bindBidirectional(mainConfig.maxNumberPropertyProperty());
    }

}
