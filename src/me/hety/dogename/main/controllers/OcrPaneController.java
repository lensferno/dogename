package me.hety.dogename.main.controllers;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.hety.dogename.main.Common;
import me.hety.dogename.main.ocr.Ocr;
import me.hety.dogename.main.ocr.ScreenCapture;

public class OcrPaneController {

    Ocr ocr;

    @FXML
    private TextArea ocrText;

    @FXML
    private Text statusText;

    @FXML
    private JFXSpinner loadingSpinner;

    Stage pStage;

    public void setpStage(Stage pStage) {
        this.pStage = pStage;
    }

    @FXML
    void addNew(ActionEvent event) {

        Stage stage=(Stage)ocrText.getScene().getWindow();
        stage.hide();
        pStage.hide();

        //等待系统动画结束
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean captureSuccess=ScreenCapture.getScreenCapture();

        if(!captureSuccess){
            statusText.setText("状态：截屏失败。");
            System.out.println("状态：截屏失败。");
            pStage.show();
            stage.show();
            return;
        }
        loadingSpinner.setVisible(true);

        if(ocr==null){
            ocr=new Ocr();
            ocr.init();
        }

        new Thread(()->{
            boolean ocrSuccrss=ocr.identifyPrecisely(ScreenCapture.SCREEN_CAPTURE_LOCA);

            if (ocrSuccrss) {

                Platform.runLater(()->{
                    ocrText.setText(ocrText.getText()+ocr.getResult());
                    statusText.setText("状态：成功。");
                    System.out.println("状态：成功。");
                    loadingSpinner.setVisible(false);

                });
            }else {
                Platform.runLater(()->{
                    ocrText.setText(ocr.getResult());
                    statusText.setText("状态：失败。");
                    System.out.println("状态：失败。");
                    loadingSpinner.setVisible(false);

                });
            }
        }).start();
        pStage.show();
        stage.show();
    }

    @FXML
    void copyText(ActionEvent event) {
        Common.copyToClipboard(ocrText.getText());
    }
}
