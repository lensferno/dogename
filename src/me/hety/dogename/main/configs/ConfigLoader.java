package me.hety.dogename.main.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.*;
import me.hety.dogename.main.configs.adapters.BooleanPropertyAdapter;
import me.hety.dogename.main.configs.adapters.DoublePropertyAdapter;
import me.hety.dogename.main.configs.adapters.IntegerPropertyAdapter;
import me.hety.dogename.main.configs.adapters.StringPropertyAdapter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ConfigLoader {

    Logger log =Logger.getLogger("configLoaderLogger");

    //ConfigValuesBean config;
    private MainConfig mainConfig;
    private VoiceConfig voiceConfig;

    public MainConfig getMainConfig() {
        return mainConfig;
    }


    public MainConfig readConfigFromFile(String fileLocation){

        //property属性应该要自定义一个json适配器才能解析出来
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class,new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class,new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class,new StringPropertyAdapter())
                .registerTypeAdapter(SimpleDoubleProperty.class,new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        String ConfigJSON;

        try{
            File configFile=new File(fileLocation);
            if(!configFile.exists()){
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                mainConfig=new MainConfig();
                writeMainConfigToFile("files"+ File.separator+"Config.json");
                return mainConfig;
            }
            InputStream inputStream=new FileInputStream(configFile);
            ConfigJSON=IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            mainConfig=gson.fromJson(ConfigJSON,MainConfig.class);
            if (mainConfig == null) {
                mainConfig=new MainConfig();
                writeMainConfigToFile("files"+ File.separator+"Config.json");
                return mainConfig;
            }
        }catch (Exception e){
            log.warning("Error to load config file:"+e.toString()+"\nUse Default config.");
            mainConfig=new MainConfig();
            writeMainConfigToFile("files"+ File.separator+"Config.json");
            e.printStackTrace();
            return mainConfig;
        }

        return this.mainConfig;
    }

    public VoiceConfig readVoiceConfigFromFile(String fileLocation){

        //property属性应该要自定义一个json适配器才能解析出来
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class,new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class,new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class,new StringPropertyAdapter())
                .registerTypeAdapter(SimpleDoubleProperty.class,new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        String ConfigJSON;

        try{
            File configFile=new File(fileLocation);
            if(!configFile.exists()){
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                voiceConfig=new VoiceConfig();
                writeVoiceConfigToFile("files"+ File.separator+"VoiceConfig.json");
                return voiceConfig;
            }
            InputStream inputStream=new FileInputStream(configFile);
            ConfigJSON=IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            writeVoiceConfigToFile("files"+ File.separator+"VoiceConfig.json");
            voiceConfig=gson.fromJson(ConfigJSON,VoiceConfig.class);
            if (voiceConfig == null) {
                voiceConfig=new VoiceConfig();
                writeVoiceConfigToFile("files"+ File.separator+"VoiceConfig.json");
                return voiceConfig;
            }

        }catch (Exception e){
            log.warning("Error to load voice config file:"+e.toString()+"\nUse Default voice config.");
            voiceConfig=new VoiceConfig();
            writeVoiceConfigToFile("files"+ File.separator+"VoiceConfig.json");
            e.printStackTrace();
            return voiceConfig;
        }

        return this.voiceConfig;
    }

    //
    public MainConfig setValuesToProperty(){
        //mainconfig.set..(config.get..)
        //...so on
        //
        return this.mainConfig;
    }

    private String toJSON(MainConfig config){

        Gson gson=new GsonBuilder()
            .registerTypeAdapter(SimpleBooleanProperty.class,new BooleanPropertyAdapter())
            .registerTypeAdapter(SimpleIntegerProperty.class,new IntegerPropertyAdapter())
            .registerTypeAdapter(SimpleStringProperty.class,new StringPropertyAdapter())
            .setPrettyPrinting()
            .create();

        return gson.toJson(config);
    }

    private String VoiceConfigtoJSON(VoiceConfig config){

        Gson gson=new GsonBuilder()
                .registerTypeAdapter(SimpleDoubleProperty.class,new DoublePropertyAdapter())
                .setPrettyPrinting()
                .create();

        return gson.toJson(config);
    }

    public void writeAllConfigToFile(String outputLocation, String voiceConfigFile){
        File outputFile = new File(outputLocation);

        try{
            if(! outputFile.exists()){
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            OutputStream stream=new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.mainConfig).getBytes(StandardCharsets.UTF_8),stream);

            OutputStream voiceConfigFileStream=new FileOutputStream(voiceConfigFile);
            IOUtils.write(VoiceConfigtoJSON(this.voiceConfig).getBytes(StandardCharsets.UTF_8),voiceConfigFileStream);

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void writeMainConfigToFile(String outputLocation){
        File outputFile = new File(outputLocation);

        try{
            if(! outputFile.exists()){
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }

            OutputStream stream=new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.mainConfig).getBytes(StandardCharsets.UTF_8),stream);

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void writeVoiceConfigToFile(String voiceConfigFile){
        File outputFile = new File(voiceConfigFile);

        try{

            if(! outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }
            OutputStream voiceConfigFileStream=new FileOutputStream(voiceConfigFile);
            IOUtils.write(VoiceConfigtoJSON(this.voiceConfig).getBytes(StandardCharsets.UTF_8),voiceConfigFileStream);

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
