package me.lensferno.dogename.data;

import com.google.gson.Gson;
import me.lensferno.dogename.utils.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class Data {

    public static final int IGNORELIST_NAME_ONLY = 0;
    public static final int IGNORELIST_NUMBER_ONLY = 1;
    public static final int IGNORELIST_ALL = 2;

    private final File dataFile = new File(FilePath.toSpecificPathForm("files/Namelist.data"));
    private final SecureRandom secRandom = new SecureRandom();
    private final Random random = new Random();

    private ArrayList<String> nameList;
    private final IgnoreList ignoreList = new IgnoreList();

    public Data() {
        try {
            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
                nameList = new ArrayList<>();
                saveToFile();
                return;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
            this.nameList = (ArrayList) ois.readObject();
            ois.close();

            System.out.println(nameList.size() + " names loaded.");
        } catch (EOFException EOFe) {
            nameList = new ArrayList<>();
            System.out.println("Data file is empty.");
            saveToFile();
        } catch (Exception e) {
            nameList = new ArrayList<>();
            saveToFile();
            System.out.println("Failed to load data file.");
            e.printStackTrace();
        }

        ignoreList.readIgnoreList();
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void exportNameList(File path) {
        if (path != null) {
            try {
                FileOutputStream oos = new FileOutputStream(path);
                oos.write(new Gson().toJson(nameList).getBytes(StandardCharsets.UTF_8));
                oos.close();
                System.out.println("Exported list to:" + path.getPath());
            } catch (Exception e) {
                System.out.println("error in export namelist: " + e);
                e.printStackTrace();
            }

            this.saveToFile();
        }
    }

    public void importNameList(File path) {
        if (path != null) {
            try {
                FileInputStream fis = new FileInputStream(path);
                BufferedReader bis = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

                String temp;
                StringBuilder sb = new StringBuilder();

                while ((temp = bis.readLine()) != null) {
                    sb.append(temp);
                    sb.append("\n");
                }
                fis.close();
                nameList = new Gson().fromJson(sb.toString(), ArrayList.class);
                System.out.println("Imported list from:" + path.getPath());
            } catch (Exception e) {
                System.out.println("error in import namelist:" + e);
                e.printStackTrace();
            }

            this.saveToFile();
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

        this.saveToFile();
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
        this.saveToFile();
    }

    public boolean compareNameIgnoreList() {
        return ignoreList.getNameIgnoreListSize() >= nameList.size();
    }

    public void delete(String name) {
        if (nameList.isEmpty())
            return;

        nameList.remove(name);
        ignoreList.removeName(name);

        this.saveToFile();
        System.gc();
    }

    public boolean isEmpty() {
        return nameList.isEmpty();
    }

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
        this.saveToFile();
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

    static class IgnoreList {

        private final File nameIgnoreFile = new File(FilePath.toSpecificPathForm("files/IgnoredNameList.data"));
        private final File numbIgnoreFile = new File(FilePath.toSpecificPathForm("files/IgnoredNumberList.data"));
        private HashSet<String> ignoreNameList = new HashSet<>();
        private HashSet<String> ignoreNumberList = new HashSet<>();

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
            System.out.printf("There are %d names and %d numbers ignored\n", ignoreNameList.size(), ignoreNumberList.size());
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
                ois.close();
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
                ois.close();
            } catch (EOFException e) {
                ignoreNumberList = new HashSet<>();
                System.out.println("Ignored number list is empty.");
                writeIgnoreList(IGNORELIST_NUMBER_ONLY);
            } catch (Exception e) {
                ignoreNumberList = new HashSet<>();
                System.out.println("Failed to load ignored number list");
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
