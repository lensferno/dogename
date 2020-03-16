package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.data.History;

import java.util.ArrayList;

public class HistoryPaneController extends VBox {
    History history;

    public static final ObservableList<String> shownHistoryList = FXCollections.observableArrayList();

    public HistoryPaneController(History history){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/HistoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.history=history;

        try {
            loader.load();
            shownHistoryList.setAll(history.getHistoryList());
            historyList.setItems(shownHistoryList);
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> currentPois=0);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private JFXListView<String> historyList;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXButton previousBtn;

    @FXML
    private JFXButton nextBtn;

    int currentPois=0;
    @FXML
    void upSearch(ActionEvent event) {
        String[] historyArrayList =history.getHistoryList().toArray(new String[0]);

        for(;currentPois>0;currentPois--){
            if (currentPois>=historyArrayList.length){
                currentPois=0;
            }
            if(historyArrayList[currentPois].contains(searchBar.getText())){
                historyList.getSelectionModel().select(currentPois);
            }
        }
    }

    @FXML
    void downSearch(ActionEvent event) {

        String[] historyArrayList =history.getHistoryList().toArray(new String[0]);

        for(;currentPois<historyArrayList.length;currentPois++){
            if(historyArrayList[currentPois].contains(searchBar.getText())){
                historyList.getSelectionModel().select(currentPois);
            }
        }

    }





}
