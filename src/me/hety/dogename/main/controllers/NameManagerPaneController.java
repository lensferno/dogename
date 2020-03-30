package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.hety.dogename.main.Common;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.data.NameData;
import me.hety.dogename.main.ocr.Ocr;

import java.io.File;
import java.util.logging.Logger;

public class NameManagerPaneController extends VBox  {

    NameData nameData;
    Pane rootPane;
    Ocr ocrTool;

    Logger log = Logger.getLogger("NameManagerPaneLOgger");

    public static final ObservableList<String> shownNameList = FXCollections.observableArrayList();

    public NameManagerPaneController(NameData nameData, Pane rootPane, Ocr ocrTool){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/NameManagerPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.nameData=nameData;
        this.rootPane=rootPane;

        shownNameList.setAll(nameData.getNameList());
        this.nameList.setItems(shownNameList);

        this.ocrTool=ocrTool;
    }

    @FXML
    private JFXListView<String> nameList;

    @FXML
    private JFXButton deleteAll;

    @FXML
    private JFXButton addName;

    @FXML
    private JFXButton deleteName;

    @FXML
    private JFXTextArea inputName;


    @FXML
    void deleteName(ActionEvent event) {

        new DialogMaker(rootPane).creatDialogWithOKAndCancel(
                "问一下",
                "真的要这个名字吗？该操作无法撤销，除非您已经备份了名单。",
                e -> {
                    nameData.delete(nameList.getSelectionModel().getSelectedItems().get(0));
                    shownNameList.remove(nameList.getSelectionModel().getSelectedItems().get(0));

                    //删除忽略列表中被清除的名字
                    for(int i=0;i<nameData.getIgnoreNameList().size();i++){
                        if(!nameData.getIgnoreNameList().contains(nameData.get(i))){
                            nameData.getIgnoreNameList().remove(nameData.get(i));
                        }
                    }

                    //删除套路列表中被清除的名字
                    nameData.clearTaoluedName();

                    nameData.saveToFile();
                    System.gc();
                });

    }

    @FXML
    void deleteAllName(ActionEvent event) {
        new DialogMaker(rootPane).creatDialogWithOKAndCancel(
                "问一下",
                "真的要删掉所有名字吗？该操作无法撤销，除非您已经备份了名单。",
                e -> {
                    //delete all name
                    nameData.deleteAll();
                    shownNameList.setAll(nameData.getNameList());
                    nameData.saveToFile();
                });

    }

    @FXML
    void makeMass(ActionEvent event) {
        nameData.makeMass();
        shownNameList.clear();
        shownNameList.setAll(nameData.getNameList());
        nameData.saveToFile();
    }

    @FXML
    void exoprtNameList(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("nameList.txt");
        fileChooser.setTitle("想保存到哪？");
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        nameData.exportNameList(file);
        System.gc();
    }

    @FXML
    void importNameList(ActionEvent event) {

        new DialogMaker(rootPane).creatDialogWithOKAndCancel(
                "问一下",
                "导入恢复名单会覆盖当前已有的名单，是否继续？",
                event1 -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("告诉我在哪？");
                    File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

                    nameData.importNameList(file);
                    shownNameList.clear();
                    shownNameList.setAll(nameData.getNameList());

                    nameData.clearNameIgnoreList();
                    nameData.clearNumberIgnoreList();
                    nameData.clearTaoluedName();

                    nameData.saveToFile();
                    System.gc();
        });

    }

    @FXML
    void addName(ActionEvent event) {

        if(inputName.getText().equals("")){
            new DialogMaker(rootPane).creatMessageDialog("诶诶诶~","输入框怎么是空的呢？");
            return;
        }

        nameData.add(inputName.getText());

        shownNameList.clear();
        shownNameList.setAll(nameData.getNameList());

        nameData.saveToFile();
        inputName.clear();
        System.gc();
    }

    @FXML
    void addNameFromScreen(ActionEvent event) {
        FXMLLoader fxmlLoader;
        Parent parent;

        Stage stage=new Stage();

        try{
            fxmlLoader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/OcrPane.fxml"));
            parent=fxmlLoader.load();
        }catch (Exception e){
            log.warning("Error to load main interface FXML :"+e.toString());
            return;
        }

        Scene scene=new Scene(parent,515,604);
        stage.setTitle("Ocr模块");
        stage.setScene(scene);
        log.fine("窗口加载完成");

        OcrPaneController ocrPaneController= fxmlLoader.getController();
        ocrPaneController.setpStage((Stage)inputName.getScene().getWindow());
        stage.show();

    }

    @FXML
    void copyTo(ActionEvent event) {
        inputName.setText(inputName.getText()+ Common.getClipboardString());
    }


}
