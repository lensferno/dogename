package main;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.update.ResourcesType;
import main.update.Updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class InterfaceController {

    @FXML
    private JFXCheckBox backupBtn;

    @FXML
    private JFXCheckBox openOnFinishedBtn;

    @FXML
    private JFXRadioButton githubBtn;

    @FXML
    private JFXRadioButton giteeBtn;

    @FXML
    private JFXButton startUpdate;

    @FXML
    private ImageView topImg;

    @FXML
    private JFXCheckBox checkBtn;

    @FXML
    private JFXTabPane settingsPane;

    @FXML
    private JFXTextArea infoTextArea;

    @FXML
    private Tab infoTab;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void doUpdate() {
        progress.set(0);
        settingsPane.getSelectionModel().select(infoTab);
        infoTextArea.setText("检查可用更新...\n");
        new Thread(() -> {
            updater = new Updater();
            if (updater.checkUpdate(whichResources, currentVer)) {
                updateProgram();
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("哟~");
                    alert.setHeaderText("");
                    alert.setContentText("已经是最新版啦！");
                    alert.showAndWait();
                });

            }
        }).start();
    }

    public void doUpdateAutomatically() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                doUpdate();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 5000);

    }

    private Updater updater;

    private int whichResources = 0;

    private SimpleBooleanProperty githubResources = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty backupOldJar = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty openOnFinished = new SimpleBooleanProperty(true);

    private SimpleBooleanProperty checkUpdateFile = new SimpleBooleanProperty(true);

    private SimpleStringProperty infoText = new SimpleStringProperty();

    private SimpleDoubleProperty progress = new SimpleDoubleProperty(0);

    public void init() {
        ToggleGroup toggleGroup = new ToggleGroup();
        githubBtn.setToggleGroup(toggleGroup);
        giteeBtn.setToggleGroup(toggleGroup);

        githubBtn.selectedProperty().bindBidirectional(githubResources);

        githubResources.addListener((t, oldValue, newValue) -> {
            if (newValue == true) {
                whichResources = ResourcesType.GITHUB;
            } else {
                whichResources = ResourcesType.GITEE;
            }
        });

        backupBtn.selectedProperty().bindBidirectional(backupOldJar);
        openOnFinishedBtn.selectedProperty().bindBidirectional(openOnFinished);
        checkBtn.selectedProperty().bindBidirectional(checkUpdateFile);
        infoTextArea.textProperty().bindBidirectional(infoText);
        progressBar.progressProperty().bindBidirectional(progress);
    }

    private String javaPid;
    private String oldJarPath;
    private String updateInfoJson;
    private String saveLocation;
    private int currentVer;

    public void setArgs(String[] args) {
        this.javaPid = args[0];
        this.oldJarPath = args[1];
        this.currentVer = Integer.parseInt(args[2]);
        this.saveLocation = new File(oldJarPath).getParent() + "\\";
    }

    void updateProgram() {

        Platform.runLater(() -> {
            topImg.setVisible(true);
            infoText.set(infoText.get() + "杀死程序进程...\n");
        });

        killOldProgram();

        if (backupOldJar.get()) {

            Platform.runLater(() -> infoText.set(infoText.get() + "备份原程序...\n"));
            boolean backupSucceed = backupOldFile(oldJarPath);

            if (!backupSucceed) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("备份失败");
                    alert.setHeaderText("备份过程看起来好像失败了。");
                    alert.setContentText("要继续进行升级工作吗？");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.CANCEL) {
                        return;
                    }
                });
            }
        }


        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                boolean downloadSucceed = downloadUpdateFile();

                if (downloadSucceed) {
                    succeeded();
                } else {
                    failed();
                }

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Done!");

                Platform.runLater(() -> infoText.set(infoText.get() + "替换旧的程序文件...\n"));
                boolean replaceOldJarFileSucceed = replaceOldJar(oldJarPath);

                if (!replaceOldJarFileSucceed) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("啊呀！");
                        alert.setHeaderText("出问题啦！");
                        alert.setContentText("替换旧程序文件时失败QAQ");
                        alert.showAndWait();

                    });
                }

                if (openOnFinished.get()) {
                    runNewProgram();
                }

            }

            @Override
            protected void cancelled() {
                super.cancelled();
                updateMessage("Cancelled!");
            }

            @Override
            protected void failed() {
                super.failed();
                updateMessage("Failed!");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("啊呀！");
                    alert.setHeaderText("出问题啦！");
                    alert.setContentText("下载升级文件时失败QAQ");
                    alert.showAndWait();
                });
            }
        };

        Platform.runLater(() -> infoText.set(infoText.get() + "下载更新包...\n"));

        new Thread(downloadTask).start();
    }

    private boolean replaceOldJar(String oldJarFilePath) {
        return true;
    }


    private void runNewProgram() {

    }

    private String info = null;

    private void killOldProgram() {
        try {
            Process killProcess = Runtime.getRuntime().exec(String.format("taskkill /PID %s", javaPid));
            BufferedReader reader = new BufferedReader(new InputStreamReader(killProcess.getInputStream(), StandardCharsets.UTF_8));
            new Thread(() -> {
                try {
                    while ((info = reader.readLine()) != null) {
                        Platform.runLater(() -> infoText.set(infoText.get() + info + '\n'));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean downloadUpdateFile() {
        SimpleStringProperty textPerproty = new SimpleStringProperty();
        textPerproty.bindBidirectional(infoTextArea.textProperty());
        return updater.doUpdate(saveLocation, whichResources, checkUpdateFile.get(), textPerproty, progress);
    }

    private boolean backupOldFile(String oldJarFilePath) {
        //File oldJarFile=new File(filePath);
        File oldJarFile = new File(oldJarFilePath);
        File backupJarFile = new File(oldJarFile.getParent() + "dogename_backup.jar");

        try {
            Files.copy(oldJarFile.toPath(), backupJarFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getInfoText() {
        return infoText.get();
    }

    public SimpleStringProperty infoTextProperty() {
        return infoText;
    }
}