package me.lensferno.dogename.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class HtmlRequseter {

    static Logger log = LogManager.getLogger();

    public static String getHtml(String address) {
        return getHtml(address, false);
    }

    public static String getHtml(String address,boolean output)
    {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bis;
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");

            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.connect();

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
            log.error("Error in getting HTML:"+e);
            return null;
        }

        if(output)
            System.out.println("[INFO]Got："+sb.toString());

        return sb.toString();
    }
}
