package me.hety.dogename.main.data;

import com.google.gson.Gson;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Logger;

public class NameData {

    Logger log = Logger.getLogger("NameDataLogger");

    private List<String> nameList;

    private List<String> chooseList;
    private int listSize = 0;


    HashSet<String> ignoreNameList=new HashSet<>();

    HashSet<String> ignoreNumberList=new HashSet<>();

    File dataFile ;//=new File("namelist.data");

    boolean newAlgo=true;
    SecureRandom secRandom =new SecureRandom();

    //不做注释了，自己慢慢看。：）

    File nameIgnoreFile =new File("files\\IgnoredNameList.data");
    File numbIgnoreFile =new File("files\\IgnoredNumberList.data");

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
                oos.write(new Gson().toJson(nameList).getBytes("utf-8"));
                oos.close();
                System.out.println("[INFO]Exported list to:"+path.getPath());
            }catch (Exception e){e.printStackTrace();}

        }else
            return;
    }

    public void importNameList(File path) {
        if(path!=null) {

            try{
                FileInputStream fis =new FileInputStream(path);
                String temp;
                BufferedReader bis=new BufferedReader(new InputStreamReader(fis, "utf-8"));
                StringBuilder sb=new StringBuilder();

                while ((temp = bis.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }

                nameList=new Gson().fromJson(sb.toString(),List.class);
                System.out.println("[INFO]Imported list from:"+path.getPath());
            }catch (Exception e){e.printStackTrace();}

        }else
            return;

    }

    public void makeMass() {

        HashSet<Integer> alreadyList = new HashSet<>();
        List<String> tempList = new LinkedList<>();
        int i = 0;
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
            dataFile.createNewFile();

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

            if(dataFile.exists()!=true){
                nameList= new ArrayList<>();
                return;
            }

            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(dataFile));
            this.nameList=(ArrayList)ois.readObject();

            listSize=nameList.size();
            this.chooseList=new ArrayList<>(nameList);

        }catch (EOFException EOFe){
            nameList=new ArrayList<>();
            chooseList=new ArrayList<>();
            log.warning("Data file is empty.");
        }catch (Exception e){
            nameList=new ArrayList<>();
            chooseList=new ArrayList<>();
            log.warning("Failed to load data file.");
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
    public void clearTaoluedName() {
        chooseList = new ArrayList<>();
    }
}
