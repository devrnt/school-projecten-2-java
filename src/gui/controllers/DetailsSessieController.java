/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import domein.Sessie;
import gui.events.DeleteEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class DetailsSessieController extends AnchorPane {

    @FXML
    private Label naamLabel;
    @FXML
    private Label omschrijvingLabel;
    @FXML
    private Label klasLabel;
    @FXML
    private Label datumLabel;
    @FXML
    private Label sessiecodeLabel;
    @FXML
    private Button verwijderBtn;
    @FXML
    private Label onderwijsLabel;
    @FXML
    private Label reactieFoutAntw;
    @FXML
    private Label boxLabel;

    private Sessie sessie;

    public DetailsSessieController(Sessie sessie) {
        this.sessie = sessie;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsSessie.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        AlertCS verwijderAlert = new AlertCS(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen sessie");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze sessie wilt verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                Event deleteEvent = new DeleteEvent(sessie.getId());
                this.fireEvent(deleteEvent);
            }
        });
    }

    private void initialize() {
        naamLabel.setText(sessie.getNaam());
        omschrijvingLabel.setText(sessie.getOmschrijving());
        klasLabel.setText(sessie.getKlas().getNaam());
        reactieFoutAntw.setText(sessie.getFoutAntwoordActie().toString());
        onderwijsLabel.setText(sessie.getSoortOnderwijs().toString());
        datumLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(sessie.getDatum()));
        sessiecodeLabel.setText(sessie.getSessieCode());
        boxLabel.setText(sessie.getBoxNaam());

    }

}
