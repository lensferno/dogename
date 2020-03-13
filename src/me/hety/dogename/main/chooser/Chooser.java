package me.hety.dogename.main.chooser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import me.hety.dogename.main.Voice;
import me.hety.dogename.main.data.NameData;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

        /*

┴┬┴┬／￣＼＿／￣＼
┬┴┬┴▏　　▏▔▔▔▔＼
┴┬┴／＼　／　　　　　　﹨
┬┴∕　　　　　　　／　　　）
┴┬▏　　　　　　　　●　　▏
┬┴▏　　　　　　　　　　　▔█　
┴◢██◣　　　　　＼＿＿＿／
┬█████◣　　　　　　　／　　
┴█████████████◣
◢██████████████▆▄
█◤◢██◣◥█████████◤＼
◥◢████　████████◤　　 ＼
┴█████　██████◤　　　　　 ﹨
┬│　　　│█████◤　　　　　　　　▏
┴│　　　│　　　　　　　　　　　　　　▏
┬ ∕　　　 ∕　　　　／▔▔▔＼　　　　 ∕
┴/＿＿＿／﹨　　　∕　　　　　﹨　　／＼
┬┴┬┴┬┴＼ 　　 ＼ 　　　　　﹨／　　 ﹨
┴┬┴┬┴┬┴ ＼＿＿＿＼　　　　 ﹨／▔＼﹨ ▔＼
▲△▲▲╓╥╥╥╥╥╥╥╥＼　　 ∕　 ／▔﹨／▔﹨
　＊＊╠╬╬╬╬╬╬╬╬＊﹨　　／　　／／

         */

public class Chooser {

    Logger log =Logger.getLogger("ChooserLogger");

    boolean taoluMode;

    Random random =new Random();

    public short minNumber;
    public short maxNumber;

    boolean voicePlay=true;

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

    public JFXButton anpaiBtn;

    public Label chosen_1;
    public Label chosen_2;

    public boolean isNameChoose=true;
    public short speed;

    NameData nameData;

    boolean isRunning;

    StringProperty upLabelText;
    StringProperty downLabelText;

    List history;
    private String HISTORY_FILE;
    private File historyFile;

