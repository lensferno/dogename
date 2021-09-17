package me.lensferno.dogename.voice;

import com.goxr3plus.streamplayer.stream.StreamPlayer;
import com.goxr3plus.streamplayer.stream.StreamPlayerException;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.utils.IOUtil;
import me.lensferno.dogename.utils.NetworkUtil;

import java.io.File;
import java.net.URLEncoder;

public class VoicePlayer {

    public static final String separator = File.separator;

    private final String VOICE_API = "https://tsn.baidu.com/text2audio";
    String cachePath = "caches" + separator + "voice" + separator;
    Token token;
    StreamPlayer streamPlayer = new StreamPlayer();
    private VoiceConfig voiceConfig = null;

    public VoicePlayer(Token token, VoiceConfig voiceConfig) {
        this.token = token;
        this.voiceConfig = voiceConfig;
    }

    public void playVoice(String name) {

        String speaker = voiceConfig.getSpeaker();
        String intonation = String.valueOf(voiceConfig.getIntonation());
        String speed = String.valueOf(voiceConfig.getSpeed());

        String cacheName = String.format("%s_%s_%s_%s", name, speaker, speed, intonation);

        File cache = new File(cachePath + cacheName + ".mp3");

        new Thread(() -> {
            if (!cache.exists()) {
                System.out.printf("Voice of %s not exists,fetch from network.%n", cache);
                boolean getVoiceSuccess = this.getVoiceData(name, speaker, speed, intonation, cache);
                if (getVoiceSuccess) {
                    this.playSound(cache);
                }
            } else {
                this.playSound(cache);
            }
        }).start();
    }

    private boolean getVoiceData(String name, String speaker, String speed, String intonation, File cache) {
        boolean success;
        try {
            String requestUrl = String.format(
                    "%s?ctp=1&lan=zh&tok=%s&cuid=%s&spd=%s&pit=%s&per=%s&aue=3&tex=%s",
                    VOICE_API,
                    token.getAccessToken(),
                    NetworkUtil.getMacMD5(),
                    speed,
                    intonation,
                    speaker,
                    URLEncoder.encode(name, "UTF-8")
            );

            byte[] receivedData = NetworkUtil.download(requestUrl);
            success = cacheVoiceFile(cache, receivedData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return success;
    }

    private boolean cacheVoiceFile(File cache, byte[] data) {
        boolean success = false;
        try {
            File cacheDir = new File(cachePath);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            if (!cache.exists())
                cache.createNewFile();

            IOUtil.writeFile(data, cache);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    private void playSound(File file) {
        try {
            streamPlayer.open(file);
            streamPlayer.play();
            //streamPlayer.setGain(voiceConfig.getVolume() * 0.1);
        } catch (StreamPlayerException e) {
            e.printStackTrace();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
