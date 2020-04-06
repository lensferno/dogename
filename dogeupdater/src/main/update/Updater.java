package main.update;

import com.google.gson.Gson;
import main.update.pojos.UpdateInfo;
import main.utils.Net;

public class Updater {

    public static final String UPDATE_INFO_URL_GITEE="https://gitee.com/hety2002/dogename/raw/master/update/update_info.json";

    public static final String UPDATE_INFO_URL_GITHUB="https://github.com/eatenid/dogename/raw/master/update/update_info.json";

    public UpdateInfo updateInfo;

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

}
