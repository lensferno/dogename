package me.lensferno.dogename.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtil {
    public static String inputStreamToString(InputStream inputStream, Charset charSet) {
        byte[] bytes = readDataFromInputStream(inputStream);
        if (bytes != null) {
            return new String(bytes, charSet);
        } else {
            return null;
        }
    }

    public static byte[] readDataFromInputStream(InputStream inputStream) {
        return readDataFromInputStream(inputStream, 5);// read every 5kb in default
    }

    public static byte[] readDataFromInputStream(InputStream inputStream, int byteAllocation) {
        try {
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * byteAllocation];

            for (int length; (length = inputStream.read(bytes)) != -1; ) {
                byteArrayInputStream.write(bytes, 0, length);
            }

            byteArrayInputStream.flush();

            inputStream.close();
            byteArrayInputStream.close();

            return byteArrayInputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(byte[] bytes, File file) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }
}
