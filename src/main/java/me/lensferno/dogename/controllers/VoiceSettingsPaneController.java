package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import me.lensferno.dogename.configs.VoiceConfig;

import java.util.logging.Logger;

public class VoiceSettingsPaneController extends VBox {
    Logger log = Logger.getLogger("VoiceSettingsPaneControllerLogger");

    VoiceConfig voiceConfig=new VoiceConfig();


    @FXML
    private JFXSlider intonationBar;

    @FXML
    private JFXComboBox<String> speakerSelectBar;

    @FXML
    private JFXSlider voiceSpeedBar;

    public static final ObservableList<String> shownSpeakerList = FXCollections.observableArrayList();

    private final String[] speakers={
            "1","0","3","4",
            "106","110","111","103","5"};
    //度小宇=1,度小美=0,度逍遥=3,度丫丫=4
    //度博文=106,度小童=110,度小萌=111,度米朵=103,度小娇=5

    public VoiceSettingsPaneController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/VoiceSettingsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            log.warning("Error to load settings pane FXML: " + e.toString());
        }
        if(shownSpeakerList.isEmpty()) {
            shownSpeakerList.addAll(
                    "度小宇=1", "度小美=0", "度逍遥=3", "度丫丫=4",
                    "度博文=106", "度小童=110", "度小萌=111", "度米朵=103", "度小娇=5");
        }

        speakerSelectBar.setItems(shownSpeakerList);
    }

    public void bindPropertied(VoiceConfig voiceConfig){

        this.voiceConfig=voiceConfig;

        //speakerSelectBar.

        speakerSelectBar.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("what?"+newValue.getSelectedIndex());
            this.voiceConfig.setSpeaker(speakers[newValue.getSelectedIndex()]);
            this.voiceConfig.setSelectedSpeaker(newValue.getSelectedIndex());
        });

        //speakerSelectBar.setValue(this.voiceConfig.getSpeaker());

        voiceSpeedBar.valueProperty().bindBidirectional(voiceConfig.speedProperty());
        intonationBar.valueProperty().bindBidirectional(voiceConfig.intonationProperty());

        speakerSelectBar.getSelectionModel().select(this.voiceConfig.getSelectedSpeaker());
    }

    @FXML
    void showAdvancedVoiceSettings(ActionEvent event) {

    }



}


