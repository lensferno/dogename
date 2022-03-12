package me.lensferno.dogename.configs;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainConfig {

    // ---------------------- Default values ---------------------------------------------------------
    @Expose
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
    private final SimpleBooleanProperty randomCount; // 挑选次数是否随机
    private final SimpleBooleanProperty ignoreSelectedResult; // 是否忽略已经被点过的名字/数字

    private final SimpleIntegerProperty chooseMethod; // 挑选方式: 0->名字挑选法 1->数字挑选法
    private final SimpleIntegerProperty maxTotalCount; // 挑选轮回次数

    private final SimpleIntegerProperty speed; // 速度

    private final SimpleStringProperty minNumber; // 最小值
    private final SimpleStringProperty maxNumber; // 最大值

    private final SimpleBooleanProperty equalMode; // 是否开启"机会均等"

    private final SimpleBooleanProperty secureRandom; // 是否使用secure random
    private final SimpleBooleanProperty voicePlay; // 是否使用语音播报

    private final SimpleBooleanProperty showSaying;

    // -------------------------- 初始化 --------------------------------------------------------------
    public MainConfig() {
        randomCount = new SimpleBooleanProperty(DEFAULT_RANDOM_TIMES);
        ignoreSelectedResult = new SimpleBooleanProperty(DEFAULT_IGNORE_PAST);

        chooseMethod = new SimpleIntegerProperty(METHOD_NAME);

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
    public boolean getRandomCount() {
        return randomCount.get();
    }

    public void setRandomCount(boolean randomCount) {
        this.randomCount.set(randomCount);
    }

    public SimpleBooleanProperty randomCountProperty() {
        return randomCount;
    }

    public boolean getIgnoreSelectedResult() {
        return ignoreSelectedResult.get();
    }

    public void setIgnoreSelectedResult(boolean ignoreSelectedResult) {
        this.ignoreSelectedResult.set(ignoreSelectedResult);
    }

    public SimpleBooleanProperty ignoreSelectedResultProperty() {
        return ignoreSelectedResult;
    }

    public int getChooseMethod() {
        return chooseMethod.get();
    }

    public void setChooseMethod(int chooseMethod) {
        this.chooseMethod.set(chooseMethod);
    }

    public SimpleIntegerProperty chooseMethodProperty() {
        return chooseMethod;
    }

    public int getMaxTotalCount() {
        return maxTotalCount.get();
    }

    public void setMaxTotalCount(int maxTotalCount) {
        this.maxTotalCount.set(maxTotalCount);
    }

    public SimpleIntegerProperty maxTotalCountProperty() {
        return maxTotalCount;
    }

    public int getSpeed() {
        return speed.get();
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public SimpleIntegerProperty speedProperty() {
        return speed;
    }

    public String getMinNumber() {
        return minNumber.get();
    }

    public void setMinNumber(String minNumber) {
        this.minNumber.set(minNumber);
    }

    public SimpleStringProperty minNumberProperty() {
        return minNumber;
    }

    public String getMaxNumber() {
        return maxNumber.get();
    }

    public void setMaxNumber(String maxNumber) {
        this.maxNumber.set(maxNumber);
    }

    public SimpleStringProperty maxNumberProperty() {
        return maxNumber;
    }

    public boolean getEqualMode() {
        return equalMode.get();
    }

    public void setEqualMode(boolean equalMode) {
        this.equalMode.set(equalMode);
    }

    public SimpleBooleanProperty equalModeProperty() {
        return equalMode;
    }

    public boolean getSecureRandom() {
        return secureRandom.get();
    }

    public void setSecureRandom(boolean secureRandom) {
        this.secureRandom.set(secureRandom);
    }

    public SimpleBooleanProperty secureRandomProperty() {
        return secureRandom;
    }

    public boolean getVoicePlay() {
        return voicePlay.get();
    }

    public void setVoicePlay(boolean voicePlay) {
        this.voicePlay.set(voicePlay);
    }

    public SimpleBooleanProperty voicePlayProperty() {
        return voicePlay;
    }

    public boolean isShowSaying() {
        return showSaying.get();
    }

    public void setShowSaying(boolean showSaying) {
        this.showSaying.set(showSaying);
    }

    public SimpleBooleanProperty showSayingProperty() {
        return showSaying;
    }
}
