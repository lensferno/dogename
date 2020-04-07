package main.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Net {

    public static String getHtml(String address,boolean output)
    {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bis;
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");

            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.connect();

            System.out.println("--------------------------------------------------------------------");
            System.out.println("[INFO]Getting："+conn.getURL());
            System.out.println("[INFO]Content compress type："+conn.getContentEncoding());

            InputStream is = conn.getInputStream();
            String connEncoding=conn.getContentEncoding();

            if(connEncoding==null)
                connEncoding="none";

            switch (connEncoding) {
                case "deflate":
                    InflaterInputStream deflate = new InflaterInputStream(is, new Inflater(true));
                    bis = new BufferedReader(new InputStreamReader(deflate, StandardCharsets.UTF_8));
                    break;
                case "gzip":
                    GZIPInputStream gzip = new GZIPInputStream(is);
                    bis = new BufferedReader(new InputStreamReader(gzip, StandardCharsets.UTF_8));
                    break;
                default:
                    bis = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    break;
            }
            String temp;
            while ((temp = bis.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
        }catch(Exception e){
            System.out.println("Error in getting HTML:"+e);
            return null;
        }

        if(output)
            System.out.println("[INFO]Got："+sb.toString());

        return sb.toString();
    }

    public static int download(String URL,String fileLocation){
        try{
            File dir=new File(fileLocation);
            if(!dir.exists()){
                dir.mkdirs();
            }
            URL sourcesURL = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");

            connection.setRequestProperty("Accept-Encoding","identity");

            connection.connect();

            InputStream stream = connection.getInputStream();

            String[] temp=URL.split("/");
            String fileName=temp[temp.length-1];
            FileOutputStream fileStream = new FileOutputStream(new File(fileLocation+fileName));

            byte[] data = new byte[1024*50];  // 50KB

            int totalBytes = 0;
            int length;

            while ((length = stream.read(data)) != -1) {
                totalBytes += length;
                fileStream.write(data, 0, length);
            }
            fileStream.flush();
/*
            for (int i = stream.read(); i != -1; i = stream.read())
                fileStream.write(i);
*/
            fileStream.close();

            System.out.println("[INFO]Download done ："+fileLocation+fileName+" ,total:"+totalBytes);
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

}
