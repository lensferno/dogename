package main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;

public class Config implements Serializable {

    private BooleanProperty isRandomTimes=new SimpleBooleanProperty(true);

    private BooleanProperty isNameChoose=new SimpleBooleanProperty(true);

    private BooleanProperty ignorePast=new SimpleBooleanProperty(true);
    private int chosenTime=120;
    private short speed=20;

    private short minNumber;
    private short maxNumber;

    private BooleanProperty taoluMode=new SimpleBooleanProperty(false);

    private BooleanProperty equalMode=new SimpleBooleanProperty(true);

    private BooleanProperty newAlgo=new SimpleBooleanProperty(true);
    
    private BooleanProperty voicePlay=new SimpleBooleanProperty(true);

    public BooleanProperty isVoicePlay() {
        return voicePlay;
    }
    public void setVoicePlay(BooleanProperty voicePlay) {
        this.voicePlay = voicePlay;
    }
    
    public BooleanProperty isNewAlgo() {
        return newAlgo;
    }

    public void setNewAlgo(BooleanProperty newAlgo) {
        this.newAlgo = newAlgo;
    }

    public BooleanProperty isEqualMode() {
        return equalMode;
    }

    public void setEqualMode(BooleanProperty equalMode) {
        this.equalMode = equalMode;
    }

    public BooleanProperty isIgnorePast() {
        return ignorePast;
    }

    public BooleanProperty isTaoluMode(){
        return taoluMode;
    }

    public BooleanProperty isRandomTimes() {
        return isRandomTimes;
    }

    public BooleanProperty isNameChoose() {
        return isNameChoose;
    }

    public int getChosenTime() {
        return chosenTime;
    }

    public short getMaxNumber() {
        return maxNumber;
    }

    public short getMinNumber() {
        return minNumber;
    }

    public short getSpeed() {
        return speed;
    }

    public void setNameChoose(BooleanProperty nameChoose) {
        isNameChoose = nameChoose;
    }

    public void setChosenTime(int chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setIgnorePast(BooleanProperty ignorePast) {
        this.ignorePast = ignorePast;
    }

    public void setRandomTimes(BooleanProperty randomTimes) {
        isRandomTimes = randomTimes;
    }

    public void setMaxNumber(short maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setMinNumber(short minNumber) {
        this.minNumber = minNumber;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public void setTaoluMode(BooleanProperty taoluMode){
        this.taoluMode=taoluMode;
    }





}

