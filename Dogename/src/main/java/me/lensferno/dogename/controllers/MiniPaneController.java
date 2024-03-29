package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import me.lensferno.dogename.configs.GlobalConfig;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.controllers.WindowListeners.MoveWindowByMouse;
import me.lensferno.dogename.controllers.WindowListeners.MoveWindowByTouch;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.select.Selector;

import java.util.Random;

public class MiniPaneController {

    Stage oldStage;
    Stage currentStage;
    Scene currentScene;
    StringProperty[] oldTextProperties = null;
    @FXML
    private Label chosenNameLabel;
    @FXML
    private JFXButton anPaiBtn;
    @FXML
    private JFXButton miniModeBtn;
    private final Random random = new Random();
    private Data data;
    private Selector selector = new Selector();

    public void setOldStage(Stage oldStage) {
        this.oldStage = oldStage;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setBase(Data data, Selector selector) {
        this.data = data;
        this.selector = selector;
        this.selector.setLabelTexts(chosenNameLabel.textProperty());
    }

    public void setOldTextProperties(StringProperty ...stringProperty) {
        this.oldTextProperties = stringProperty;
    }

    @FXML
    void recoverMode(ActionEvent event) {
        this.oldStage.setOnShown((e) -> selector.setLabelTexts(oldTextProperties));
        this.oldStage.show();

        currentStage.close();
    }

    public void setListeners() {
        EventHandler<MouseEvent> mouseHandler = new MoveWindowByMouse(currentStage);
        chosenNameLabel.setOnMousePressed(mouseHandler);
        chosenNameLabel.setOnMouseDragged(mouseHandler);

        EventHandler<TouchEvent> touchHandler = new MoveWindowByTouch(currentStage);
        chosenNameLabel.setOnTouchPressed(touchHandler);
        chosenNameLabel.setOnTouchMoved(touchHandler);

        anPaiBtn.setOnMousePressed(mouseHandler);
        anPaiBtn.setOnMouseDragged(mouseHandler);

        anPaiBtn.setOnTouchPressed(touchHandler);
        anPaiBtn.setOnTouchMoved(touchHandler);

        miniModeBtn.setOnMousePressed(mouseHandler);
        miniModeBtn.setOnMouseDragged(mouseHandler);

        miniModeBtn.setOnTouchPressed(touchHandler);
        miniModeBtn.setOnTouchMoved(touchHandler);
    }

    @FXML
    void anPai() {

        if (selector.isWorkerRunning()) {
            selector.forceStop();
            anPaiBtn.setText("安排一下");
            return;
        }

        if (GlobalConfig.mainConfig.getRandomCount()) {
            GlobalConfig.mainConfig.setMaxTotalCount(100 + random.nextInt(151));
        }

        if (GlobalConfig.mainConfig.getChooseMethod() == MainConfig.METHOD_NAME) {
            runNameMode();
        } else {
            runNumberMode();
        }

    }

    private void runNameMode() {

        if (data.isEmpty()) {
            return;
        }

        if (data.compareNameIgnoreList() && GlobalConfig.mainConfig.getIgnoreSelectedResult()) {
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }

    private void runNumberMode() {

        try {

            int minNumber = Integer.parseInt(GlobalConfig.mainConfig.getMinNumber());
            int maxNumber = Integer.parseInt(GlobalConfig.mainConfig.getMaxNumber());

            if (maxNumber - minNumber <= 0) {
                return;
            }

            if (data.getNumberIgnoreListSize() >= (maxNumber - minNumber + 1) && GlobalConfig.mainConfig.getIgnoreSelectedResult()) {
                return;
            }

        } catch (Exception e) {
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }
}
