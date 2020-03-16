package me.hety.dogename.main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class History {

    Logger log =Logger.getLogger("HistoryLogger");


    private String HISTORY_FILE;
    public static final String separator=File.separator;

    ArrayList<String> history;

    public void loadHistory(){

        HISTORY_FILE="files"+separator+"history.data";

        try {
            File historyFile = new File(HISTORY_FILE);
            if (!historyFile.exists()) {
                historyFile.createNewFile();
                history = new ArrayList<>();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile));
            history = (ArrayList<String>) ois.readObject();

        } catch (EOFException e){
            history =new ArrayList<>();
            log.warning("History file is empty.");
        }catch (Exception e) {
            history = new ArrayList<>();
            log.warning("Failed to load history file:"+e.toString());
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
        history.add((history.size() + 1) +". "+name);

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
            e.printStackTrace();
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
