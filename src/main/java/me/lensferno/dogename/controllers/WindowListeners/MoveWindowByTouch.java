package me.lensferno.dogename.controllers.WindowListeners;

import javafx.event.EventHandler;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;

public class MoveWindowByTouch implements EventHandler<TouchEvent> {

    private Stage primaryStage;
    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public MoveWindowByTouch(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public void handle(TouchEvent e) {
        if (e.getEventType() == TouchEvent.TOUCH_PRESSED) {
            this.oldStageX = this.primaryStage.getX();
            this.oldStageY = this.primaryStage.getY();
            this.oldScreenX = e.getTouchPoint().getScreenX();
            this.oldScreenY = e.getTouchPoint().getScreenY();

        } else if (e.getEventType() == TouchEvent.TOUCH_MOVED) {
            this.primaryStage.setX(e.getTouchPoint().getScreenX() - this.oldScreenX + this.oldStageX);
            this.primaryStage.setY(e.getTouchPoint().getScreenY() - this.oldScreenY + this.oldStageY);
        }

    }
}

