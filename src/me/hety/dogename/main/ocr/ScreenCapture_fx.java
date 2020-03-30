package me.hety.dogename.main.ocr;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class ScreenCapture_fx extends Application {
    int height;
    int weigh;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        height=screenSize.height;
        weigh=screenSize.width;
        Pane root=new Pane();
        root.setMinWidth(weigh);
        root.setMinHeight(height);
        Stage stage=new Stage();
        Scene scene=new Scene(root,weigh,height);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    public static void main(String[] args){

    }
}
