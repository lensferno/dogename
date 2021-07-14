package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.data.NameData;

public class NumberSettingsPaneController extends VBox {
    NameData nameData;
    public NumberSettingsPaneController(NameData nameData){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/NumberSettingPane.fxml"));
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

        minValueField.textProperty().bindBidirectional(mainConfig.minNumberProperty());
        minValueField.textProperty().addListener((observable, oldValue, newValue) -> nameData.clearNumberIgnoreList() );

        maxValueField.textProperty().bindBidirectional(mainConfig.maxNumberProperty());
        maxValueField.textProperty().addListener((observable, oldValue, newValue) -> nameData.clearNumberIgnoreList() );
    }

}
