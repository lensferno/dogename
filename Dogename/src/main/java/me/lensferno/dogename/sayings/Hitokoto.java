package me.lensferno.dogename.sayings;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import me.lensferno.dogename.utils.DialogMaker;
import me.lensferno.dogename.controllers.HitokotoPaneController;
import me.lensferno.dogename.utils.NetworkUtil;

public class Hitokoto {


    private final String HITOKOTO_API = "https://v1.hitokoto.cn/";

    String hitokotoJSON = null;

    private String getHitokoto() {
        return NetworkUtil.getHtml(HITOKOTO_API);
    }

    public void showHitokoto(Pane rootPane, Label topBar, boolean showOnDialog) {

        new Thread(() -> {

            //hitokotoJSON = getHitokoto();

            String hitokoto, from, author, creator, type;

            if ((hitokotoJSON = getHitokoto()) != null) {
                HitokotoData hitokotoData = new Gson().fromJson(hitokotoJSON, HitokotoData.class);

                hitokoto = hitokotoData.getHitokoto();
                from = hitokotoData.getFrom();
                author = hitokotoData.getAuthor();
                creator = hitokotoData.getCreator();
                type = hitokotoData.getType();

                Platform.runLater(() -> {
                    topBar.setText(String.format("《%s》：%s (%s)", from, hitokoto, author));
                    if (showOnDialog) {
                        HitokotoPaneController hitokotoPaneController = new HitokotoPaneController(hitokoto, from, author, creator, type);
                        new DialogMaker(rootPane).createDialogWithOneBtn("每日一言", hitokotoPaneController);
                    }
                });
            }
        }).start();
    }

    static class HitokotoData {

        private int id;
        private String hitokoto;
        private String type;
        private String from;

        @SerializedName("from_who")
        private String author;

        private String creator;

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
            switch (type) {
                case "a":
                    type = "动画";
                    break;
                case "b":
                    type = "漫画";
                    break;
                case "c":
                    type = "游戏";
                    break;
                case "d":
                    type = "文学";
                    break;
                case "e":
                    type = "原创";
                    break;
                case "f":
                    type = "来自网络";
                    break;
                case "g":
                    type = "其他";
                    break;
                case "h":
                    type = "影视";
                    break;
                case "i":
                    type = "诗词";
                    break;
                case "j":
                    type = "网易云";
                    break;
                case "k":
                    type = "哲学";
                    break;
                case "l":
                    type = "抖机灵";
                    break;
                default:
                    type = "未知";

            }
            return type;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getFrom() {
            return from;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor() {
            return author;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreator() {
            return creator;
        }

    }
}
