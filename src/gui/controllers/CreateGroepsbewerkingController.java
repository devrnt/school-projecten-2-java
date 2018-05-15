/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.GroepsbewerkingController;
import domein.Groepsbewerking;
import domein.OperatorEnum;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class CreateGroepsbewerkingController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextArea omschrijvingInput;
    @FXML
    private Label omschrijvingFout;
    @FXML
    private TextField factorInput;
    @FXML
    private Label factorFout;
    @FXML
    private ChoiceBox<OperatorEnum> operatorCBX;
    @FXML
    private Button bevestigButton;
    @FXML
    private Button annuleerButton;

    private GroepsbewerkingController groepsbewerkingController;
    private Groepsbewerking groepsbewerking;

    private AlertCS bevestigAlert;

    CreateGroepsbewerkingController(GroepsbewerkingController groepsbewerkingController) {
        this.groepsbewerkingController = groepsbewerkingController;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateGroepsbewerking.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        operatorCBX.setValue(OperatorEnum.optellen);
        Arrays.asList(OperatorEnum.values())
                .forEach(operator -> operatorCBX.getItems().add(operator));

        addInputValidation();

        bevestigAlert = new AlertCS(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer groepsbewerkingen");
        bevestigAlert.setHeaderText("Aanmaken groepsbewerking");
        bevestigAlert.setContentText("Groepsbewerking is succesvol aangemaakt");
    }

    public CreateGroepsbewerkingController(GroepsbewerkingController groepsbewerkingController, int id) {
        this(groepsbewerkingController);
        groepsbewerking = groepsbewerkingController.getGroepsbewerking(id);

        omschrijvingInput.setText(groepsbewerking.getOmschrijving());
        factorInput.setText(Integer.toString(groepsbewerking.getFactor()));
        operatorCBX.setValue(groepsbewerking.getOperator());

        bevestigAlert = new AlertCS(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer groepsbewerkingen");
        bevestigAlert.setHeaderText("Wijzigen groepsbewerking");
        bevestigAlert.setContentText("Groepsbewerking is succesvol gewijzigd");

    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        boolean inputGeldig = !omschrijvingInput.getText().trim().isEmpty()
                && omschrijvingFout.getText().length() == 0
                && !factorInput.getText().trim().isEmpty()
                && factorFout.getText().length() == 0;

        if (inputGeldig) {
            String omschrijving = omschrijvingInput.getText();
            int factor = Integer.parseInt(factorInput.getText());
            OperatorEnum operator = operatorCBX.getSelectionModel().getSelectedItem();

            if (groepsbewerking == null) {
                groepsbewerkingController.createGroepsbewerking(omschrijving, factor, operator);
            } else {
                System.out.println("from " + groepsbewerking.getId());
                groepsbewerkingController.updateGroepsbewerking(groepsbewerking.getId(), omschrijving, factor, operator);
            }
            showSuccesAlert();
        } else {
            showErrorAlert();
        }
    }

    @FXML
    private void annuleerButtonClicked(ActionEvent event) {
        Event annuleerEvent = new AnnuleerEvent(groepsbewerking == null ? -1 : groepsbewerking.getId());
        this.fireEvent(annuleerEvent);
    }

    private void addInputValidation() {
        omschrijvingInput.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                omschrijvingFout.setText("Vul omschrijving in");
            } else {
                omschrijvingFout.setText("");
            }
        });

        factorInput.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                factorFout.setText("Vul factor in");
            } else {
                String factorAntwoord = factorInput.getText();
                try {
                    Integer.parseInt(factorAntwoord);
                    factorFout.setText("");
                } catch (NumberFormatException e) {
                    factorFout.setText("Factor moet een getal zijn");
                }
            }
        });
    }

    private void showSuccesAlert() {
        bevestigAlert.showAndWait();
        Event beheerEvent = new DetailsEvent(groepsbewerking == null ? -1 : groepsbewerking.getId());
        this.fireEvent(beheerEvent);
    }

    private void showErrorAlert() {
        AlertCS invalidInput = new AlertCS(Alert.AlertType.ERROR);
        invalidInput.setTitle("Groepsbewerking  aanmaken");
        invalidInput.setHeaderText("Er zijn nog ongeldige velden");
        invalidInput.setContentText("Pas de invoer aan zodat deze geldig is");
        invalidInput.showAndWait();
    }

}
