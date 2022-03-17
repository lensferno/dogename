package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.lensferno.dogename.configs.ConfigLoader;
import me.lensferno.dogename.configs.GlobalConfig;
import me.lensferno.dogename.configs.MainConfig;
import me.lensferno.dogename.data.Data;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.select.Selector;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.utils.FilePath;
import me.lensferno.dogename.utils.ocr.OcrTool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public final class MainInterfaceController {

    public JFXTextArea message;

    OcrTool ocrTool = null;

    History history = new History();
    Random random = new Random();
    Data data = new Data();
    Selector selector = new Selector();

    @FXML
    private Pane rootPane;
    @FXML
    private JFXRadioButton nameChoose;
    @FXML
    private JFXButton showHistoryBtn;
    @FXML
    private JFXRadioButton numbChoose;
    @FXML
    private JFXButton anPaiBtn;
    @FXML
    private Pane mainPane;
    @FXML
    private Label topBar;
    @FXML
    private JFXButton showNameMangerButton;
    @FXML
    private Label upperLabel;
    @FXML
    private ImageView mainView;
    @FXML
    private JFXButton miniModeBtn;
    @FXML
    private Label downLabel;

    public MainInterfaceController() {
        history.loadHistory();
    }

    private final ToggleGroup toggleGroup = new ToggleGroup();
    public void bindProperties() {
        nameChoose.setToggleGroup(toggleGroup);
        numbChoose.setToggleGroup(toggleGroup);

        toggleGroup.selectToggle(GlobalConfig.mainConfig.getChooseMethod() == MainConfig.METHOD_NAME ? nameChoose : numbChoose);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            GlobalConfig.mainConfig.setChooseMethod(newValue == nameChoose ? MainConfig.METHOD_NAME : MainConfig.METHOD_NUMBER);
            System.out.println("change!"+(newValue == nameChoose));
        });
    }

    public void setImg(InputStream stream) {
        mainView.setImage(new Image(stream));
    }

    public void setUpConfig(ConfigLoader configLoader) {
        configLoader.readMainConfig(FilePath.toSpecificPathForm("files/Config.json"));
        configLoader.loadVoiceConfig(FilePath.toSpecificPathForm("files/VoiceConfig.json"));
    }

    @FXML
    void showProgramInfo(ActionEvent event) {
        new DialogMaker(rootPane).createDialogWithOneBtn("程序信息", new ProgramInfoPaneController(rootPane));
    }

    @FXML
    void showNameManger(ActionEvent event) {

        if (selector.isWorkerRunning()) {
            new DialogMaker(rootPane).createMessageDialog("(・。・)", "安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NameManagerPaneController nameManagerPaneController = new NameManagerPaneController(data, rootPane, ocrTool);
        new DialogMaker(rootPane).createDialogWithOneBtn("名单管理", nameManagerPaneController);
    }

    @FXML
    void showNumberSetting(ActionEvent event) {

        if (selector.isWorkerRunning()) {
            new DialogMaker(rootPane).createMessageDialog("(・。・)", "安排中......\n为保证运行的稳定，此时还不能进行该操作哦。");
            return;
        }
        NumberSettingsPaneController numberSettingsPaneController = new NumberSettingsPaneController(data);
        numberSettingsPaneController.bindProperties(GlobalConfig.mainConfig);
        new DialogMaker(rootPane).createDialogWithOneBtn("调整数字", numberSettingsPaneController);
    }

    @FXML
    void miniMode(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/MiniPane.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene miniScene = new Scene(parent, 300, 134);
        Stage miniStage = new Stage();
        miniStage.setScene(miniScene);
        miniStage.initStyle(StageStyle.UNDECORATED);

        MiniPaneController miniPaneController = loader.getController();
        miniPaneController.setBase(data, selector);

        Stage currentStage = (Stage) anPaiBtn.getScene().getWindow();
        miniPaneController.setOldStage(currentStage);

        miniPaneController.setCurrentStage(miniStage);
        miniPaneController.setCurrentScene(miniScene);

        miniPaneController.setListeners();
        miniPaneController.setOldTextProperties(upperLabel.textProperty(), downLabel.textProperty());

        miniStage.show();
        currentStage.hide();
    }

    @FXML
    void showSettings(ActionEvent event) {
        SettingsPaneController settingsPaneController = new SettingsPaneController();

        settingsPaneController.setToggleGroup();
        settingsPaneController.bindProperties();

        settingsPaneController.setRootPane(rootPane);

        settingsPaneController.setData(data);

        new DialogMaker(rootPane).createDialogWithOneBtn("更多设置", settingsPaneController);
    }

    @FXML
    void showHistory(ActionEvent event) {

        HistoryPaneController historyPaneController = new HistoryPaneController(history, rootPane);

        new DialogMaker(rootPane).createDialogWithOneBtn("历史记录", historyPaneController);
    }

    public void init() {

        selector.initialVariable(data, history, upperLabel.textProperty(), downLabel.textProperty());
        selector.addStoppedEventListener((observableValue, oldValue, stop) -> {
            if (stop) {
                anPaiBtn.setText("安排一下");
                nameChoose.setDisable(false);
                numbChoose.setDisable(false);
            } else {
                anPaiBtn.setText("不玩了！");
                nameChoose.setDisable(true);
                numbChoose.setDisable(true);
            }
        });
    }

    @FXML
    void anPai() {

        if (selector.isWorkerRunning()) {
            selector.forceStop();
            anPaiBtn.setText("安排一下");
            return;
        }

        if (GlobalConfig.mainConfig.getRandomCount()) {
            GlobalConfig.mainConfig.setMaxTotalCount(100 + random.nextInt(151));
        }

        if (GlobalConfig.mainConfig.getChooseMethod() == MainConfig.METHOD_NAME) {
            runNameMode();
        } else {
            runNumberMode();
        }

    }

    public void setToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();
        nameChoose.setToggleGroup(toggleGroup);
        numbChoose.setToggleGroup(toggleGroup);
    }

    private void runNameMode() {

        if (data.isEmpty()) {
            new DialogMaker(rootPane).createMessageDialog("哦霍~", "现在名单还是空的捏~请前往名单管理添加名字 或 使用数字挑选法。");
            return;
        }

        if (data.compareNameIgnoreList() && GlobalConfig.mainConfig.getIgnoreSelectedResult()) {

            if (GlobalConfig.mainConfig.getEqualMode()) {
                new DialogMaker(rootPane).createDialogWithOKAndCancel("啊？", "全部名字都被点完啦！\n要把名字的忽略列表重置吗？", e -> data.clearNameIgnoreList());
            } else {
                new DialogMaker(rootPane).createMessageDialog("啊？", "全部名字都被点完啦！\n请多添加几个名字 或 点击“机会均等”的“重置”按钮。");
            }
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }

    private void runNumberMode() {

        try {

            int minNumber = Integer.parseInt(GlobalConfig.mainConfig.getMinNumber());
            int maxNumber = Integer.parseInt(GlobalConfig.mainConfig.getMaxNumber());

            if (maxNumber - minNumber <= 0) {
                new DialogMaker(rootPane).createMessageDialog("嗯哼？", "数字要前小后大啊~");
                return;
            }

            if (data.getNumberIgnoreListSize() >= (maxNumber - minNumber + 1) && GlobalConfig.mainConfig.getIgnoreSelectedResult()) {
                if (GlobalConfig.mainConfig.getEqualMode()) {
                    new DialogMaker(rootPane).createDialogWithOKAndCancel("啊？", "全部数字都被点完啦！\n要把数字的忽略列表重置吗？", e -> data.clearNumberIgnoreList());
                } else {
                    new DialogMaker(rootPane).createMessageDialog("啊？", "全部数字都被点完啦！\n请扩大数字范围 或 点击“机会均等”的“重置”按钮。");
                }
                return;
            }

        } catch (Exception e) {
            new DialogMaker(rootPane).createMessageDialog("嗯哼？", "输入个有效的数字啊~");
            return;
        }

        anPaiBtn.setText("不玩了！");

        selector.run();
    }

    public Label getTopBar() {
        return topBar;
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public MainConfig getMainConfig() {
        return GlobalConfig.mainConfig;
    }
}
