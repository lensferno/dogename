package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.utils.DialogMaker;


public class SettingsPaneController extends VBox {

    MainConfig mainConfig;
    VoiceConfig voiceConfig;
    Pane rootPane;
    Data data;
    @FXML
    private JFXCheckBox showSayingBtn;
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
    private JFXRadioButton randomTimes;
    @FXML
    private JFXRadioButton fixedTimes;


    public SettingsPaneController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/SettingsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainConfig(MainConfig mainConfig) {
        this.mainConfig = mainConfig;
    }

    public void setVoiceConfig(VoiceConfig voiceConfig) {
        this.voiceConfig = voiceConfig;
    }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void bindProperties(MainConfig mainConfig) {
        setMainConfig(mainConfig);

        ignoreOnce.selectedProperty().bindBidirectional(mainConfig.passSelectedResultProperty());
        chooseOnce.setSelected(!mainConfig.getPassSelectedResult());

        randomTimes.selectedProperty().bindBidirectional(mainConfig.randomCountProperty());
        fixedTimes.setSelected(!mainConfig.getRandomCount());

        equalModeBtn.selectedProperty().bindBidirectional(mainConfig.equalModeProperty());

        newAlgoBtn.selectedProperty().bindBidirectional(mainConfig.secureRandomProperty());

        voicePlayBtn.selectedProperty().bindBidirectional(mainConfig.voicePlayProperty());

        cycleTimesBar.valueProperty().bindBidirectional(mainConfig.maxTotalCountProperty());

        speedBar.valueProperty().bindBidirectional(mainConfig.speedProperty());

        showSayingBtn.selectedProperty().bindBidirectional(mainConfig.showSayingProperty());

        mainConfig.passSelectedResultProperty().addListener((observable, oldValue, isIgnorePast) -> {
            if (!isIgnorePast) {
                //如果 忽略被点过的名字 被取消后就把机会均等模式的按钮给取消掉
                equalModeBtn.setSelected(false);
            }
        });
    }

    public void setToggleGroup() {
        ToggleGroup pastGroup = new ToggleGroup();
        chooseOnce.setToggleGroup(pastGroup);
        ignoreOnce.setToggleGroup(pastGroup);

        ToggleGroup fixedTimesGroup = new ToggleGroup();
        randomTimes.setToggleGroup(fixedTimesGroup);
        fixedTimes.setToggleGroup(fixedTimesGroup);
    }

    @FXML
    void showVoiceSettingsPane(ActionEvent event) {
        VoiceSettingsPaneController voiceSettingsPaneController = new VoiceSettingsPaneController();
        voiceSettingsPaneController.bindPropertied(voiceConfig);
        new DialogMaker(rootPane).createDialogWithOneBtn("语音设置", voiceSettingsPaneController);
    }

    @FXML
    void showEqualMode(ActionEvent event) {
        new DialogMaker(rootPane).createMessageDialog("什么？",
                "//有待补充。;-)");

    }

    @FXML
    void clearIgnoreList(ActionEvent event) {
        new DialogMaker(rootPane).createDialogWithOKAndCancel("真的吗？", "真的要重置吗？", (e) -> {
            data.clearNumberIgnoreList();
            data.clearNameIgnoreList();
        });
    }

    @FXML
    void equalBtnAction(ActionEvent event) {
        if (!mainConfig.getPassSelectedResult()) {
            equalModeBtn.setSelected(false);
            new DialogMaker(rootPane).createMessageDialog("且慢",
                    "无法在“概率均分”的模式下使用，如需使用请在“人人有份”模式下启用。");
        }
    }

    public void setData(Data data) {
        this.data = data;
    }
}
