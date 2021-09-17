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
import me.lensferno.dogename.data.History;
import me.lensferno.dogename.utils.DialogMaker;

public class HistoryPaneController extends VBox {

    public static final ObservableList<String> shownHistoryList = FXCollections.observableArrayList();
    History history;
    Pane rootPane;
    int pointer = 0;
    @FXML
    private JFXListView<String> historyList;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXButton previousBtn;

    @FXML
    private JFXButton nextBtn;

    public HistoryPaneController(History history, Pane rootPane) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/HistoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.history = history;
        this.rootPane = rootPane;

        try {
            loader.load();
            shownHistoryList.setAll(history.getHistoryList());
            historyList.setItems(shownHistoryList);
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> pointer = 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pointOutSearchResult(int pointer) {
        historyList.getSelectionModel().select(pointer);
    }

    @FXML
    void upSearch(ActionEvent event) {

        String searchText = searchBar.getText();
        String[] historyArrayList = history.getHistoryList().toArray(new String[0]);

        if (historyArrayList.length == 0) {
            return;
        }

        if (pointer > historyArrayList.length - 1 || pointer < 0) {
            pointer = historyArrayList.length - 1;
        }

        while (!historyArrayList[pointer].contains(searchText)) {
            pointer--;
            if (pointer < 0) {
                pointer = historyArrayList.length - 1;
                return;
            }
        }
        pointOutSearchResult(pointer);
        pointer--;
        if (pointer < 0) {
            pointer = historyArrayList.length - 1;
        }

    }

    @FXML
    void downSearch(ActionEvent event) {

        String searchText = searchBar.getText();
        String[] historyArrayList = history.getHistoryList().toArray(new String[0]);

        if (historyArrayList.length == 0) {
            return;
        }

        if (pointer > historyArrayList.length - 1 || pointer < 0) {
            pointer = 0;
        }

        while (!historyArrayList[pointer].contains(searchText)) {
            pointer++;
            if (pointer < historyArrayList.length - 1) {
                pointer = 0;
                return;
            }
        }
        pointOutSearchResult(pointer);
        pointer++;
        if (pointer < historyArrayList.length - 1) {
            pointer = 0;
        }

    }

    @FXML
    void clearHistory() {
        new DialogMaker(rootPane).createDialogWithOKAndCancel("且慢！", "真的要清除全部历史记录吗？", (e) -> {
            this.history.clearHistory();
            pointer = 0;
        });
    }

}
