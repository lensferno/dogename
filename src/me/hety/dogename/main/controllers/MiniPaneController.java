package me.hety.dogename.main.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.chooser.Chooser;
import me.hety.dogename.main.configs.MainConfig;
import me.hety.dogename.main.configs.VoiceConfig;
import me.hety.dogename.main.controllers.WindowListeners.MoveWindowByMouse;
import me.hety.dogename.main.controllers.WindowListeners.MoveWindowByTouch;
import me.hety.dogename.main.data.History;
import me.hety.dogename.main.data.NameData;
import me.hety.dogename.main.voice.Token;

import java.util.Random;

public class MiniPaneController {

    @FXML
    private Label chosenNameLabel;

    @FXML
    private JFXButton anPaiBtn;

    @FXML
    private JFXButton miniModeBtn;

    Stage forwStage;

    public Stage getForwStage() {
        return forwStage;
    }

    public void setForwStage(Stage forwStage) {
        this.forwStage = forwStage;
    }

    private Random random=new Random();
    private NameData nameData;
    private Pane rootPane;
    private History history;
    private Token token;
    private VoiceConfig voiceConfig;

    Stage currentStage;
    Scene currentScene;

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setBase(History history, NameData nameData, Token token, VoiceConfig voiceConfig, MainConfig mainConfig){

        this.history = history;
        this.nameData = nameData;

        this.token=token;

        this.voiceConfig=voiceConfig;

        this.mainConfig=mainConfig;

    }

    @FXML
    void recoverMode(ActionEvent event) {
        this.forwStage.show();
        currentStage.close();
    }

    public void setListeners(){
        EventHandler<MouseEvent> mouseHandler=new MoveWindowByMouse(currentStage);
        chosenNameLabel.setOnMousePressed(mouseHandler);
        chosenNameLabel.setOnMouseDragged(mouseHandler);

        EventHandler<TouchEvent> touchHandler=new MoveWindowByTouch(currentStage);
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



    private MainConfig mainConfig;
    Chooser chooser=new Chooser();

    @FXML
    void anPai() {
        if(chooser.isRunning()){
            chooser.setForceStop(true);
            anPaiBtn.setText("安排一下");
            return;
        }

        if(mainConfig.isRandomTimesProperty()) {
            mainConfig.setCycleTimesProperty(100+random.nextInt(151));
        }

        if(mainConfig.isNameChooseProperty()){
            runNameMode(chooser);
        }else {
            runNumberMode(chooser);
        }

    }


    private void runNameMode(Chooser chooser){

        if(nameData.isEmpty(mainConfig.isTaoluModeProperty())){
            return;
        }

        if((nameData.getIgnoreNameList().size()>=nameData.getSize())&&mainConfig.isIgnorePastProperty()){

            return;
        }

        anPaiBtn.setText("不玩了！");

        chooser.set(chosenNameLabel.textProperty(),chosenNameLabel.textProperty(),anPaiBtn,history,nameData,token,voiceConfig);

        chooser.run(
                (short) mainConfig.getSpeedProperty(),
                mainConfig.getCycleTimesProperty(),
                mainConfig.isIgnorePastProperty(),
                mainConfig.isEqualModeProperty(),
                mainConfig.isTaoluModeProperty(),
                mainConfig.isVoicePlayProperty()
        );

    }


    private void runNumberMode(Chooser chooser){

        try{

            int minNumber=Integer.parseInt(mainConfig.getMinNumberProperty());
            int maxNumber=Integer.parseInt(mainConfig.getMaxNumberProperty());

            if(maxNumber-minNumber<=0){
                return;
            }

            if(nameData.getIgnoreNumberList().size()>=(maxNumber-minNumber+1) && mainConfig.isIgnorePastProperty()){
                return;
            }

        }catch (Exception e){
            new DialogMaker(rootPane).creatMessageDialog("嗯哼？","倒是输入个有效的数字啊~");
            return;
        }

        anPaiBtn.setText("不玩了！");

        chooser.set(chosenNameLabel.textProperty(),chosenNameLabel.textProperty(),anPaiBtn,history,nameData,token,voiceConfig);

        chooser.run(
                Short.parseShort(mainConfig.getMaxNumberProperty()),
                Short.parseShort(mainConfig.getMinNumberProperty()),
                (short) mainConfig.getSpeedProperty(),
                mainConfig.getCycleTimesProperty(),
                mainConfig.isIgnorePastProperty(),
                mainConfig.isEqualModeProperty(),
                mainConfig.isTaoluModeProperty(),
                mainConfig.isVoicePlayProperty()
        );
    }
}
