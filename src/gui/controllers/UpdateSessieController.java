/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.KlasController;
import controllers.SessieController;
import domein.Klas;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author devri
 */
public class UpdateSessieController extends AnchorPane {

    @FXML
    private TextField naamInput;
    @FXML
    private Label naamFout;
    @FXML
    private TextArea omschrijvingInput;
    @FXML
    private Label omschrijvingFout;
    @FXML
    private ChoiceBox<Klas> klasChoiceBox;
    @FXML
    private Label klasFout;
    @FXML
    private Button bekijkLlnButton;
    @FXML
    private DatePicker datumInput;
    @FXML
    private Label datumFout;
    @FXML
    private TextField lesuurInput;
    @FXML
    private Label lesuurFout;
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortonderwijsChoiceBox;
    @FXML
    private ChoiceBox<String> reactieFoutAntwChoiceBox;
    @FXML
    private Button bevestigButton;
    @FXML
    private Button annuleerBtn;

    private SessieController sessieController;
    private KlasController klasController;
    private Sessie sessie;

    public UpdateSessieController(SessieController sessieController, int sessieId) {
        this.sessieController = sessieController;
        klasController = new KlasController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateSessie.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize(sessieId);

        addInputValidation();
    }

    @FXML
    public void bevestigButtonClicked(ActionEvent event) {
        Label[] foutLabels = {naamFout, omschrijvingFout, klasFout, datumFout, lesuurFout};
        String[] inputs = {naamInput.getText(), omschrijvingInput.getText(), lesuurInput.getText()};
        Klas gekozenKlas = klasChoiceBox.getSelectionModel().getSelectedItem();

        boolean inputGeldig = (Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty()) && Arrays.stream(inputs).allMatch(i -> !i.trim().isEmpty()));

        if (inputGeldig) {
            sessieController.updateSessie(
                    sessie.getId(), naamInput.getText(),
                    omschrijvingInput.getText(), gekozenKlas, Integer.parseInt(lesuurInput.getText()),
                    convertToDate(datumInput.getValue()),
                    soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get(),
                    reactieFoutAntwChoiceBox.getSelectionModel().selectedItemProperty().get()
            );

            Alert sessieSuccesvolGewijzigd = new Alert(Alert.AlertType.INFORMATION);
            sessieSuccesvolGewijzigd.setTitle("Sessie");
            sessieSuccesvolGewijzigd.setHeaderText("Wijzigen van een sessie");
            sessieSuccesvolGewijzigd.setContentText("Sessie is succesvol gewijzigd");
            sessieSuccesvolGewijzigd.showAndWait();
            terugNaarDetails();

        } else {
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setTitle("Sessie aanmaken");
            invalidInput.setHeaderText("Niet alle velden zijn geldig!");
            invalidInput.setContentText("Vul alle velden correct in.");
            invalidInput.showAndWait();
        }
    }

    @FXML
    private void bekijkLlnButtonClicked(ActionEvent event) {
        klasController = new KlasController();
        Klas klas = klasChoiceBox.getSelectionModel().getSelectedItem();
        Scene scene = new Scene(new OverzichtLeerlingenInKlasController(klasController, klas.getId()));
        Stage stage = new Stage();
        stage.setTitle("Bekijk leerlingen");
        stage.setScene(scene);
        stage.show();
    }

    private void initialize(int sessieId) {
        sessie = sessieController.getSessie(sessieId);

        naamInput.setText(sessie.getNaam());
        omschrijvingInput.setText(sessie.getOmschrijving());

        klasChoiceBox.setConverter(new StringConverter<Klas>() {
            @Override
            public String toString(Klas klas) {
                return klas.getNaam();
            }

            @Override
            public Klas fromString(String klasNaam) {
                return klasChoiceBox.getItems()
                        .stream()
                        .filter(k -> k.getNaam().equals(klasNaam))
                        .findFirst()
                        .orElse(null);
            }
        });
        klasController.getAllKlassen()
                .forEach(klas -> klasChoiceBox.getItems().add(klas));
        klasChoiceBox.setValue(sessie.getKlas());
        // datepicker
        LocalDate sessieDatum = sessie.getDatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datumInput.setValue(sessieDatum);

        lesuurInput.setText(String.valueOf(sessie.getLesuur()));

        // set the choiceboxes
        Arrays.asList(SoortOnderwijsEnum.values())
                .forEach(soortOnderwijs -> soortonderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortonderwijsChoiceBox.setValue(sessie.getSoortOnderwijs());

        reactieFoutAntwChoiceBox.getItems().addAll("Feedback", "Na 3maal blokkeren");
        reactieFoutAntwChoiceBox.setValue(sessie.getFoutAntwActie().substring(0, 1).toUpperCase() + sessie.getFoutAntwActie().substring(1));

        klasChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            bekijkLlnButton.setDisable(false);
        });
        annuleerBtn.setOnAction(event -> terugNaarDetails());

    }

    // <editor-fold desc="=== Help methodes ===" >
    private void addInputValidation() {
        naamInput.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                naamFout.setText("Vul sessienaam in");
            } else {
                String sessieNaam = naamInput.getText();
                if (sessieController.bestaatSessieNaam(sessieNaam)) {
                    naamFout.setText("Sessienaam bestaat al, vul een andere in!");
                } else {
                    naamFout.setText("");
                }
            }
        });
        omschrijvingInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                omschrijvingFout.setText("Geef een omschrijving in");
            } else {
                omschrijvingFout.setText("");
            }
        });

        klasChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            bekijkLlnButton.setDisable(false);
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
                    datumFout.setText("Datum moet in het heden/toekomst liggen");
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

    private void terugNaarDetails() {
        Scene scene = new Scene(new DetailsSessieController(sessieController, sessie.getId()));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Beheer Sessies");
        stage.setScene(scene);
        stage.show();
    }
    // </editor-fold>
}
