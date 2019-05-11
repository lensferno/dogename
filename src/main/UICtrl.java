package main;

import com.jfoenix.controls.*;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class UICtrl {

    boolean taoluMode=true;

    List<String> chooseList=new ArrayList<>();

    List<String> ignoreNameList=new ArrayList<>();
    short ignoreNameTimes=0;

    List<String> ignoreNumberList=new ArrayList<>();
    short ignoreNumberTimes=0;

    public JFXTextField minNumb;
    public JFXTextField maxNumb;

    public short minNumber;
    public short maxNumber;


    int chosenTime=120;
    int times=0;
    int already=0;
    int singleCycle=0;
    int showWhich=1;

    String chosenName;

    boolean cycleEnd =true;
    boolean ignoreTinmesOut=false;
    boolean ignorePast=true;

//-------------------------------------------------------------------------------------------
    AnimationTimer timer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            try{
                Thread.sleep(speed);
            }catch (Exception e){ }

            if(already>=chosenTime){
                if(!ignoreNameList.contains(chosenName)||!ignorePast){
                    ignoreNameList.add(chosenName);
                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTinmesOut=false;
                    ignoreNameTimes++;

                    switch (showWhich){
                        case 1:{
                            if(chosen_2.getText().contains("→"))
                                chosen_2.setText(chosen_2.getText().replace("→",""));

                            chosen_1.setText("→"+chosen_1.getText());

                            for(int i=0;i<15;i++)
                                chooseList.add(chosen_1.getText());

                            break;
                        }
                        case 2:{
                            if(chosen_1.getText().contains("→"))
                                chosen_1.setText(chosen_1.getText().replace("→",""));

                            chosen_2.setText("→"+chosen_2.getText());

                            for(int i=0;i<15;i++)
                                chooseList.add(chosen_2.getText());

                            break;
                        }
                    }
                    stop();
                    controllerPane.setDisable(false);
                    return;
                }else
                    ignoreTinmesOut=true;



            }
            if(singleCycle>=times&&!ignoreTinmesOut){
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
                    chosenName=data.randomGet();
                    chosen_1.setText(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    chosenName=data.randomGet();
                    chosen_2.setText(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }
            }


        }
    };
//---------------------------------------------------------------------------------------
    AnimationTimer numbTimer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            try{
                Thread.sleep(speed);
            }catch (Exception e){ }

            if(already>=chosenTime){
                if(!ignoreNumberList.contains(chosenName)||!ignorePast){
                    ignoreNumberList.add(chosenName);
                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTinmesOut=false;
                    ignoreNameTimes++;

                    switch (showWhich){
                        case 1:{
                            if(chosen_2.getText().contains("→"))
                                chosen_2.setText(chosen_2.getText().replace("→",""));

                            chosen_1.setText("→"+chosen_1.getText());

                            for(int i=0;i<15;i++)
                                chooseList.add(chosen_1.getText());

                            break;
                        }
                        case 2:{
                            if(chosen_1.getText().contains("→"))
                                chosen_1.setText(chosen_1.getText().replace("→",""));

                            chosen_2.setText("→"+chosen_2.getText());

                            for(int i=0;i<15;i++)
                                chooseList.add(chosen_2.getText());

                            break;
                        }
                    }
                    stop();
                    controllerPane.setDisable(false);
                    return;
                }else
                    ignoreTinmesOut=true;

            }
            if(singleCycle>=times&&!ignoreTinmesOut){
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
                    chosen_1.setText(String.valueOf(
                            (int)minNumber+(int)(Math.random()*(maxNumber-minNumber))
                    ));

                    chosenName=chosen_1.getText();
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    chosen_2.setText(String.valueOf(
                            (int)minNumber+(int)(Math.random()*(maxNumber-minNumber))
                    ));
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
    
    public ImageView mainView;
    public ImageView backBtn;
    public Image mainImage =new Image(releaseData.getMainImageStream());
    public Image backBtnImage =new Image(releaseData.getBackBtnStream());



    short oldX;
    short oldY;
    short oldW;
    short oldH;

    private Data data=new Data();

    public boolean isRandomTimes=true;

    public Config config;

    public static final ObservableList names = FXCollections.observableArrayList();

    public void setImages(){
        mainView.setImage(mainImage);
        backBtn.setImage(backBtnImage);
    }

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


    final static private String CONFIG_FILE="D:\\DM_Master_sources-master\\config";
    final private File configFile=new File(CONFIG_FILE);

    public int saveConfigToFile(){

        config.setChosenTime(chosenTime);
        config.setIgnorePast(ignorePast);
        config.setMaxNumber(maxNumber);
        config.setMinNumber(minNumber);
        config.setNameChoose(isNameChoose);
        config.setSpeed(speed);
        config.setRandomTimes(isRandomTimes);

        System.out.println(speed);
        System.out.println(chosenTime);

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


    @FXML
    void anPai(){

        if(isRandomTimes) {
            chosenTime =  100 + (int) (Math.random() * (250 - 100));
            chooseTimes.setValue(chosenTime);
        }
        else
            chosenTime=(int)chooseTimes.getValue();


        //int s=(int)min+(int)(Math.random()*(max-min));

        if(isNameChoose){
            if(data.isEmpty()){
                showInfoDialog("哦霍~","现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
                return;
             }

             if(ignoreNameList.size()>=data.getSize()&&ignorePast){
                 showInfoDialog("啊？","全部名字都被点完啦！\n请多添加几个名字 或 选择“被点过的还要点”。");
                 return;
             }
            controllerPane.setDisable(true);
            speed=(short) (100-speedBar.getValue());
            timer.start();

        }else {

            try{
                minNumber=Short.parseShort(minNumb.getText());
                maxNumber=Short.parseShort(maxNumb.getText());

                if(maxNumber-minNumber<0){
                    showInfoDialog("嗯哼？","数字要前小后大啊~");
                    return;
                }

                if(ignoreNumberList.size()>=(maxNumber-minNumber) && ignorePast){
                    showInfoDialog("啊？","全部数字都被点完啦！\n请扩大数字范围 或 选择“被点过的还要点”。");
                    return;
                }

            }catch (Exception e){
                showInfoDialog("嗯哼？","倒是输入个有效的数字啊~");
                return;
            }

            controllerPane.setDisable(true);
            speed=(short) (100-speedBar.getValue());
            numbTimer.start();

        }

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
    }

    @FXML
    void nameChoose_selected(){
        isNameChoose=true;
        nameChoose.setSelected(true);
        numbPane.setVisible(false);
        numbChoose.setSelected(false);
        namePane.setVisible(true);
        showNameMangerButton.setVisible(true);
    }
    @FXML
    void addName(){
        data.add(inputName.getText());

        names.clear();
        names.addAll(data.getAll());
        nameList.setItems(names);
        nameList.refresh();

        data.saveToFile();
        inputName.setText("");
    }

    @FXML
    void deleteName(){
        data.delete((String)nameList.getSelectionModel().getSelectedItems().get(0));
        names.remove((String)nameList.getSelectionModel().getSelectedItems().get(0));
        data.saveToFile();
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
        names.clear();
        data.saveToFile();
    }

    @FXML
    void ignoreOnce_selected() {
        ignorePast=true;
        ignoreOnce.setSelected(true);
        chooseOnce.setSelected(false);
    }

    @FXML
    void chooseOnce_selected() {
        ignorePast=false;
        chooseOnce.setSelected(true);
        ignoreOnce.setSelected(false);
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
