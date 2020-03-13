package me.hety.dogename.main.voice;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import org.apache.commons.io.IOUtils;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VoicePlayer {

    public static final String separator=File.separator;

    Logger log=Logger.getLogger("VoicePlayerLogger");

    private final String VOICE_API="http://tsn.baidu.com/text2audio?";

    Token token;

    public VoicePlayer(Token token){
        this.token=token;
    }

    String cachedVoicePath="caches"+separator+"voice"+separator;

    File cacheDir =new File(cachedVoicePath);

    OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .build();

    public void playVoice(String name,String speaker,String speed){

        String cachedVoiceName;
        cachedVoiceName=name+"_"+speaker+"_"+speed;

        File cachedVoice=new File(cachedVoicePath+name+".wav");

        if(!cachedVoice.exists()){

            new Thread(() -> {


                try{

                    FormBody formBody=new FormBody.Builder()
                            .add("tex",URLEncoder.encode(name,"utf-8"))
                            .add("tok",token.getAccessToken())
                            .add("cuid",getMACAddress())
                            .add("ctp","")
                            .add("lan","zh")
                            .add("spd",speed)
                            .add("per",speaker)
                            .add("aue","6")
                            .build();

                    String[] b ={"1","3","106","0"};
                    String URL="http://tsn.baidu.com/text2audio?lan=zh&ctp=1&cuid=abcdxxx&tok="+token.getAccessToken()+"&tex="+ URLEncoder.encode(name,"utf-8")
                            +"&vol=0&per="+b[new Random().nextInt(b.length)]+"&spd=5&pit=4&aue=6";
                    System.out.println(URL);
                    java.net.URL sourcesURL = new URL(URL);
                    HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    if(connection.getHeaderField("Content-type").contains("json")){
                        log.warning("Error to get voice file.");
                        return;
                    }

                    if(!cacheDir.exists())
                        cacheDir.mkdirs();

                    if(!cachedVoice.exists())
                        cachedVoice.createNewFile();

                    FileOutputStream fileStream = new FileOutputStream(new File(cachedVoicePath+name+".wav"));

                    fileStream.write(IOUtils.toByteArray(stream));

                    fileStream.close();

                    playSound(new FileInputStream(cachedVoice));

                    File cachedVoice1 =new File(cachedVoicePath+name+".wav");

                    new File(cachedVoicePath).mkdirs();

                    System.out.println("[INFO]Download done save to："+cachedVoicePath+name+".wav");

                }catch(Exception e){
                    e.printStackTrace();
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

    private void soundPlayer(String cachedVoicePath) {

        try {
            File file = new File(cachedVoicePath);
            //使用 mp3spi 解码 mp3 音频文件
            MpegAudioFileReader mp = new MpegAudioFileReader();
            AudioInputStream stream = mp.getAudioInputStream(file);
            AudioFormat baseFormat = stream.getFormat();
            //设定输出格式为pcm格式的音频文件
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            // 输出到音频
            stream = AudioSystem.getAudioInputStream(format, stream);
            AudioFormat target = stream.getFormat();
            DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, target, AudioSystem.NOT_SPECIFIED);
            SourceDataLine line = null;
            int len = -1;
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(target);
            line.start();
            byte[] buffer = new byte[1024];
            while ((len = stream.read(buffer)) > 0) {
                line.write(buffer, 0, len);
            }
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private static String getMACAddress() {

        try{

            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString().toUpperCase();

        }catch (Exception e){
            return String.valueOf(new Random().nextLong());
        }

    }

}
