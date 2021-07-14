package me.lensferno.dogename.controllers;

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
import me.lensferno.dogename.select.Selector;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.configs.ConfigLoader;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.ocr.Ocr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public final class MainInterfaceController {

    public JFXTextArea message;

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
    private Label upperLabel;

    @FXML
    private ImageView mainView;

    @FXML
    private JFXButton miniModeBtn;

    @FXML
    private Label downLabel;

    public MainInterfaceController(){
        history.loadHistory();
    }

    MainConfig mainConfig;
    VoiceConfig voiceConfig;

    public void bindProperties(){
        nameChoose.selectedProperty().bindBidirectional(mainConfig.nameChooseProperty());

        numbChoose.selectedProperty().bind(mainConfig.nameChooseProperty().not());
        numbChoose.selectedProperty().unbind();

        mainConfig.nameChooseProperty().addListener((observable, oldValue, newValue) -> {
            mainConfig.setChooseMethod(mainConfig.getNameChoose() ? MainConfig.METHOD_NAME : MainConfig.METHOD_NUMBER);
        });
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
        new DialogMaker(rootPane).createDialogWithOneBtn("程序信息",new ProgramInfoPaneController(rootPane));
    }


    @FXML
    void showNameManger(ActionEvent event) {

        if (selector.isWorkerRunning()){
            new DialogMaker(rootPane).createMessageDialog("(・。・)","安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NameManagerPaneController nameManagerPaneController =new NameManagerPaneController(data,rootPane,ocrTool);
        new DialogMaker(rootPane).createDialogWithOneBtn("名单管理",nameManagerPaneController);
    }

    @FXML
    void showNunberSetting(ActionEvent event) {

        if (selector.isWorkerRunning()){
            new DialogMaker(rootPane).createMessageDialog("(・。・)","安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NumberSettingsPaneController numberSettingsPaneController =new NumberSettingsPaneController(data);
        numberSettingsPaneController.bindProperties(mainConfig);
        new DialogMaker(rootPane).createDialogWithOneBtn("调整数字",numberSettingsPaneController);

    }

    Logger log= LogManager.getLogger();

    @FXML
    void miniMode(ActionEvent event) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/MiniPane.fxml"));
        Parent parent;
        try {
            parent=loader.load();
        } catch (IOException e) {
            log.error("Error in loading MiniPane Fxml:"+e);
            return;
        }

        Scene miniScene=new Scene(parent,300,134);
        Stage miniStage=new Stage();
        miniStage.setScene(miniScene);
        miniStage.initStyle(StageStyle.UNDECORATED);

        MiniPaneController miniPaneController=loader.getController();
        miniPaneController.setBase(data, mainConfig, selector);

        Stage currentStage=(Stage)anPaiBtn.getScene().getWindow();
        miniPaneController.setForwStage(currentStage);

        miniPaneController.setCurrentStage(miniStage);
        miniPaneController.setCurrentScene(miniScene);

        miniPaneController.setListeners();

        miniStage.show();
        currentStage.hide();
    }

    @FXML
    void showSettings(ActionEvent event) {

        SettingsPaneController settingsPaneController =new SettingsPaneController();

        settingsPaneController.setToggleGroup();
        settingsPaneController.bindProperties(mainConfig);

        settingsPaneController.setVoiceConfig(voiceConfig);

        settingsPaneController.setRootPane(rootPane);

        settingsPaneController.setData(data);

        new DialogMaker(rootPane).createDialogWithOneBtn("更多设置",settingsPaneController);
    }

    @FXML
    void showHistory(ActionEvent event) {

        HistoryPaneController historyPaneController =new HistoryPaneController(history,rootPane);

        new DialogMaker(rootPane).createDialogWithOneBtn("历史记录",historyPaneController);
    }

    Random random=new Random();

    Data data =new Data();

    Selector selector =new Selector();

    public void init(){

        selector.initialVariable(mainConfig, data, history, upperLabel.textProperty(), downLabel.textProperty());
        selector.addStoppedEventListener((observableValue, oldValue, stop)->{
            if (stop) {
                anPaiBtn.setText("安排一下");
            } else {
                anPaiBtn.setText("不玩了！");
            }
        });
    }
    
    @FXML
    void anPai() {

        if(selector.isWorkerRunning()){
            selector.forceStop();
            anPaiBtn.setText("安排一下");
            return;
        }
        
        if(mainConfig.getRandomCount()) {
            mainConfig.setMaxTotalCount(100+random.nextInt(151));
        }

        if(mainConfig.getNameChoose()){
            runNameMode();
        }else {
            runNumberMode();
        }

    }

    public void setToggleGroup(){
        ToggleGroup toggleGroup =new ToggleGroup();
        nameChoose.setToggleGroup(toggleGroup);
        numbChoose.setToggleGroup(toggleGroup);
    }

    private void runNameMode(){

        if(data.isEmpty()){
            new DialogMaker(rootPane).createMessageDialog("哦霍~","现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
            return;
        }

        if(data.compareNameIgnoreList() && mainConfig.getPassSelectedResult()){

            if(mainConfig.getEqualMode()) {
                new DialogMaker(rootPane).createDialogWithOKAndCancel("啊？", "全部名字都被点完啦！\n要把名字的忽略列表重置吗？", e -> data.clearNameIgnoreList());
            }else {
                new DialogMaker(rootPane).createMessageDialog("啊？", "全部名字都被点完啦！\n请多添加几个名字 或 点击“机会均等”的“重置”按钮。");
            }
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }

    private void runNumberMode(){

        try{

            int minNumber=Integer.parseInt(mainConfig.getMinNumber());
            int maxNumber=Integer.parseInt(mainConfig.getMaxNumber());

            if(maxNumber-minNumber <= 0){
                new DialogMaker(rootPane).createMessageDialog("嗯哼？","数字要前小后大啊~");
                return;
            }

            if(data.getNumberIgnoreListSize() >= (maxNumber-minNumber+1) && mainConfig.getPassSelectedResult()){
                if(mainConfig.getEqualMode()) {
                    new DialogMaker(rootPane).createDialogWithOKAndCancel("啊？", "全部数字都被点完啦！\n要把数字的忽略列表重置吗？", e -> data.clearNumberIgnoreList());
                }else {
                    new DialogMaker(rootPane).createMessageDialog("啊？", "全部数字都被点完啦！\n请扩大数字范围 或 点击“机会均等”的“重置”按钮。");
                }
                return;
            }

        }catch (Exception e){
            new DialogMaker(rootPane).createMessageDialog("嗯哼？","输入个有效的数字啊~");
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }

    public Label getTopBar() {
        return topBar;
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }
}
