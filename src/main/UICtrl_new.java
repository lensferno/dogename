package main;

import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.DialogBuilder.DialogBuilder;
import main.everydaySaying.Gushici;
import main.everydaySaying.Hitokoto;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public final class UICtrl_new {

    boolean taoluMode;


    HashSet<String> ignoreNameList=new HashSet<>();
    short ignoreNameTimes=0;

    HashSet<String> ignoreNumberList=new HashSet<>();
    short ignoreNumberTimes=0;

    public JFXTextField minNumb;
    public JFXTextField maxNumb;

    Random random =new Random();

    public short minNumber;
    public short maxNumber;



    App app=new App();
    int chosenTime=120;
    int times=0;
    int already=0;
    int singleCycle=0;
    int showWhich=1;

    String chosenName;

    boolean cycleEnd =true;
    boolean ignoreTimesOut=false;
    boolean ignorePast=true;

    boolean equalMode=true;

    boolean forceStop =false;

    File nameIgnoreFile =new File(app.APP_LOCA+"files\\nameIgnoreList.data");
    File numbIgnoreFile =new File(app.APP_LOCA+"files\\numbIgnoreList.data");


    @FXML
    void clearIgnoreList(){
        if(isNameChoose)
            ignoreNameList=new HashSet<>();
        else
            ignoreNumberList=new HashSet<>();
        writeIgnoreList();

        ignoreNameTimes=0;
        ignoreNumberTimes=0;
        System.gc();

    }

    public Label topBar;
    @FXML
    void showShiCi() {
        new Hitokoto().showSaying(mainPane,topBar);
        //new Gushici().showShiCi(mainPane,topBar);
    }


    @FXML
    void clearTaoluList(){
        data.clearTaoluedName();
    }

    @FXML
    void showTaoluMode(){
        showInfoDialog("啥玩意？","旧称“套路模式”，勾选后会使被点过的名字在挑选列表中多出现4~5次，增加了再次被点中的几率。\n注意：仅在勾选此模式后点中的名字才会被多增加4~5次，不勾选时选中的名字不受影响。\n退出后会自动重置，不影响下次使用。");
    }

    @FXML
    void showEqualMode(){
        showInfoDialog("啥玩意？","勾选“机会均等”后，将会保存已点过的的名字和数字到文件中，下次启动时仍不会被点到，直到全部名字或数字被点完 或点击“机会均等”的“重置”按钮。\n注意：仅保存“这次点过就不点了”模式下选中的名字或数字。");
    }

    @FXML
    void showController(){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("高级设置"));
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        mainPane.getChildren().remove(controllerPane);
        vb.getChildren().add(controllerPane);
        vb.setPrefHeight(controllerPane.getPrefHeight());
        vb.setPrefWidth(controllerPane.getPrefWidth());
        content.setBody(vb);

        content.setPrefWidth(controllerPane.getPrefWidth());

        controllerPane.setVisible(true);
        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());

        mainPane.getChildren().add(tempPane);

        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(controllerPane.getPrefHeight());
        dialog.setPrefWidth(controllerPane.getPrefWidth());
        JFXButton button = new JFXButton("OK");
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                mainPane.getChildren().remove(tempPane);
                saveConfigToFile();
            }
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
        });

        content.setActions(button);

        dialog.show();
    }

    public void readIgnoreList(){

        try{

            if(nameIgnoreFile.exists()!=true){
                nameIgnoreFile.createNewFile();
                ignoreNameList= new HashSet<>();
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(nameIgnoreFile));
            this.ignoreNameList=(HashSet)ois.readObject();

            ignoreNameTimes=(short) ignoreNameList.size();

        }catch (Exception e){
            ignoreNameList=new HashSet<>();
            e.printStackTrace();
        }

        try{

            if(numbIgnoreFile.exists()!=true){
                numbIgnoreFile.createNewFile();
                ignoreNumberList= new HashSet<>();
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(nameIgnoreFile));
            this.ignoreNumberList=(HashSet)ois.readObject();

            ignoreNumberTimes=(short) ignoreNumberList.size();

        }catch (Exception e){
            ignoreNumberList=new HashSet<>();
            e.printStackTrace();
        }

        System.out.println("There are "+ignoreNameList.size()+"names and "+ignoreNumberList.size()+"numbers.");
    }

    void writeIgnoreList(){
        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(nameIgnoreFile));
            oos.writeObject(ignoreNameList);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(numbIgnoreFile));
            oos.writeObject(ignoreNumberList);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Voice voice =new Voice();
    //-------------------------------------------------------------------------------------------
    
    public boolean isNameChoose=true;
    public short speed;

    @FXML
    public Label chosen_1;
    public Label chosen_2;

    public JFXListView nameList;
    public JFXChipView a;


    public JFXButton goBackButton;
    public JFXButton choose;
    public JFXButton showNameMangerButton;
    public JFXButton recover;

    public JFXCheckBox taoluModeBtn;
    public JFXCheckBox equalModeBtn;
    public JFXCheckBox newAlgoBtn;
    public JFXCheckBox voicePlayBtn;

    public JFXRadioButton numbChoose;
    public JFXRadioButton nameChoose;

    public JFXRadioButton chooseOnce;
    public JFXRadioButton ignoreOnce;

    public JFXRadioButton randomTimes;
    public JFXRadioButton fixedTimes;

    public JFXSlider chooseTimes;
    public JFXSlider speedBar;
    public JFXTextArea inputName;
    

    public Pane numbPane;
    public Pane namePane;
    public ScrollPane controllerPane;

    public Stage stage;
    public Scene scene;

    public Pane mainPane;

    short oldX;
    short oldY;
    short oldW;
    short oldH;
    //AipSpeech aipSpeech =new AipSpeech("","","");

    private Data data=new Data();

    public boolean isRandomTimes=true;

    public Config config;

    public static final ObservableList names = FXCollections.observableArrayList();

    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setScene(Scene scene){
        this.scene = scene;
    }


    public void setConfig(Config config) {
        this.config = config;
    }


    public void setMinNumber(short minNumber) {
        this.minNumber = minNumber;
    }

    public void setMaxNumber(short maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setChosenTime(int chosenTime) {
        this.chosenTime = chosenTime;
        chooseTimes.setValue(chosenTime);
    }

    public void setSpeed(short speed) {
        this.speed = speed;
        speedBar.setValue(100-speed);
    }


    public void setChosenTimeHere() {
        this.chosenTime = (int)chooseTimes.getValue();
    }

    public void setSpeedHere() {
        this.speed = (short) speedBar.getValue();
    }

    @FXML
    public void setTaoluMode(boolean taoluMode){
        this.taoluMode=taoluMode;
    }


    boolean newAlgo=true;

    boolean voicePlay=true;

    boolean sysMethod=true;
    @FXML
    JFXCheckBox sysMMbtn;


    @FXML
    public void voicePlayBtnAction(){
        if(voicePlay)
            unselectVoicePlayBtn();
        else
            selectVoicePlayBtn();
    }
    
    void unselectVoicePlayBtn(){
	    voicePlay=false;
        voicePlayBtn.setSelected(false);
    }

    
    void selectVoicePlayBtn(){
	    voicePlay = true;
        voicePlayBtn.setSelected(true);
    }
    
    public boolean isVoicePlay() {
        return voicePlay;
    }

    public void setVoicePlay(boolean voicePlay) {
        this.voicePlay = voicePlay;
    }

    @FXML
    public void sysMMbtnAction(){
        if(sysMethod)
            unselectSysMMbtn();
        else
            selectSysMMbtn();
    }

    void unselectSysMMbtn(){
        sysMethod=false;
        sysMMbtn.setSelected(false);
    }


    void selectSysMMbtn(){
        sysMethod = true;
        sysMMbtn.setSelected(true);
    }

    public boolean isSysMethod() {
        return sysMethod;
    }

    SystemSimpleMethod systemSimpleMethod;
    public void setSysMethod(boolean sysMethod,SystemSimpleMethod systemSimpleMethod) {
        this.sysMethod = sysMethod;
        this.systemSimpleMethod=systemSimpleMethod;
    }

    @FXML
    public void newAlgoBtnAction(){
        if(newAlgo)
            unselectNewAlgoBtn();
        else
            selectNewAlgoBtn();
    }
    
    void unselectNewAlgoBtn(){
        newAlgo=false;
        newAlgoBtn.setSelected(false);
        data.setNewAlgo(newAlgo);
    }

    
    void selectNewAlgoBtn(){
        newAlgo = true;
        newAlgoBtn.setSelected(true);
        data.setNewAlgo(newAlgo);
    }
    
    public boolean isNewAlgo() {
        return newAlgo;
    }

    public void setNewAlgo(boolean newAlgo) {
        this.newAlgo = newAlgo;
    }
    



    private String HISTORY_FILE;
    private File historyFile;

    public static final ObservableList historyShow = FXCollections.observableArrayList();
    List history;
    public JFXListView historyList=new JFXListView();


    private String CONFIG_FILE;
    private File configFile;




    public int saveConfigToFile(){

        config.setChosenTime(chosenTime);
        config.setIgnorePast(ignorePast);
        config.setMaxNumber(maxNumber);
        config.setMinNumber(minNumber);
        config.setNameChoose(isNameChoose);
        config.setRandomTimes(isRandomTimes);
        config.setTaoluMode(taoluMode);
        config.setEqualMode(equalMode);
        config.setNewAlgo(newAlgo);
        config.setVoicePlay(voicePlay);



	if(System.getProperty("os.name").toLowerCase().contains("window"))
            CONFIG_FILE=app.APP_LOCA+"files\\config.data";
	else
            CONFIG_FILE=app.APP_LOCA+"files/config.data";

        configFile=new File(CONFIG_FILE);
        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(configFile));
            oos.writeObject(config);
            oos.close();
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }



    }

    Core core =new Core();

    void loadHistory(){

        if(System.getProperty("os.name").toLowerCase().contains("window"))
            HISTORY_FILE=app.APP_LOCA+"files\\history.data";
        else
            HISTORY_FILE=app.APP_LOCA+"files/history.data";

        try {
            File historyFile = new File(HISTORY_FILE);
            if (historyFile.exists() != true) {
                historyFile.createNewFile();
                history = new ArrayList();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile));
            this.history = (ArrayList) ois.readObject();

        } catch (Exception e) {
            history = new ArrayList();

            e.printStackTrace();
        }
        core.set( chosen_2, chosen_1, controllerPane, choose,history,voice,app);
        core.loadHistory();

    }


    boolean isRunning=false;

    @FXML
    void anPai(){/*
        if(sysMethod){

            int systemSMCode=systemSimpleMethod.showNext(chosen_1, chosenTime);
            switch (systemSMCode) {
                case -1:
                    
                    break;
            
                default:
                    break;
            }
        }*/
        saveConfigToFile();
        if(core.isRunning()){
            core.setForceStop(true);
            choose.setText("安排一下");
            return;
        }



        if(isRandomTimes) {
            chosenTime=100+random.nextInt(151);
            //chosenTime =  100 + (int) (Math.random() * (250 - 100));
            chooseTimes.setValue(chosenTime);
        }
        else {
            
            chosenTime=(int)chooseTimes.getValue();
        }
            
        //int s=(int)min+(int)(Math.random()*(max-min));

        if(isNameChoose){
            if(data.isEmpty(taoluMode)){
                showInfoDialog("哦霍~","现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
                return;
            }

            if((ignoreNameList.size()>=data.getSize())&&ignorePast){
                if(equalMode) {
                    showInfoDialog("啊？", "全部名字都被点完啦！\n名字列表将会重置");
                    clearIgnoreList();
                }else
                    showInfoDialog("啊？","全部名字都被点完啦！\n请多添加几个名字 或 点击“机会均等”的“重置”按钮。");

                return;
            }
            controllerPane.setDisable(true);
            speed=(short) (100-speedBar.getValue());
            isRunning=true;
            choose.setText("不玩了！");
            showWhich=1+random.nextInt(2);
            //    timer.start();
            core.set( chosen_2, chosen_1, controllerPane, choose,history,voice,app);
            core.setIgnoreNameList(ignoreNameList);
            core.setIgnoreNumberList(ignoreNumberList);
            core.run( speed, data, chosenTime, ignorePast, equalMode, taoluMode,voicePlay);

        }else {

            try{
                minNumber=Short.parseShort(minNumb.getText());
                maxNumber=Short.parseShort(maxNumb.getText());

                if(maxNumber-minNumber<=0){
                    showInfoDialog("嗯哼？","数字要前小后大啊~");
                    return;
                }

                if(ignoreNumberList.size()>=(maxNumber-minNumber+1) && ignorePast){
                    if(equalMode) {
                        showInfoDialog("啊？", "全部数字都被点完啦！\n数字列表将会重置");
                        clearIgnoreList();
                    }else
                        showInfoDialog("啊？","全部数字都被点完啦！\n请扩大数字范围 或 点击“机会均等”的“重置”按钮。");
                    return;
                }

            }catch (Exception e){
                showInfoDialog("嗯哼？","倒是输入个有效的数字啊~");
                return;
            }

            controllerPane.setDisable(true);
            speed=(short) (100-speedBar.getValue());
            isRunning=true;
            choose.setText("不玩了！");
            showWhich=1+random.nextInt(2);
            core.set( chosen_2, chosen_1, controllerPane, choose,history,voice,app);
            core.setIgnoreNameList(ignoreNameList);
            core.setIgnoreNumberList(ignoreNumberList);
            core.run( maxNumber,minNumber,speed , chosenTime, ignorePast, equalMode, taoluMode,voicePlay);

        }

    }

    @FXML
    public ImageView mainView;
    @FXML
    public ImageView backBtn;
    public Image mainImage =new Image(releaseData.getMainImageStream());
    public Image backBtnImage =new Image(releaseData.getBackBtnStream());

    public void setImages(){
        mainView.setImage(mainImage);
        backBtn.setImage(backBtnImage);
    }

    //两种选择方式的切换，没什么好说的。
    @FXML
    void numbChoose_selected(){
        isNameChoose=false;
        numbChoose.setSelected(true);
        namePane.setVisible(false);
        nameChoose.setSelected(false);
        numbPane.setVisible(true);
        showNameMangerButton.setVisible(false);

        taoluMode=false;
        taoluModeBtn.setSelected(false);
        taoluModeBtn.setDisable(true);

    }

    @FXML
    void nameChoose_selected(){
        isNameChoose=true;
        nameChoose.setSelected(true);
        numbPane.setVisible(false);
        numbChoose.setSelected(false);
        namePane.setVisible(true);
        showNameMangerButton.setVisible(true);
        if(!ignorePast){
            //taoluMode=true;
            taoluModeBtn.setDisable(false);
        }
    }


    void taoluModeBtn_selected(){
        taoluMode=true;
        taoluModeBtn.setSelected(true);
    }

    void taoluModeBtn_unselect(){
        taoluMode=false;
        taoluModeBtn.setSelected(false);
    }

    @FXML
    void taoluModeBtn_Aciton(){
        if(taoluMode)
            taoluModeBtn_unselect();
        else
            taoluModeBtn_selected();
    }

    @FXML
    void equalBtnAction(){
        if(equalMode)
            unSelectEqualBtn();
        else
            selectEqualBtn();
    }

    @FXML
    void selectEqualBtn(){
        equalModeBtn.setSelected(true);
        equalMode=true;
        readIgnoreList();
    }

    @FXML
    void unSelectEqualBtn(){
        equalModeBtn.setSelected(false);
        equalMode=false;
    }


    @FXML
    void addName(){
        data.add(inputName.getText());

        names.clear();
        names.addAll(data.getAll());
        nameList.setItems(names);
        nameList.refresh();

        clearIgnoreList();
        clearTaoluList();

        data.saveToFile();
        inputName.setText("");
        System.gc();
    }

    @FXML
    void showHistory(){

        historyShow.clear();
        historyShow.addAll(history);

        historyList.setItems(historyShow);
        historyList.refresh();


        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("历史记录"));
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        //content.setBody(new Text(message));

        vb.getChildren().add(historyList);
        historyList.setPrefHeight(400);
        vb.setAlignment(Pos.CENTER);
        content.setBody(vb);

        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(mainPane.getPrefHeight());
        dialog.setPrefWidth(mainPane.getPrefWidth());
        JFXButton button = new JFXButton("OK");
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
        @Override
        public void handle(JFXDialogEvent event) {
          mainPane.getChildren().remove(tempPane);
            }
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
        });
        content.setActions(button);

        dialog.show();
    }
    @FXML
    void deleteName(){
        data.delete((String)nameList.getSelectionModel().getSelectedItems().get(0));
        names.remove((String)nameList.getSelectionModel().getSelectedItems().get(0));
        clearIgnoreList();
        clearTaoluList();
        data.saveToFile();
        System.gc();
    }

    @FXML
    void showNameManger(){
        names.clear();
        names.addAll(data.getAll());
        nameList.setItems(names);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(namePane.layoutXProperty(), 0,Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(400), kv);


        final KeyValue kv2 = new KeyValue(mainPane.layoutXProperty(), -mainPane.getWidth()/2,Interpolator.EASE_BOTH);
        final KeyFrame kf2 = new KeyFrame(Duration.millis(400), kv2);

        timeline.getKeyFrames().add(kf);
        timeline.getKeyFrames().add(kf2);

        timeline.play();

    }

    @FXML
    void turnBack() {

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(namePane.layoutXProperty(), namePane.getWidth(),Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(400), kv);


        final KeyValue kv2 = new KeyValue(mainPane.layoutXProperty(), 0,Interpolator.EASE_BOTH);
        final KeyFrame kf2 = new KeyFrame(Duration.millis(400), kv2);

        timeline.getKeyFrames().add(kf);
        timeline.getKeyFrames().add(kf2);

        timeline.play();
    }

    @FXML
    void test(){/*
        new DialogBuilder(showNameMangerButton).setTitle("提示").setMessage("输入").setTextFieldText(new DialogBuilder.OnInputListener() {
            @Override
            public void onGetText(String result) {
                //返回一个输入结果result
                //相关的逻辑操作
            }
        }).setPositiveBtn("确定").setNegativeBtn("取消").create();*/
        goBack();
    }

    @FXML
    void goBack() {

        Scene scene=mainPane.getScene();
        stage=(Stage)scene.getWindow();

        oldX=(short) stage.getX();
        oldY=(short) stage.getY();
        oldW=(short)stage.getWidth();
        oldH=(short)stage.getHeight();

        EventHandler eventHandler=new MoveWindow(stage);

        scene.setOnMousePressed(eventHandler);
        scene.setOnMouseDragged(eventHandler);

        EventHandler hander=new MoveWindowByTouch(stage);

        scene.setOnTouchPressed(hander);
        scene.setOnTouchMoved(hander);

        goBackButton.setOnMousePressed(eventHandler);
        goBackButton.setOnMouseDragged(eventHandler);

        goBackButton.setOnTouchPressed(hander);
        goBackButton.setOnTouchMoved(hander);

        stage.setAlwaysOnTop(true);
/*
        stage.close();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
*/
        stage.setWidth(30);
        stage.setHeight(100);
        stage.setY(stage.getY()+stage.getHeight()-50);
        mainPane.setVisible(false);
        recover.setVisible(true);

    }

    @FXML
    void recoverWindow(){
        Scene scene=mainPane.getScene();
        stage=(Stage)scene.getWindow();

        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);

        scene.setOnTouchPressed(null);
        scene.setOnTouchMoved(null);

        goBackButton.setOnMousePressed(null);
        goBackButton.setOnMouseDragged(null);

        goBackButton.setOnTouchPressed(null);
        goBackButton.setOnTouchMoved(null);

        stage.setAlwaysOnTop(false);
