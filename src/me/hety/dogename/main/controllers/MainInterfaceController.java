package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.chooser.Chooser;
import me.hety.dogename.main.configs.ConfigLoader;
import me.hety.dogename.main.configs.MainConfig;
import me.hety.dogename.main.configs.VoiceConfig;
import me.hety.dogename.main.data.History;
import me.hety.dogename.main.data.NameData;
import me.hety.dogename.main.ocr.Ocr;
import me.hety.dogename.main.voice.Token;
import me.hety.dogename.main.voice.TokenManager;
import me.hety.dogename.main.voice.VoicePlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainInterfaceController {
    public JFXTextArea message;

    //ConfigLoader configLoader=new ConfigLoader();

    Token token;
    TokenManager tokenManager=new TokenManager();

    Ocr ocrTool=null;

    History history=new History();

    @FXML
    private Pane rootPane;

    @FXML
    private JFXRadioButton nameChoose;

    @FXML
    private JFXButton showHistoryBtn;

    @FXML
    private JFXRadioButton numbChoose;

    @FXML
    private JFXButton anPaiBtn;

    @FXML
    private Pane mainPane;

    @FXML
    private Label topBar;

    @FXML
    private JFXButton showNameMangerButton;

    @FXML
    private Label chosen_1;

    @FXML
    private ImageView mainView;

    @FXML
    private JFXButton miniModeBtn;

    @FXML
    private Label chosen_2;

    public MainInterfaceController(){
        history.loadHistory();
        nameData.readIgnoreList();
        tokenManager.init();
        this.ignoreNameList=nameData.getIgnoreNameList();
        this.ignoreNumberList=nameData.getIgnoreNumberList();
        if(tokenManager.getTokenStatus().equals("ok")){
            token=tokenManager.getToken();
        }
    }

    MainConfig mainConfig;
    VoiceConfig voiceConfig;

    public void bindProperties(){
        nameChoose.selectedProperty().bindBidirectional(mainConfig.nameChoosePropertyProperty());
        //mainConfig.nameChoosePropertyProperty().not()

        numbChoose.selectedProperty().bind(mainConfig.nameChoosePropertyProperty().not());
        numbChoose.selectedProperty().unbind();
/*
        mainConfig.nameChoosePropertyProperty().addListener((observable, oldValue, newValue) -> {
            //numbChoose.selectedProperty().unbind();
            numbChoose.setSelected(oldValue);
        });*/

    }

    public void setImg(InputStream stream){
        mainView.setImage(new Image(stream));
    }

    public void setUpConfig(ConfigLoader configLoader){
        mainConfig=configLoader.readConfigFromFile("files"+ File.separator +"Config.json");
        voiceConfig=configLoader.readVoiceConfigFromFile("files"+ File.separator +"VoiceConfig.json");
    }

    @FXML
    void showProgramInfo(ActionEvent event) {
        new DialogMaker(rootPane).creatDialogWithOneBtn("程序信息",new ProgramInfoPaneController(rootPane));
    }

    @FXML
    void nameChoose_selected(ActionEvent event) {

    }

    @FXML
    void showNameManger(ActionEvent event) {

        if (chooser.isRunning()){
            new DialogMaker(rootPane).creatMessageDialog("(・。・)","安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NameManagerPaneController nameManagerPaneController =new NameManagerPaneController(nameData,rootPane,ocrTool);
        new DialogMaker(rootPane).creatDialogWithOneBtn("名单管理",nameManagerPaneController);
    }


    @FXML
    void numbChoose_selected(ActionEvent event) {

    }

    @FXML
    void showNunberSetting(ActionEvent event) {

        if (chooser.isRunning()){
            new DialogMaker(rootPane).creatMessageDialog("(・。・)","安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NumberSettingsPaneController numberSettingsPaneController =new NumberSettingsPaneController(nameData);
        numberSettingsPaneController.bindProperties(mainConfig);
        new DialogMaker(rootPane).creatDialogWithOneBtn("调整数字",numberSettingsPaneController);

    }


    @FXML
    void miniMode(ActionEvent event) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/MiniPane.fxml"));
        Parent parent;
        try {
            parent=loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene miniScene=new Scene(parent,300,134);
        Stage miniStage=new Stage();
        miniStage.setScene(miniScene);
        miniStage.initStyle(StageStyle.UNDECORATED);

        MiniPaneController miniPaneController=loader.getController();
        miniPaneController.setBase(history,nameData,token,voiceConfig,mainConfig);

        Stage currentStage=(Stage)anPaiBtn.getScene().getWindow();
        miniPaneController.setForwStage(currentStage);

        miniPaneController.setCurrentStage(miniStage);
        miniPaneController.setCurrentScene(miniScene);

        miniPaneController.setListeners();

        miniStage.show();
        currentStage.close();

    }

    @FXML
    void showSettings(ActionEvent event) {

        SettingsPaneController settingsPaneController =new SettingsPaneController();

        settingsPaneController.setToggleGroup();
        settingsPaneController.bindProperties(mainConfig);

        settingsPaneController.setVoiceConfig(voiceConfig);

        settingsPaneController.setRootPane(rootPane);

        new DialogMaker(rootPane).creatDialogWithOneBtn("更多设置",settingsPaneController);
    }

    @FXML
    void showHistory(ActionEvent event) {

        HistoryPaneController historyPaneController =new HistoryPaneController(history);

        new DialogMaker(rootPane).creatDialogWithOneBtn("历史记录",historyPaneController);
    }

    Random random=new Random();

    HashSet<String> ignoreNameList;
    HashSet<String> ignoreNumberList;

    NameData nameData=new NameData();
    //boolean isRunning=false;

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



    public void setToggleGroup(){
        ToggleGroup toggleGroup =new ToggleGroup();
        nameChoose.setToggleGroup(toggleGroup);
        numbChoose.setToggleGroup(toggleGroup);
    }

    private void runNameMode(Chooser chooser){

        if(nameData.isEmpty(mainConfig.isTaoluModeProperty())){
            new DialogMaker(rootPane).creatMessageDialog("哦霍~","现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
            return;
        }

        if((nameData.getIgnoreNameList().size()>=nameData.getSize())&&mainConfig.isIgnorePastProperty()){

            if(mainConfig.isEqualModeProperty()) {
                new DialogMaker(rootPane).creatDialogWithOKAndCancel("啊？", "全部名字都被点完啦！\n要把名字的忽略列表重置吗？",e ->nameData.clearNameIgnoreList());
            }else {
                new DialogMaker(rootPane).creatMessageDialog("啊？", "全部名字都被点完啦！\n请多添加几个名字 或 点击“机会均等”的“重置”按钮。");
            }
            return;
        }

        anPaiBtn.setText("不玩了！");

        chooser.set(chosen_1.textProperty(),chosen_2.textProperty(),anPaiBtn,history,nameData,token,voiceConfig);

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
                new DialogMaker(rootPane).creatMessageDialog("嗯哼？","数字要前小后大啊~");
                return;
            }

            if(nameData.getIgnoreNumberList().size()>=(maxNumber-minNumber+1) && mainConfig.isIgnorePastProperty()){
                if(mainConfig.isEqualModeProperty()) {
                    new DialogMaker(rootPane).creatDialogWithOKAndCancel("啊？", "全部数字都被点完啦！\n要把数字的忽略列表重置吗？", e ->nameData.clearNumberIgnoreList());
                }else {
                    new DialogMaker(rootPane).creatMessageDialog("啊？", "全部数字都被点完啦！\n请扩大数字范围 或 点击“机会均等”的“重置”按钮。");
                }
                return;
            }

        }catch (Exception e){
            new DialogMaker(rootPane).creatMessageDialog("嗯哼？","输入个有效的数字啊~");
            return;
        }

        anPaiBtn.setText("不玩了！");

        chooser.set(chosen_1.textProperty(),chosen_2.textProperty(),anPaiBtn,history,nameData,token,voiceConfig);

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

    public Pane getRootPane() {
        return rootPane;
    }
}
