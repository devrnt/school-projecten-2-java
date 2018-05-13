package gui.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sam
 */
public class NotificatiePanelController extends AnchorPane {

    @FXML
    private Label notificatieLabel;

    public NotificatiePanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/NotificatiePanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public NotificatiePanelController(String notificatieText, String color) {
        this();
        setNotificatieText(notificatieText);
        setNotificationColor(color);
    }
    
    public void setNotificatieText(String notificatieText){
        notificatieLabel.setText(notificatieText);
    }
    
    public void setNotificationColor(String color){
        notificatieLabel.setStyle(String.format("-fx-background-color:%s;-fx-background-radius:5;", color));
    }

}
