package main;

import com.google.gson.Gson;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Voice {

    App app =new App();
    String cachedVoicePath=app.APP_LOCA+"caches\\voice\\";
    final String API_KEY="dIHCtamVdD0ERO1yyFir2iI4";
    final String SEC_KEY="HmpBQY3gG4PyZ0cmudnCbMeoMcMejuuW";
    final String TOKEN_URL="https://openapi.baidu.com/oauth/2.0/token";
    String tokenFilePath=app.APP_LOCA+"caches\\voice\\";
    File tokenFile;

    Token token=null;


    boolean net=true;

    public Voice(){
        if(System.getProperty("os.name").toLowerCase().contains("indow"))
            cachedVoicePath=app.APP_LOCA+"caches\\voice\\";
        else
            cachedVoicePath=app.APP_LOCA+"caches//voice//";

        if(System.getProperty("os.name").toLowerCase().contains("indow"))
            tokenFilePath=app.APP_LOCA+"caches\\voice\\";
        else
            tokenFilePath=app.APP_LOCA+"caches//voice//";

        checkNet();

    }

    void getToken(){
        try{
            token=new Gson().fromJson(Common.getHtml(TOKEN_URL+"?grant_type=client_credentials&client_id="+API_KEY+"&client_secret="+SEC_KEY,true),Token.class);
            token.setExpTime();
        }catch (Exception e){
            token=null;
        }
    }

    void checkNet(){
        try {
            URL sourcesURL = new URL("htttp://www.baidu.com");
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
            connection.connect();

            InputStream stream = connection.getInputStream();
            stream.read();
            net=true;
        }catch (Exception e){
            e.printStackTrace();
            net=false;
        }
    }

    boolean loadToken(){
        ObjectInputStream ois;
        try{
            ois =new ObjectInputStream(new FileInputStream(tokenFile));
            this.token=(Token) ois.readObject();
            ois.close();
            if(token.isTokenTimeOut()){
                getToken();
                if(token==null||token.getAccess_token()==null)
                    return false;
                else {
                    //saveToken();
                    return true;
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            if(tokenFile.exists())
                tokenFile.delete();
            return false;

        }
    }


    void playVoice(String name){


        File cachedVoice=new File(cachedVoicePath+name+".wav");

        if(!cachedVoice.exists()){
            new Thread(new Runnable() {
               @Override
               public void run() {
                   try{

                      String URL="http://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=abcdxxx&tok=24.ed81f7799b5f83949042882e7e9fcbd7.2592000.1573717537.282335-17531281&tex="+ URLEncoder.encode(name,"utf-8")
                               +"&vol=9&per=111&spd=5&pit=5&aue=6";
                     //System.out.println(URL);
                       URL sourcesURL = new URL(URL);
                        HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
                        connection.connect();

                        InputStream stream = connection.getInputStream();

                        playSound(stream);

                       String[] temp=URL.split("/");
                       String fileName=temp[temp.length-1];
                       FileOutputStream fileStream = new FileOutputStream(new File(cachedVoicePath+name+".wav"));

                       for (int i = stream.read(); i != -1; i = stream.read())
                           fileStream.write(i);

                       fileStream.close();

                       System.out.println("[INFO]Download done save to："+cachedVoicePath+name+".wav");

                    }catch(Exception e){
                    e.printStackTrace();
                    }

                }
            }).start();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        playSound(new FileInputStream(cachedVoice));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }


    void playSound(InputStream inptustream){
        AudioInputStream audioInputStream = null;
        try {
            BufferedInputStream bf =new BufferedInputStream(inptustream);
            audioInputStream = AudioSystem.getAudioInputStream(bf);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[512];
        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }

    }

/*
返回
需要根据 Content-Type的头部来确定是否服务端合成成功。

如果合成成功，返回的Content-Type以“audio”开头

aue =3 ，返回为二进制mp3文件，具体header信息 Content-Type: audio/mp3；
aue =4 ，返回为二进制pcm文件，具体header信息 Content-Type:audio/basic;codec=pcm;rate=16000;channel=1
aue =5 ，返回为二进制pcm文件，具体header信息 Content-Type:audio/basic;codec=pcm;rate=8000;channel=1
aue =6 ，返回为二进制wav文件，具体header信息 Content-Type: audio/wav；
如果合成出现错误，则会返回json文本，具体header信息为：Content-Type: application/json。其中sn字段主要用于DEBUG追查问题，如果出现问题，可以提供sn帮助确认问题。
 */


    class Token {

        private long expTime;

        private String access_token;
        private long expires_in;
        private String refresh_token;
        private String scope;
        private String session_key;
        private String session_secret;

        public void setExpTime() {
            this.expTime = System.currentTimeMillis()+(expires_in-3600)*1000;
        }

        public boolean isTokenTimeOut(){
            return System.currentTimeMillis()>expTime;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
        public String getAccess_token() {
            return access_token;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }
        public long getExpires_in() {
            return expires_in;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }
        public String getRefresh_token() {
            return refresh_token;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
        public String getScope() {
            return scope;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }
        public String getSession_key() {
            return session_key;
        }

        public void setSession_secret(String session_secret) {
            this.session_secret = session_secret;
        }
        public String getSession_secret() {
            return session_secret;
        }

    }
}
