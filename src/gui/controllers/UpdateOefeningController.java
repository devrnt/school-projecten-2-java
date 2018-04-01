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
public class UpdateOefeningController extends AnchorPane {

    private OefeningController controller;

    @FXML
    private TextField opgave;

    @FXML
    private Label opgaveFout;

    @FXML
    private TextField antwoord;
    @FXML
    private Label antwoordFout;

    @FXML
    private TextField feedback;
    @FXML
    private Label feedbackFout;

    @FXML
    private ListView<String> groepsbewerkingen;

    @FXML
    private Label groepsbewerkingenFout;

    @FXML
    private Button bevestigBtn;

    private Oefening oefening;

    public UpdateOefeningController(OefeningController controller) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        oefening = controller.getOefening(1);
        opgave.setText(oefening.getOpgave());
        antwoord.setText(Integer.toString(oefening.getAntwoord()));
        feedback.setText(oefening.getFeedback());

        groepsbewerkingen.setItems(controller.getGroepsbewerkingen());
        groepsbewerkingen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groepsbewerkingen.getSelectionModel().selectFirst();
        opgave.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (opgave.getText() == null || opgave.getText().trim().length() == 0) {
                    opgaveFout.setText("Geef een opgave in");
                } else {
                    opgaveFout.setText("");
                }
            }
        });

        antwoord.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (antwoord.getText() == null || antwoord.getText().trim().length() == 0) {
                    antwoordFout.setText("Geef een antwoord in");
                } else {
                    try {
                        Integer.parseInt(antwoord.getText());
                        antwoordFout.setText("");
                    } catch (NumberFormatException e) {
                        antwoordFout.setText("Antwoord moet een getal zijn");
                    }
                }
            }
        });

        feedback.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (feedback.getText() == null || feedback.getText().trim().length() == 0) {
                    feedbackFout.setText("Geef een feedback in");
                } else {
                    feedbackFout.setText("");
                }
            }
        });

        groepsbewerkingen.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (groepsbewerkingen.getSelectionModel().getSelectedItems() == null) {
                    groepsbewerkingenFout.setText("Selecteer minstens 1 groepsbewerking");
                } else {
                    groepsbewerkingenFout.setText("");
                }
            }
        });

    }

    @FXML
    public void bevestigClicked(ActionEvent event) {
        Label[] foutLabels = {opgaveFout, antwoordFout, feedbackFout, groepsbewerkingenFout};
        List<String> geselecteerdeItems = groepsbewerkingen.getSelectionModel().getSelectedItems();

        boolean inputGeldig = Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());

        if (inputGeldig) {
            controller.updateOefening(1, opgave.getText(), Integer.parseInt(antwoord.getText()), feedback.getText(), geselecteerdeItems);

            Alert oefeningCreatedSuccess = new Alert(Alert.AlertType.INFORMATION);
            oefeningCreatedSuccess.setTitle("Oefening");
            oefeningCreatedSuccess.setHeaderText("Wijzigen van een oefening");
            oefeningCreatedSuccess.setContentText("Oefening is succesvol gewijzigd");
            oefeningCreatedSuccess.showAndWait();
            Scene scene = new Scene(new BeheerOefeningenController(controller));
            Stage stage = (Stage) opgave.getScene().getWindow();
            stage.setTitle("Beheer Oefeningen");
            stage.setScene(scene);
            stage.show();

        } else {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Oefening aanmaken");
            invalidInput.setHeaderText("Er zijn nog ongeldige velden");
            invalidInput.setContentText("Pas de invoer aan zodat deze geldig is");
            invalidInput.showAndWait();
        }
    }

}
