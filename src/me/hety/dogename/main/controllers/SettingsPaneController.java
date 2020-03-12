package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.configs.MainConfig;

import java.util.logging.Logger;

public class SettingsPaneController extends VBox {
    @FXML
    private JFXCheckBox newAlgoBtn;

    @FXML
    private JFXSlider cycleTimesBar;

    @FXML
    private JFXCheckBox voicePlayBtn;

    @FXML
    private JFXSlider speedBar;

    @FXML
    private JFXCheckBox equalModeBtn;

    @FXML
    private JFXRadioButton ignoreOnce;

    @FXML
    private JFXRadioButton chooseOnce;

    @FXML
    private JFXCheckBox taoluModeBtn;

    @FXML
    private JFXRadioButton randomTimes;

    @FXML
    private JFXRadioButton fixedTimes;

    MainConfig mainConfig;

    Logger log = Logger.getLogger("SettingsPaneControllerLogger");

    public SettingsPaneController(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/SettingsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            log.warning("Error to load settings pane FXML: "+e.toString());
        }
    }

    public void setMainConfig(MainConfig mainConfig) {
        this.mainConfig = mainConfig;

    }

    public void bindProperties(MainConfig mainConfig){
        setMainConfig(mainConfig);

        ignoreOnce.selectedProperty().bindBidirectional(mainConfig.ignorePastPropertyProperty());
        chooseOnce.setSelected(!mainConfig.isIgnorePastProperty());

        randomTimes.selectedProperty().bindBidirectional(mainConfig.randomTimesPropertyProperty());
        fixedTimes.setSelected(mainConfig.isRandomTimesProperty());

        equalModeBtn.selectedProperty().bindBidirectional(mainConfig.equalModePropertyProperty());

        taoluModeBtn.selectedProperty().bindBidirectional(mainConfig.taoluModePropertyProperty());

        newAlgoBtn.selectedProperty().bindBidirectional(mainConfig.newAlgoPropertyProperty());

        voicePlayBtn.selectedProperty().bindBidirectional(mainConfig.voicePlayPropertyProperty());

        cycleTimesBar.valueProperty().bindBidirectional(mainConfig.cycleTimesPropertyProperty());

        speedBar.valueProperty().bindBidirectional(mainConfig.speedPropertyProperty());
    }

    public void setToggleGroup(){
        ToggleGroup pastGroup=new ToggleGroup();
        chooseOnce.setToggleGroup(pastGroup);
        ignoreOnce.setToggleGroup(pastGroup);

        ToggleGroup fixedTimesGroup=new ToggleGroup();
        randomTimes.setToggleGroup(fixedTimesGroup);
        fixedTimes.setToggleGroup(fixedTimesGroup);
    }

    @FXML
    void showVoiceSettingsPane(ActionEvent event) {
        //new DialogMaker()
    }

    @FXML
    void ignoreOnce_selected(ActionEvent event) {

    }

    @FXML
    void showEqualMode(ActionEvent event) {

    }

    @FXML
    void clearIgnoreList(ActionEvent event) {

    }

    @FXML
    void equalBtnAction(ActionEvent event) {

    }


    @FXML
    void chooseOnce_selected(ActionEvent event) {

    }

    @FXML
    void showTaoluMode(ActionEvent event) {

    }


    @FXML
    void clearTaoluList(ActionEvent event) {

    }

    @FXML
    void taoluModeBtn_Aciton(ActionEvent event) {

    }

    @FXML
    void randomTimes_selected(ActionEvent event) {

    }

    @FXML
    void fixedTimes_selected(ActionEvent event) {

    }

    @FXML
    void setChosenTimeHere(ActionEvent event) {

    }

    @FXML
    void setSpeedHere(ActionEvent event) {

    }

    @FXML
    void newAlgoBtnAction(ActionEvent event) {

    }

    @FXML
    void voicePlayBtnAction(ActionEvent event) {

    }


}
