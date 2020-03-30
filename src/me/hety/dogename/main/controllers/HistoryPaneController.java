package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.hety.dogename.main.DialogMaker;
import me.hety.dogename.main.data.History;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HistoryPaneController extends VBox {
    Logger log= LogManager.getLogger(HitokotoPaneController.class);

    History history;
    Pane rootPane;

    public static final ObservableList<String> shownHistoryList = FXCollections.observableArrayList();

    public HistoryPaneController(History history, Pane rootPane){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/HistoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.history=history;
        this.rootPane=rootPane;

        try {
            loader.load();
            shownHistoryList.setAll(history.getHistoryList());
            historyList.setItems(shownHistoryList);
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> currentPois=0);

        }catch(Exception e){
            log.error("Error in loading history Fxml:"+e.toString());
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

        if(currentPois<0||currentPois>historyArrayList.length){
            currentPois=0;
            log.debug("Search pois set to 0.");
        }

        for(;currentPois>0;currentPois--){
            if (currentPois>=historyArrayList.length){
                currentPois=0;
                log.debug("Search pois set to 0.");
            }
            if(historyArrayList[currentPois].contains(searchBar.getText())){
                historyList.getSelectionModel().select(currentPois);
                log.debug("Search to :"+currentPois);
            }
        }
    }

    @FXML
    void downSearch(ActionEvent event) {

        String[] historyArrayList =history.getHistoryList().toArray(new String[0]);

        if(currentPois<0||currentPois>historyArrayList.length){
            currentPois=0;
            log.debug("Search pois set to 0.");
        }

        for(;currentPois<historyArrayList.length;currentPois++){
            if(historyArrayList[currentPois].contains(searchBar.getText())){
                log.debug("Search to :"+currentPois);
                historyList.getSelectionModel().select(currentPois);
            }
        }

    }

    @FXML
    void clearHistory(){
        new DialogMaker(rootPane).creatDialogWithOKAndCancel("且慢！","真的要清除全部历史记录吗？",(e)->this.history.clearHistory());
    }

}
