package main;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.codec.binary.Hex;
import sun.rmi.runtime.Log;

import static javafx.stage.StageStyle.*;

public class ProgramMain extends Application {


    final static private String FXML_FILE = "file:/D:/DM_Master_sources-master/sources/UI.fxml";

    final static private String UI_FILE = "D:\\DM_Master_sources-master\\sources\\UI.fxml";
    final static private String IAMGE_FILE = "D:\\DM_Master_sources-master\\sources\\img1.png";
    final static private String BACKIAMGE_FILE = "D:\\DM_Master_sources-master\\sources\\back.png";

    final static private String FXML_FILE_MD5 = "0b33ef9a80e667c2435f7513b489e803";
    final static private String IAMGE_FILE_MD5 = "c525694ef2bb39c0d04826ca6cf58c79";
    final static private String BACKIAMGE_FILE_MD5 = "e481912f539f59bc38b6cc26d278dfcd";

    final static private String STYLE_FILE = "file:/D:/DM_Master_sources-master/sources/css.css";
    final static private String SOURCES_LOCA = "D:\\";
    final static private String SOURCES_URL = "https://github.com/Het7230/DM_Master_sources/archive/master.zip";
    final static private String ZIP_FILE_LOCA = "D:\\TEMP.ZIP";
    final static private String CONFIG_FILE = "D:\\DM_Master_sources-master\\config";

    final static private File imageFile = new File(IAMGE_FILE);
    final static private File backImageFile = new File(BACKIAMGE_FILE);
    final static private File UIFile = new File(UI_FILE);

    private Config config;

    Stage stage = new Stage();

    Text text_1 = new Text("第一次运行 或 文件受损 需要加载文件，视网络情况需要一段时间，请耐心等等。：）");
    Text text_2 = new Text("资源文件不大，正常情况下不会用太长时间。");
    Text text_3 = new Text("若网络不可用将尝试使用本地加载。");

    Stage firstStage = new Stage();

    Pane secondPane = new Pane();

    JFXSpinner loading = new JFXSpinner();


    Runnable showRunnable = (new Runnable() {
        @Override
        public void run() {
            showWindow(firstStage);
        }
    });


