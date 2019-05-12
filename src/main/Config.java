package main;

import java.io.Serializable;

public class Config implements Serializable {

    private boolean isRandomTimes=true;

    private boolean isNameChoose=true;

    private boolean ignorePast=true;
    private int chosenTime=120;
    private short speed=80;

    private short minNumber;
    private short maxNumber;

    private boolean taoluMode=false;

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

