package me.lensferno.dogename.configs;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class VoiceConfig {
    @Expose
    public static final int DEFAULT_SPEED = 5;
    @Expose
    public static final int DEFAULT_INTONATION = 5;

    @Expose
    public static final int AUDIO_FORMAT_WAV = 6;
    @Expose
    public static final int AUDIO_FORMAT_MP3 = 3;

    //度小宇=1,度小美=0,度逍遥=3,度丫丫=4
    //度博文=106,度小童=110,度小萌=111,度米朵=103,度小娇=5
    private String speakerIdString;
    private int audioFormat;
    private int speaker;
    private final SimpleDoubleProperty speed;
    private final SimpleDoubleProperty intonation;

    public VoiceConfig() {
        this.speaker = 0;
        this.speakerIdString = "1";
        this.speed = new SimpleDoubleProperty(DEFAULT_SPEED);
        this.intonation = new SimpleDoubleProperty(DEFAULT_INTONATION);
        this.audioFormat = AUDIO_FORMAT_WAV;
    }

    public String getSpeakerIdString() {
        return speakerIdString;
    }

    public void setSpeakerIdString(String speakerIdString) {
        this.speakerIdString = speakerIdString;
    }

    public int getSpeaker() {
        return speaker;
    }

    public void setSpeaker(int speaker) {
        this.speaker = speaker;
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

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public static String getAudioFileSuffix(int format) {
        switch (format) {
            case VoiceConfig.AUDIO_FORMAT_WAV:
                return "wav";
            case VoiceConfig.AUDIO_FORMAT_MP3:
                return "mp3";
            default:
                return "cache";
        }
    }
}
