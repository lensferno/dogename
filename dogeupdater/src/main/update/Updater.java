package main.update;

import com.google.gson.Gson;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import main.update.pojos.UpdateInfo;
import main.utils.Net;

public class Updater {

    public static final String UPDATE_INFO_URL_GITEE="https://gitee.com/hety2002/dogename/raw/master/update/update_info.json";

    public static final String UPDATE_INFO_URL_GITHUB="https://github.com/eatenid/dogename/raw/master/update/update_info.json";

    public UpdateInfo updateInfo;

    private boolean packageCurrent=false;

    public boolean checkUpdate(int resType,int ver){
        switch (resType){
            case ResourcesType.GITEE:
                return checkUpdateFromGitee(ver);
            case ResourcesType.GITHUB:
                return checkUpdateFromGithub(ver);
            default:
                return false;
        }
    }

    private boolean checkUpdateFromGitee(int ver){
        updateInfo=null;

        this.updateInfo=getUpdateInfo(UPDATE_INFO_URL_GITEE);
        if (updateInfo == null) {
            return false;
        }

        return updateInfo.getVer() > ver;
    }

    private boolean checkUpdateFromGithub(int ver){
        updateInfo=null;

        getUpdateInfo(UPDATE_INFO_URL_GITHUB);
        if (updateInfo == null) {
            return false;
        }

        return updateInfo.getVer() > ver;
    }

    private UpdateInfo getUpdateInfo(String Url){
        String updateInfoJson= Net.getHtml(Url,false);
        if (updateInfoJson == null) {
            return null;
        }

        return new Gson().fromJson(updateInfoJson,UpdateInfo.class);
    }

    public boolean doUpdate(String saveLocation, int downloadResources, boolean checkMd5, SimpleStringProperty message, SimpleDoubleProperty progress){
        DownloadTask downloadTask = new DownloadTask(saveLocation);
        int totalPackage=updateInfo.getResources().size();

        for(int i=0;i<updateInfo.getResources().size();i++){
            downloadTask.addPackage(updateInfo.getResources().get(downloadResources).getUrls().get(i));
        }

        boolean downloadSucceeded=downloadTask.startDownload(message,totalPackage,progress);

        if(!downloadSucceeded){
            return downloadSucceeded;
        }

        if(checkMd5){
            for(int i=0;i<totalPackage;i++){
                message.set(message.get()+String.format("检查第 %d 个包，共 %d 个包...\n",i,totalPackage));
                boolean packageCurrent=downloadTask.checkPackages(i);
                if(!packageCurrent){
                    message.set(message.get()+String.format("第 %d 个包校验有误。\n",i));
                    return packageCurrent;
                }
                message.set(message.get()+String.format("第 %d 个包校验无误。\n",i));
            }
        }

        return true;
    }

}
