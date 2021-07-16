package me.lensferno.dogename.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.lensferno.dogename.DataReleaser;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.utils.IOUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ProgramInfoPaneController extends VBox {

    @FXML
    public ImageView dogeView;

    Pane rootPane;

    public ProgramInfoPaneController(Pane rootPane){
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/me/lensferno/dogename/FXMLs/ProgramInfoPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch(Exception e){
            e.printStackTrace();
        }
        Image dogeImage=new Image(DataReleaser.getDogenameStream());
        dogeView.setImage(dogeImage);
        this.rootPane=rootPane;
    }

    @FXML
    void showLicense(ActionEvent event) {
        TextArea textArea=new TextArea(IOUtil.inputStreamToString(getClass().getResourceAsStream("/gpl-3.0.txt"), StandardCharsets.UTF_8));
        textArea.setFont(Font.font("Microsoft YaHei",14));
        textArea.setMinWidth(600);
        textArea.setPrefHeight(400);
        textArea.setEditable(false);
        new DialogMaker(rootPane).createDialogWithOneBtn("开源协议（GPL v3）",textArea);
    }

    @FXML
    void showLibLicense(ActionEvent event) {
        TextArea textArea=new TextArea(IOUtil.inputStreamToString(getClass().getResourceAsStream("/LibLicense.txt"), StandardCharsets.UTF_8));
        textArea.setFont(Font.font("Microsoft YaHei",14));
        textArea.setMinWidth(600);
        textArea.setPrefHeight(400);
        textArea.setEditable(false);
        new DialogMaker(rootPane).createDialogWithOneBtn("其他开源许可",textArea);
    }

    @FXML
    void showHelp(ActionEvent event) {

        JFXButton YesButton = new JFXButton("好的～去吧去吧");
        YesButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,14));
        YesButton.setPrefWidth(160);
        YesButton.setPrefHeight(40);
        YesButton.addEventHandler(ActionEvent.ACTION,e -> jumpToHelp());

        JFXButton cancelButton = new JFXButton("算了算了");
        cancelButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,14));
        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(40);

        Text messageText=new Text("即将跳转到本程序Github页面上的使用帮助，是否继续？");
        messageText.setFont(Font.font("Microsoft YaHei",14));

        new DialogMaker(rootPane).createDialog("查看帮助",messageText,cancelButton,YesButton);

    }

    private void jumpToHelp(){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/lensferno/dogename/blob/main/res/usage.md"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewCode(ActionEvent event) {
        JFXButton githubButton = new JFXButton("前往Github查看");
        githubButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,14));
        githubButton.setPrefWidth(150);
        githubButton.setPrefHeight(40);
        githubButton.addEventHandler(ActionEvent.ACTION,e -> jumpToGithub());

        JFXButton giteeButton = new JFXButton("前往Gitee查看");
        giteeButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,14));
        giteeButton.setPrefWidth(150);
        giteeButton.setPrefHeight(40);
        giteeButton.addEventHandler(ActionEvent.ACTION,e -> jumpToGitee());

        JFXButton cancelButton = new JFXButton("哪都不去");
        cancelButton.setFont(Font.font("Microsoft YaHei", FontWeight.BOLD,14));
        cancelButton.setPrefWidth(100);
        cancelButton.setPrefHeight(40);

        Text messageText=new Text("Dogename在Github和码云(Gitee)都发布有代码和介绍。\n您想去哪里？\nGithub：将跳转到https://github.com/lensferno/dogename\nGitee：将跳转到https://gitee.com/lensferno/dogename");
        messageText.setFont(Font.font("Microsoft YaHei",14));

        new DialogMaker(rootPane).createDialog("查看源代码",messageText,cancelButton,githubButton,giteeButton);

    }

    private void jumpToGithub(){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/lensferno/dogename"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void jumpToGitee(){
        try {
            Desktop.getDesktop().browse(new URI("https://gitee.com/lensferno/dogename"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
