package me.hety.dogename.main.everydaySaying;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import me.hety.dogename.main.GuShiCi;

public class Gushici {
    public void showShiCi(Pane mainPane, Label topBar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GuShiCi gsc=new GuShiCi();
                    String shici =gsc.get();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String topText=gsc.getShiciContent();
                            showShiCi(mainPane,"每日古诗词",gsc);
                            topBar.setText(topText+"——"+gsc.getAuthor()+"《"+gsc.getOrigin()+"》");
                        }
                    });
                }catch(Exception e) {e.printStackTrace();}
            }
        }).start();
    }

    private void showShiCi(Pane mainPane,String header, GuShiCi gsc) {
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        Text contentText =new Text("“"+gsc.getShiciContent()+"”");
        contentText.setFont(new Font("BLOOD",30));
        contentText.setTextAlignment(TextAlignment.CENTER);
        Text authorText =new Text("\n\n——"+gsc.getAuthor()+"《"+gsc.getOrigin()+"》");
        authorText.setTextAlignment(TextAlignment.RIGHT);
        authorText.setFont(new Font(14));
        Text categoryText =new Text("\n\n\n#"+gsc.getCategory());
        categoryText.setFont(new Font(12));
        categoryText.setTextAlignment(TextAlignment.RIGHT);
        vb.getChildren().addAll(contentText,authorText,categoryText);
        vb.setAlignment(Pos.CENTER);

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        Text text =new Text("");
        text.setFont(new Font(14));
        text.setTextAlignment(TextAlignment.CENTER);
        content.setBody(vb);
        StackPane tempPane=new StackPane();
        tempPane.setPrefHeight(mainPane.getPrefHeight());
        tempPane.setPrefWidth(mainPane.getPrefWidth());
        mainPane.getChildren().add(tempPane);
        JFXDialog dialog = new JFXDialog(tempPane,content,JFXDialog.DialogTransition.TOP);
        dialog.setPrefHeight(mainPane.getPrefHeight());
        dialog.setPrefWidth(mainPane.getPrefWidth());
        JFXButton button = new JFXButton("已阅");
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                mainPane.getChildren().remove(tempPane);
            }
        });
        button.setPrefWidth(50);
        button.setPrefHeight(30);
        button.setOnAction((ActionEvent e) -> {
            dialog.close();

        });
        content.setActions(button);

        dialog.show();
    }
}