    Runnable getSourceRunnable = new Runnable() {
        @Override
        public void run() {

            releaseData rd = new releaseData();
            if (rd.releaseAllFile() == 0) {
                Platform.runLater(showRunnable);
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisible(false);
                        showInfoDialog("啊？", "程序无法加载资源文件到这台计算机上，请检查系统是否有关于文件权限的问题，然后重启程序尝试。", secondPane);
                        text_1.setText("资源文件加载失败，请检查程序是否有权限访问文件，然后重启本程序尝试。");
                        text_2.setText("");
                        text_3.setText("");
                    }
                });
                return;
            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws IOException {

/*
            int min=250;
            int max=100;

            int s=(int)min+(int)(Math.random()*(max-min));
            for(int i=0;i<200;i++){
                System.out.println((int)min+(int)(Math.random()*(max-min)));
            }*/
/*
            Downloader dl=new Downloader("https://github.com/Het7230/DM_master/releases/download/1.3.2.0/DMmaster.exe","I:\\DM_master\\update.temp",16);

            try{
            dl.startDown();
            }
            catch (Exception e){e.printStackTrace();}*/

        if (hasSources() == false) {

            secondPane.setPrefWidth(700);
            secondPane.setPrefHeight(460);

            firstStage.setTitle("MDmaster-资源文件加载");

            text_1.setLayoutX(106);
            text_1.setLayoutY(293);

            text_2.setLayoutX(106);
            text_2.setLayoutY(322);

            text_3.setLayoutX(106);
            text_3.setLayoutY(351);

            text_1.setTextAlignment(TextAlignment.CENTER);
            text_2.setTextAlignment(TextAlignment.CENTER);
            text_3.setTextAlignment(TextAlignment.CENTER);


            text_1.setFont(new Font(16));
            text_2.setFont(new Font(16));
            text_3.setFont(new Font(16));

            loading.setLayoutX(258);
            loading.setLayoutY(50);

            loading.setPrefWidth(200);
            loading.setPrefHeight(200);

            Scene newScene = new Scene(secondPane, 700, 460);

            secondPane.getChildren().add(loading);
            secondPane.getChildren().add(text_1);
            secondPane.getChildren().add(text_2);
            secondPane.getChildren().add(text_3);

            firstStage.setScene(newScene);

            firstStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    new Thread(getSourceRunnable).start();
                }
            });

            firstStage.show();

            //showInfoDialog("先等等吧","第一次运行 或 资源文件受损 加载资源文件中，视网络情况需要一段时间，请耐心等等。\n：）",secondPane);

        } else

            showWindow();

    }

    //程序主函数
    public static void main(String[] args) {
        launch(args);
    }


    public void showWindow(Stage firstStage) {

        try {
            File configFile = new File(CONFIG_FILE);

            if (configFile.exists() != true) {
                configFile.createNewFile();
                config = new Config();
            } else {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
                this.config = (Config) ois.readObject();
            }

        } catch (Exception e) {
            config = new Config();
            e.printStackTrace();
        }


        try {
            FXMLLoader loader = new FXMLLoader(new URL(FXML_FILE));
            Parent root = loader.load();
            Scene scene = new Scene(root, 990, 700);
            stage.setTitle("MDmaster 初号姬");
            stage.setScene(scene);
            stage.setResizable(false);
            //scene.getStylesheets().add(STYLE_FILE);
            UICtrl controller = loader.getController(); //获取Controller的实例对象//传递primaryStage，scene参数给Controller
            controller.setStage(stage);
            controller.setScene(scene);
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    firstStage.close();
                }
            });
            stage.show();
            //controller.setImages();
            //primaryStage.show();

            //stage.close();

            controller.setConfig(config);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.saveConfigToFile();
                    //primaryStage.close();
                }
            });

            if (config.isRandomTimes())
                controller.randomTimes_selected();
            else
                controller.fixedTimes_selected();


            if (config.isIgnorePast())
                controller.ignoreOnce_selected();
            else
                controller.chooseOnce_selected();


            if (config.isNameChoose())
                controller.nameChoose_selected();
            else
                controller.numbChoose_selected();


            if(config.isTaoluMode())
                controller.taoluModeBtn_selected();
            else
                controller.taoluModeBtn_unselect();
                
            
            controller.setMaxNumber(config.getMaxNumber());
            controller.maxNumb.setText(String.valueOf(config.getMaxNumber()));
            controller.setMinNumber(config.getMinNumber());
            controller.minNumb.setText(String.valueOf(config.getMinNumber()));

            controller.setSpeed(config.getSpeed());
            controller.setChosenTime(config.getChosenTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void showWindow() {

        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists() != true) {
                configFile.createNewFile();
                config = new Config();
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
            this.config = (Config) ois.readObject();
            System.out.println("ha?");
        } catch (Exception e) {
            config = new Config();
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(new URL(FXML_FILE));
            Parent root = loader.load();

            Scene scene = new Scene(root, 990, 700);
            stage.setTitle("MDmaster 初号姬");
            System.out.println("ha2?");
            stage.setScene(scene);
            stage.setResizable(false);
            UICtrl controller = loader.getController(); //获取Controller的实例对象//传递primaryStage，scene参数给Controller
            controller.setStage(stage);
            controller.setScene(scene);
            stage.show();
            //controller.setImages();

            //primaryStage.show();

            //stage.close();

            controller.setConfig(config);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.saveConfigToFile();
                    //primaryStage.close();
                }
            });

            if (config.isRandomTimes())
                controller.randomTimes_selected();
            else
                controller.fixedTimes_selected();


            if (config.isIgnorePast())
                controller.ignoreOnce_selected();
            else
                controller.chooseOnce_selected();


            if (config.isNameChoose())
                controller.nameChoose_selected();
            else
                controller.numbChoose_selected();


            if(config.isTaoluMode())
                controller.taoluModeBtn_selected();
            else
                controller.taoluModeBtn_unselect();
     
           
            controller.setMaxNumber(config.getMaxNumber());
            controller.maxNumb.setText(String.valueOf(config.getMaxNumber()));
            controller.setMinNumber(config.getMinNumber());
            controller.minNumb.setText(String.valueOf(config.getMinNumber()));

            controller.setSpeed(config.getSpeed());
            controller.setChosenTime(config.getChosenTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            String md5 = new String(Hex.encodeHex(MD5.digest()));
            System.out.println(md5);
            return md5;
        } catch (Exception e) {
            e.printStackTrace();
            return "[ERROR]";
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //从gayhub上抓点好东西
    public static void getSources() throws IOException {

        URL sourcesURL = new URL(SOURCES_URL);
        HttpURLConnection connection = (HttpURLConnection) sourcesURL.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
        connection.connect();

        InputStream stream = connection.getInputStream();
        FileOutputStream fileStream = new FileOutputStream(new File("D:\\TEMP.ZIP"));

        for (int i = stream.read(); i != -1; i = stream.read())
            fileStream.write(i);

        fileStream.close();

        System.out.println("[INFO]资源拉取成功。");

    }


    //解压资源文件
    public static void unZip() throws IOException {

        File pathFile = new File(SOURCES_LOCA);
        File zipFile = new File(ZIP_FILE_LOCA);

        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (SOURCES_LOCA + zipEntryName).replaceAll("\\*", "/");
            ;

            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }

            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }

            //输出文件路径信息
            System.out.println("[INFO]文件：" + outPath);
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0)
                out.write(buf1, 0, len);

            in.close();
            out.close();
        }
        System.out.println("[INFO]资源解压完毕。");
        zipFile.delete();

    }


    //判断有没有资源文件，有则告诉main不需再下载资源文件(返回true)
    public static boolean hasSources() {

        boolean debugMode=true;
        System.out.println(UIFile.exists());
        System.out.println(imageFile.exists());
        System.out.println(backImageFile.exists());
        if (!UIFile.exists() || !imageFile.exists() || !backImageFile.exists()) {
            System.out.println("[INFO]没有UI文件，获取资源并解压。");
            return false;
        } else {
            if (debugMode) {
                System.out.println("[DEBUG][调试模式]跳过检查");
                return true;
            } else {
                String UIFileMD5 = getMD5(UIFile);
                String imageFileMD5 = getMD5(imageFile);
                String backImageFileMD5 = getMD5(backImageFile);
                if (UIFileMD5.equals(FXML_FILE_MD5) && imageFileMD5.equals(IAMGE_FILE_MD5) && backImageFileMD5.equals(BACKIAMGE_FILE_MD5)) {
                    System.out.println("[INFO]有UI文件，直接运行。");
                    return true;
                } else {
                    System.out.println("[INFO]文件校验错误，拉取文件。");
                    return false;
                }
            }
        }
    }

        public void showInfoDialog (String header, String message, Pane pane){
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(header));
            content.setBody(new Text(message));
            StackPane tempPane = new StackPane();
            tempPane.setPrefHeight(pane.getPrefHeight());
            tempPane.setPrefWidth(pane.getPrefWidth());
            pane.getChildren().add(tempPane);

            JFXDialog dialog = new JFXDialog(tempPane, content, JFXDialog.DialogTransition.TOP);
            dialog.setPrefHeight(pane.getPrefHeight());
            dialog.setPrefWidth(pane.getPrefWidth());

            JFXButton button = new JFXButton("OK");
            button.setPrefWidth(50);
            button.setPrefHeight(30);
            button.setOnAction((ActionEvent e) -> {
                dialog.close();
                pane.getChildren().remove(tempPane);
            });
            content.setActions(button);
            dialog.show();

        }

    }


