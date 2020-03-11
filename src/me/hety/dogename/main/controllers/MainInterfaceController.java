package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import me.hety.dogename.main.DialogBuilder.DialogBuilder;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.configs.ConfigLoader;
import me.hety.dogename.main.configs.MainConfig;

public class MainInterfaceController {

    //ConfigLoader configLoader=new ConfigLoader();


    @FXML
    private Pane rootPane;

    @FXML
    private JFXRadioButton nameChoose;

    @FXML
    private JFXButton showHistoryBtn;

    @FXML
    private JFXRadioButton numbChoose;

    @FXML
    private JFXButton anPaiBtn;

    @FXML
    private Pane mainPane;

    @FXML
    private Label topBar;

    @FXML
    private JFXButton showNameMangerButton;

    @FXML
    private Label chosen_1;

    @FXML
    private ImageView mainView;

    @FXML
    private JFXButton miniModeBtn;

    @FXML
    private Label chosen_2;

    MainConfig mainConfig;

    public void bindProperties(){
        nameChoose.selectedProperty().bindBidirectional(mainConfig.nameChoosePropertyProperty());
        //System.out.println(nameChoose.selectedProperty());
        //System.out.println(mainConfig.nameChoosePropertyProperty());
        //mainConfig.toString();
        //mainConfig.nameChoosePropertyProperty().addListener((observable, oldValue, newValue) -> configLoader.writeConfigToFile("files\\Config.json"));

    }

    public void setUpConfig(ConfigLoader configLoader){
        mainConfig=configLoader.readConfigFromFile("files\\Config.json");
    }

    @FXML
    void showProgramInfo(ActionEvent event) {
        new DialogMaker(rootPane).creatDialongWithOneBtn("程序信息",new ProgramInfoPaneController());
    }

    @FXML
    void nameChoose_selected(ActionEvent event) {

    }

    @FXML
    void showNameManger(ActionEvent event) {

        NameManagerPaneController nameManagerPaneController =new NameManagerPaneController();
        new DialogMaker(rootPane).creatDialongWithOneBtn("名单管理",nameManagerPaneController);
    }


    @FXML
    void numbChoose_selected(ActionEvent event) {

    }

    @FXML
    void showNunberSetting(ActionEvent event) {

        NumberSettingsPaneController numberSettingsPaneController =new NumberSettingsPaneController();
        new DialogMaker(rootPane).creatDialongWithOneBtn("更多设置",numberSettingsPaneController);
    }


    @FXML
    void miniMode(ActionEvent event) {

    }

    @FXML
    void showSettings(ActionEvent event) {
        SettingsPaneController settingsPaneController =new SettingsPaneController();
        new DialogMaker(rootPane).creatDialongWithOneBtn("更多设置",settingsPaneController);
        settingsPaneController.setToggleGroup();
    }

    @FXML
    void showHistory(ActionEvent event) {

        HistoryPaneController historyPaneController =new HistoryPaneController();
        new DialogMaker(rootPane).creatDialongWithOneBtn("更多设置",historyPaneController);
    }

    @FXML
    void anPai(ActionEvent event) {

    }

    public void setToggleGroup(){
        ToggleGroup toggleGroup =new ToggleGroup();
        nameChoose.setToggleGroup(toggleGroup);
        numbChoose.setToggleGroup(toggleGroup);
    }
}
