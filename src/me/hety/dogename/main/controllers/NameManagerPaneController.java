package me.hety.dogename.main.controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class NameManagerPaneController extends VBox  {

    public NameManagerPaneController(){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/hety/dogename/main/FXMLs/NameManagerPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private  JFXListView nameList;

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

    }

    @FXML
    void deleteAllName(ActionEvent event) {

    }

    @FXML
    void makeAMass(ActionEvent event) {

    }

    @FXML
    void exoprtNameList(ActionEvent event) {

    }

    @FXML
    void importNameList(ActionEvent event) {

    }

    @FXML
    void addName(ActionEvent event) {

    }



}
