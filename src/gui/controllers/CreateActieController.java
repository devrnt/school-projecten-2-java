/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.ActieController;
import domein.Actie;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class CreateActieController extends AnchorPane {
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextArea omschrijvingInput;
    @FXML
    private Label omschrijvingFout;
    @FXML
    private Button bevestigButton;
    @FXML
    private Button annuleerButton;
    
    private ActieController actieController;
    private Actie actie;
    
    private AlertCS bevestigAlert;
    
    public CreateActieController(ActieController actieController) {
        this.actieController = actieController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateActie.fxml"));
        
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        addInputValidation();
        
        bevestigAlert = new AlertCS(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer acties");
        bevestigAlert.setHeaderText("Aanmaken actie");
        bevestigAlert.setContentText("Actie is succesvol aangemaakt");
    }
    
    public CreateActieController(ActieController actieController, int actieId) {
        this(actieController);
        
        actie = actieController.getActie(actieId);
        
        omschrijvingInput.setText(actie.getOmschrijving());
        
        bevestigAlert = new AlertCS(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer acties");
        bevestigAlert.setHeaderText("Wijzigen actie");
        bevestigAlert.setContentText("Actie is succesvol gewijzigd");
    }
    
    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        boolean inputGeldig = !omschrijvingInput.getText().trim().isEmpty();
        if (inputGeldig) {
            String omschrijving = omschrijvingInput.getText();
            if (actie == null) {
                actieController.createActie(omschrijving);
            } else {
                actieController.updateActie(actie.getId(), omschrijving);
            }
            showSuccesAlert();
        } else {
            showErrorAlert();
        }
    }
    
    @FXML
    private void annuleerButtonClicked(ActionEvent event) {
        Event annuleerEvent = new AnnuleerEvent(actie == null ? -1 : actie.getId());
        this.fireEvent(annuleerEvent);
    }
    
    private void addInputValidation() {
        omschrijvingInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                omschrijvingFout.setText("Geef een omschrijving in");
            } else {
                omschrijvingFout.setText("");
            }
        });
    }
    
    private void showSuccesAlert() {
        Event beheerEvent = new DetailsEvent(actie == null ? -1 : actie.getId());
        this.fireEvent(beheerEvent);
    }
    
    private void showErrorAlert() {
        List<String> velden = new ArrayList<>();
        velden.add("Er zijn nog ongeldige velden");
        Event inputInvEvent = new InvalidInputEvent(velden);
        this.fireEvent(inputInvEvent);
    }
    
}
