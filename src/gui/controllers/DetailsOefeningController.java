package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
public class DetailsOefeningController extends AnchorPane {

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
    private Button wijzigBtn;

    @FXML
    private Button verwijderBtn;

    @FXML
    private Button terugBtn;

    private Oefening oefening;

    public DetailsOefeningController(OefeningController controller, int id) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        oefening = controller.getOefening(id);
        opgaveLabel.setText(oefening.getOpgave());
        antwoordLabel.setText(Integer.toString(oefening.getAntwoord()));
        feedbackLabel.setText(oefening.getFeedback());

        groepsbewerkingen.setItems(controller.getGroepsbewerkingenByOefening(id));
        groepsbewerkingen.setDisable(true);

        terugBtn.setOnAction(event -> terugNaarLijst());

    }

    @FXML
    public void wijzigBtnClicked(ActionEvent event) {
        Scene scene = new Scene(new UpdateOefeningController(controller, oefening.getId()));
        Stage stage = (Stage) wijzigBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Wijzig oefening");
        stage.show();
    }

    @FXML
    public void verwijderBtnClicked(ActionEvent event) {
        Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen oefening");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze oefening wil verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                controller.deleteOefening(oefening.getId());
                terugNaarLijst();
            }
        });
    }

    private void terugNaarLijst() {
        Scene scene = new Scene(new BeheerOefeningenController(controller));
        Stage stage = (Stage) wijzigBtn.getScene().getWindow();
        stage.setTitle("Beheer Oefeningen");
        stage.setScene(scene);
        stage.show();
    }

//    @FXML
//    public void bevestigClicked(ActionEvent event) {
//
//        controller.deleteOefening(1);
//
//        Alert oefeningCreatedSuccess = new Alert(Alert.AlertType.INFORMATION);
//        oefeningCreatedSuccess.setTitle("Oefening");
//        oefeningCreatedSuccess.setHeaderText("Verwijderen van een oefening");
//        oefeningCreatedSuccess.setContentText("Oefening is succesvol verwijderd");
//        oefeningCreatedSuccess.showAndWait();
//        Scene scene = new Scene(new BeheerOefeningenController(controller));
//        Stage stage = (Stage) opgaveLabel.getScene().getWindow();
//        stage.setTitle("Beheer Oefeningen");
//        stage.setScene(scene);
//        stage.show();
//
//    }
}
