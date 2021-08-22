package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.lensferno.dogename.utils.ocr.ScreenCapture;
import me.lensferno.dogename.utils.Clipboard;

public class OcrPaneController {

    @FXML
    private TextArea ocrText;

    @FXML
    private JFXSpinner loadingSpinner;

    Stage mainStage;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    void addNew() {

        Stage thisStage=(Stage)ocrText.getScene().getWindow();
        thisStage.hide();
        mainStage.hide();

        // 等待系统动画结束
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ScreenCapture screenCapture = new ScreenCapture();

        ocrText.textProperty().bindBidirectional(screenCapture.resultProperty());
        loadingSpinner.visibleProperty().bind(screenCapture.endProperty().not());

        screenCapture.startCapture();

        screenCapture.endProperty().addListener((observable, oldValue, end) -> {
            if (end) {
                mainStage.show();
                thisStage.show();
            }
        });
    }

    @FXML
    void copyText() {
        Clipboard.copyToClipboard(ocrText.getText());
    }
}
