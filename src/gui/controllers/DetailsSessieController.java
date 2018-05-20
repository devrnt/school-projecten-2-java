/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import com.itextpdf.text.DocumentException;
import controllers.SessieController;
import domein.Sessie;
import gui.events.DeleteEvent;
import gui.events.DownloadEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Button kopieerBtn;
    @FXML
    private Label kopieerLabel;
    @FXML
    private Label onderwijsLabel;
    @FXML
    private Label reactieFoutAntw;
    @FXML
    private Label boxLabel;

    private Sessie sessie;
    private SessieController controller;

    public DetailsSessieController(Sessie sessie, SessieController controller) {
        this.sessie = sessie;
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/DetailsSessie.fxml"));
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
        toggleButton();
        Event deleteEvent = new DeleteEvent(sessie.getId());
        this.fireEvent(deleteEvent);
    }

    @FXML
    private void kopieerBtnClicked(ActionEvent event) {
        String code = sessiecodeLabel.getText();
        StringSelection selection = new StringSelection(code);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        kopieerLabel.setText("Code  succesvol gekopieerd.");
    }

    @FXML
    private void samenvattingBtnClicked(ActionEvent event) throws IOException {
        try {
            controller.createSamenvattingSessie(sessie.getId());
            Event downloadEvent = new DownloadEvent();
            this.fireEvent(downloadEvent);
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(DetailsSessieController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void toggleButton() {
        verwijderBtn.setVisible(!verwijderBtn.isVisible());
    }

}
