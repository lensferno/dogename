package me.hety.dogename.main.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import me.hety.dogename.main.configs.adapters.BooleanPropertyAdapter;
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

    public MainConfig getMainConfig() {
        return mainConfig;
    }


    public MainConfig readConfigFromFile(String fileLocation){

        //property属性应该要自定义一个json适配器才能解析出来
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(SimpleBooleanProperty.class,new BooleanPropertyAdapter())
                .registerTypeAdapter(SimpleIntegerProperty.class,new IntegerPropertyAdapter())
                .registerTypeAdapter(SimpleStringProperty.class,new StringPropertyAdapter())
                .setPrettyPrinting()
                .create();

        String ConfigJSON;

        try{
            InputStream inputStream=new FileInputStream(fileLocation);
            ConfigJSON=IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            mainConfig=gson.fromJson(ConfigJSON,MainConfig.class);


        }catch (Exception e){
            log.warning("Error to load config file:"+e.toString()+"\nUse Default config.");
            mainConfig=new MainConfig();
            e.printStackTrace();
        }

        return this.mainConfig;
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

    public void writeConfigToFile(String outputLocation){
        File outputFile = new File(outputLocation);

        try{
            if(! outputFile.exists())
                outputFile.createNewFile();

            OutputStream stream=new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.mainConfig).getBytes(StandardCharsets.UTF_8),stream);

        }catch (IOException ioe){

        }
    }

}
