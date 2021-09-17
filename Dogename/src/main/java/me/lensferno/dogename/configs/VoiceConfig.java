package me.lensferno.dogename.configs;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class VoiceConfig {

    public final int DEFAULT_SPEED = 5;
    public final int DEFAULT_INTONATION = 5;


    //度小宇=1,度小美=0,度逍遥=3,度丫丫=4
    //度博文=106,度小童=110,度小萌=111,度米朵=103,度小娇=5
    private String speaker;
    private int selectedSpeaker;
    private final SimpleDoubleProperty speed;
    private final SimpleDoubleProperty intonation;

    public VoiceConfig() {
        selectedSpeaker = 0;
        speaker = "1";
        speed = new SimpleDoubleProperty(DEFAULT_SPEED);
        intonation = new SimpleDoubleProperty(DEFAULT_INTONATION);
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public int getSelectedSpeaker() {
        return selectedSpeaker;
    }

    public void setSelectedSpeaker(int selectedSpeaker) {
        this.selectedSpeaker = selectedSpeaker;
    }

    public double getSpeed() {
        return speed.get();
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }

    public SimpleDoubleProperty speedProperty() {
        return speed;
    }

    public double getIntonation() {
        return intonation.get();
    }

    public void setIntonation(double intonation) {
        this.intonation.set(intonation);
    }

    public SimpleDoubleProperty intonationProperty() {
        return intonation;
    }

    @FXML
    void showAdvancedVoiceSettings(ActionEvent event) {

    }


}
