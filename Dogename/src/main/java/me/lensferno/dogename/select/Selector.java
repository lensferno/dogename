package me.lensferno.dogename.select;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import me.lensferno.dogename.configs.GlobalConfig;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.select.core.Worker;
import me.lensferno.dogename.voice.TokenManager;
import me.lensferno.dogename.voice.VoicePlayer;

public final class Selector {

    Processor processor = new Processor();

    private VoicePlayer voicePlayer = null;

    public Selector() {
        initialVoicePlayer();
    }

    public void initialVoicePlayer() {
        TokenManager tokenManager = new TokenManager();
        tokenManager.init();
        if (tokenManager.getTokenStatus() == TokenManager.TOKEN_OK) {
            voicePlayer = new VoicePlayer(tokenManager.getToken());
        }
    }

    public void run() {
        processor.setNumberRange();
        processor.updateNewValue();
        processor.stoppedIndicatorProperty().set(false);

        processor.start();
    }

    public void forceStop() {
        processor.stopProcess();
    }

    public void initialVariable(Data data, History history, StringProperty... labelTexts) {
        processor.initialVariable(voicePlayer, data, history, labelTexts);
    }

    public boolean isWorkerRunning() {
        return processor.isWorkerRunning();
    }

    public void addStoppedEventListener(ChangeListener<? super Boolean> listener) {
        processor.stoppedIndicatorProperty().addListener(listener);
    }

    public void setLabelTexts(StringProperty... labelTexts) {
        processor.setWorkerLabelTexts(labelTexts);
    }

    // ---------------------------------------------------
    static class Processor extends AnimationTimer {
        private Worker coreWorker;

        protected void initialVariable(VoicePlayer voicePlayer, Data data, History history, StringProperty... labelTexts) {
            coreWorker = new Worker(labelTexts, data, history, voicePlayer);
        }

        protected void setWorkerLabelTexts(StringProperty... labelTexts) {
            coreWorker.setLabelTexts(labelTexts);
        }

        protected void updateNewValue() {
            coreWorker.setSpeed(100 - GlobalConfig.mainConfig.getSpeed());
            coreWorker.setMaxTotalCount(GlobalConfig.mainConfig.getMaxTotalCount());
        }

        protected SimpleBooleanProperty stoppedIndicatorProperty() {
            return coreWorker.stoppedIndicatorProperty();
        }

        @Override
        public void handle(long time) {
            coreWorker.displaySelectedResult();
            if (coreWorker.getStoppedIndicator()) {
                this.stop();
            }
        }

        public void stopProcess() {
            coreWorker.setForceStop(true);
        }

        public boolean isWorkerRunning() {
            return !coreWorker.getStoppedIndicator();
        }

        public void setNumberRange() {
            int minNumber = Integer.parseInt(GlobalConfig.mainConfig.getMinNumber());
            int maxNumber = Integer.parseInt(GlobalConfig.mainConfig.getMaxNumber());
            coreWorker.setNumberRange(minNumber, maxNumber);
        }
    }
}

