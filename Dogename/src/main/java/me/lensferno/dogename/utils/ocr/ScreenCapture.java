package me.lensferno.dogename.utils.ocr;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ScreenCapture {

    private final String cacheImageFileLocation = "caches/image/ocrImageCache.png";
    private final SimpleStringProperty result = new SimpleStringProperty();
    private final SimpleBooleanProperty end = new SimpleBooleanProperty(true);
    private final ImageView screenImageView = new ImageView();
    private final Pane rootPane = new Pane();
    private final Stage stage = new Stage();
    private final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final Line MouseLine_X = new Line();
    private final Line MouseLine_Y = new Line();
    ScreenshotTool screenshotTool = new ScreenshotTool();
    private OcrTool ocrTool;
    private final Runnable finalEvent = new Runnable() {
        @Override
        public void run() {
            if (ocrTool == null) {
                ocrTool = new OcrTool();
            }
            end.set(true);
            ocrTool.requestOcrAPI(cacheImageFileLocation);
            result.set(ocrTool.getResult());
        }
    };
    private double dragStartX = 0;
    private double dragStartY = 0;

    private double dragEndX = 0;
    private double dragEndY = 0;

    private double selectedAreaWidth = 0;
    private double selectedAreaHeight = 0;

    private Line Width_Follow;
    private Line Height_Follow;
    private Line Width_Fixed;
    private Line Height_Fixed;

    public void startCapture() {
        end.set(false);
        Image fullscreenImage = screenshotTool.getFullScreenshotImageData();
        createScreenCaptureWindow(fullscreenImage);
    }

    public void createScreenCaptureWindow(Image screenImage) {
        this.screenImageView.setImage(screenImage);
        stage.setFullScreen(true);
        stage.setAlwaysOnTop(true);
        stage.setFullScreenExitHint("拖动鼠标选择需要截取识别的区域，\n按ESC键可退出截屏选取");
        //stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        rootPane.setPrefWidth(screenWidth);
        rootPane.setPrefHeight(screenHeight);
        rootPane.getChildren().add(screenImageView);
        screenImageView.setFitWidth(screenWidth);
        screenImageView.setFitHeight(screenHeight);

        Scene scene = new Scene(rootPane, screenWidth, screenHeight);
        //rootPane.setStyle("-fx-background-color: black");
        scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                stage.close();
                end.set(true);
            }
        });

        this.addMouseLine();
        scene.setOnMouseMoved(e -> this.drawMouseLine(e.getX(), e.getY()));

        this.addEventListeners();

        stage.setScene(scene);
        stage.show();
    }

    private void addEventListeners() {
        rootPane.setOnMousePressed(e -> {
            dragStartX = e.getX();
            dragStartY = e.getY();
            this.createNewSelectedAreaRectangle();
            this.setLinePosition();
        });
        rootPane.setOnMouseDragged(e -> {
            this.drawMouseLine(e.getX(), e.getY());
            this.drawSelectedAreaRectangle(e.getX(), e.getY());
        });
        rootPane.setOnMouseReleased(e -> {
            dragEndX = e.getX();
            dragEndY = e.getY();
            int clipPositionX = (int) dragStartX;
            int clipPositionY = (int) dragStartY;
            if (selectedAreaWidth < 0) {
                clipPositionX = (int) dragEndX;
            }
            if (selectedAreaHeight < 0) {
                clipPositionY = (int) dragEndY;
            }
            try {
                this.saveImage(clipPositionX, clipPositionY, Math.abs((int) selectedAreaWidth), Math.abs((int) selectedAreaHeight));
                this.runFinalEvent(finalEvent);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                stage.close();
            }
        });
    }

    private void runFinalEvent(Runnable finalEvent) {
        Platform.runLater(finalEvent);
    }

    private void saveImage(int x, int y, int width, int height) {
        try {
            File outputFile = new File(cacheImageFileLocation);
            File outputDir = outputFile.getParentFile();

            if (!outputDir.exists()) {
                outputDir.mkdirs();
                outputFile.createNewFile();
            }
            screenshotTool.saveClippedImage(x, y, width, height, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewSelectedAreaRectangle() {
        rootPane.getChildren().removeAll(Width_Follow, Height_Follow, Width_Fixed, Height_Fixed);

        Width_Follow = new Line();
        Height_Follow = new Line();
        Width_Fixed = new Line();
        Height_Fixed = new Line();

        Width_Follow.setStrokeWidth(5);
        Height_Follow.setStrokeWidth(5);
        Width_Fixed.setStrokeWidth(5);
        Height_Fixed.setStrokeWidth(5);

        rootPane.getChildren().addAll(Width_Follow, Height_Follow, Width_Fixed, Height_Fixed);
    }

    private void setLinePosition() {
        Width_Fixed.layoutXProperty().set(dragStartX);
        Width_Fixed.layoutYProperty().set(dragStartY);

        Height_Fixed.layoutYProperty().set(dragStartY);
        Height_Fixed.layoutXProperty().set(dragStartX);

        Width_Follow.layoutXProperty().set(dragStartX);
        Width_Follow.layoutYProperty().set(dragStartY);

        Height_Follow.layoutXProperty().set(dragStartX);
        Height_Follow.layoutYProperty().set(dragStartY);
    }

    private void addMouseLine() {
        MouseLine_X.layoutXProperty().set(0);
        MouseLine_X.endXProperty().set(screenWidth);

        MouseLine_Y.layoutYProperty().set(0);
        MouseLine_Y.endYProperty().set(screenHeight);

        rootPane.getChildren().addAll(MouseLine_X, MouseLine_Y);
    }

    private void drawMouseLine(double mouseX, double mouseY) {
        MouseLine_X.setLayoutY(mouseY);
        MouseLine_Y.setLayoutX(mouseX);
    }

    private void drawSelectedAreaRectangle(double mouseX, double mouseY) {

        // 通过计算鼠标起始和当前坐标来确定四根线的长度和位置
        selectedAreaWidth = mouseX - dragStartX;
        selectedAreaHeight = mouseY - dragStartY;

        Width_Fixed.endXProperty().set(selectedAreaWidth);
        Height_Fixed.endYProperty().set(selectedAreaHeight);

        Width_Follow.layoutYProperty().set(mouseY);
        Height_Follow.layoutXProperty().set(mouseX);

        Width_Follow.endXProperty().set(selectedAreaWidth);
        Height_Follow.endYProperty().set(selectedAreaHeight);
    }

    public SimpleStringProperty resultProperty() {
        return result;
    }

    public SimpleBooleanProperty endProperty() {
        return end;
    }

    class ScreenshotTool {
        BufferedImage fullscreenBufferedImage;

        public Image getFullScreenshotImageData() {
            try {
                Robot robot = new Robot();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                fullscreenBufferedImage = robot.createScreenCapture(new Rectangle(screenWidth, screenHeight));
                ImageIO.write(fullscreenBufferedImage, "png", byteArrayOutputStream);

                return new Image(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void saveClippedImage(int x, int y, int width, int height, String formateName, File output) throws Exception {
            ImageIO.write(fullscreenBufferedImage.getSubimage(x, y, width, height), formateName, output);
        }
    }
}
