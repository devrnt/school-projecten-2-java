/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import domein.Groepsbewerking;
import gui.events.DeleteEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class DetailsGroepsbewerkingController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label omschrijvingLbl;
    @FXML
    private Label operatieLbl;
    @FXML
    private Label factorLbl;
    @FXML
    private Button wijzigBtn;
    @FXML
    private Button verwijderBtn;

    private Groepsbewerking groepsbewerking;

    public DetailsGroepsbewerkingController(Groepsbewerking groepsbewerking) {
        this.groepsbewerking = groepsbewerking;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/DetailsGroepsbewerking.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    public void initialize() {
        omschrijvingLbl.setText(groepsbewerking.getOmschrijving());
        factorLbl.setText(Integer.toString(groepsbewerking.getFactor()));
        operatieLbl.setText(groepsbewerking.getOperator().toString());

    }

    @FXML
    private void wijzigBtnClicked(ActionEvent event) {
        Event wijzigEvent = new WijzigEvent(groepsbewerking.getId());
        this.fireEvent(wijzigEvent);
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        Event deleteEvent = new DeleteEvent(groepsbewerking.getId());
        this.fireEvent(deleteEvent);
    }

    public void toggleButton() {
        System.out.println(verwijderBtn.isVisible());
        verwijderBtn.setVisible(!verwijderBtn.isVisible());
    }

}
