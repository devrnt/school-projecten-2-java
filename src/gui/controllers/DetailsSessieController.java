/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private Button wijzigBtn;
    @FXML
    private Button verwijderBtn;
    @FXML
    private Label onderwijsLabel;
    @FXML
    private Label reactieFoutAntw;
    @FXML
    private Button terugBtn;

    private SessieController sessieController;
    private Sessie sessie;

    public DetailsSessieController(SessieController sessieController, int id) {
        this.sessieController = sessieController;

        sessie = this.sessieController.getSessie(id);

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
    private void wijzigBtnClicked(ActionEvent event) {
        Scene scene = new Scene(new UpdateSessieController(sessieController, sessie.getId()));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Wijzig sessie");
        stage.show();
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen sessie");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze sessie wilt verwijderen?");
        Optional<ButtonType> result = verwijderAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sessieController.deleteSessie(sessie.getId());
            Scene scene = new Scene(new BeheerSessiesController(sessieController));
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setTitle("Beheer Sessies");
            stage.setScene(scene);
            stage.show();
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
        terugBtn.setOnAction(event -> terugNaarLijst());
    }

    private void terugNaarLijst() {
        Scene scene = new Scene(new BeheerSessiesController(sessieController));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Beheer Sessies");
        stage.setScene(scene);
        stage.show();
    }

}
