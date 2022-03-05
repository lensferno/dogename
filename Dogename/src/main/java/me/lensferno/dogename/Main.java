package me.lensferno.dogename;

import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.lensferno.dogename.configs.ConfigLoader;
import me.lensferno.dogename.controllers.MainInterfaceController;
import me.lensferno.dogename.sayings.Gushici;
import me.lensferno.dogename.sayings.Hitokoto;
import me.lensferno.dogename.utils.FilePath;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

            new File("process").mkdirs();

            File tempFile = new File(FilePath.toSpecificPathForm("process/" + pid));
            tempFile.createNewFile();
            tempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader;
        Parent parent;

        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/MainInterface.fxml"));
            parent = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println("Error to load main interface FXML :" + e);
            return;
        }

        Scene scene = new Scene(parent, 990, 700);
        primaryStage.setTitle("DogeName 叁号姬");
        primaryStage.setScene(scene);

        primaryStage.show();

        ConfigLoader configLoader = new ConfigLoader();

        MainInterfaceController mainInterfaceController = fxmlLoader.getController();

        mainInterfaceController.setToggleGroup();

        mainInterfaceController.setUpConfig(configLoader);

        mainInterfaceController.bindProperties();

        try {
            mainInterfaceController.setImg(getClass().getResourceAsStream("/images/doge.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainInterfaceController.init();

        primaryStage.setOnCloseRequest(event -> {
            configLoader.writeAllConfigToFile(configLoader.getMainConfigLocation(), configLoader.getVoiceConfigLocation());
            System.exit(0);
        });

        if (new Random().nextBoolean()) {
            new Gushici().showGushici(mainInterfaceController.getRootPane(), mainInterfaceController.getTopBar(), mainInterfaceController.getMainConfig().isShowSaying());
        } else {
            new Hitokoto().showHitokoto(mainInterfaceController.getRootPane(), mainInterfaceController.getTopBar(), mainInterfaceController.getMainConfig().isShowSaying());
        }
    }

}