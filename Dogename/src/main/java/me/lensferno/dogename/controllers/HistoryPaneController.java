package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.utils.DialogMaker;

import java.util.ArrayList;

public class HistoryPaneController extends VBox {

    History history;

    public static final ObservableList<String> historyListCollection = FXCollections.observableArrayList();
    private final Pane rootPane;

    @FXML
    private JFXListView<String> historyList;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXButton previousBtn;

    @FXML
    private JFXButton nextBtn;

    @FXML
    private Text searchMessage;

    public HistoryPaneController(History history, Pane rootPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/HistoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.history = history;
        this.rootPane = rootPane;

        try {
            loader.load();
            historyListCollection.setAll(history.getHistoryList());
            historyList.setItems(historyListCollection);
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchIndex = 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pointOutSearchResult(int pointer) {
        historyList.scrollTo(pointer);
        historyList.getSelectionModel().select(pointer);
        historyList.getFocusModel().focus(pointer);
    }

    private int searchIndex = 0;

    @FXML
    void upSearch(ActionEvent event) {
        searchMessage.textProperty().set("");

        String searchText = searchBar.getText();
        if (searchText.isEmpty()) {
            return;
        }

        ArrayList<String> historyArrayList = history.getHistoryList();
        if (historyArrayList.isEmpty()) {
            searchIndex = 0;
            return;
        }

        // 逐个查找，直到searchIndex == 0
        while (searchIndex > 0 && !historyArrayList.get(searchIndex).contains(searchText)) {
            searchIndex--;
        }

        pointOutSearchResult(searchIndex);

        // 上移索引，避免原地停留
        searchIndex--;
        if (searchIndex < 0) {
            searchIndex = historyArrayList.size() - 1;
            searchMessage.textProperty().set("到达列表开头，再次点击将从列表尾部重新搜索");
        }
    }

    @FXML
    void downSearch(ActionEvent event) {
        searchMessage.textProperty().set("");

        String searchText = searchBar.getText();
        if (searchText.isEmpty()) {
            return;
        }

        ArrayList<String> historyList = history.getHistoryList();

        if (historyList.isEmpty()) {
            searchIndex = 0;
            return;
        }

        // 逐个查找，直到最后一个元素
        while (searchIndex < historyList.size() - 1 && !historyList.get(searchIndex).contains(searchText)) {
            searchIndex++;
        }

        pointOutSearchResult(searchIndex);

        // 下移索引，避免原地停留
        searchIndex++;
        if (searchIndex >= historyList.size()) {
            searchIndex = 0;
            searchMessage.textProperty().set("到达列表尾部，再次点击将返回列表开头重新搜索");
        }
    }

    @FXML
    void clearHistory() {
        new DialogMaker(rootPane).createDialogWithOKAndCancel("且慢！", "真的要清除全部历史记录吗？", (e) -> {
            this.history.clearHistory();
            historyListCollection.clear();
            searchIndex = 0;
        });
    }

}
