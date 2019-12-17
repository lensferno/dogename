
package main.everydaySaying;

import com.google.gson.Gson;
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
import main.Common;
import main.GuShiCi;

import java.util.Timer;
import java.util.TimerTask;

public class Hitokoto {

    String sayingAPI="https://v1.hitokoto.cn/";

    public void showSaying(Pane mainPane, Label topBar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HitokotoData hit=new Gson().fromJson(Common.getHtml(sayingAPI,true),HitokotoData.class);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String topText=hit.getHitokoto();
                            showSaying(mainPane,"每日一言·Hitokoto",hit);
                            topBar.setText(topText+"——"+hit.getFrom());
                        }
                    });
                }catch(Exception e) {e.printStackTrace();}
            }
        }).start();
        startTime(mainPane, topBar);
    }

    private void showSaying(Pane mainPane,String header, HitokotoData hit) {
        VBox vb =new VBox();
        vb.setPrefHeight(200);
        vb.setPrefWidth(300);
        Text contentText =new Text("『 "+hit.getHitokoto()+" 』");
        contentText.setFont(new Font("BLOOD",30));
        contentText.setTextAlignment(TextAlignment.CENTER);
        contentText.setWrappingWidth(700);
        Text authorText =new Text("\n\n出自："+hit.getFrom()+"  |  上传者："+hit.getCreator()+"");
        authorText.setTextAlignment(TextAlignment.RIGHT);
        authorText.setFont(new Font(14));
        Text categoryText =new Text("\n\n\n#"+hit.getType());
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

    void startTime(Pane mainPane, Label topBar){
        Timer timer =new Timer();

        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                try {
                    HitokotoData hit=new Gson().fromJson(Common.getHtml(sayingAPI,true),HitokotoData.class);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            String topText=hit.getHitokoto();
                            topBar.setText(topText+"——"+hit.getFrom());
                        }
                    });

                }catch(Exception e) {e.printStackTrace();}
            }
        };

        timer.schedule(task,30000,60000);

    }


    class HitokotoData{

        private int id;
        private String hitokoto;
        private String type;
        private String from;
        private String creator;
        private String created_at;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setHitokoto(String hitokoto) {
            this.hitokoto = hitokoto;
        }
        public String getHitokoto() {
            return hitokoto;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setFrom(String from) {
            this.from = from;
        }
        public String getFrom() {
            return from;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }
        public String getCreator() {
            return creator;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
        public String getCreated_at() {
            return created_at;
        }
    }


}