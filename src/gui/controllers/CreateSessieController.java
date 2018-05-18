/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.BoxController;
import controllers.KlasController;
import controllers.SessieController;
import domein.BreakOutBox;
import domein.FoutAntwoordActieEnum;
import domein.ISessie;
import domein.Klas;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import gui.events.GeefSessieDoorEvent;
import gui.events.InvalidInputEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.AlertCS;

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
    private ChoiceBox<Klas> klasChoiceBox;
    @FXML
    private ChoiceBox<BreakOutBox> boxChoiceBox;
    @FXML
    private Label boxFout;
    @FXML
    private Label klasFout;
    @FXML
    private Button bekijkLlnButton;
    @FXML
    private DatePicker datumInput;
    @FXML
    private Label datumFout;
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortonderwijsChoiceBox;
    @FXML
    private ChoiceBox<FoutAntwoordActieEnum> reactieFoutAntwChoiceBox;
    @FXML
    private Button bevestigButton;
    @FXML
    private Button annuleerBtn;

    private SessieController sessieController;
    private Sessie sessie;
    private KlasController klasController;
    private BoxController boxController;

    public CreateSessieController(SessieController sessieController) {
        this.sessieController = sessieController;
        klasController = new KlasController();
        boxController = new BoxController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateSessie.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
        addInputValidation();
    }

    private void initialize() {
        // Options choicebox Soort onderwijs
        soortonderwijsChoiceBox.setValue(SoortOnderwijsEnum.dagonderwijs);
        Arrays.asList(SoortOnderwijsEnum.values())
                .forEach(soortOnderwijs -> soortonderwijsChoiceBox.getItems().add(soortOnderwijs));

        // Choicebox options worden weergeven volgens deze converter 
        // anders wordt het object getoond
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

        boxChoiceBox.setConverter(new StringConverter<BreakOutBox>() {
            @Override
            public String toString(BreakOutBox box) {
                return box.getNaam();
            }

            @Override
            public BreakOutBox fromString(String boxNaam) {
                return boxChoiceBox.getItems()
                        .stream()
                        .filter(b -> b.getNaam().equals(boxNaam))
                        .findFirst()
                        .orElse(null);
            }
        });

        klasController.getAllKlassen()
                .forEach(klas -> klasChoiceBox.getItems().add(klas));
        boxChoiceBox.getItems().setAll(boxController.getAllBreakOutBoxen(soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get()));

        reactieFoutAntwChoiceBox.setValue(FoutAntwoordActieEnum.feedback);
        reactieFoutAntwChoiceBox.getItems().addAll(FoutAntwoordActieEnum.feedback, FoutAntwoordActieEnum.blokkeren);

        //bekijkLlnButton.setDisable(true);
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(-1);
            this.fireEvent(annuleerEvent);
        });

        boxChoiceBox.getSelectionModel().selectFirst();
        soortonderwijsChoiceBox.getSelectionModel().selectFirst();
        soortonderwijsChoiceBox.getSelectionModel().selectFirst();
        klasChoiceBox.getSelectionModel().selectFirst();
        reactieFoutAntwChoiceBox.getSelectionModel().selectFirst();

        LocalDate ndd = LocalDate.now();

        datumInput.setValue(ndd);

    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        if (boxChoiceBox.getSelectionModel().selectedIndexProperty().getValue() == -1) {
            boxFout.setText("Selecteer een box of maak er een");
        } else {
            boxFout.setText("");
        }
        Label[] foutLabels = {naamFout, omschrijvingFout, datumFout, boxFout};
        String[] inputs = {naamInput.getText(), omschrijvingInput.getText()};

        Klas gekozenKlas = klasChoiceBox.getSelectionModel().getSelectedItem();
        BreakOutBox gekozenBox = boxChoiceBox.getSelectionModel().getSelectedItem();
        boolean inputGeldig = Arrays.stream(inputs).allMatch(i -> !i.trim().isEmpty()) && Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());
        if (inputGeldig) {
            Sessie doorgevenSessie = new Sessie(naamInput.getText(), omschrijvingInput.getText(), gekozenKlas, gekozenBox, convertToDate(datumInput.getValue()), soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get(),
                    reactieFoutAntwChoiceBox.getSelectionModel().selectedItemProperty().get(), false, null);
            Event geefdoorevent = new GeefSessieDoorEvent(doorgevenSessie, 1);
            this.fireEvent(geefdoorevent);
        } else {
            Event invalidInputEvent = new InvalidInputEvent(Arrays.stream(foutLabels).map(l -> l.getText()).collect(Collectors.toList()));
            this.fireEvent(invalidInputEvent);
        }
    }

    @FXML
    private void bekijkLlnButtonClicked(ActionEvent event) {
        klasController = new KlasController();
        int klasId = klasChoiceBox.getSelectionModel().getSelectedItem().getId();
        Scene scene = new Scene(new OverzichtLeerlingenInKlasController(klasController, klasId));
        Stage stage = new Stage();
        stage.setTitle("Bekijk leerlingen");
        stage.setScene(scene);
        stage.show();
    }

    // <editor-fold desc="=== Help methodes ===" >
    private void addInputValidation() {
        naamInput.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                naamFout.setText("Vul sessienaam in");
            } else {
                String sessieNaam = naamInput.getText();
                if (sessieController.getSessieBeheer().bestaatSessieNaam(sessieNaam)) {
                    naamFout.setText("Sessienaam bestaat al");
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
                // object for validation
                ISessie validation = sessieController.getISessie();
                LocalDate datumInputVal = datumInput.getValue();

                Date gekozenDag = Date.from(datumInputVal.atStartOfDay(ZoneId.systemDefault()).toInstant());

                try {
                    validation.setDatum(gekozenDag);
                    // if no exception is thrown
                    datumFout.setText("");
                } catch (IllegalArgumentException e) {
                    System.out.println("Exception is trown");
                    datumFout.setText(e.getMessage());
                }
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        });

        // listener for choicebox select change
        soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            boxChoiceBox.getItems().setAll(boxController.getAllBreakOutBoxen(soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get()));
            if (boxChoiceBox.getSelectionModel().selectedIndexProperty().getValue() == -1) {
                boxFout.setText("Selecteer een box of maak er een");
            } else {
                boxFout.setText("");
            }
            if (boxController.getAllBreakOutBoxen(soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get()).size() < 1) {
                boxChoiceBox.setValue(null);
            } else {
                boxChoiceBox.setValue(boxController.getAllBreakOutBoxen(soortonderwijsChoiceBox.getSelectionModel().selectedItemProperty().get()).get(0));
            }
            if (newVal == SoortOnderwijsEnum.dagonderwijs) {
                reactieFoutAntwChoiceBox.getItems().clear();
                reactieFoutAntwChoiceBox.getItems().addAll(FoutAntwoordActieEnum.feedback, FoutAntwoordActieEnum.blokkeren);
                reactieFoutAntwChoiceBox.setValue(FoutAntwoordActieEnum.feedback);
            }
            if (newVal == SoortOnderwijsEnum.afstandsonderwijs) {
                reactieFoutAntwChoiceBox.getItems().clear();
                reactieFoutAntwChoiceBox.getItems().addAll(FoutAntwoordActieEnum.feedback);
                reactieFoutAntwChoiceBox.setValue(FoutAntwoordActieEnum.feedback);
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
    // </editor-fold>

}
