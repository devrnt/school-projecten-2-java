package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author sam
 */
public class DeleteOefeningController extends AnchorPane {

    private OefeningController controller;

    @FXML
    private Label opgaveLabel;
    @FXML
    private Label antwoordLabel;
    @FXML
    private Label feedbackLabel;

    @FXML
    private ListView<String> groepsbewerkingen;

    @FXML
    private Button bevestigBtn;

    public DeleteOefeningController(OefeningController controller) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DeleteOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> oefeningDetails = controller.getOefening(1);
        opgaveLabel.setText(oefeningDetails.get(0));
        antwoordLabel.setText(oefeningDetails.get(1));
        feedbackLabel.setText(oefeningDetails.get(2));

        groepsbewerkingen.setItems(controller.getGroepsbewerkingenByOefening(1));
        groepsbewerkingen.setDisable(true);

    }

    @FXML
    public void bevestigClicked(ActionEvent event) {

        controller.deleteOefening(1);

        Alert oefeningCreatedSuccess = new Alert(Alert.AlertType.INFORMATION);
        oefeningCreatedSuccess.setTitle("Oefening");
        oefeningCreatedSuccess.setHeaderText("Verwijderen van een oefening");
        oefeningCreatedSuccess.setContentText("Oefening is succesvol verwijderd");
        oefeningCreatedSuccess.showAndWait();
        Scene scene = new Scene(new BeheerOefeningenController(controller));
        Stage stage = (Stage) opgaveLabel.getScene().getWindow();
        stage.setTitle("Beheer Oefeningen");
        stage.setScene(scene);
        stage.show();

    }

}
