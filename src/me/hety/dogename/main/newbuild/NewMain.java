package me.hety.dogename.main.newbuild;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.hety.dogename.main.configs.ConfigLoader;
import me.hety.dogename.main.configs.MainConfig;
import me.hety.dogename.main.controllers.MainInterfaceController;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.EventListener;
import java.util.logging.Logger;

public class NewMain extends Application {

    Logger log =Logger.getLogger("mainLOgger");

    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader;
        Parent parent;

        try{
            fxmlLoader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/MainInterface.fxml"));
            parent=fxmlLoader.load();
        }catch (Exception e){
            log.warning("Error to load main interface FXML :"+e.toString());
            return;
        }

        Scene scene=new Scene(parent,990,700);
        primaryStage.setTitle("DogeName 贰号姬");
        primaryStage.setScene(scene);
        log.fine("窗口加载完成");

        ConfigLoader configLoader=new ConfigLoader();

        MainInterfaceController mainInterfaceController=fxmlLoader.getController();

        mainInterfaceController.setToggleGroup();

        mainInterfaceController.setUpConfig(configLoader);

        mainInterfaceController.bindProperties();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                configLoader.writeConfigToFile("files\\Config.json");
            }
        });

        primaryStage.show();

    }

}
