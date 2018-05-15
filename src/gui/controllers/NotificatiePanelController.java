package gui.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author sam
 */
public class NotificatiePanelController extends AnchorPane {

    @FXML
    private Label notificatieLabel;

    @FXML
    private VBox vbox;

    public NotificatiePanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/NotificatiePanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setStyle("-fx-background-color:rgba(0,0,0,0);");
        vbox.setStyle("-fx-background-color:rgba(0,0,0,0);");

    }

    public NotificatiePanelController(String notificatieText, String color) {
        this();
        setNotificatieText(notificatieText);
        setNotificationColor(color);
    }

    public void setNotificatieText(String notificatieText) {
        notificatieLabel.setText(notificatieText);
    }

    public void setNotificationColor(String color) {
        vbox.toFront();
        toFront();
        notificatieLabel.getStyleClass().add("notificatie");
        notificatieLabel.setStyle(String.format("-fx-background-color:%s;", color));
    }

}
