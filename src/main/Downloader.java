package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
    public int download(String URL,String fileLocation){
        try{
            URL sourcesURL = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
            connection.connect();

            InputStream stream = connection.getInputStream();
            FileOutputStream fileStream = new FileOutputStream(new File(fileLocation));

            for (int i = stream.read(); i != -1; i = stream.read())
               fileStream.write(i);

            fileStream.close();

            System.out.println("[INFO]文件下载成功。保存至："+fileLocation);
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return -1; 
        }
    }
 
}
