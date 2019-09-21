package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Common {

    public static String getHtml(String address)
    {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bis;
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.connect();

            System.out.println("--------------------------------------------------------------------");
            System.out.println("[INFO]Getting："+conn.getURL());
            System.out.println("[INFO]URL compress type："+conn.getContentEncoding());

            InputStream is = conn.getInputStream();
            String connEncoding=conn.getContentEncoding();
            if(connEncoding==null)
                connEncoding="none";
            switch (connEncoding) {
                case "deflate":
                    InflaterInputStream deflate = new InflaterInputStream(is, new Inflater(true));
                    bis = new BufferedReader(new InputStreamReader(deflate, "utf-8"));
                    break;
                case "gzip":
                    GZIPInputStream gzip = new GZIPInputStream(is);
                    bis = new BufferedReader(new InputStreamReader(gzip, "utf-8"));
                    break;
                default:
                    bis = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    break;
            }
            String temp;
            while ((temp = bis.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
        }catch(Exception e){e.printStackTrace();}
        //System.out.println("[INFO]Got："+sb.toString());
        return sb.toString();
    }
}
