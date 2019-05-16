package main;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;

public class MoveWindow implements EventHandler<MouseEvent> {

    private Stage primaryStage;
    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public MoveWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.oldStageX = this.primaryStage.getX();
            this.oldStageY = this.primaryStage.getY();
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();

        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            this.primaryStage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            this.primaryStage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);
        }

    }
}