/*
        stage.close();
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
*/
        stage.setResizable(false);

        stage.setWidth(oldW);
        stage.setHeight(oldH);
        stage.setY(oldY);
        mainPane.setVisible(true);
        recover.setVisible(false);
    }



    @FXML
    void deleteAllName(){
        data.deleteAll();
        clearIgnoreList();
        clearTaoluList();
        names.clear();
        data.saveToFile();
        System.gc();
    }

    @FXML
    void ignoreOnce_selected() {
        ignorePast=true;
        ignoreOnce.setSelected(true);
        chooseOnce.setSelected(false);

        taoluMode=false;
        taoluModeBtn.setDisable(true);
        taoluModeBtn.setSelected(false);

        equalModeBtn.setDisable(false);

    }

    @FXML
    void chooseOnce_selected() {
        ignorePast=false;
        chooseOnce.setSelected(true);
        ignoreOnce.setSelected(false);
        unSelectEqualBtn();
        equalModeBtn.setDisable(true);
        if(isNameChoose){
            //taoluMode=true;
            taoluModeBtn.setDisable(false);

        }
    }


    @FXML
    void randomTimes_selected() {
        isRandomTimes=true;
        fixedTimes.setSelected(false);
        randomTimes.setSelected(true);
    }


    @FXML
    void fixedTimes_selected() {
        isRandomTimes=false;
        fixedTimes.setSelected(true);
        randomTimes.setSelected(false);
    }

    @FXML
    void exoprtNameList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("nameList.txt");
        fileChooser.setTitle("想保存到哪？");
        File file = fileChooser.showSaveDialog(stage);
        data.exportNameList(file);
        System.gc();
    }

    @FXML
    void importNameList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("告诉我在哪？");
        File file = fileChooser.showOpenDialog(stage);

        data.importNameList(file);
        names.clear();
        names.addAll(data.getAll());
        nameList.setItems(names);
        nameList.refresh();

        clearIgnoreList();
        clearTaoluList();

        data.saveToFile();
        System.gc();
    }

    @FXML
    void makeAMass() {
        data.makeMass();

        names.clear();
        names.addAll(data.getAll());
        nameList.setItems(names);
        nameList.refresh();


        data.saveToFile();
        System.gc();
    }

    @FXML
    void showInfo(){
	showAuInfo("Me?","这是一个以Java语言编写，采用Google Material Design（Google MD）为界面风格的用来点名的东西。\n该程序的源代码可在 https://github.com/eatenid/dogename 查看和获取。（更新什么的基本不打算的了ヾ§ ￣▽)ゞ）\n\n使用到的第三方库：\nJFoenix(8.0.4)\nApache Commons Codec(1.11)\nGson(2.8.5) \n感谢gushici项目提供的古诗词数据接口，详情请前往：https://github.com/xenv/gushici\n\n关于作者的一些东西：\nGithub主页：https://github.com/eatenid\nGoogle+：kygbuff@gamil.com\n\n邮箱等：\nHet2002@outlook.com\n2318724550@qq.com\nulcch@foxmail.com\n\n\nCreated by He T.Y.");
    }


    public void showInfoDialog(String header,String message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        content.setBody(new Text(message));
        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(mainPane.getPrefHeight());
        dialog.setPrefWidth(mainPane.getPrefWidth());
        JFXButton button = new JFXButton("OK");
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                mainPane.getChildren().remove(tempPane);
            }
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
        });
        content.setActions(button);

        dialog.show();
    }


    public void showAuInfo(String header,String message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        Text text=new Text(message);
        //content.setBody(new Text(message));

        vb.getChildren().add(new ImageView(new Image(releaseData.getDogenameStream())));
        vb.getChildren().add(text);
        vb.setAlignment(Pos.CENTER);
        content.setBody(vb);
        
        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(mainPane.getPrefHeight());
        dialog.setPrefWidth(mainPane.getPrefWidth());
        JFXButton button = new JFXButton("OK");dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                mainPane.getChildren().remove(tempPane);
            }
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
        });
        content.setActions(button);

        dialog.show();
    }

    /**
     * @return the app
     */
    public App getApp() {
        return app;
    }

    /**
     * @param app the app to set
     */
    public void setApp(App app) {
        this.app = app;
    }

}
