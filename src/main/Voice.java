package main;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Voice {

    void playVoice(String name){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    String URL="http://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=abcdxxx&tok=24.ed81f7799b5f83949042882e7e9fcbd7.2592000.1573717537.282335-17531281&tex="+ URLEncoder.encode(name,"utf-8")
                            +"&vol=9&per=0&spd=5&pit=5&aue=6";
                    //System.out.println(URL);
                    URL sourcesURL = new URL(URL);
                    HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    playSound(new BufferedInputStream(stream));

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }

    void playSound(InputStream stream){
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(stream);
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
        //这是缓冲
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




    class api {

        private String access_token;
        private long expires_in;
        private String refresh_token;
        private String scope;
        private String session_key;
        private String session_secret;
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
