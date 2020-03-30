package me.hety.dogename.main.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.configs.MainConfig;
import me.hety.dogename.main.data.NameData;

public class NumberSettingsPaneController extends VBox {
    NameData nameData;
    public NumberSettingsPaneController(NameData nameData){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/NumberSettingPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.nameData=nameData;
    }

    @FXML
    private JFXTextField minValueField;

    @FXML
    private JFXTextField maxValueField;

    public void bindProperties(MainConfig mainConfig){

        minValueField.textProperty().bindBidirectional(mainConfig.minNumberPropertyProperty());
        minValueField.textProperty().addListener((observable, oldValue, newValue) -> nameData.clearNumberIgnoreList() );

        maxValueField.textProperty().bindBidirectional(mainConfig.maxNumberPropertyProperty());
        maxValueField.textProperty().addListener((observable, oldValue, newValue) -> nameData.clearNumberIgnoreList() );
    }

}
