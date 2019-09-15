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

    final static private String CONFIG_FILE = "D:\\dogename\\files\\config";


    private Config config;

    Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws IOException {
        showWindow();
    }

    public static void main(String[] args) {
	
	File fileDir = new File("D:\\dogename\\files");
	if(!fileDir.exists())
	    fileDir.mkdir();
	
        launch(args);

    }


    
    private boolean debugMode=true;
    
    public void showWindow() {

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
                System.out.print("[INFO]Running in debug mode\n");
                System.out.print("[INFO]Working in:"+getClass().getResource("/").toString()+"\n");
                System.out.println("[INFO]UI file is in:"+getClass().getResource("/main/sourcesData/uifiles/UI.fxml").toString());
                
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

            //stage.setResizable(false);
            stage.setResizable(true);
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
            
            controller.showShiCi();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