    Voice voice;
    void loadHistory(){

        if(System.getProperty("os.name").toLowerCase().contains("window"))
            HISTORY_FILE="files\\history.data";
        else
            HISTORY_FILE="files/history.data";

        try {
            File historyFile = new File(HISTORY_FILE);
            if (!historyFile.exists()) {
                historyFile.createNewFile();
                history = new ArrayList();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile));
            this.history = (ArrayList) ois.readObject();

        } catch (EOFException e){
            //ignoreNameList=new HashSet<>();
            log.warning("History file is empty.");
        }catch (Exception e) {
            history = new ArrayList();
            log.warning("Failed to load history file.");
            e.printStackTrace();
        }

    }

    void writeIgnoreList(){
        nameData.writeIgnoreList("");
    }

    void addHistory(String name){
        if(history.size()>2000)
            history.clear();
        history.add((history.size() + 1) +". "+name);
        saveHistory();
    }


    void saveHistory(){

        if(System.getProperty("os.name").toLowerCase().contains("window"))
            HISTORY_FILE="files\\history.data";
        else
            HISTORY_FILE="files/history.data";

        historyFile=new File(HISTORY_FILE);
        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(historyFile));
            oos.writeObject(history);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    SimpleBooleanProperty beTrueProperty=new SimpleBooleanProperty();
    SimpleBooleanProperty beFalseProperty=new SimpleBooleanProperty();

    AnimationTimer timer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            if(forceStop){
                already=chosenTime+1;
            }

            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(already>=chosenTime){
                if(!nameData.getIgnoreNameList().contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        nameData.getIgnoreNameList().add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTimesOut=false;

                    switch (showWhich){
                        case 1:{

                            if(downLabelText.get().contains("→")||downLabelText.get().contains("←"))
                                downLabelText.set(downLabelText.get().replace("→ ","").replace(" ←",""));

                            upLabelText.set("→ "+chosenName+" ←");

                            if(taoluMode)
                                nameData.addTaoluedName(chosenName,5);

                            break;
                        }
                        case 2:{
                            if(upLabelText.get().contains("→")||upLabelText.get().contains("←"))
                                upLabelText.set(upLabelText.get().replace("→ ","").replace(" ←",""));

                            downLabelText.set("→ "+chosenName+" ←");

                            if(taoluMode)
                                nameData.addTaoluedName(chosenName,4);

                            break;
                        }
                    }
                    isRunning=false;
                    anpaiBtn.setText("安排一下");
                    beTrueProperty.set(true);
                    beFalseProperty.set(false);
                    stop();
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
                    chosenName= nameData.randomGet(taoluMode);
                    upLabelText.set(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    chosenName= nameData.randomGet(taoluMode);
                    downLabelText.set(chosenName);
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
            }

            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(already>=chosenTime){
                if(!nameData.getIgnoreNumberList().contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        nameData.getIgnoreNumberList().add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    already=0;
                    singleCycle=0;
                    ignoreTimesOut=false;


                    switch (showWhich){
                        case 1:{
                            if(downLabelText.get().contains("→")||downLabelText.get().contains("←"))
                                downLabelText.set(downLabelText.get().replace("→ ","").replace(" ←",""));

                            upLabelText.set("→ "+chosenName+" ←");

                            break;
                        }
                        case 2:{
                            if(upLabelText.get().contains("→")||upLabelText.get().contains("←"))
                                upLabelText.set(upLabelText.get().replace("→ ","").replace(" ←",""));

                            downLabelText.set("→ "+chosenName+" ←");

                            break;
                        }
                    }
                    isRunning=false;
                    anpaiBtn.setText("安排一下");
                    beTrueProperty.set(true);
                    beFalseProperty.set(false);
                    stop();
                    System.gc();
                    addHistory(chosenName);
                    if(voicePlay)
                        voice.playVoice(chosenName);
                    return;
                }else
                    ignoreTimesOut=true;

            }

            showWhich=1+random.nextInt(2);
            //speed=(short)(65+random.nextInt(100));

            switch (showWhich){
                case 1:{
                    if(newAlgo)
                        chosenName=String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1));
                    else
                        chosenName=String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1));

                    upLabelText.set(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    if(newAlgo)
                        chosenName=String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1));
                    else
                        chosenName=String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1));

                    downLabelText.set(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }
            }


        }
    };

    public void forceStop(){

    }



    public void run(NameData nameData,short speed,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode,boolean voicePlay){
        setSpeed(speed);
        setNameData(nameData);
        setChosenTime(chosenTime);
        setIgnorePast(ignorePast);
        setEqualMode(equalMode);
        setTaoluMode(taoluMode);
        setVoicePlay(voicePlay);
        isRunning=true;
        timer.start();
    }

    public void run(short maxNumber,short minNumber,short speed,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode,boolean voicePlay){
        setMaxNumber(maxNumber);
        setMinNumber(minNumber);
        setSpeed(speed);
        setChosenTime(chosenTime);
        setIgnorePast(ignorePast);
        setEqualMode(equalMode);
        setTaoluMode(taoluMode);
        setVoicePlay(voicePlay);
        isRunning=true;
        numbTimer.start();
    }

    public void set(StringProperty upLabelText,StringProperty downLabelText,JFXButton anpaiBtn,List history,Voice voice){

        this.upLabelText=upLabelText;
        this.downLabelText=downLabelText;
        
        setChoose(anpaiBtn);
        setHistory(history);
        setVoice(voice);
    }

    public void setHistory(List history) {
        this.history = history;
    }

    public boolean isBeTrueProperty() {
        return beTrueProperty.get();
    }

    public SimpleBooleanProperty beTruePropertyProperty() {
        return beTrueProperty;
    }

    public boolean isBeFalseProperty() {
        return beFalseProperty.get();
    }

    public SimpleBooleanProperty beFalsePropertyProperty() {
        return beFalseProperty;
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

    public void setChoose(JFXButton anpaiBtn) {
        this.anpaiBtn = anpaiBtn;
    }

    public void setNameData(NameData data) {
        this.nameData = data;
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
