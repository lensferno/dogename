package me.hety.dogename.main.chooser;

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import me.hety.dogename.main.configs.VoiceConfig;
import me.hety.dogename.main.data.History;
import me.hety.dogename.main.data.NameData;
import me.hety.dogename.main.voice.Token;
import me.hety.dogename.main.voice.VoicePlayer;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
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

public final class Chooser {

    Logger log =Logger.getLogger("ChooserLogger");

    boolean taoluMode;

    Token token;
    VoicePlayer voicePlayer;

    VoiceConfig voiceConfig;

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

    History history;
    private String HISTORY_FILE;
    private File historyFile;

    String speaker,speakSpeed,intonation;

    void writeIgnoreList(){
        nameData.writeIgnoreList("");
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
                    history.addHistory(chosenName);
                    if(voicePlay)
                        voicePlayer.playVoice(chosenName,speaker,intonation,speakSpeed);
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
                    chosenName= nameData.randomGet();
                    upLabelText.set(chosenName);
                    already++;
                    singleCycle++;
                    break;
                }

                case 2:{
                    chosenName= nameData.randomGet();
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
                    history.addHistory(chosenName);
                    if(voicePlay)
                        voicePlayer.playVoice(chosenName,speaker,intonation,speakSpeed);
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



    public void run(short speed,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode,boolean voicePlay){

        this.speed = speed;
        this.chosenTime = chosenTime;
        this.ignorePast = ignorePast;
        this.equalMode = equalMode;
        this.taoluMode = taoluMode;
        this.voicePlay = voicePlay;

        isRunning=true;
        timer.start();
    }

    public void run(short maxNumber,short minNumber,short speed,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode,boolean voicePlay){
        this.maxNumber = maxNumber;
        this.minNumber = minNumber;
        this.speed = speed;
        this.chosenTime = chosenTime;
        this.ignorePast = ignorePast;
        this.equalMode = equalMode;
        this.taoluMode = taoluMode;
        this.voicePlay = voicePlay;

        isRunning=true;
        numbTimer.start();
    }

    public void set(StringProperty upLabelText, StringProperty downLabelText, JFXButton anpaiBtn, History history, NameData nameData, Token token, VoiceConfig voiceConfig){

        this.upLabelText=upLabelText;
        this.downLabelText=downLabelText;

        this.anpaiBtn = anpaiBtn;
        this.history = history;
        this.nameData = nameData;

        this.token=token;
        this.voicePlayer=new VoicePlayer(token);

        this.voiceConfig=voiceConfig;

        this.speaker=String.valueOf(voiceConfig.getSpeaker());
        this.speakSpeed=String.valueOf(voiceConfig.getSpeed());
        this.intonation=String.valueOf(voiceConfig.getIntonation());
        this.speaker=voiceConfig.getSpeaker();
    }


    public void setChoose(JFXButton anpaiBtn) {
        this.anpaiBtn = anpaiBtn;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }
}
