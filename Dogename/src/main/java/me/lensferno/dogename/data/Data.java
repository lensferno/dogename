package me.lensferno.dogename.data;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class Data {

    Logger log = LogManager.getLogger("dataLogger");

    public static final int IGNORELIST_NAME_ONLY = 0;
    public static final int IGNORELIST_NUMBER_ONLY = 1;
    public static final int IGNORELIST_ALL = 2;


    private List<String> nameList;
    private IgnoreList ignoreList = new IgnoreList();

    File dataFile;

    SecureRandom secRandom = new SecureRandom();

    public List<String> getNameList() {
        return nameList;
    }

    public void exportNameList(File path) {
        if (path != null) {
            try {
                FileOutputStream oos = new FileOutputStream(path);
                oos.write(new Gson().toJson(nameList).getBytes(StandardCharsets.UTF_8));
                oos.close();
                log.info("Exported list to:" + path.getPath());
            } catch (Exception e) {
                log.warn("error in export namelist: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void importNameList(File path) {
        if (path != null) {

            try {
                FileInputStream fis = new FileInputStream(path);
                String temp;
                BufferedReader bis = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();

                while ((temp = bis.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }

                nameList = new Gson().fromJson(sb.toString(), List.class);
                log.info("Imported list from:" + path.getPath());
            } catch (Exception e) {
                log.warn("error in import namelist:" + e.toString());
                e.printStackTrace();
            }

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

    public Data() {


        if (System.getProperty("os.name").toLowerCase().contains("window"))
            dataFile = new File("files\\Namelist.data");
        else
            dataFile = new File("files/Namelist.data");

        File oldDataFile = new File("D:\\dogename\\files\\data");

        try {

            if (oldDataFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(oldDataFile));
                this.nameList = (ArrayList) ois.readObject();

                ois.close();
                oldDataFile.delete();
                saveToFile();
                return;
            }

            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                nameList = new ArrayList<>();
                saveToFile();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
            this.nameList = (ArrayList) ois.readObject();

            log.info(nameList.size() + " names loaded.");

        } catch (EOFException EOFe) {
            nameList = new ArrayList<>();
            log.warn("Data file is empty.");
            saveToFile();
        } catch (Exception e) {
            nameList = new ArrayList<>();
            saveToFile();
            log.warn("Failed to load data file.");
            e.printStackTrace();
        }

        ignoreList.readIgnoreList();
    }


    public void add(String text) {
        String[] splitedText;

        if (text.contains("\n") && text.contains("\r")) {
            // for windows
            splitedText = text.split("\r\n");
            nameList.addAll(Arrays.asList(splitedText));
        } else if (text.contains("\n")) {
            // for linux
            splitedText = text.split("\n");
            nameList.addAll(Arrays.asList(splitedText));
        } else if (text.contains("\r")) {
            // for macos
            splitedText = text.split("\r");
            nameList.addAll(Arrays.asList(splitedText));
        } else {
            nameList.add(text);
        }

        System.gc();
    }


    public boolean compareNameIgnoreList() {
        return ignoreList.getNameIgnoreListSize() >= nameList.size();
    }

    public void delete(String name) {
        if (nameList.isEmpty())
            return;

        nameList.remove(name);
        ignoreList.removeName(name);

        System.gc();
    }

    public boolean isEmpty() {
        return nameList.isEmpty();
    }

    Random random = new Random();

    public String randomGet(boolean secureRandom) {
        if (secureRandom)
            return nameList.get(secRandom.nextInt(nameList.size()));
        else
            return nameList.get(random.nextInt(nameList.size()));
    }

    public void saveToFile() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
            oos.writeObject(nameList);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteAllName() {
        nameList.clear();
        ignoreList.clearNameIgnoreList();
    }

    public boolean checkNameIgnored(String name) {
        return ignoreList.checkNameExists(name);
    }

    public boolean checkNumberIgnored(String number) {
        return ignoreList.checkNumberExists(number);
    }

    public void addNameToIgnoreList(String name) {
        ignoreList.addName(name);
    }

    public void addNumberToIgnoreList(String number) {
        ignoreList.addNumber(number);
    }

    public void writeIgnoreList(int switchy) {
        ignoreList.writeIgnoreList(switchy);
    }

    public void clearNameIgnoreList() {
        ignoreList.clearNameIgnoreList();
    }

    public void clearNumberIgnoreList() {
        ignoreList.clearNumberIgnoreList();
    }

    public int getNumberIgnoreListSize() {
        return ignoreList.getNumberIgnoreListSize();
    }

    class IgnoreList {

        private HashSet<String> ignoreNameList = new HashSet<>();

        private HashSet<String> ignoreNumberList = new HashSet<>();

        private final File nameIgnoreFile = new File("files" + File.separator + "IgnoredNameList.data");
        private final File numbIgnoreFile = new File("files" + File.separator + "IgnoredNumberList.data");

        public void writeIgnoreList(int switchy) {
            switch (switchy) {
                case IGNORELIST_NAME_ONLY:
                    writeNameIgnoreList();
                case IGNORELIST_NUMBER_ONLY:
                    writeNumberIgnoreList();
                default:
                    writeNumberIgnoreList();
                    writeNameIgnoreList();
            }
        }

        private void writeNameIgnoreList() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nameIgnoreFile));
                oos.writeObject(ignoreNameList);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writeNumberIgnoreList() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(numbIgnoreFile));
                oos.writeObject(ignoreNumberList);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void readIgnoreList() {
            readNameIgnoreList();
            readNumberIgnoreList();
            log.info("There are " + ignoreNameList.size() + " names and " + ignoreNumberList.size() + " numbers ignored.");
        }

        private void readNameIgnoreList() {
            try {
                if (!nameIgnoreFile.exists()) {
                    nameIgnoreFile.getParentFile().mkdirs();
                    nameIgnoreFile.createNewFile();
                    ignoreNameList = new HashSet<>();
                    writeIgnoreList(IGNORELIST_NAME_ONLY);
                    return;
                }

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nameIgnoreFile));
                this.ignoreNameList = (HashSet) ois.readObject();

            } catch (EOFException e) {
                ignoreNameList = new HashSet<>();
                writeIgnoreList(IGNORELIST_NAME_ONLY);
            } catch (Exception e) {
                ignoreNameList = new HashSet<>();
                writeIgnoreList(IGNORELIST_NAME_ONLY);
                e.printStackTrace();
            }

        }

        private void readNumberIgnoreList() {
            try {
                if (!numbIgnoreFile.exists()) {
                    numbIgnoreFile.getParentFile().mkdirs();
                    numbIgnoreFile.createNewFile();
                    ignoreNumberList = new HashSet<>();
                    writeIgnoreList(IGNORELIST_NUMBER_ONLY);
                    return;
                }
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(numbIgnoreFile));
                this.ignoreNumberList = (HashSet) ois.readObject();
            } catch (EOFException e) {
                ignoreNumberList = new HashSet<>();
                log.warn("Ignored number list is empty.");
                writeIgnoreList(IGNORELIST_NUMBER_ONLY);
            } catch (Exception e) {
                ignoreNumberList = new HashSet<>();
                log.warn("Failed to load ignored number list");
                writeIgnoreList(IGNORELIST_NUMBER_ONLY);
                e.printStackTrace();
            }
        }


        protected void clearNameIgnoreList() {
            ignoreNameList.clear();
            writeIgnoreList(IGNORELIST_NAME_ONLY);
        }

        protected void clearNumberIgnoreList() {
            ignoreNumberList.clear();
            writeIgnoreList(IGNORELIST_NUMBER_ONLY);
        }

        protected int getNameIgnoreListSize() {
            return ignoreNameList.size();
        }

        public int getNumberIgnoreListSize() {
            return ignoreNumberList.size();
        }

        protected void removeName(String name) {
            ignoreNameList.remove(name);
        }

        protected void addName(String name) {
            ignoreNameList.add(name);
        }

        protected void addNumber(String number) {
            ignoreNumberList.add(number);
        }

        protected boolean checkNameExists(String name) {
            return ignoreNameList.contains(name);
        }

        protected boolean checkNumberExists(String number) {
            return ignoreNumberList.contains(number);
        }

    }

}
