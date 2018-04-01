/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author devri
 */
public class CreateSessieController extends AnchorPane {

    @FXML
    private TextField naamInput;
    @FXML
    private Label naamFout;
    @FXML
    private TextArea omschrijvingInput;
    @FXML
    private Label omschrijvingFout;
    @FXML
    private TextField klasInput;
    @FXML
    private Label klasFout;
    @FXML
    private DatePicker datumInput;
    @FXML
    private Label datumFout;
    @FXML
    private Label sessiecodeLabel;
    @FXML
    private Button bevestigButton;

    private SessieController sessieController;
    private Sessie sessie;

    public CreateSessieController(SessieController sessieController) {
        this.sessieController = sessieController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateSessie.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        naamInput.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (naamInput.getText() == null || naamInput.getText().trim().length() == 0) {
                    naamFout.setText("Vul het veld sessienaam in");
                } else {
                    String sessieNaam = naamInput.getText();
                    if (sessieController.bestaatSessieNaam(sessieNaam)) {
                        naamFout.setText("Een sessie met deze naam bestaat al, vul een andere in!");
                    } else {
                        naamFout.setText("");
                    }
                }
            }

        });
        omschrijvingInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.isEmpty()) {
                    omschrijvingFout.setText("Geef een omschrijving in");

                } else {
                    omschrijvingFout.setText("");
                }
            }

        });

        klasInput.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (klasInput.getText() == null || klasInput.getText().trim().length() == 0) {
                    klasFout.setText("Geef een klas in");
                } else {
                    klasFout.setText("");
                }
            }
        });

        // date picker
        datumInput.setOnAction(event -> {
            LocalDate date = datumInput.getValue();
            if (date == null) {
                datumFout.setText("Geef een datum in");
            }
            // controle: datum moet na vandaag zijn
            Calendar cal = Calendar.getInstance();

            Date vandaag = cal.getTime();
            if (convertToDate(date).before(vandaag)) {
                datumFout.setText("De datum moet in de toekomst liggen");
            } else {
                datumFout.setText("");
            }
        });

    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        Label[] foutLabels = {naamFout, omschrijvingFout, klasFout, datumFout};

        boolean inputGeldig = Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());

        if (inputGeldig) {
          
            sessieController.createSessie(naamInput.getText(), omschrijvingInput.getText());

            Alert sessieSuccesvolGewijzigd = new Alert(Alert.AlertType.INFORMATION);
            sessieSuccesvolGewijzigd.setTitle("Sessie");
            sessieSuccesvolGewijzigd.setHeaderText("Wijzigen van een sessie");
            sessieSuccesvolGewijzigd.setContentText("Sessie is succesvol gewijzigd");
            sessieSuccesvolGewijzigd.showAndWait();
            Scene scene = new Scene(new BeheerSessiesController(sessieController));
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setTitle("Beheer Sessies");
            stage.setScene(scene);
            stage.show();

        } else {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Sessie aanmaken");
            invalidInput.setHeaderText("Niet alle velden zijn geldig!");
            invalidInput.setContentText("Vul alle velden correct in.");
            invalidInput.showAndWait();
        }
    }

    private Date convertToDate(LocalDate value) {
        Instant instant = Instant.from(value.atStartOfDay(ZoneId.systemDefault()));
        Date datum = Date.from(instant);
        return datum;
    }
}
