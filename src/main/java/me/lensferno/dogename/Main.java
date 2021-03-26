package me.lensferno.dogename;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.lensferno.dogename.configs.ConfigLoader;
import me.lensferno.dogename.controllers.MainInterfaceController;
import me.lensferno.dogename.sayings.Gushici;
import me.lensferno.dogename.sayings.Hitokoto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Main extends Application {

    Logger log = LogManager.getLogger();

    public static void main(String[] args){ launch(args);}

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader;
        Parent parent;

        try{
            fxmlLoader=new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/MainInterface.fxml"));
            parent=fxmlLoader.load();
        }catch (Exception e){
            log.error("Error to load main interface FXML :"+e.toString());
            return;
        }

        Scene scene=new Scene(parent,990,700);
        primaryStage.setTitle("DogeName 叁号姬");
        primaryStage.setScene(scene);

        primaryStage.show();

        ConfigLoader configLoader=new ConfigLoader();

        MainInterfaceController mainInterfaceController=fxmlLoader.getController();

        mainInterfaceController.setToggleGroup();

        mainInterfaceController.setUpConfig(configLoader);

        mainInterfaceController.bindProperties();

        mainInterfaceController.setImg(DataReleaser.getMainPicStream());

        primaryStage.setOnCloseRequest(event -> configLoader.writeAllConfigToFile(configLoader.getMainConfigLocation(),configLoader.getVoiceConfigLocation()));
        
        if (mainInterfaceController.getMainConfig().isShowSaying()) {
            if (new Random().nextBoolean()){
                new Gushici().showGushici(mainInterfaceController.getRootPane(),mainInterfaceController.getTopBar());
            }else {
                new Hitokoto().showHitokoto(mainInterfaceController.getRootPane(),mainInterfaceController.getTopBar());
            }
        }

    }

}