package main;

import com.jfoenix.controls.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;

public class UICtrl {

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

    File nameIgnoreFile =new File("D:\\dogename\\files\\nameIgnoreList");
    File numbIgnoreFile =new File("D:\\dogename\\files\\numbIgnoreList");


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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GuShiCi gsc=new GuShiCi();
                    String shici =gsc.get();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String topText=gsc.getShiciContent();
                            showShiCi("每日古诗词",gsc);
                            topBar.setText(topText+"——"+gsc.getAuthor()+"《"+gsc.getOrigin()+"》");
                        }
                    });
                }catch(Exception e) {e.printStackTrace();}
            }
        }).start();
    }

    public void showShiCi(String header,GuShiCi gsc) {
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        Text contentText =new Text("“"+gsc.getShiciContent()+"”");
        contentText.setFont(new Font("BLOOD",26));
        contentText.setTextAlignment(TextAlignment.CENTER);
        Text authorText =new Text("\n\n——"+gsc.getAuthor()+"《"+gsc.getOrigin()+"》");
        authorText.setTextAlignment(TextAlignment.RIGHT);
        authorText.setFont(new Font(14));
        Text categoryText =new Text("\n\n\n#"+gsc.getCategory());
        categoryText.setFont(new Font(12));
        categoryText.setTextAlignment(TextAlignment.RIGHT);
        vb.getChildren().addAll(contentText,authorText,categoryText);

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        Text text =new Text("");
        text.setFont(new Font(14));
        text.setTextAlignment(TextAlignment.CENTER);
        content.setBody(vb);
        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(mainPane.getPrefHeight());
        dialog.setPrefWidth(mainPane.getPrefWidth());
        JFXButton button = new JFXButton("已阅");
        tempPane.setOnMousePressed((MouseEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        tempPane.setOnTouchPressed((TouchEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        content.setActions(button);

        dialog.show();
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
    //-------------------------------------------------------------------------------------------
    AnimationTimer timer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            if(forceStop){
                already=chosenTime+1;
                isRunning=false;
            }


            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(already>=chosenTime){
                if(!ignoreNameList.contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        ignoreNameList.add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTimesOut=false;
                    ignoreNameTimes++;

                    switch (showWhich){
                        case 1:{
                            if(chosen_2.getText().contains("→"))
                                chosen_2.setText(chosen_2.getText().replace("→",""));

                            chosen_1.setText("→"+chosen_1.getText());

                            if(taoluMode)
                                data.addTaoluedName(chosen_1.getText().replace("→",""),5);

                            break;
                        }
                        case 2:{
                            if(chosen_1.getText().contains("→"))
                                chosen_1.setText(chosen_1.getText().replace("→",""));

                            chosen_2.setText("→"+chosen_2.getText());

                            if(taoluMode)
                                data.addTaoluedName(chosen_2.getText().replace("→",""),4);

                            break;
                        }
                    }
                    isRunning=false;
                    choose.setText("安排一下");
                    stop();
                    controllerPane.setDisable(false);
                    System.gc();
                    return;
                }else
                    ignoreTimesOut=true;



            }
            if(singleCycle>=times&&!ignoreTimesOut){
                cycleEnd=true;
                singleCycle=0;
            }

            if(cycleEnd){
                times=(int)(1+Math.random()*(chosenTime-already));
                cycleEnd=false;
                showWhich=(int)(1+Math.random()*2);
            }



            switch (showWhich){
                case 1:{
                    chosenName=data.randomGet(taoluMode);
                    chosen_1.setText(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    chosenName=data.randomGet(taoluMode);
                    chosen_2.setText(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }
            }


        }
    };
    //---------------------------------------------------------------------------------------
    SecureRandom secRandom =new SecureRandom();
    AnimationTimer numbTimer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            if(forceStop){
                already=chosenTime+1;
                isRunning=false;
            }

            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(already>=chosenTime){
                if(!ignoreNumberList.contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        ignoreNumberList.add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTimesOut=false;
                    ignoreNameTimes++;

                    switch (showWhich){
                        case 1:{
                            if(chosen_2.getText().contains("→"))
                                chosen_2.setText(chosen_2.getText().replace("→",""));

                            chosen_1.setText("→"+chosen_1.getText());

                            break;
                        }
                        case 2:{
                            if(chosen_1.getText().contains("→"))
                                chosen_1.setText(chosen_1.getText().replace("→",""));

                            chosen_2.setText("→"+chosen_2.getText());

                            break;
                        }
                    }
                    isRunning=false;
                    choose.setText("安排一下");
                    stop();
                    controllerPane.setDisable(false);
                    System.gc();
                    return;
                }else
                    ignoreTimesOut=true;

            }
            if(singleCycle>=times&&!ignoreTimesOut){
                cycleEnd=true;
                singleCycle=0;
            }

            if(cycleEnd){
                times=(int)(1+Math.random()*(chosenTime-already));
                cycleEnd=false;
                showWhich=(int)(1+Math.random()*2);
            }

            switch (showWhich){
                case 1:{
                    if(newAlgo)
                        chosen_1.setText(String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1)));
                    else
                        chosen_1.setText(String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1)));

                    chosenName=chosen_1.getText();
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    if(newAlgo)
                        chosen_2.setText(String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1)));
                    else
                        chosen_2.setText(String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1)));

                    chosenName=chosen_2.getText();
                    already++;
                    singleCycle++;
                    break;
                }
            }


        }
    };



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
    public  JFXButton recover;

    public JFXCheckBox taoluModeBtn;
    public JFXCheckBox equalModeBtn;
    public JFXCheckBox newAlgoBtn;

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
    public Pane controllerPane;

    public Stage stage;
    public Scene scene;

    public Pane mainPane;

    short oldX;
    short oldY;
    short oldW;
    short oldH;

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


    @FXML
    public void newAlgoBtnAction(){
        if(newAlgo)
            unselectNewAlgoBtn();
        else
            selectNewAlgoBtn();
    }

    public boolean isNewAlgo() {
        return newAlgo;
    }

    public void setNewAlgo(boolean newAlgo) {
        this.newAlgo = newAlgo;
    }

    void selectNewAlgoBtn(){
        newAlgo = true;
        newAlgoBtn.setSelected(true);
        data.setNewAlgo(newAlgo);
    }

    void unselectNewAlgoBtn(){
        newAlgo=false;
        newAlgoBtn.setSelected(false);
        data.setNewAlgo(newAlgo);
    }


    final static private String CONFIG_FILE="D:\\dogename\\files\\config";
    final private File configFile=new File(CONFIG_FILE);

    public int saveConfigToFile(){

        config.setChosenTime(chosenTime);
        config.setIgnorePast(ignorePast);
        config.setMaxNumber(maxNumber);
        config.setMinNumber(minNumber);
        config.setNameChoose(isNameChoose);
        config.setSpeed(speed);
        config.setRandomTimes(isRandomTimes);
        config.setTaoluMode(taoluMode);
        config.setEqualMode(equalMode);
        config.setNewAlgo(newAlgo);


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


    boolean isRunning=false;

    @FXML
    void anPai(){
        saveConfigToFile();
        if(isRunning){
            forceStop=true;
            choose.setText("安排一下");
            return;
        }

        if(isRandomTimes) {
            chosenTime =  100 + (int) (Math.random() * (250 - 100));
            chooseTimes.setValue(chosenTime);
        }
        else
            chosenTime=(int)chooseTimes.getValue();


        //int s=(int)min+(int)(Math.random()*(max-min));

        if(isNameChoose){
            if(data.isEmpty(taoluMode)){
                showInfoDialog("哦霍~","现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
                return;
            }

            if(ignoreNameList.size()>=data.getSize()&&ignorePast){
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
            timer.start();

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
            numbTimer.start();

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
        File file = fileChooser.showSaveDialog(stage);

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
        showInfoDialog("Me?","这是一个以Java语言编写，采用Google Material Design（Google MD）为界面风格的用来点名的东西。\n该程序的源代码可在 https://github.com/eatenid/dogename 查看和获取。（更新什么的基本不打算的了ヾ§ ￣▽)ゞ）\n\n使用到的第三方库：\nJFoenix(8.0.4)\nApache Commons Codec(1.11)\nGson(2.8.5) \n感谢gushici项目提供的古诗词数据接口，详情请前往：https://github.com/xenv/gushici\n\n关于作者的一些东西：\nGithub主页：https://github.com/eatenid\nGoogle+：kygbuff@gamil.com\n\n邮箱等：\nHet2002@outlook.com\n2318724550@qq.com\nulcch@foxmail.com\n\n\nCreated by He T.Y.");
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
        tempPane.setOnMousePressed((MouseEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        tempPane.setOnTouchPressed((TouchEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();
            mainPane.getChildren().remove(tempPane);
        });
        content.setActions(button);

        dialog.show();
    }


}
