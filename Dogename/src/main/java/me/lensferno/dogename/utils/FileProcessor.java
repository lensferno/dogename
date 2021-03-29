package me.lensferno.dogename.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileProcessor {

    static Logger log = LogManager.getLogger();

    public static void writeFile(byte[] bytes, File file){
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            log.error("Error in writting file"+"\""+file.getName()+"\""+":"+e);
        }
    }
}
