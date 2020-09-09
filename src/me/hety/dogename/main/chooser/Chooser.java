package me.hety.dogename.main.chooser;

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.beans.property.StringProperty;
import me.hety.dogename.main.configs.VoiceConfig;
import me.hety.dogename.main.data.History;
import me.hety.dogename.main.data.NameData;
import me.hety.dogename.main.voice.Token;
import me.hety.dogename.main.voice.VoicePlayer;

import java.security.SecureRandom;
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
//一坨屎山，有待修改😒

public final class Chooser {

    Logger log =Logger.getLogger("ChooserLogger");

    final int UPPER_LABEL_ID = 1;
    final int UNDER_LABEL_ID = 2;

    boolean taoluMode;

    Token token;
    VoicePlayer voicePlayer;

    VoiceConfig voiceConfig;

    Random random =new Random();

    public short minNumber;
    public short maxNumber;

    boolean voicePlay=true;

    int totalMaxCount =120;
    int cycleMaxCount =0;

    int totalCount =0;
    int totalCycleCount =0;

    int shownLabelId =1;

    String chosenName;

    boolean cycleEnd =true;
    boolean ignoreTimesOut=false;
    boolean ignorePast=true;

    boolean equalMode=true;

    boolean forceStop =false;

    boolean newAlgo=true;

    public JFXButton anpaiBtn;

    public short speed;

    NameData nameData;

    boolean isRunning;

    StringProperty upLabelText;
    StringProperty downLabelText;

    History history;

    String speaker,speakSpeed,intonation;

    void writeIgnoreList(){
        nameData.writeIgnoreList("");
    }


    AnimationTimer timer =new AnimationTimer() {
        @Override
        public void handle(long now) {

            if(forceStop){
                totalCount = totalMaxCount +1;
            }

            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(totalCount >= totalMaxCount){
                if(!nameData.getIgnoreNameList().contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        nameData.getIgnoreNameList().add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    totalCount =0;
                    totalCycleCount =0;
                    ignoreTimesOut=false;

                    switch (shownLabelId){
                        case UPPER_LABEL_ID:{

                            if(downLabelText.get().contains("→")||downLabelText.get().contains("←"))
                                downLabelText.set(downLabelText.get().replace("→ ","").replace(" ←",""));

                            upLabelText.set("→ "+chosenName+" ←");

                            if(taoluMode)
                                nameData.addTaoluedName(chosenName,5);

                            break;
                        }
                        case UNDER_LABEL_ID:{
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
                    stop();
                    System.gc();
                    history.addHistory(chosenName);
                    if(voicePlay)
                        voicePlayer.playVoice(chosenName,speaker,intonation,speakSpeed);
                    return;
                }else
                    ignoreTimesOut=true;



            }
            if(totalCycleCount >= cycleMaxCount &&!ignoreTimesOut){
                cycleEnd=true;
                totalCycleCount =0;
            }

            if(cycleEnd){
                //times=(int)(1+Math.random()*(chosenTime-already));
                cycleMaxCount =1+random.nextInt(totalMaxCount - totalCount +1);
                cycleEnd=false;
                //showWhich=(int)(1+Math.random()*2);
                shownLabelId =1+random.nextInt(2);
            }



            switch (shownLabelId){
                case UPPER_LABEL_ID:{
                    chosenName= nameData.randomGet();
                    upLabelText.set(chosenName);
                    totalCount++;
                    totalCycleCount++;
                    break;
                }

                case UNDER_LABEL_ID:{
                    chosenName= nameData.randomGet();
                    downLabelText.set(chosenName);
                    totalCount++;
                    totalCycleCount++;
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
                totalCount = totalMaxCount +1;
            }

            try{
                Thread.sleep(speed);
            }catch (Exception e){e.printStackTrace(); }

            if(totalCount >= totalMaxCount){
                if(!nameData.getIgnoreNumberList().contains(chosenName)||!ignorePast||forceStop){

                    forceStop=false;

                    if(ignorePast)
                        nameData.getIgnoreNumberList().add(chosenName);
                    if(equalMode)
                        writeIgnoreList();

                    cycleEnd=true;
                    totalCount =0;
                    totalCycleCount =0;
                    ignoreTimesOut=false;


                    switch (shownLabelId){
                        case UPPER_LABEL_ID:{
                            if(downLabelText.get().contains("→")||downLabelText.get().contains("←"))
                                downLabelText.set(downLabelText.get().replace("→ ","").replace(" ←",""));

                            upLabelText.set("→ "+chosenName+" ←");

                            break;
                        }
                        case UNDER_LABEL_ID:{
                            if(upLabelText.get().contains("→")||upLabelText.get().contains("←"))
                                upLabelText.set(upLabelText.get().replace("→ ","").replace(" ←",""));

                            downLabelText.set("→ "+chosenName+" ←");

                            break;
                        }
                    }
                    isRunning=false;
                    anpaiBtn.setText("安排一下");
                    stop();
                    System.gc();
                    history.addHistory(chosenName);
                    if(voicePlay)
                        voicePlayer.playVoice(chosenName,speaker,intonation,speakSpeed);
                    return;
                }else
                    ignoreTimesOut=true;

            }

            shownLabelId =1+random.nextInt(2);
            //speed=(short)(65+random.nextInt(100));

            switch (shownLabelId){
                case UPPER_LABEL_ID:{
                    if(newAlgo)
                        chosenName=String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1));
                    else
                        chosenName=String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1));

                    upLabelText.set(chosenName);
                    totalCount++;
                    totalCycleCount++;
                    break;
                }

                case UNDER_LABEL_ID:{
                    if(newAlgo)
                        chosenName=String.valueOf(minNumber+random.nextInt(maxNumber-minNumber+1));
                    else
                        chosenName=String.valueOf(minNumber+secRandom.nextInt(maxNumber-minNumber+1));

                    downLabelText.set(chosenName);
                    totalCount++;
                    totalCycleCount++;
                    break;
                }
            }


        }
    };

    public void forceStop(){

    }



    public void run(short speed,int chosenTime,boolean ignorePast,boolean equalMode,boolean taoluMode,boolean voicePlay){

        this.speed = speed;
        this.totalMaxCount = chosenTime;
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
        this.totalMaxCount = chosenTime;
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
