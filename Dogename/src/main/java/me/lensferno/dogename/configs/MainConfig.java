package me.lensferno.dogename.configs;

import com.google.gson.annotations.Expose;
import javafx.beans.property.*;

public class MainConfig {

    // ---------------------- Default values ---------------------------------------------------------

    private final int currentVersion = 3;

    @Expose
    public static final boolean DEFAULT_NAME_CHOOSE = true;

    public static final int METHOD_NAME = 0; // 名字挑选法
    public static final int METHOD_NUMBER = 1; // 数字挑选法

    public static final int DEFAULT_MAX_TOTAL_COUNT = 120; // 默认轮回次数:120
    public static final int DEFAULT_SPEED = 80; // 默认速度:20ms,对应滑动条80的位置

    public static final boolean DEFAULT_RANDOM_TIMES = true; // 默认挑选轮回次数是否随机:ture

    public static final boolean DEFAULT_IGNORE_PAST = true; // 默认忽略已经点过的名字:ture

    public static final boolean DEFAULT_EQUAL_MODE = true; // 默认开启"机会均等"

    public static final boolean DEFAULT_NEW_ALGO = true; // 默认使用新算法"Java sec random"
    public static final boolean DEFAULT_VOICE_PLAY = true; // 默认使用语音播报

    public static final boolean DEFAULT_SHOW_SAYING = true;

    // ----------------------Properties----------------------------------------------------------------

    private SimpleBooleanProperty nameChoose;

    private SimpleBooleanProperty randomCount; // 挑选次数是否随机
    private SimpleBooleanProperty passSelectedResult; // 是否忽略已经被点过的名字/数字

    private SimpleIntegerProperty chooseMethod; // 挑选方式: 0->名字挑选法 1->数字挑选法
    private SimpleIntegerProperty maxTotalCount; // 挑选轮回次数

    private SimpleIntegerProperty speed; // 速度

    private SimpleStringProperty minNumber; // 最小值
    private SimpleStringProperty maxNumber; // 最大值

    private SimpleBooleanProperty equalMode; // 是否开启"机会均等"

    private SimpleBooleanProperty secureRandom; // 是否使用secure random
    private SimpleBooleanProperty voicePlay; // 是否使用语音播报

    private SimpleBooleanProperty showSaying;

    // -------------------------- 初始化 --------------------------------------------------------------
    public MainConfig() {
        randomCount = new SimpleBooleanProperty(DEFAULT_RANDOM_TIMES);
        passSelectedResult = new SimpleBooleanProperty(DEFAULT_IGNORE_PAST);

        chooseMethod = new SimpleIntegerProperty(METHOD_NAME);
        nameChoose = new SimpleBooleanProperty(DEFAULT_NAME_CHOOSE);

        maxTotalCount = new SimpleIntegerProperty(DEFAULT_MAX_TOTAL_COUNT);

        speed = new SimpleIntegerProperty(DEFAULT_SPEED);

        minNumber = new SimpleStringProperty("0");
        maxNumber = new SimpleStringProperty("10");

        equalMode = new SimpleBooleanProperty(DEFAULT_EQUAL_MODE);

        secureRandom = new SimpleBooleanProperty(DEFAULT_NEW_ALGO);
        voicePlay = new SimpleBooleanProperty(DEFAULT_VOICE_PLAY);

        showSaying = new SimpleBooleanProperty(DEFAULT_SHOW_SAYING);
    }

    // -------------------------- Getters and Setters ---------------------------------------------

    public boolean getNameChoose() {
        return nameChoose.get();
    }

    public SimpleBooleanProperty nameChooseProperty() {
        return nameChoose;
    }

    public void setNameChoose(boolean nameChoose) {
        this.nameChoose.set(nameChoose);
    }

    public boolean getRandomCount() {
        return randomCount.get();
    }

    public SimpleBooleanProperty randomCountProperty() {
        return randomCount;
    }

    public void setRandomCount(boolean randomCount) {
        this.randomCount.set(randomCount);
    }

    public boolean getPassSelectedResult() {
        return passSelectedResult.get();
    }

    public SimpleBooleanProperty passSelectedResultProperty() {
        return passSelectedResult;
    }

    public void setPassSelectedResult(boolean passSelectedResult) {
        this.passSelectedResult.set(passSelectedResult);
    }

    public int getChooseMethod() {
        return chooseMethod.get();
    }

    public SimpleIntegerProperty chooseMethodProperty() {
        return chooseMethod;
    }

    public void setChooseMethod(int chooseMethod) {
        this.chooseMethod.set(chooseMethod);
    }

    public int getMaxTotalCount() {
        return maxTotalCount.get();
    }

    public SimpleIntegerProperty maxTotalCountProperty() {
        return maxTotalCount;
    }

    public void setMaxTotalCount(int maxTotalCount) {
        this.maxTotalCount.set(maxTotalCount);
    }

    public int getSpeed() {
        return speed.get();
    }

    public SimpleIntegerProperty speedProperty() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public String getMinNumber() {
        return minNumber.get();
    }

    public SimpleStringProperty minNumberProperty() {
        return minNumber;
    }

    public void setMinNumber(String minNumber) {
        this.minNumber.set(minNumber);
    }

    public String getMaxNumber() {
        return maxNumber.get();
    }

    public SimpleStringProperty maxNumberProperty() {
        return maxNumber;
    }

    public void setMaxNumber(String maxNumber) {
        this.maxNumber.set(maxNumber);
    }

    public boolean getEqualMode() {
        return equalMode.get();
    }

    public SimpleBooleanProperty equalModeProperty() {
        return equalMode;
    }

    public void setEqualMode(boolean equalMode) {
        this.equalMode.set(equalMode);
    }

    public boolean getSecureRandom() {
        return secureRandom.get();
    }

    public SimpleBooleanProperty secureRandomProperty() {
        return secureRandom;
    }

    public void setSecureRandom(boolean secureRandom) {
        this.secureRandom.set(secureRandom);
    }

    public boolean getVoicePlay() {
        return voicePlay.get();
    }

    public SimpleBooleanProperty voicePlayProperty() {
        return voicePlay;
    }

    public void setVoicePlay(boolean voicePlay) {
        this.voicePlay.set(voicePlay);
    }

    public boolean isShowSaying() {
        return showSaying.get();
    }

    public SimpleBooleanProperty showSayingProperty() {
        return showSaying;
    }

    public void setShowSaying(boolean showSaying) {
        this.showSaying.set(showSaying);
    }

    public int getCurrentConfigVersion() {
        return currentVersion;
    }

}
