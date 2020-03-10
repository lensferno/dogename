package me.hety.dogename.main.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import sun.applet.Main;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public class ConfigLoader {

    Logger log =Logger.getLogger("configLoaderLogger");

    ConfigValuesBean config;
    MainConfig mainConfig;

    public ConfigValuesBean readConfigFromFile(String fileLocation){

        String ConfigJSON;
        try{
            InputStream inputStream=new FileInputStream(fileLocation);
            ConfigJSON=IOUtils.toString(inputStream,"utf-8");

        }catch (IOException ioe){
            log.finer("Error to read config fro file:"+ioe.toString());
        }

        return this.config;
    }

    //
    public MainConfig setValuesToProperty(){
        //mainconfig.set..(config.get..)
        //...so on
        //
        return this.mainConfig;
    }

    private String toJSON(ConfigValuesBean config){
        Gson gson = new Gson();
        return gson.toJson(config);
    }

    public void writeConfigToFile(String outputLocation){
        File outputFile = new File(outputLocation);

        try{
            if(! outputFile.exists())
                outputFile.createNewFile();

            OutputStream stream=new FileOutputStream(outputFile);
            IOUtils.write(toJSON(this.config).getBytes("utf-8"),stream);

        }catch (IOException ioe){

        }
    }

}
