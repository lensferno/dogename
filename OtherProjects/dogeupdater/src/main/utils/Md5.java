package main.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

    public static boolean checkMd5(String filePath,String md5){
        return checkMd5(new File(filePath),md5);
    }

    public static boolean checkMd5(File file,String md5){
        if(!FileAccessChecker.checkReadAccess(file)){
            System.out.println("File can't be read!");
            return false;
        }
        ByteOutputStream dataStream=new ByteOutputStream();

        try(FileInputStream fileInputStream=new FileInputStream(file);) {

            byte[] b = new byte[1024];
            int len;
            while((len = fileInputStream.read(b)) != -1) {
                dataStream.write(b, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return checkMd5(dataStream.getBytes(),md5);

    }

    public static boolean checkMd5(byte[] data,String md5){

        String realMd5=getMd5(data);

        if (realMd5 == null) {
            System.out.println(String.format("Giving md5 value:%s,true value:%s",md5,realMd5));
            return false;
        }else {
            return md5.toLowerCase().equals(realMd5);
        }

    }

    //取得32位小写md5值
    private static String getMd5(byte[] data){
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(data);

            byte[] resultByte = messageDigest.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b : resultByte){
                int bt = b&0xff;
                if(bt<16){
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }

            return sb.toString().toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
