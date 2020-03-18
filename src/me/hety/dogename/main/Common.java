package me.hety.dogename.main;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.net.HttpURLConnection;

public class Common {

    public static void write(byte[] bytes,File file){
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    public static void copyToClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(text);
        clipboard.setContents(trans, null);
    }

    public static String getClipboardString() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
//---------------------------------------------------------------------------------------

    public static int download(String URL,String fileLocation){
        try{
            URL sourcesURL = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");

            connection.connect();

            InputStream stream = connection.getInputStream();

            String[] temp=URL.split("/");
            String fileName=temp[temp.length-1];
            FileOutputStream fileStream = new FileOutputStream(new File(fileLocation+fileName));

            for (int i = stream.read(); i != -1; i = stream.read())
                fileStream.write(i);

            fileStream.close();

            System.out.println("[INFO]Download done save to："+fileLocation+fileName);
            return 0;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

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
        }catch(Exception e){e.printStackTrace();return null;}
        if(output)
            System.out.println("[INFO]Got："+sb.toString());
        return sb.toString();
    }
}
