/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
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
import javafx.scene.control.ChoiceBox;
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
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortonderwijsChoiceBox;
    @FXML
    private ChoiceBox<String> reactieFoutAntwChoiceBox;
    @FXML
    private TextField lesuurInput;

    @FXML
    private Label lesuurFout;

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
        // set the choiceboxes
        Arrays.asList(SoortOnderwijsEnum.values())
                .forEach(soortOnderwijs -> soortonderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortonderwijsChoiceBox.setValue(SoortOnderwijsEnum.dagonderwijs);

        reactieFoutAntwChoiceBox.getItems().addAll("Feedback", "Na 3maal blokkeren");
        reactieFoutAntwChoiceBox.setValue("Feedback");

        naamInput.textProperty().addListener((ob, oldValue, newValue) -> {
            System.out.println("Got to naam");

            if (newValue == null || newValue.trim().isEmpty()) {
                naamFout.setText("Vul het veld sessienaam in");
            } else {
                String sessieNaam = naamInput.getText();

                if (sessieController.bestaatSessieNaam(sessieNaam)) {
                    naamFout.setText("Een sessie met deze naam bestaat al, vul een andere in!");
                } else {
                    naamFout.setText("");
                }
            }

        });
        omschrijvingInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            System.out.println("Got to omschrijving");
            if (newValue == null || newValue.trim().isEmpty()) {
                omschrijvingFout.setText("Geef een omschrijving in");
            } else {
                omschrijvingFout.setText("");
            }
        });

        klasInput.textProperty().addListener((ob, oldValue, newValue) -> {
            System.out.println("Got to klas");

            if (klasInput.getText() == null || klasInput.getText().trim().length() == 0) {
                klasFout.setText("Geef een klas in");
            } else {
                klasFout.setText("");
            }

        });

        // date picker
        datumInput.setOnAction(event -> {
            try {
                LocalDate datumInputVal = datumInput.getValue();
                Date gekozenDag = Date.from(datumInputVal.atStartOfDay(ZoneId.systemDefault()).toInstant());

                // controle: datum moet na vandaag zijn
                LocalDate vandaagLoc = LocalDate.now(ZoneId.systemDefault());
                Date vandaag = Date.from(vandaagLoc.atStartOfDay(ZoneId.systemDefault()).toInstant());

                if (gekozenDag.before(vandaag)) {
                    datumFout.setText("De datum moet in de toekomst liggen");
                } else {
                    datumFout.setText("");
                }
            } catch (NullPointerException e) {
                datumFout.setText("Geef een datum in");
            }

        });

        // listener for choicebox sleect change
        soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {

            if (newVal == SoortOnderwijsEnum.dagonderwijs) {
                reactieFoutAntwChoiceBox.getItems().clear();
                reactieFoutAntwChoiceBox.getItems().addAll("Feedback", "Na 3maal blokkeren");
                reactieFoutAntwChoiceBox.setValue("Feedback");
            }
            if (newVal == SoortOnderwijsEnum.afstandsonderwijs) {
                reactieFoutAntwChoiceBox.getItems().clear();
                reactieFoutAntwChoiceBox.getItems().add("Feedback");
                reactieFoutAntwChoiceBox.setValue("Feedback");

            }
        });

        lesuurInput.textProperty().addListener((v, oldVal, newVal) -> {
            if (!getalOfNiet(newVal)) {
                lesuurFout.setText("Lesuur moet een getal zijn!");
            } else {
                int getal = Integer.parseInt(newVal);
                if (getal < 0 || getal > 10) {
                    lesuurFout.setText("Lesuur moet tussen 0 en 10 liggen");
                } else {
                    lesuurFout.setText("");
                }
            }
        });

    }

    @FXML

    private void bevestigButtonClicked(ActionEvent event) {
        Label[] foutLabels = {naamFout, omschrijvingFout, klasFout, datumFout, lesuurFout};
        String[] inputs = {naamInput.getText(), omschrijvingInput.getText(), klasInput.getText()};

        boolean inputGeldig = (Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty()) && Arrays.stream(inputs).allMatch(i -> !i.trim().isEmpty()));

        if (inputGeldig) {

            sessieController.createSessie(naamInput.getText(), omschrijvingInput.getText(),
                    klasInput.getText(), Integer.parseInt(lesuurInput.getText()), convertToDate(datumInput.getValue()),
                    soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get(),
                    reactieFoutAntwChoiceBox.getSelectionModel().selectedItemProperty().get()
            );

            Alert sessieSuccesvolGewijzigd = new Alert(Alert.AlertType.INFORMATION);
            sessieSuccesvolGewijzigd.setTitle("Sessie");
            sessieSuccesvolGewijzigd.setHeaderText("Aanmaken van een sessie");
            sessieSuccesvolGewijzigd.setContentText("Sessie is succesvol aangemaakt");
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

    private boolean getalOfNiet(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
