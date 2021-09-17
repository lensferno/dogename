package me.lensferno.dogename.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class NetworkUtil {

    public static final String REQUEST_USERAGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.67";

    public static String getHtml(String address) {
        return getHtml(address, false);
    }

    public static String getHtml(String address, boolean output) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bis;
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", REQUEST_USERAGENT);

            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.connect();

            InputStream is = conn.getInputStream();
            String connEncoding = conn.getContentEncoding();

            if (connEncoding == null)
                connEncoding = "none";

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

            for (String temp; (temp = bis.readLine()) != null; ) {
                sb.append(temp).append("\n");
            }

            is.close();
        } catch (Exception e) {
            System.out.println("Error in getting HTML:" + e);
            return null;
        }

        if (output)
            System.out.println("[INFO]Got：" + sb);

        return sb.toString();
    }

    public static byte[] download(String url) {
        try {
            URL requestUrl = new URL(url);
            URLConnection connection = requestUrl.openConnection();

            connection.setRequestProperty("user-agent", REQUEST_USERAGENT);

            InputStream inputStream = connection.getInputStream();

            return IOUtil.readDataFromInputStream(inputStream, 15);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMacMD5() {
        try {
            byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            StringBuffer sb = new StringBuffer();

            for (byte b : mac) {
                String s = Integer.toHexString(b & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return DigestUtils.md5Hex(sb.toString().toUpperCase());
        } catch (Exception e) {
            return DigestUtils.md5Hex("(+_+)?");
        }
    }
}
