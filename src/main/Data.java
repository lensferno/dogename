package main;

import java.io.*;
import java.util.*;

public class Data {
    private List<String> nameList;

    private List<String> chooseList;
    private int listSize = 0;


    File dataFile =new File("D:\\dogename\\files\\data");

    //不做注释了，自己慢慢看。：）

    //------------------------------------------------------
    public Data(){

        try{

            if(dataFile.exists()!=true){
                dataFile.createNewFile();
                nameList= new ArrayList<>();
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(dataFile));
            this.nameList=(ArrayList)ois.readObject();
            
            listSize=nameList.size();
            this.chooseList=new ArrayList<>(nameList);

        }catch (Exception e){
            nameList=new ArrayList<>();
            chooseList=new ArrayList<>();
            e.printStackTrace();
        }



    }

    //------------------------------------------------------
    public void add(String text){
        String[] temp;

        if(text.contains("\n")||text.contains("\t")){
            temp=text.split("\n");
            for(int i=0;i<temp.length;i++)
                nameList.add(temp[i]);
        }else {
            nameList.add(text);
            listSize=nameList.size();
        }
        chooseList=new ArrayList<>(nameList);
        System.gc();
    }
    //------------------------------------------------------
    public String get(int i){
        if(i<listSize-1)
            return null;
        else
            return nameList.get(i);
    }

    //------------------------------------------------------
    public int getSize(){
        return nameList.size();
    }

    //------------------------------------------------------
    public void delete(String name){
        if(nameList.isEmpty())
            return;
        nameList.remove(name);
        chooseList=new ArrayList<>(nameList);
        listSize=nameList.size();
        System.gc();

        
    }

    //------------------------------------------------------
    public boolean isEmpty(boolean taoluMode){
        if(taoluMode){
            if(nameList.isEmpty()&&chooseList.isEmpty())
                return true;
            else
                return false;
        }
        return nameList.isEmpty();
    }

    Random random =new Random();
    //------------------------------------------------------
    public String randomGet(boolean taoluMode){
        if(taoluMode){
            int i=random.nextInt(nameList.size());
            return  chooseList.get(i);
        }else{
            //int i=(int)(1+Math.random()*(nameList.size()));
            int i=random.nextInt(nameList.size());
            return  nameList.get(i);
        }
    }

    

    //------------------------------------------------------
    public String[] getAll(){
        return nameList.toArray(new String[0]);
    }

    //------------------------------------------------------
    public void addTaoluedName(String taoluedName,int taoluLevel){
        for(int i=0;i<taoluLevel;i++)
            chooseList.add(taoluedName);
    }

    //------------------------------------------------------
    public int saveToFile(){

        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(dataFile));
            oos.writeObject(nameList);
            oos.close();
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }

    //------------------------------------------------------
    public void deleteAll(){
        nameList.clear();
        chooseList.clear();
    }

    //------------------------------------------------------
    public void clearTaoluedName(){
        chooseList=new ArrayList<String>();
    }

}



