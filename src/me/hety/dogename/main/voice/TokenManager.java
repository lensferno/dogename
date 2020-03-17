package me.hety.dogename.main.voice;

import com.google.gson.Gson;
import me.hety.dogename.main.Common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TokenManager {

    public static final String separator=File.separator;

    String cachedVoicePath="caches\\voice\\";

    final String API_KEY="dIHCtamVdD0ERO1yyFir2iI4";
    final String SEC_KEY="HmpBQY3gG4PyZ0cmudnCbMeoMcMejuuW";

    final String TOKEN_API_URL ="https://openapi.baidu.com/oauth/2.0/token";

    File tokenFile=new File("API_voice.token");

    private Token token=null;

    String tokenStatus="ok";

    //boolean netAvailable =true;

    public TokenManager(){


    }

    public void init(){

        if(tokenFile.exists()){
            loadToken();

            if (checkTokenAvailable()){
                tokenStatus="ok";
            }else {
                tokenStatus="not ok";
            }
        }else{
            if(netAvailable()){

                fetchToken();
                writeToken();

                if (checkTokenAvailable()){
                    tokenStatus="ok";
                }else {
                    tokenStatus="not ok";
                }
            }else {
                tokenStatus="not ok";
            }

        }
    }

    public String getTokenStatus() {
        return tokenStatus;
    }

    public Token getToken() {
        return token;
    }

    private boolean checkTokenAvailable() {

        //token是空的就返回false
        if (token == null || token.getAccessToken() == null) {
            return false;
        }

        //token过期了就返回false
        if (token.isTokenTimeOut()) {
            return false;
        }

        //正常的话就返回ture
        return true;
    }


    void fetchToken(){
        try{
            //token=new Gson().fromJson("{\"access_token\":\"24.86c56b964c3e14596dcc80e21d74fd24.2592000.1574998125.282335-17531281\",\"session_key\":\"9mzdA5wjKQQPSG1GToYpIVb1dGzEgMrv9UDeBWuXOJi9se\\/pfjkph2Lmpl9PQd0S6SBK4RHU7zjt5DaAluz\\/zQjPcKbmxQ==\",\"scope\":\"audio_voice_assistant_get brain_enhanced_asr audio_tts_post public brain_all_scope picchain_test_picchain_api_scope wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test\\u6743\\u9650 vis-classify_flower lpq_\\u5f00\\u653e cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey smartapp_swanid_verify smartapp_opensource_openapi smartapp_opensource_recapi fake_face_detect_\\u5f00\\u653eScope vis-ocr_\\u865a\\u62df\\u4eba\\u7269\\u52a9\\u7406 idl-video_\\u865a\\u62df\\u4eba\\u7269\\u52a9\\u7406\",\"refresh_token\":\"25.a851090e2bd267f70f8186f23ab1bbb7.315360000.1887766125.282335-17531281\",\"session_secret\":\"6f439d43b796b04298ca1f9dfd32f1d4\",\"expires_in\":2592000}\n",Token.class);
            token=new Gson().fromJson(Common.getHtml(TOKEN_API_URL +"?grant_type=client_credentials&client_id="+API_KEY+"&client_secret="+SEC_KEY,true), Token.class);
            token.setExpTime();
        }catch (Exception e){
            token=null;
        }
    }

    private boolean netAvailable(){
        try {
            URL sourcesURL = new URL("http://www.baidu.com");
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
            connection.connect();

            InputStream stream = connection.getInputStream();
            stream.read();
            stream.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void loadToken(){
        ObjectInputStream ois;
        try{
            ois =new ObjectInputStream(new FileInputStream(tokenFile));
            this.token =(Token) ois.readObject();
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
            this.token=null;
        }
    }

    private void writeToken(){

        try{
            ObjectOutputStream oos =new ObjectOutputStream(new FileOutputStream(tokenFile));
            oos.writeObject(token);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
