package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.utils.Clipboard;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.utils.ocr.OcrTool;

import java.io.File;
import java.util.logging.Logger;

public class NameManagerPaneController extends VBox {

    public static final ObservableList<String> shownNameList = FXCollections.observableArrayList();
    Data data;
    Pane rootPane;
    OcrTool ocrTool;
    Logger log = Logger.getLogger("NameManagerPaneLOgger");
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

    public NameManagerPaneController(Data data, Pane rootPane, OcrTool ocrTool) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/NameManagerPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.data = data;
        this.rootPane = rootPane;

        shownNameList.setAll(data.getNameList());
        this.nameList.setItems(shownNameList);
        this.nameList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.ocrTool = ocrTool;
    }

    @FXML
    void deleteName(ActionEvent event) {
        new DialogMaker(rootPane).createDialogWithOKAndCancel(
                "问一下",
                "真的要这个(些)名字吗？该操作无法撤销，除非您已经备份了名单。",
                e -> {
                    String[] deletedNames = nameList.getSelectionModel().getSelectedItems().toArray(new String[0]);
                    for (String deletedName : deletedNames) {
                        data.delete(deletedName);
                        shownNameList.remove(deletedName);
                    }
                });
    }

    @FXML
    void deleteAllName(ActionEvent event) {
        new DialogMaker(rootPane).createDialogWithOKAndCancel(
                "问一下",
                "真的要删掉所有名字吗？该操作无法撤销，除非您已经备份了名单。",
                e -> {
                    data.deleteAllName();
                    shownNameList.setAll(data.getNameList());
                });
    }

    @FXML
    void makeMass(ActionEvent event) {
        data.makeMass();
        shownNameList.clear();
        shownNameList.setAll(data.getNameList());
    }

    @FXML
    void exoprtNameList(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("nameList.txt");
        fileChooser.setTitle("想保存到哪？");
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        data.exportNameList(file);
        System.gc();
    }

    @FXML
    void importNameList(ActionEvent event) {
        new DialogMaker(rootPane).createDialogWithOKAndCancel(
                "问一下",
                "导入恢复名单会覆盖当前已有的名单，是否继续？",
                event1 -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("告诉我在哪？");
                    File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

                    data.importNameList(file);
                    shownNameList.clear();
                    shownNameList.setAll(data.getNameList());

                    data.clearNameIgnoreList();
                    data.clearNumberIgnoreList();

                    System.gc();
                });
    }

    @FXML
    void addName(ActionEvent event) {
        if (inputName.getText().equals("")) {
            new DialogMaker(rootPane).createMessageDialog("诶诶诶~", "输入框怎么是空的呢？");
            return;
        }

        data.add(inputName.getText());

        shownNameList.clear();
        shownNameList.setAll(data.getNameList());

        inputName.clear();
        System.gc();
    }

    @FXML
    void addNameFromScreen(ActionEvent event) {
        FXMLLoader fxmlLoader;
        Parent parent;

        Stage stage = new Stage();

        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/OcrPane.fxml"));
            parent = fxmlLoader.load();
        } catch (Exception e) {
            log.warning("Error to load main interface FXML :" + e);
            return;
        }

        Scene scene = new Scene(parent, 515, 604);
        stage.setTitle("Ocr模块");
        stage.setScene(scene);
        log.fine("窗口加载完成");

        OcrPaneController ocrPaneController = fxmlLoader.getController();
        ocrPaneController.setMainStage((Stage) inputName.getScene().getWindow());
        stage.show();
    }

    @FXML
    void paste(ActionEvent event) {
        inputName.setText(inputName.getText() + Clipboard.getClipboardString());
    }
}
