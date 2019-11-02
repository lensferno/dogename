package main;

import java.io.Serializable;

public class Config implements Serializable {

    private boolean isRandomTimes=true;

    private boolean isNameChoose=true;

    private boolean ignorePast=true;
    private int chosenTime=120;
    private short speed=20;

    private short minNumber;
    private short maxNumber;

    private boolean taoluMode=false;

    private boolean equalMode=true;

    private boolean newAlgo=true;
    
    private boolean voicePlay=true;
    


    public boolean isVoicePlay() {
        return voicePlay;
    }

    public void setVoicePlay(boolean voicePlay) {
        this.voicePlay = voicePlay;
    }
    
    public boolean isNewAlgo() {
        return newAlgo;
    }

    public void setNewAlgo(boolean newAlgo) {
        this.newAlgo = newAlgo;
    }

    public boolean isEqualMode() {
        return equalMode;
    }

    public void setEqualMode(boolean equalMode) {
        this.equalMode = equalMode;
    }

    public boolean isIgnorePast() {
        return ignorePast;
    }

    public boolean isTaoluMode(){
        return taoluMode;
    }

    public boolean isRandomTimes() {
        return isRandomTimes;
    }

    public boolean isNameChoose() {
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

    public void setNameChoose(boolean nameChoose) {
        isNameChoose = nameChoose;
    }

    public void setChosenTime(int chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setIgnorePast(boolean ignorePast) {
        this.ignorePast = ignorePast;
    }

    public void setRandomTimes(boolean randomTimes) {
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

    public void setTaoluMode(boolean taoluMode){
        this.taoluMode=taoluMode;
    }





}

