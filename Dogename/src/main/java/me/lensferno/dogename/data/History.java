package me.lensferno.dogename.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class History {

    Logger log = LogManager.getLogger();


    private String HISTORY_FILE;
    public static final String separator=File.separator;

    ArrayList<String> history;

    public void loadHistory(){

        HISTORY_FILE="files"+separator+"history.data";

        try {
            File historyFile = new File(HISTORY_FILE);
            if (!historyFile.exists()) {
                historyFile.getParentFile().mkdirs();
                historyFile.createNewFile();

                history = new ArrayList<>();
                writeHistory();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile));
            history = (ArrayList<String>) ois.readObject();

        } catch (EOFException e){
            history =new ArrayList<>();
            log.warn("History file is empty.");
            writeHistory();
        }catch (Exception e) {
            history = new ArrayList<>();
            log.error("Failed to load history file:"+e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<String> getHistoryList(){
        return history;
    }

    public void addHistory(String name){
        if(history.size()>2000) {
            history.clear();
        }
        history.add(String.format("%d. %s", (history.size() + 1), name));
        writeHistory();
    }

    private void writeHistory(){

        HISTORY_FILE="files"+separator+"history.data";
        File historyFile=new File(HISTORY_FILE);

        try{

            if (!historyFile.exists()) {
                historyFile.createNewFile();
            }

            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(historyFile));
            oos.writeObject(history);
            oos.close();
        }catch (Exception e){
            log.error("Error in writing history file:"+e);
        }
    }

    public int downSearch(){
        return 1;
    }

    public int upSearch(){
        return 1;
    }

    public void clearHistory(){
        this.history.clear();
    }

}
