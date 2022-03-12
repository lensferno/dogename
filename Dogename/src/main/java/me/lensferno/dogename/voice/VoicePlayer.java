package me.lensferno.dogename.voice;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import me.lensferno.dogename.configs.VoiceConfig;
import me.lensferno.dogename.utils.FilePath;
import me.lensferno.dogename.utils.IOUtil;
import me.lensferno.dogename.utils.NetworkUtil;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URLEncoder;

public class VoicePlayer {

    private static final String VOICE_API = "https://tsn.baidu.com/text2audio";

    public static final String cachePath = FilePath.toSpecificPathForm("caches/voice/");

    private final Token token;

    public static VoiceConfig voiceConfig;

    public VoicePlayer(Token token, VoiceConfig voiceConfig) {
        this.token = token;
        VoicePlayer.voiceConfig = voiceConfig;
    }

    public void playVoice(String name) {

        String speaker = voiceConfig.getSpeakerIdString();
        String intonation = String.valueOf(voiceConfig.getIntonation());
        String speed = String.valueOf(voiceConfig.getSpeed());

        String cacheName = String.format("%s_%s_%s_%s", name, speaker, speed, intonation);

        File cache = new File(String.format("%s%s.%s", cachePath, cacheName, VoiceConfig.getAudioFileSuffix(voiceConfig.getAudioFormat())));

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
                    "%s?ctp=1&lan=zh&tok=%s&cuid=%s&spd=%s&pit=%s&per=%s&aue=%s&tex=%s",
                    VOICE_API,
                    token.getAccessToken(),
                    NetworkUtil.getMacMD5(),
                    speed,
                    intonation,
                    speaker,
                    voiceConfig.getAudioFormat(),
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
            AudioInputStream sourceAudioInputStream;
            if (voiceConfig.getAudioFormat() == VoiceConfig.AUDIO_FORMAT_WAV) {
                sourceAudioInputStream = AudioSystem.getAudioInputStream(file);
            } else {
                sourceAudioInputStream = new MpegAudioFileReader().getAudioInputStream(file);
            }
            
            AudioFormat sourceFormat = sourceAudioInputStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(),
                    sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(targetFormat, sourceAudioInputStream);

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, targetFormat, AudioSystem.NOT_SPECIFIED);

            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            audioLine.open(targetFormat);
            audioLine.start();

            byte[] buffer = new byte[1024];
            int length = audioInputStream.read(buffer);
            while (length > 0) {
                audioLine.write(buffer, 0, length);
                length = audioInputStream.read(buffer);
            }

            audioLine.drain();
            audioLine.stop();
            audioLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
