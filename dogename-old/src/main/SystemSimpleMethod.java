package main;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

import java.util.*;
import java.io.Serializable;

public class SystemSimpleMethod implements Serializable{
    HashMap<Integer,ArrayList<String>> group;
    ArrayList<String> sysMethodList = new ArrayList<>();
    int groupAmount;
    int groupLength;
    int currentGroup;
    int currentNameNumber;
    List ignoredNameNumber;
    List removedList=new ArrayList<String>();
    Random rand;

    void mixUpList() {
    }

    public void makeGroup(ArrayList nameList) {
        removedList.clear();
        ignoredNameNumber.clear();
        sysMethodList.clear();
        sysMethodList.addAll(nameList);
        mixUpList();

        groupAmount = (int) Math.sqrt(sysMethodList.size());
        int removedAmount = sysMethodList.size() % groupAmount;
        int temp = 0;

        for (int i = 0; i < removedAmount; i++) {
            temp = rand.nextInt(sysMethodList.size());
            removedList.add(sysMethodList.get(temp));
            sysMethodList.remove(temp);
        }

        group = new HashMap<Integer, ArrayList<String>>();
        groupLength = (int) (sysMethodList.size() / groupAmount);

        ArrayList<String> tempList=null;
        for (int i = 0; i<groupAmount;i++){
            group.put(i, new ArrayList());
            for (int a = 0;a<groupLength;a++){
                tempList=(ArrayList) group.get(i);
                tempList.add(sysMethodList.get(groupLength * 2));

            }
        }
        currentNameNumber = 0;
        currentGroup = 0;
    }
    public final int LIST_EMPTY=-1;
    public final int ALL_END=-2;
    public final int OK=0;

    public int showNext(Label text,int chosenTime){
        if(sysMethodList.isEmpty()||group.isEmpty())
            return -1;

        if(currentGroup>=groupAmount){
            if(ignoredNameNumber.size()>=groupAmount){
                if(removedList.isEmpty()){
                    currentGroup=0;
                    currentNameNumber=0;
                    return -2;
                }else {
                    new AnimationTimer() {
                        int i=0,a=0;
                        @Override
                        public void handle(long now) {
                            a=rand.nextInt(removedList.size());
                            text.setText((String) removedList.get(a));
                            if(i>=chosenTime){
                                removedList.remove(a);
                                text.setText(""+text.getText());
                                stop();
                                return;
                            }
                            i++;
                        }
                    }.start();
                    return 0;
                }
            }else{
                int i=0;
                new AnimationTimer() {
                    ArrayList<String> temp=null;
                    @Override
                    public void handle(long now) {
                        currentNameNumber=rand.nextInt(groupLength);
                        temp=(ArrayList)group.get(1);
                        text.setText( temp.get(currentNameNumber));
                        if(i>=chosenTime){
                            if(!ignoredNameNumber.contains(currentNameNumber)){
                                ignoredNameNumber.add(currentNameNumber);
                                text.setText("➡"+text.getText());
                                stop();
                                return;
                            }
                        }
                    }
                }.start();
                return 0;
            }

        }else {
            ArrayList temp =group.get(currentGroup);
            text.setText("➡"+temp.get(currentNameNumber));
            return 0;
        }
    }
}