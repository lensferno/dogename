package me.lensferno.dogename.data;

import me.lensferno.dogename.utils.FilePath;

import java.io.*;
import java.util.ArrayList;

public class History {

    private ArrayList<String> history;

    private final String HISTORY_FILE = FilePath.toSpecificPathForm("files/history.data");

    public void loadHistory() {

        File historyFile = new File(HISTORY_FILE);

        try {
            if (!historyFile.exists()) {
                historyFile.getParentFile().mkdirs();
                historyFile.createNewFile();

                history = new ArrayList<>();
                writeHistory();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile));
            history = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (EOFException e) {
            history = new ArrayList<>();
            System.out.println("History file is empty.");
            writeHistory();
        } catch (Exception e) {
            history = new ArrayList<>();
            System.out.println("Failed to load history file:" + e);
            e.printStackTrace();
        }
    }

    public ArrayList<String> getHistoryList() {
        return history;
    }

    public void addHistory(String name) {
        if (history.size() > 2000) {
            history.clear();
        }
        history.add(String.format("%d. %s", (history.size() + 1), name));
        writeHistory();
    }

    private void writeHistory() {

        File historyFile = new File(HISTORY_FILE);

        try {
            if (!historyFile.exists()) {
                historyFile.createNewFile();
            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(historyFile));
            oos.writeObject(history);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error in writing history file:" + e);
        }
    }

    public void clearHistory() {
        this.history.clear();
        writeHistory();
    }

}