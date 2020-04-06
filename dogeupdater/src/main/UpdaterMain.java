package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdaterMain extends Application {


    private static Logger log = LogManager.getLogger();


    //args need to be received: [Dogename Pid] [File Path of Dogename Jar] [Current version]
    public static void main(String[] args){
        if(args.length!=3){
            System.out.println("请传入正确的2个参数：[主程序进程号（PID）] [主程序文件位置] [当前版本]");
            System.exit(0);
            return;
        }
        System.out.println(String.format("Received args:%s; %s; %s",args));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader;
        Parent parent;

        try{
            fxmlLoader=new FXMLLoader(getClass().getResource("/resources/MainInterface.fxml"));
            parent=fxmlLoader.load();
        }catch (Exception e){
            log.error("Error to load main interface FXML :"+e.toString());
            return;
        }

        Scene scene=new Scene(parent,592,496);
        primaryStage.setTitle("Doge Updater v1");
        primaryStage.setScene(scene);

        primaryStage.show();

        InterfaceController interfaceController=fxmlLoader.getController();

        interfaceController.init();
        interfaceController.setArgs(getParameters().getRaw().toArray(new String[0]));

    }
}
