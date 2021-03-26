package me.lensferno.dogename.data;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class NameData {

    Logger log = LogManager.getLogger("NameDataLogger");

    private List<String> nameList;

    private List<String> chooseList;
    private int listSize = 0;


    HashSet<String> ignoreNameList=new HashSet<>();

    HashSet<String> ignoreNumberList=new HashSet<>();

    File dataFile ;//=new File("namelist.data");

    boolean newAlgo=true;
    SecureRandom secRandom =new SecureRandom();

    //不做注释了，自己慢慢看。：）

    File nameIgnoreFile =new File("files"+File.separator+"IgnoredNameList.data");
    File numbIgnoreFile =new File("files"+File.separator+"IgnoredNumberList.data");

    public void writeIgnoreList(String switchy){

        if(!switchy.equals("not name")) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nameIgnoreFile));
                oos.writeObject(ignoreNameList);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!switchy.equals("not number")) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(numbIgnoreFile));
                oos.writeObject(ignoreNumberList);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void readIgnoreList(){

        try{

            if(!nameIgnoreFile.exists()){
                nameIgnoreFile.getParentFile().mkdirs();
                nameIgnoreFile.createNewFile();
                ignoreNameList= new HashSet<>();
                writeIgnoreList("not number");
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(nameIgnoreFile));
            this.ignoreNameList=(HashSet)ois.readObject();

        }catch (EOFException e){
            ignoreNameList=new HashSet<>();
            log.warn("Past name list is empty.");
            writeIgnoreList("not number");
        }catch (Exception e){
            ignoreNameList=new HashSet<>();
            writeIgnoreList("not number");
            log.warn("Failed to load past name list:"+e.toString());
            e.printStackTrace();
        }

        try{

            if(!numbIgnoreFile.exists()){
                numbIgnoreFile.getParentFile().mkdirs();
                numbIgnoreFile.createNewFile();
                ignoreNumberList= new HashSet<>();
                writeIgnoreList("not name");
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(numbIgnoreFile));
            this.ignoreNumberList=(HashSet)ois.readObject();

        }catch (EOFException e){
            ignoreNumberList=new HashSet<>();
            log.warn("Ignored number list is empty.");
            writeIgnoreList("not name");
        }catch (Exception e){
            ignoreNumberList=new HashSet<>();
            log.warn("Failed to load ignored number list");
            writeIgnoreList("not name");
            e.printStackTrace();
        }

        log.info("There are "+ignoreNameList.size()+" names and "+ignoreNumberList.size()+" numbers ignored.");
    }


    public void clearNameIgnoreList(){
        ignoreNameList.clear();
        writeIgnoreList("not number");
    }

    public void clearNumberIgnoreList(){
        ignoreNumberList.clear();
        writeIgnoreList("not name");
    }


    public HashSet<String> getIgnoreNameList() {
        return ignoreNameList;
    }

    public void setIgnoreNameList(HashSet<String> ignoreNameList) {
        this.ignoreNameList = ignoreNameList;
    }

    public HashSet<String> getIgnoreNumberList() {
        return ignoreNumberList;
    }

    public void setIgnoreNumberList(HashSet<String> ignoreNumberList) {
        this.ignoreNumberList = ignoreNumberList;
    }


    public List<String> getNameList() {
        return nameList;
    }

    public void exportNameList(File path) {
        if(path!=null) {
            try{
                FileOutputStream oos =new FileOutputStream(path);
                oos.write(new Gson().toJson(nameList).getBytes(StandardCharsets.UTF_8));
                oos.close();
                log.info("Exported list to:"+path.getPath());
            }catch (Exception e){log.warn("error in export namelist: "+e.toString());e.printStackTrace();}

        }
    }

    public void importNameList(File path) {
        if(path!=null) {

            try{
                FileInputStream fis =new FileInputStream(path);
                String temp;
                BufferedReader bis=new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                StringBuilder sb=new StringBuilder();

                while ((temp = bis.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }

                nameList=new Gson().fromJson(sb.toString(),List.class);
                log.info("Imported list from:"+path.getPath());
            }catch (Exception e){log.warn("error in import namelist:"+e.toString());e.printStackTrace();}

        }

    }

    public void makeMass() {

        HashSet<Integer> alreadyList = new HashSet<>();
        List<String> tempList = new LinkedList<>();
        int i;
        Random random = new Random();
        while (tempList.size() < nameList.size()) {
            i = random.nextInt(nameList.size());
            while (alreadyList.contains(i))
                i = random.nextInt(nameList.size());
            tempList.add(nameList.get(i));
            alreadyList.add(i);
        }
        nameList.clear();
        nameList.addAll(tempList);

    }

    //------------------------------------------------------
    public void setNewAlgo(boolean newAlgo) {
        this.newAlgo=newAlgo;
        if(newAlgo)
            System.out.println("[INFO]Use SecureRandom");
        else
            System.out.println("[INFO]Not use SecureRandom");
    }
    //------------------------------------------------------

    public NameData(){

        if(System.getProperty("os.name").toLowerCase().contains("window"))
            dataFile=new File("files\\Namelist.data");
        else
            dataFile=new File("files/Namelist.data");

        File oldDataFile=new File("D:\\dogename\\files\\data");

        try{

            if(oldDataFile.exists()) {
                ObjectInputStream ois =new ObjectInputStream(new FileInputStream(oldDataFile));
                this.nameList=(ArrayList)ois.readObject();

                listSize=nameList.size();
                this.chooseList=new ArrayList<>(nameList);
                ois.close();
                oldDataFile.delete();
                saveToFile();
                return;
            }

            if(!dataFile.exists()){
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                nameList= new ArrayList<>();
                saveToFile();
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(dataFile));
            this.nameList=(ArrayList)ois.readObject();

            log.info(nameList.size()+" names loaded.");

            listSize=nameList.size();
            this.chooseList=new ArrayList<>(nameList);

        }catch (EOFException EOFe){
            nameList=new ArrayList<>();
            chooseList=new ArrayList<>();
            log.warn("Data file is empty.");
            saveToFile();
        }catch (Exception e){
            nameList=new ArrayList<>();
            chooseList=new ArrayList<>();
            saveToFile();
            log.warn("Failed to load data file.");
            e.printStackTrace();
        }

    }

    //------------------------------------------------------
    public void add(String text){
        String[] splitedText;

        if(text.contains("\n")&&text.contains("\r")){//-----------windows
            splitedText=text.split("\r\n");
            nameList.addAll(Arrays.asList(splitedText));
        }else if(text.contains("\n")){//--------------------------linux,unix
            splitedText=text.split("\n");
            nameList.addAll(Arrays.asList(splitedText));
        }else if(text.contains("\r")){//--------------------------macos
            splitedText=text.split("\r");
            nameList.addAll(Arrays.asList(splitedText));
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
        ignoreNameList.remove(name);

        chooseList=new ArrayList<>(nameList);
        listSize=nameList.size();
        System.gc();


    }

    //------------------------------------------------------
    public boolean isEmpty(){
        return nameList.isEmpty();
    }

    Random random =new Random();

    //------------------------------------------------------
    public String randomGet(){
        if(newAlgo)
            return  nameList.get(secRandom.nextInt(nameList.size()));
        else
            return  nameList.get(random.nextInt(nameList.size()));
    }

    //------------------------------------------------------
    public String[] getAll(){
        return nameList.toArray(new String[0]);
    }

    //------------------------------------------------------
    public void saveToFile(){

        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(dataFile));
            oos.writeObject(nameList);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //------------------------------------------------------
    public void deleteAll(){
        nameList.clear();
        chooseList.clear();
    }

    //------------------------------------------------------
}
