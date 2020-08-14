package main;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.google.gson.Gson;

public class GuShiCi {
    private String shiciContent;
    private final String API="https://v1.jinrishici.com/all.json";
    Gushici gsc;
    public String get() {
	String respond = getHtml(API);
	gsc = new Gson().fromJson(respond, Gushici.class);
	shiciContent = gsc.getContent();
	return gsc.getOrigin()+"\n\n"+gsc.getAuthor()+"\n\n"+gsc.getContent()+"\n\n\n#"+gsc.getCategory();
    }

    public String getShiciContent() {
        return shiciContent;
    }
    
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
        System.out.println("[INFO]Got："+sb.toString());
        return sb.toString();
    }

    public String getCategory() {
        return gsc.getCategory();
    }

    public String getOrigin() {
        return gsc.getOrigin();
    }

    public String getAuthor() {
        return gsc.getAuthor();
    }
    class Gushici {

       private String content;//诗歌的内容
       private String origin;//诗歌的标题
       private String author;//诗歌作者
       private String category;//诗歌流派
       public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

       public void setOrigin(String origin) {
            this.origin = origin;
        }
        public String getOrigin() {
            return origin;
        }

       public void setAuthor(String author) {
            this.author = author;
        }
        public String getAuthor() {
            return author;
        }

       public void setCategory(String category) {
            this.category = category;
        }
        public String getCategory() {
            return category;
        }

   }

}
