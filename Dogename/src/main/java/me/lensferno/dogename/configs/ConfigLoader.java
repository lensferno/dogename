package me.lensferno.dogename.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import me.lensferno.dogename.configs.adapters.BooleanPropertyAdapter;
import me.lensferno.dogename.configs.adapters.DoublePropertyAdapter;
import me.lensferno.dogename.configs.adapters.IntegerPropertyAdapter;
import me.lensferno.dogename.configs.adapters.StringPropertyAdapter;
import me.lensferno.dogename.utils.FilePath;
import me.lensferno.dogename.voice.VoicePlayer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigLoader {
    private final String mainConfigLocation = FilePath.toSpecificPathForm("files/Config.json");
    private final String voiceConfigLocation = FilePath.toSpecificPathForm("files/VoiceConfig.json");
    //ConfigValuesBean config;
    private MainConfig mainConfig;

    public String getMainConfigLocation() {
        return mainConfigLocation;
    }

    public String getVoiceConfigLocation() {
        return voiceConfigLocation;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MainConfig readConfigFromFile(String fileLocation) {
        //property属性应该要自定义一个json适配器才能解析出来
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class, new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class, new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class, new StringPropertyAdapter())
                .registerTypeAdapter(SimpleDoubleProperty.class, new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        String configJson;

        try {
            File configFile = new File(fileLocation);
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                mainConfig = new MainConfig();
                writeMainConfigToFile(mainConfigLocation);
                return mainConfig;
            }
            InputStream inputStream = new FileInputStream(configFile);
            configJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();

            mainConfig = gson.fromJson(configJson, MainConfig.class);

            if (mainConfig == null) {
                mainConfig = new MainConfig();
                writeMainConfigToFile(mainConfigLocation);
                return mainConfig;
            }

        } catch (Exception e) {
            System.out.println("Error to load config file:" + e + "\nUse Default config.");

            mainConfig = new MainConfig();
            writeMainConfigToFile(mainConfigLocation);
            return mainConfig;
        }

        return this.mainConfig;
    }

    public VoiceConfig readVoiceConfigFromFile(String fileLocation) {
        //property属性应该要自定义一个json适配器才能解析出来
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class, new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class, new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class, new StringPropertyAdapter())
                .registerTypeAdapter(SimpleDoubleProperty.class, new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        String configJson;

        try {
            File configFile = new File(fileLocation);
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                VoicePlayer.voiceConfig = new VoiceConfig();
                writeVoiceConfigToFile(voiceConfigLocation);
                return VoicePlayer.voiceConfig;
            }
            InputStream inputStream = new FileInputStream(configFile);
            configJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();

            VoicePlayer.voiceConfig = gson.fromJson(configJson, VoiceConfig.class);

            if (VoicePlayer.voiceConfig == null) {
                VoicePlayer.voiceConfig = new VoiceConfig();
                writeVoiceConfigToFile(voiceConfigLocation);
                return VoicePlayer.voiceConfig;
            }

        } catch (Exception e) {
            System.out.println("Error to load voice config file:" + e + "\nUse Default voice config.");

            VoicePlayer.voiceConfig = new VoiceConfig();
            writeVoiceConfigToFile(voiceConfigLocation);
            return VoicePlayer.voiceConfig;
        }

        return VoicePlayer.voiceConfig;
    }

    private String toJSON(MainConfig config) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class, new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class, new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class, new StringPropertyAdapter())
                .setPrettyPrinting()
                .create();

        return gson.toJson(config);
    }

    private String getConfigJson(VoiceConfig config) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SimpleDoubleProperty.class, new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        return gson.toJson(config);
    }

    public void writeAllConfigToFile(String outputLocation, String voiceConfigFile) {
        File outputFile = new File(outputLocation);

        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            OutputStream stream = new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.mainConfig).getBytes(StandardCharsets.UTF_8), stream);

            OutputStream voiceConfigFileStream = new FileOutputStream(voiceConfigFile);
            IOUtils.write(getConfigJson(VoicePlayer.voiceConfig).getBytes(StandardCharsets.UTF_8), voiceConfigFileStream);

        } catch (Exception e) {
            System.out.println("Error in writing all config:" + e);
        }
    }

    public void writeMainConfigToFile(String outputLocation) {
        File outputFile = new File(outputLocation);

        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }

            OutputStream stream = new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.mainConfig).getBytes(StandardCharsets.UTF_8), stream);

        } catch (Exception e) {
            System.out.println("Error in writing main config:" + e);
        }
    }

    public void writeVoiceConfigToFile(String voiceConfigFile) {
        File outputFile = new File(voiceConfigFile);

        try {

            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            OutputStream voiceConfigFileStream = new FileOutputStream(voiceConfigFile);
            IOUtils.write(getConfigJson(VoicePlayer.voiceConfig).getBytes(StandardCharsets.UTF_8), voiceConfigFileStream);

        } catch (Exception e) {
            System.out.println("Error in writing voice config:" + e);
        }
    }

}
