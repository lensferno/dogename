package me.hety.dogename.main;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.hety.dogename.main.configs.ConfigLoader;
import me.hety.dogename.main.controllers.MainInterfaceController;
import me.hety.dogename.main.sayings.Gushici;
import me.hety.dogename.main.sayings.Hitokoto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.util.Random;

public class Main extends Application {

    Logger log = LogManager.getLogger();

    public static void main(String[] args){System.out.println("Received "+args.length+" args");launch(args);}

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader;
        Parent parent;

        try{
            fxmlLoader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/MainInterface.fxml"));
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

        primaryStage.setOnCloseRequest(event -> configLoader.writeAllConfigToFile("files"+ File.separator+"Config.json","files"+ File.separator+"VoiceConfig.json"));

        if (new Random().nextBoolean()) {
            new Gushici().showGushici(mainInterfaceController.getRootPane(),mainInterfaceController.getTopBar());
        }else {
            new Hitokoto().showHitokoto(mainInterfaceController.getRootPane(),mainInterfaceController.getTopBar());
        }

        startMessageThread(mainInterfaceController);

    }

    private void startMessageThread(MainInterfaceController mainInterfaceController){
        new Thread(()->{
            String messageJson=Common.getHtml("https://gitee.com/hety2002/dogename/raw/master/message2.txt",false);
            MessageBean messageBean =new Gson().fromJson(messageJson,MessageBean.class);
            if (messageBean==null&&messageBean.getMessage()==null){
                return;
            }
            String messageShow="";

            if(messageBean.getType().equals("other")){
                messageShow=messageBean.getMessage();
            }else if (messageBean.getType().equals("update")){
                if (messageBean.getLower_ver_show()>3.21){
                    messageShow=messageBean.getMessage();
                }
            }
            String finalMessageShow = messageShow;

            Platform.runLater(()->mainInterfaceController.message.setText(finalMessageShow));
        }).start();
    }

}
class MessageBean{
    private String type;
    private double lower_ver_show;
    private String message;
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setLower_ver_show(double lower_ver_show) {
        this.lower_ver_show = lower_ver_show;
    }
    public double getLower_ver_show() {
        return lower_ver_show;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}