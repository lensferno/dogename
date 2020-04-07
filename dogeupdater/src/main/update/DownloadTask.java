package main.update;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import main.utils.Md5;
import main.utils.Net;

import java.util.ArrayList;
import java.util.List;

public class DownloadTask {
    List<Package> packages=new ArrayList<>();

    private int taskNumber;

    String saveLocation;

    public DownloadTask(String saveLocation){
        this.saveLocation=saveLocation;
    }

    public void addPackage(String field){
        String[] url_md5=field.split("::");
        packages.add(new Package(url_md5[0],url_md5[1],saveLocation));
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public boolean startDownload(SimpleStringProperty message, int totalPackage, SimpleDoubleProperty progress){
        for(int i=0;i<packages.size();i++){
            message.set(message.get()+String.format("正在下载第 %d 个包，共 %d 个包...\n",i,totalPackage));
            progress.set(i/totalPackage);
            int downloadStat=packages.get(i).startDownload();
            if(downloadStat==-1){
                return false;
            }
        }

        return true;
    }

    public boolean checkPackages(int whichPackage){
        return packages.get(whichPackage).checkMD5();
    }

}

class Package {

    private boolean singleTaskFailed = false;
    private boolean singleTaskSucceeded = false;

    private String md5;
    private String downloadURL;

    private String saveLocation;

    public boolean isSingleTaskFailed() {
        return singleTaskFailed;
    }

    public boolean isSingleTaskSucceeded() {
        return singleTaskSucceeded;
    }

    public Package(String downloadURL, String md5, String saveLocation) {
        this.downloadURL = downloadURL;
        this.md5 = md5;
        this.saveLocation = saveLocation;
    }

    public int startDownload(){
        return Net.download(downloadURL, saveLocation);
    }

    public boolean checkMD5(){
        return Md5.checkMd5(saveLocation,md5);
    }

}