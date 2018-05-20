/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author devri
 */
public class AlertCS {

    private Alert alert;

    public AlertCS(Alert.AlertType type) {
        // normal flow of creating an alert
        alert = new Alert(type);
        //alert.initStyle(StageStyle.UNDECORATED);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // title bar icon
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/main/iconBlue128x128.png")),
                new Image(getClass().getResourceAsStream("/main/iconBlue64x64.png")),
                new Image(getClass().getResourceAsStream("/main/iconBlue32x32.png")),
                new Image(getClass().getResourceAsStream("/main/iconBlue16x16.png"))); // To add an icon

        // add dialogpane to allow styling
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/gui/css/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("customDialog");
    }

    public void setTitle(String titel) {
        alert.setTitle(titel);
    }

    public Optional<ButtonType> showAndWait() {
        return alert.showAndWait();
    }

    public void setHeaderText(String header) {
        alert.setHeaderText(header);
    }

    public void setContentText(String content) {
        alert.setContentText(content);
    }

}
