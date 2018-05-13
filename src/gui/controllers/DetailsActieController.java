/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.ActieController;
import domein.Actie;
import gui.events.DeleteEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class DetailsActieController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label omschrijvingLabel;
    @FXML
    private Button verwijderBtn;

    private Actie actie;

    public DetailsActieController(Actie actie) {
        this.actie = actie;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsActie.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    public final void initialize() {
        omschrijvingLabel.setText(actie.getOmschrijving());
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        AlertCS verwijderAlert = new AlertCS(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen actie");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u de geselecteerde actie wilt verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                Event deleteEvent = new DeleteEvent(actie.getId());
                this.fireEvent(deleteEvent);
            }
        });
    }
    
    @FXML
    private void wijzigBtnClicked(ActionEvent event){
        Event wijzigEvent = new WijzigEvent(actie.getId());
        this.fireEvent(wijzigEvent);
    }

}
