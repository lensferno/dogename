package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.voice.VoicePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

public class VoiceSettingsPaneController extends VBox {
    private static final ObservableList<String> speakerList = FXCollections.observableArrayList();

    //度小宇=1,度小美=0,度逍遥=3,度丫丫=4
    //度博文=106,度小童=110,度小萌=111,度米朵=103,度小娇=5
    private final String[] speakers = {
            "1", "0", "3", "4",
            "106", "110", "111", "103", "5"};

    Logger log = Logger.getLogger("VoiceSettingsPaneControllerLogger");

    @FXML
    private JFXSlider intonationBar;
    @FXML
    private JFXComboBox<String> speakerSelectBar;
    @FXML
    private JFXSlider voiceSpeedBar;
    @FXML
    private JFXRadioButton wavFormatButton;
    @FXML
    private JFXRadioButton mp3FormatButton;

    private final Pane rootPane;

    public VoiceSettingsPaneController(Pane rootPane) {
        this.rootPane = rootPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/VoiceSettingsPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            log.warning("Error to load settings pane FXML: " + e);
        }
        if (speakerList.isEmpty()) {
            speakerList.addAll(
                    "度小宇=1", "度小美=0", "度逍遥=3", "度丫丫=4"
                    // 非高级API用户，这些精品音频库用不了（等把“高级设置”弄出来再说吧...）
                    /*, "度博文=106", "度小童=110", "度小萌=111", "度米朵=103", "度小娇=5"*/);
        }

        speakerSelectBar.setItems(speakerList);
    }

    @FXML
    ToggleGroup audioFormatToggleGroup = new ToggleGroup();
    HashMap<Integer, Toggle> audioFormatMap = new HashMap<>();

    public void bindPropertied() {
        wavFormatButton.setToggleGroup(audioFormatToggleGroup);
        mp3FormatButton.setToggleGroup(audioFormatToggleGroup);
        audioFormatMap.put(VoiceConfig.AUDIO_FORMAT_WAV, wavFormatButton);
        audioFormatMap.put(VoiceConfig.AUDIO_FORMAT_MP3, mp3FormatButton);

        audioFormatToggleGroup.selectToggle(audioFormatMap.get(VoicePlayer.voiceConfig.getAudioFormat()));
        audioFormatToggleGroup.selectedToggleProperty().addListener((observable, oldValue, selectBtn) ->
                VoicePlayer.voiceConfig.setAudioFormat(selectBtn.equals(wavFormatButton) ? VoiceConfig.AUDIO_FORMAT_WAV : VoiceConfig.AUDIO_FORMAT_MP3));

        speakerSelectBar.getSelectionModel().select(VoicePlayer.voiceConfig.getSpeaker());
        speakerSelectBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            int selectedSpeakerIndex = speakerSelectBar.getSelectionModel().getSelectedIndex();

            VoicePlayer.voiceConfig.setSpeakerIdString(speakers[selectedSpeakerIndex]);
            VoicePlayer.voiceConfig.setSpeaker(selectedSpeakerIndex);
        });

        voiceSpeedBar.valueProperty().bindBidirectional(VoicePlayer.voiceConfig.speedProperty());
        intonationBar.valueProperty().bindBidirectional(VoicePlayer.voiceConfig.intonationProperty());
    }

    @FXML
    void clearCache(ActionEvent event) {
        File[] cacheFiles = new File(VoicePlayer.cachePath).listFiles();
        if (cacheFiles != null) {
            for (File cacheFile : cacheFiles) {
                cacheFile.delete();
            }
        }
        new DialogMaker(rootPane).createMessageDialog("嘿咻~","缓存已清除");
    }
}