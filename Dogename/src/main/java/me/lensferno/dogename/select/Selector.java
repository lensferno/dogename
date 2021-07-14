package me.lensferno.dogename.select;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.data.NameData;
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
        VoiceConfig voiceConfig = new VoiceConfig();
        tokenManager.init();
        if (tokenManager.getTokenStatus() == TokenManager.TOKEN_OK) {
            voicePlayer = new VoicePlayer(tokenManager.getToken(), voiceConfig);
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

    public void initialVariable(MainConfig config, NameData nameData, History history, StringProperty... labelTexts) {
        processor.initialVariable(config, voicePlayer, nameData, history, labelTexts);
    }

    public boolean isWorkerRunning() {
        return processor.isWorkerRunning();
    }

    public void addStoppedEventListener(ChangeListener<? super Boolean> listener) {
        processor.stoppedIndicatorProperty().addListener(listener);
    }

    // ---------------------------------------------------
    static class Processor extends AnimationTimer {

        private Worker coreWorker;

        MainConfig config = null;

        protected void initialVariable(MainConfig config, VoicePlayer voicePlayer, NameData nameData, History history, StringProperty... labelTexts) {
            coreWorker = new Worker(labelTexts, config, nameData, history, voicePlayer);
            this.config = config;
        }

        protected void updateNewValue() {
            coreWorker.setSpeed(100 - config.getSpeed());
            coreWorker.setMaxTotalCount(config.getMaxTotalCount());
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
            int minNumber = Integer.parseInt(config.getMinNumber());
            int maxNumber = Integer.parseInt(config.getMaxNumber());
            coreWorker.setNumberRange(minNumber, maxNumber);
        }

    }

}

