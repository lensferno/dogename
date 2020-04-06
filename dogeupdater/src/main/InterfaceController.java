package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import main.update.ResourcesType;
import main.update.Updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

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
    void doUpdate(ActionEvent event) {
        new Thread(()->{
            updater=new Updater();
            if(updater.checkUpdate(whichResources,3)){
                updateProgram();
            }else{
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("哟~");
                    alert.setHeaderText("");
                    alert.setContentText("已经是最新版啦！");
                    alert.showAndWait();
                });

            }
        }).start();


    }

    private Updater updater;

    private int whichResources=0;

    private SimpleBooleanProperty githubResources=new SimpleBooleanProperty(false);

    private SimpleBooleanProperty backupOldJar=new SimpleBooleanProperty(true);

    private SimpleBooleanProperty openOnFinished=new SimpleBooleanProperty(true);

    private SimpleBooleanProperty checkUpdateFile=new SimpleBooleanProperty(true);

    public void init(){
        ToggleGroup toggleGroup=new ToggleGroup();
        githubBtn.setToggleGroup(toggleGroup);
        giteeBtn.setToggleGroup(toggleGroup);

        githubBtn.selectedProperty().bindBidirectional(githubResources);

        githubResources.addListener((t,oldValue,newValue)->{
            if(newValue==true){
                whichResources = ResourcesType.GITHUB;
            }else{
                whichResources = ResourcesType.GITEE;
            }
        });

        backupBtn.selectedProperty().bindBidirectional(backupOldJar);
        openOnFinishedBtn.selectedProperty().bindBidirectional(openOnFinished);
        checkBtn.selectedProperty().bindBidirectional(checkUpdateFile);

    }

    private String javaPid;
    private String oldJarPath;
    private String updateInfoJson;

    public void setArgs(String[] args){
        this.javaPid=args[0];
        this.oldJarPath =args[1];
        this.updateInfoJson =args[2];
    }

    void updateProgram(){

        topImg.setVisible(true);
        killOldProgram();

        if(backupOldJar.get()){
            boolean backupSucceed=backupOldFile(oldJarPath);

            if (!backupSucceed) {
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("备份失败");
                    alert.setHeaderText("备份过程看起来好像失败了。");
                    alert.setContentText("要继续进行升级工作吗？");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.CANCEL){
                        return;
                    }
                });

            }
        }

        boolean downloadSucceed=downloadUpdateFile(updateInfoJson);

        if (downloadSucceed){

            boolean replaceOldJarFileSucceed=replaceOldJar(oldJarPath);

            if (!replaceOldJarFileSucceed){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("啊呀！");
                    alert.setHeaderText("出问题啦！");
                    alert.setContentText("替换旧程序文件时失败QAQ");
                    alert.showAndWait();

                });
                return;
            }

        }else{
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("啊呀！");
                alert.setHeaderText("出问题啦！");
                alert.setContentText("下载升级文件时失败QAQ");
                alert.showAndWait();
            });

            return;
        }

        if(openOnFinished.get()) {
            runNewProgram();
        }

    }

    private boolean replaceOldJar(String oldJarFilePath){
        return true;
    }

    private void runNewProgram(){

    }

    private void killOldProgram(){
        try {
            Runtime.getRuntime().exec("taskkill /PID"+javaPid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean downloadUpdateFile(String url){
        //String[] fileUrls=url.split(",");
        return false;
    }

    private boolean backupOldFile(String oldJarFilePath){
        //File oldJarFile=new File(filePath);
        File oldJarFile=new File(oldJarFilePath);
        File backupJarFile=new File(oldJarFile.getParent()+"dogename_backup.jar");

        try{
            Files.copy(oldJarFile.toPath(),backupJarFile.toPath());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

}