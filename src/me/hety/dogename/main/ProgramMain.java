package me.hety.dogename.main;



import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public final class ProgramMain extends Application {

    Logger log= Logger.getLogger("ProgramLogger");

    private String CONFIG_FILE ;//="config.data";


    private Config config;

    Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws IOException {

        File fileDir = new File("files");
        if(!fileDir.exists())
            fileDir.mkdirs();

        showWindow();


    }

    App app=new App();
    public static void main(String[] args) {

        launch(args);

    }


    
    private boolean debugMode=true;

    void readConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists() != true) {
                configFile.createNewFile();
                config = new Config();
                return;
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFile));
            this.config = (Config) ois.readObject();
        }catch(EOFException EOFe){
            config=new Config();
            log.warning("Config file is empty.");
        } catch (Exception e) {
            config = new Config();
            log.warning("Failed to load config file.");
            e.printStackTrace();
        }
    }

    UICtrl_new controller;

    public void showWindow() {


	if(System.getProperty("os.name").toLowerCase().contains("window"))
            CONFIG_FILE="files\\config.data";
	else
            CONFIG_FILE="files/config.data";

	readConfig();

        try {
            
            FXMLLoader loader;
            Parent root;
            
            if(debugMode) {
                loader = new FXMLLoader(getClass().getResource("/me/hety/dogename/main/sourcesData/uifiles/UI.fxml"));
                root = loader.load();
            }else {
                System.out.print("[INFO]Not run in debug mode\n");
                loader =new FXMLLoader();
                root = loader.load(DataReleaser.getUIStream());
            }
            
            Scene scene = new Scene(root, 990, 700);
            //Application.setUserAgentStylesheet(getClass().getResource("/main/sourcesData/uifiles/UI.fxml").toExternalForm());
            //scene.getStylesheets().add("/main/sourcesData/uifiles/UI.fxml");
            //root.setStyle("-fx-font-family:\"Microsoft YaHei\";");
            stage.setTitle("DogeName 贰号姬");
            stage.setScene(scene);

            //stage.setResizable(true);
            controller = loader.getController();
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
            
            if(config.isVoicePlay())
        	controller.selectVoicePlayBtn();
            else
        	controller.unselectVoicePlayBtn();


            controller.setImages();

            controller.setMaxNumber(config.getMaxNumber());
            controller.maxNumb.setText(String.valueOf(config.getMaxNumber()));
            controller.setMinNumber(config.getMinNumber());
            controller.minNumb.setText(String.valueOf(config.getMinNumber()));

            controller.setSpeed(config.getSpeed());
            controller.setChosenTime(config.getChosenTime());

            controller.loadHistory();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {

                }
            });

            stage.setResizable(true);
            
            controller.showShiCi();

            //System.out.println("[INFO]Update statu:"+update.checkUpdate());

            if(System.getProperty("os.name").contains("indow")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getUpdate();
                    }
                }).start();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    Update update =new Update();
    String unzipCmd;
    String recoverBatPath=app.APP_LOCA+"recover.bat";
    int URLNumbs;
    boolean stopUpdate=true;
    int finishStatus = 0;

    void getUpdate(){

        if(update.checkUpdate()){
            unzipCmd="\""+app.APP_LOCA+"extra\\7z.exe x -y -o"+app.APP_LOCA+" "+app.APP_LOCA+"updateFiles\\"+update.getFirstFileName()+"\"";

            String[] updateURLs=update.getUpdateURL();
            URLNumbs=updateURLs.length;
            stopUpdate=false;
            File updateDir=new File("updateFiles\\");

            if(!updateDir.exists())
                updateDir.mkdirs();
            else{
                File[] updateFiles=updateDir.listFiles();
                if(updateFiles.length>0)
                    for(int i=0;i<updateFiles.length;i++)
                        updateFiles[i].delete();
            }

            for(int i=0;i<updateURLs.length;i++){
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Common.download(updateURLs[finalI],app.APP_LOCA+"updateFiles\\")==-1)
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
                            System.out.println("[INFO]Backuping jar file");
                            copyFileUsingJava7Files(new File(app.JAR_FILE),new File(app.APP_LOCA+"backup.jar"));

                            System.out.println("[INFO]Unzip update package");
                            String updateCmd=app.APP_LOCA+" "+
                                    app.PID+" "+
                                    unzipCmd+" "+
                                    recoverBatPath;

                            System.out.println("[INFO]Will do:"+updateCmd);
                            controller.showInfoDialog("有更新诶！","这次更新的主要内容有：\n"+update.getUpdateInfo().getDesc());
                            stage.setOnCloseRequest(event -> {
                                try {
                                    Runtime.getRuntime().exec(updateCmd);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            return;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            return;
            }
        }

    }

    private static void copyFileUsingJava7Files(File source, File dest)
            throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    }


