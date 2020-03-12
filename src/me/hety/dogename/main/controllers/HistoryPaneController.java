package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class HistoryPaneController extends VBox {
    public HistoryPaneController(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/HistoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){

        }
    }

    @FXML
    private JFXListView<?> historyList;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXButton previousBtn;

    @FXML
    private JFXButton nextBtn;

    @FXML
    void upSearch(ActionEvent event) {

    }

    @FXML
    void downSearch(ActionEvent event) {

    }

}
