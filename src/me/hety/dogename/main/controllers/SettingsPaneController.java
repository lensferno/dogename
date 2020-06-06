package me.hety.dogename.main.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.configs.MainConfig;
import me.hety.dogename.main.configs.VoiceConfig;
import me.hety.dogename.main.data.NameData;

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
    VoiceConfig voiceConfig;

    Pane rootPane;

    NameData nameData;

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

    public void setVoiceConfig(VoiceConfig voiceConfig) {
        this.voiceConfig = voiceConfig;
    }

    public void setRootPane(Pane rootPane){
        this.rootPane=rootPane;
    }

    public void bindProperties(MainConfig mainConfig){
        setMainConfig(mainConfig);

        ignoreOnce.selectedProperty().bindBidirectional(mainConfig.ignorePastPropertyProperty());
        chooseOnce.setSelected(!mainConfig.isIgnorePastProperty());

        randomTimes.selectedProperty().bindBidirectional(mainConfig.randomTimesPropertyProperty());
        fixedTimes.setSelected(!mainConfig.isRandomTimesProperty());

        equalModeBtn.selectedProperty().bindBidirectional(mainConfig.equalModePropertyProperty());

        taoluModeBtn.selectedProperty().bindBidirectional(mainConfig.taoluModePropertyProperty());

        newAlgoBtn.selectedProperty().bindBidirectional(mainConfig.newAlgoPropertyProperty());

        voicePlayBtn.selectedProperty().bindBidirectional(mainConfig.voicePlayPropertyProperty());

        cycleTimesBar.valueProperty().bindBidirectional(mainConfig.cycleTimesPropertyProperty());

        speedBar.valueProperty().bindBidirectional(mainConfig.speedPropertyProperty());

        mainConfig.ignorePastPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==true){
                //如果 忽略被点过的名字 选上就把套路模式的按钮给取消掉
                taoluModeBtn.setSelected(false);
            }else{
                //如果 忽略被点过的名字 被取消后就把机会均等模式的按钮给取消掉
                equalModeBtn.setSelected(false);
            }
        });
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
        VoiceSettingsPaneController voiceSettingsPaneController=new VoiceSettingsPaneController();
        voiceSettingsPaneController.bindPropertied(voiceConfig);
        new DialogMaker(rootPane).creatDialogWithOneBtn("语音设置",voiceSettingsPaneController);
    }

    @FXML
    void showEqualMode(ActionEvent event) {
        new DialogMaker(rootPane).creatMessageDialog("啥玩意？","勾选“机会均等”后，将会保存已点过的的名字和数字到文件中，下次启动时仍不会被点到，直到全部名字\n或数字被点完 或点击“机会均等”的“重置”按钮。\n注意：仅保存“这次点过就不点了”模式下选中的名字或数字。");

    }

    @FXML
    void clearIgnoreList(ActionEvent event) {
        new DialogMaker(rootPane).creatDialogWithOKAndCancel("真的吗？","真的要重置吗？",(e)->{
            nameData.clearNumberIgnoreList();
            nameData.clearNameIgnoreList();
        });
    }

    @FXML
    void equalBtnAction(ActionEvent event) {
        if(!mainConfig.isIgnorePastProperty()){
            equalModeBtn.setSelected(false);
            new DialogMaker(rootPane).creatMessageDialog("且慢","该模式不能在“被点过的还要点”这种情况下使用，若要使用，请开启“这次点过就不点了”模式。");
        }
    }

    @FXML
    void showTaoluMode(ActionEvent event) {
        new DialogMaker(rootPane).creatMessageDialog("啥玩意？","旧称“套路模式”，勾选后会使被点过的名字在挑选列表中多出现4~5次，增加了再次被点中的几率。\n注意：仅在勾选此模式后点中的名字才会被多增加4~5次，不勾选时选中的名字不受影响。\n退出后会自动重置，不影响下次使用。");
    }

    @FXML
    void clearTaoluList(ActionEvent event) {
        new DialogMaker(rootPane).creatDialogWithOKAndCancel("真的吗？","真的要重置吗？",(e)-> nameData.clearTaoluedName());
    }

    @FXML
    void taoluModeBtn_Aciton(ActionEvent event) {
        if(mainConfig.isIgnorePastProperty()){
            taoluModeBtn.setSelected(false);
            new DialogMaker(rootPane).creatMessageDialog("且慢","该模式不能“这次点过就不点了”这种情况下使用，若要使用，请开启“被点过的还要点”模式。");
        }
    }

    public void setNameData(NameData nameData) {
        this.nameData = nameData;
    }
}
