package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Data {
    private List<String> nameList;

    private List<String> chooseList;
    private int listSize = 0;


    File dataFile =new File("D:\\DM_Master_sources-master\\data");

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
            System.out.println(listSize);

        }catch (Exception e){
            nameList=new ArrayList<>();
            e.printStackTrace();
        }



    }

    //------------------------------------------------------
    public void add(String text){
        String[] temp;

        if(text.contains("。")){
            temp=text.split("。");
            for(int i=0;i<temp.length;i++)
                nameList.add(temp[i]);
        }else {
            nameList.add(text);
            listSize=nameList.size();
        }
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
        listSize=nameList.size();

        for (String myString :nameList.toArray(new String[0])) {
            System.out.println(myString);
        }
    }

    //------------------------------------------------------
    public boolean isEmpty(){
        return  nameList.isEmpty();
    }

    //------------------------------------------------------
    public String randomGet(boolean taoluMode){
        if(taoluMode){
            int i=(int)(Math.random()*((chooseList.size()-1)));
            return  chooseList.get(i);
        }else{
            //int i=(int)(1+Math.random()*(nameList.size()));
            int i=(int)(Math.random()*((nameList.size()-1)));
            return  nameList.get(i);
        }
    }

    

    //------------------------------------------------------
    public String[] getAll(){
        return nameList.toArray(new String[0]);
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
    }

}



