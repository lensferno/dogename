package main;



import java.io.*;
import java.security.SecureRandom;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ProgramMain extends Application {

    private String CONFIG_FILE ;//="config.data";


    private Config config;

    Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws IOException {

        File fileDir = new File(app.APP_LOCA+"files");
        if(!fileDir.exists())
            fileDir.mkdir();

        showWindow();


    }

    App app=new App();
    public static void main(String[] args) {



        launch(args);

    }


    
    private boolean debugMode=true;

    public void showWindow() {


	if(System.getProperty("os.name").toLowerCase().contains("window"))
            CONFIG_FILE=app.APP_LOCA+"files\\config.data";
	else
            CONFIG_FILE=app.APP_LOCA+"files/config.data";


        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists() != true) {
                configFile.createNewFile();
                config = new Config();
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
            this.config = (Config) ois.readObject();
        } catch (Exception e) {
            config = new Config();
            e.printStackTrace();
        }

        try {
            
            FXMLLoader loader;
            Parent root;
            
            if(debugMode) {
                //System.out.print("[INFO]Running in debug mode\n");
                System.out.print("[INFO]Working in:"+getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
               // System.out.println("[INFO]UI file is in:"+getClass().getResource("/main/sourcesData/uifiles/UI.fxml").toString());
                
                loader = new FXMLLoader(getClass().getResource("/main/sourcesData/uifiles/UI.fxml"));
                root = loader.load();
            }else {
                System.out.print("[INFO]Not run in debug mode\n");
                System.out.print("[INFO]Working in:"+getClass().getResource("/").toString());
                
                loader =new FXMLLoader();
                root = loader.load(releaseData.getUIStream());
            }
            
            Scene scene = new Scene(root, 990, 700);
            stage.setTitle("DogeName 贰号姬");
            stage.setScene(scene);

            //stage.setResizable(true);
            UICtrl controller = loader.getController();
            controller.setStage(stage);
            controller.setScene(scene);
            stage.show();


            controller.setConfig(config);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.saveConfigToFile();
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

            if(config.isEqualMode())
                controller.selectEqualBtn();
            else
                controller.unSelectEqualBtn();

            if(config.isNewAlgo())
                controller.selectNewAlgoBtn();
            else
                controller.unselectNewAlgoBtn();

            controller.setImages();

            controller.setMaxNumber(config.getMaxNumber());
            controller.maxNumb.setText(String.valueOf(config.getMaxNumber()));
            controller.setMinNumber(config.getMinNumber());
            controller.minNumb.setText(String.valueOf(config.getMinNumber()));

            controller.setSpeed(config.getSpeed());
            controller.setChosenTime(config.getChosenTime());

            stage.setResizable(true);
            
            controller.showShiCi();

            //System.out.println("[INFO]Update statu:"+update.checkUpdate());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getUpdate();
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    Update update =new Update();
    String unzipCmd;
    int URLNumbs;
    boolean stopUpdate=true;
    int finishStatus = 0;

    void getUpdate(){

        if(update.checkUpdate()){
            unzipCmd=app.APP_LOCA+"extra\\7z.exe x -y -o"+app.APP_LOCA+" "+app.APP_LOCA+update.getFirstFileName();
            String[] updateURLs=update.getUpdateURL();
            URLNumbs=updateURLs.length;
            stopUpdate=false;
            for(int i=0;i<updateURLs.length;i++){
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Common.download(updateURLs[finalI],app.APP_LOCA)==-1)
                            stopUpdate=true;
                        else
                            finishStatus++;

                    }
                }).start();
            }
            while(true){
                if(stopUpdate) {
                    System.out.println("[INFO]Stopped update process");
                    break;
                }else {
                    if(finishStatus==update.getUpdateURL().length){
                        try {
                            System.out.println("[INFO]Unzip update package");
                            System.out.println("[INFO]Doing :"+unzipCmd);
                            Process unzipProcess = Runtime.getRuntime().exec(unzipCmd);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

    }

}


