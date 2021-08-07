package me.lensferno.dogename.select.core;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.data.Data;

import me.lensferno.dogename.utils.Random;
import me.lensferno.dogename.voice.VoicePlayer;

public final class Worker {

    private final Random randomNumber = new Random();

    private StringProperty[] labelTexts;
    private final SimpleBooleanProperty stoppedIndicator = new SimpleBooleanProperty(true);

    private final MainConfig config;

    //挑选方法
    private final int selectMethod = MainConfig.METHOD_NAME;
    private final Data data;

    private final History history;

    private final VoicePlayer voicePlayer;

    private int speed = 0;

    //数值范围最大最小值
    private final int[] numberRange = new int[2];
    private final int MIN_NUMBER = 0;
    private final int MAX_NUMBER = 1;

    //挑选次数和每一轮的挑选次数
    private int maxTotalCount = MainConfig.DEFAULT_MAX_TOTAL_COUNT;
    private int maxCycleCount = 0;

    //已经挑选了多少次
    private int totalCount = 0;
    private int cycleCount = 0;

    private boolean finalResult = true;

    private boolean forceStop = false;

    private String selectedResult;
    private boolean continueSelecting = false;

    private int resultLabelId = 0;

    private final Counter counter = new Counter();

    public Worker(StringProperty[] labelTexts, MainConfig config, Data data, History history, VoicePlayer voicePlayer) {
        this.labelTexts = labelTexts;
        this.config = config;
        this.data = data;
        this.history = history;
        this.voicePlayer = voicePlayer;
    }

    public void setLabelTexts(StringProperty[] labelTexts) {
        this.labelTexts = labelTexts;
    }

    private void select() {
        // try的位置待定（前面or后面）
        // 延时用，用于调整挑选速度
        try {
            Thread.sleep(speed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stoppedIndicator.set(false);

        // 若 计数已达最大总数（挑选次数）或强制停止，进入停止分支,
        if (totalCount >= maxTotalCount || forceStop) {
            // 任意三种情况就停止：已忽略名单中没有该名字；不考虑忽略名单；强制停止
            // 如果不是这三种情况就选到符合其中一种情况为止
            // 由于后两者在安排进程中不可能改变，因此继续挑选后只有选中的结果不在忽略名单中才会结束挑选
            // 若忽略名单已满（名单中数量=总的名单数量），点击“安排一下”时会提示已满，无法再进行挑选，因此在运行时总有 忽略名单<总名单，总有名字不在忽略列表中，不会死循环，放心吧
            boolean resultIgnored = (config.getChooseMethod() == MainConfig.METHOD_NAME) ? (data.checkNameIgnored(selectedResult)) : (data.checkNumberIgnored(selectedResult));
            if (!resultIgnored || !config.getPassSelectedResult() || forceStop) {
                this.stopSelect();
                finalResult = true;
                return;
            } else {
                continueSelecting = true;
            }
        }

        counter.count();
        resultLabelId = counter.getNewResultLabelId();
        finalResult = false;

        this.selectedResult = this.pick(config.getChooseMethod());
    }

    public void stopSelect() {
        if (config.getPassSelectedResult()) {
            if (config.nameChooseProperty().getValue()) {
                data.addNameToIgnoreList(selectedResult);
            } else {
                data.addNumberToIgnoreList(selectedResult);
            }
        }

        if (config.getEqualMode()) {
            data.writeIgnoreList(Data.IGNORELIST_ALL);
        }

        history.addHistory(selectedResult);

        if (config.getVoicePlay() && voicePlayer != null) {
            voicePlayer.playVoice(selectedResult);
        }

        counter.resetCounter();
        stoppedIndicator.set(true);
        forceStop = false;
        System.gc();
    }

    private String pick(int selectMethod) {
        switch (selectMethod) {
            case MainConfig.METHOD_NAME:
                return data.randomGet(config.getSecureRandom());
            case MainConfig.METHOD_NUMBER:
                randomNumber.setUseSecureRandom(config.getSecureRandom());
                return String.valueOf(randomNumber.getRandomNumber(numberRange[MIN_NUMBER], numberRange[MAX_NUMBER]));
            default:
                return "(´･ω･`)?";
        }
    }

    public void displaySelectedResult() {
        this.select();

        if (!finalResult) {
            labelTexts[resultLabelId].set(selectedResult);
        } else {
            for (StringProperty labelText : labelTexts) {
                if (labelText.get().contains("→")) {
                    labelText.set(labelText.get().replace("→", "").replace("←", ""));
                }
            }
            labelTexts[resultLabelId].set(String.format("→%s←", selectedResult));
        }
    }

    // 这里有个内部类Counter计数菌
    final class Counter {

        private int newResultLabelId = 0;

        protected void count() {
            newResultLabelId = resultLabelId;

            // 每一轮的计数到达最大时并且不强制继续这轮挑选时，重置这一轮的挑选，并指派新的显示label
            // 否则这一轮的计数直接+1
            if (cycleCount >= maxCycleCount && !continueSelecting) {
                cycleCount = 0;
                maxCycleCount = randomNumber.getRandomNumber(1, maxTotalCount - totalCount);
                newResultLabelId = randomNumber.getRandomNumber(labelTexts.length - 1);
            } else {
                cycleCount++;
            }

            totalCount++;
        }

        protected void resetCounter() {
            totalCount = 0;
            cycleCount = 0;
            maxCycleCount = 0;

            continueSelecting = false;
        }

        protected int getNewResultLabelId() {
            return newResultLabelId;
        }
    }

    public boolean getStoppedIndicator() {
        return stoppedIndicator.get();
    }

    public SimpleBooleanProperty stoppedIndicatorProperty() {
        return stoppedIndicator;
    }

    public void setNumberRange(int minNumber, int maxNumber) {
        this.numberRange[MIN_NUMBER] = minNumber;
        this.numberRange[MAX_NUMBER] = maxNumber;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    public void setMaxTotalCount(int maxTotalCount) {
        this.maxTotalCount = maxTotalCount;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
