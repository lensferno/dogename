package main.utils;

import java.io.*;

public class FileAccessChecker {

    public static boolean checkWriteAccessForDir(String dirPath){

        File dir=new File(dirPath);

        if(!dirFileCanBeDelete(new File(dir,"delete_test.tmp"))){
            return false;
        }

        File testFile=new File(dir,"temp.tmp");
        try(FileWriter fileWriter=new FileWriter(testFile)) {

            testFile.createNewFile();
            fileWriter.write("test");
            fileWriter.flush();

            testFile.delete();

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean dirFileCanBeDelete(File testFile){

        try {
            testFile.createNewFile();
            testFile.delete();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean checkWriteAccessForFile(String filePath){

        File file=new File(filePath);

        ByteArrayOutputStream backupByte = new ByteArrayOutputStream();

        try(FileOutputStream fileTestOutputStream=new FileOutputStream(file,true);
            FileInputStream fileInputStream=new FileInputStream(file) ){

            if(!checkReadAccess(filePath)){
                return false;
            }

            byte[] b = new byte[1024];
            int len;
            while((len = fileInputStream.read(b)) != -1) {
                backupByte.write(b, 0, len);
            }

            byte[] testByte={0};

            fileTestOutputStream.write(testByte);
            fileTestOutputStream.flush();

        }catch (Exception e){
            return false;
        }

        //备份写回去的异常捕捉单独拎出来try是因为测试的输出流在try结束后才关闭
        try (FileOutputStream fileBackupOutputStream=new FileOutputStream(file,false)){
            fileBackupOutputStream.write(backupByte.toByteArray());
            fileBackupOutputStream.flush();
        }catch (Exception e){
            return false;
        }

        return true;
    }

    public static boolean checkReadAccess(String filePath){

        File file=new File(filePath);

        return checkReadAccess(file);
    }

    public static boolean checkReadAccess(File file){

        //是文件吗？存在吗？
        if (file.isDirectory()) {
            return false;
        } else if (!file.exists()) {
            return false;
        }

        //真的能读取吗？
        try (FileReader fd = new FileReader(file)) {
            //noinspection LoopStatementThatDoesntLoop
            while ((fd.read()) != -1) {
                break;
            }
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public static boolean checkFullAccessForFile(String file){
        return checkReadAccess(file) && checkWriteAccessForFile(file);
    }

}
