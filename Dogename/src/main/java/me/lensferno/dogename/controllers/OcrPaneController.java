package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.lensferno.dogename.utils.Clipboard;
import me.lensferno.dogename.utils.ocr.ScreenCapture;

public class OcrPaneController {

    Stage mainStage;
    @FXML
    private TextArea ocrText;
    @FXML
    private JFXSpinner loadingSpinner;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    void addNew() {

        Stage thisStage = (Stage) ocrText.getScene().getWindow();
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
        loadingSpinner.visibleProperty().set(true);

        screenCapture.startCapture();

        screenCapture.endProperty().addListener((observable, oldValue, end) -> {
            if (end) {
                mainStage.show();
                thisStage.show();
                loadingSpinner.visibleProperty().set(false);
            }
        });
    }

    @FXML
    void copyText() {
        Clipboard.copyToClipboard(ocrText.getText());
    }
}
