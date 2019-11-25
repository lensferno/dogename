package main.sourcesData;

import com.jfoenix.controls.*;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import main.App;
import main.Data;
import main.Voice;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Core {

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

    boolean voicePlay=true;

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


    boolean newAlgo=true;


    public JFXButton choose;
    public ScrollPane controllerPane;

    public Label chosen_1;
    public Label chosen_2;

    File nameIgnoreFile =new File(app.APP_LOCA+"files\\nameIgnoreList.data");
    File numbIgnoreFile =new File(app.APP_LOCA+"files\\numbIgnoreList.data");

    public boolean isNameChoose=true;
    public short speed;

    Data data;

    boolean isRunning=false;

    List history;
    private String HISTORY_FILE;
    private File historyFile;

    Voice voice;
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


    void addHistory(String name){
        if(history.size()>2000)
            history.clear();
        history.add(String.valueOf(history.size()+1)+". "+name);
        saveHistory();
    }



    void saveHistory(){

        if(System.getProperty("os.name").toLowerCase().contains("window"))
            HISTORY_FILE=app.APP_LOCA+"files\\history.data";
        else
            HISTORY_FILE=app.APP_LOCA+"files/history.data";

        historyFile=new File(HISTORY_FILE);
        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(historyFile));
            oos.writeObject(history);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
                    addHistory(chosenName);
                    if(voicePlay)
                        voice.playVoice(chosenName);
                    return;
                }else
                    ignoreTimesOut=true;



            }
            if(singleCycle>=times&&!ignoreTimesOut){
                cycleEnd=true;
                singleCycle=0;
            }

            if(cycleEnd){
                //times=(int)(1+Math.random()*(chosenTime-already));
                times=1+random.nextInt(chosenTime-already+1);
                cycleEnd=false;
                //showWhich=(int)(1+Math.random()*2);
                showWhich=1+random.nextInt(2);
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
                    addHistory(chosenName);
                    if(voicePlay)
                        voice.playVoice(chosenName);
                    return;
                }else
                    ignoreTimesOut=true;

            }

            showWhich=1+random.nextInt(2);
            speed=(short)(65+random.nextInt(100));

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

    public void run(short speed,Data data,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode){
        setSpeed(speed);
        setData(data);
        setChosenTime(chosenTime);
        setIgnorePast(ignorePast);
        setEqualMode(equalMode);
        setTaoluMode(taoluMode);
        timer.start();
    }
    public void setMinNumber(short minNumber) {
        this.minNumber = minNumber;
    }

    public void setMaxNumber(short maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setChosenTime(int chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setIgnorePast(boolean ignorePast) {
        this.ignorePast = ignorePast;
    }

    public void setEqualMode(boolean equalMode) {
        this.equalMode = equalMode;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    public void setNameChoose(boolean nameChoose) {
        isNameChoose = nameChoose;
    }

    public void setSpeed(short speed) {
        this.speed = speed;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }


    public void setVoicePlay(boolean voicePlay) {
        this.voicePlay = voicePlay;
    }

    public void setTaoluMode(boolean taoluMode) {
        this.taoluMode = taoluMode;
    }

    public void setNewAlgo(boolean newAlgo) {
        this.newAlgo = newAlgo;
    }

    public void setChoose(JFXButton choose) {
        this.choose = choose;
    }

    public void setControllerPane(ScrollPane controllerPane) {
        this.controllerPane = controllerPane;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public void setChosen_1(Label chosen_1) {
        this.chosen_1 = chosen_1;
    }

    public void setChosen_2(Label chosen_2) {
        this.chosen_2 = chosen_2;
    }

    public boolean isForceStop() {
        return forceStop;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List getHistory() {
        return history;
    }

}
