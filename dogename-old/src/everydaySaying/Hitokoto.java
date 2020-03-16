
package everydaySaying;

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
import javafx.stage.WindowEvent;
import me.hety.dogename.main.Common;

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
        startTimer(mainPane, topBar);

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

    void startTimer(Pane mainPane, Label topBar){
        Timer timer =new Timer();

        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                try {
                    if(topBar.getScene().getWindow().isFocused()){
                        HitokotoData hit=new Gson().fromJson(Common.getHtml(sayingAPI,true),HitokotoData.class);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                String topText=hit.getHitokoto();
                                topBar.setText(topText+"——"+hit.getFrom());

                            }
                        });
                    }

                }catch(Exception e) {e.printStackTrace();}

            }
        };

        timer.schedule(task,30000,60000);

        mainPane.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                timer.cancel();
            }
        });

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

        /**
         * Type:
         * a	动画
         * b	漫画
         * c	游戏
         * d	文学
         * e	原创
         * f	来自网络
         * g	其他
         * h	影视
         * i	诗词
         * j	网易云
         * k	哲学
         * l	抖机灵
         *
         * @return
         */

        public String getType() {
            switch (type){
                case "a" :
                    type="动画";
                    break;
                case "b" :
                    type="漫画";
                    break;
                case "c" :
                    type="游戏";
                    break;
                case "d" :
                    type="文学";
                    break;
                case "e" :
                    type="原创";
                    break;
                case "f" :
                    type="来自网络";
                    break;
                case "g" :
                    type="其他";
                    break;
                case "h" :
                    type="影视";
                    break;
                case "i" :
                    type="诗词";
                    break;
                case "j" :
                    type="网易云";
                    break;
                case "k" :
                    type="哲学";
                    break;
                case "l" :
                    type="抖机灵";
                    break;
                default:
                    type="未知";

            }
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