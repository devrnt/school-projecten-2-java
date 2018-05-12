package gui.controllers;

import controllers.BoxController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.SoortOnderwijsEnum;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

public class CreateBreakOutBoxController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextField naamTxt;
    @FXML
    private TextField omschrijvingTxt;
    @FXML
    private Label naamFoutLbl;
    @FXML
    private Label omschrijvingFoutLbl;
    @FXML
    private Label actiesFoutLbl;
    @FXML
    private Label oefeningenFoutLbl;
    @FXML
    private ListView<Actie> actieList1;
    @FXML
    private Button actieToevoegenBtn;
    @FXML
    private Button actieVerwijderenBtn;
    @FXML
    private ListView<Actie> actieList2;
    @FXML
    private ListView<Oefening> oefeningList1;
    @FXML
    private Button oefeningToevoegenBtn;
    @FXML
    private Button oefeningVerwijderenBtn;
    @FXML
    private ListView<Oefening> oefeningList2;
    @FXML
    private Button bevestigBtn;
    @FXML
    private Button keerTerugBtn;
    private final BoxController boxController;
    @FXML
    private Button annuleerBtn;
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortOnderwijsChoiceBox;

    public CreateBreakOutBoxController(BoxController boxController) {
        this.boxController = boxController;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateBreakOutBox.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //listviews vullen
        actieList2.setItems(boxController.getActies());
        oefeningList2.setItems(boxController.getOefeningen());
        //choicebox
        soortOnderwijsChoiceBox.setValue(SoortOnderwijsEnum.dagonderwijs);
        Arrays.asList(SoortOnderwijsEnum.values()).forEach(soortOnderwijs -> soortOnderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortOnderwijsChoiceBox.getSelectionModel().selectFirst();

        //listeners
        maakListeners();
    }

    public CreateBreakOutBoxController(BoxController boxController, BreakOutBox box) {
        this.boxController = boxController;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateBreakOutBox.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //listviews vullen
        actieList2.setItems(boxController.getActies());
        oefeningList2.setItems(boxController.getOefeningen());
        naamTxt.setText(box.getNaam() + "Kopie");
        omschrijvingTxt.setText(box.getOmschrijving());
        for (Oefening o : boxController.getOefeningenByBox(box.getId())) {
            oefeningList2.getItems().remove(o);
            oefeningList1.getItems().add(o);
        }
        for (Actie a : boxController.getActiesByBox(box.getId())) {
            actieList2.getItems().remove(a);
            actieList1.getItems().add(a);

        }
        //listeners
        maakListeners();
    }

    @FXML
    private void bevestigClicked(ActionEvent event) {
        boolean inputGeldig = true;
        List<Actie> geselecteerdeActies = actieList1.getItems();
        List<Oefening> geselecteerdeOefeningen = oefeningList1.getItems();

        if (geselecteerdeActies.isEmpty()) {
            actiesFoutLbl.setText("Selecteer minstens 1 actie!");
            inputGeldig = false;
        } else {
            actiesFoutLbl.setText("");
        }
        if (geselecteerdeOefeningen.isEmpty()) {
            inputGeldig = false;
            oefeningenFoutLbl.setText("Selecteer minstens 1 oefening!");
        } else {
            oefeningenFoutLbl.setText("");
        }
        if (naamTxt.getText().isEmpty()) {
            inputGeldig = false;

            naamFoutLbl.setText("Voer een naam in!");
        } else {
            naamFoutLbl.setText("");
        }
        if (omschrijvingTxt.getText().isEmpty()) {
            inputGeldig = false;

            omschrijvingFoutLbl.setText("Voer een omschrijving in!");
        } else {
            omschrijvingFoutLbl.setText("");
        }
        if (geselecteerdeActies.size() != geselecteerdeOefeningen.size() - 1) {
            actiesFoutLbl.setText(actiesFoutLbl.getText() + "Aantal Oefeningen moet gelijk zijn aan aantal Acties + 1.");
            inputGeldig = false;
        }
        if (inputGeldig) {
            boxController.createBreakOutBox(naamTxt.getText(), omschrijvingTxt.getText(), soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem(), geselecteerdeOefeningen, geselecteerdeActies);
            AlertCS oefeningCreatedSuccess = new AlertCS(Alert.AlertType.INFORMATION);
            oefeningCreatedSuccess.setTitle("BreakOutBox");
            oefeningCreatedSuccess.setHeaderText("Aanmaken van een box");
            oefeningCreatedSuccess.setContentText("BreakOutBox is succesvol aangemaakt");
            oefeningCreatedSuccess.showAndWait();
            terugNaarLijst();

        } else {
            AlertCS invalidInput = new AlertCS(Alert.AlertType.ERROR);
            invalidInput.setTitle("Box aanmaken");
            invalidInput.setHeaderText("Er zijn nog ongeldige velden");
            invalidInput.setContentText("Pas de invoer aan zodat deze geldig worden");
            invalidInput.showAndWait();
        }
    }

    private void terugNaarLijst() {
        Event beheerEvent = new DetailsEvent(-1);
        this.fireEvent(beheerEvent);
    }

    @FXML
    private void voegActieToeBtn(ActionEvent event) {
        Actie a = actieList2.getSelectionModel().getSelectedItem();
        if (a != null) {
            actieList2.getItems().remove(a);
            actieList1.getItems().add(a);
        }
    }

    @FXML
    private void verwijderActieBtn(ActionEvent event) {
        Actie a = actieList1.getSelectionModel().getSelectedItem();
        if (a != null) {
            actieList1.getItems().remove(a);
            actieList2.getItems().add(a);
        }
    }

    @FXML
    private void voegOefeningToeBtn(ActionEvent event) {
        Oefening o = oefeningList2.getSelectionModel().getSelectedItem();
        if (o != null) {
            oefeningList2.getItems().remove(o);
            oefeningList1.getItems().add(o);
        }
    }

    @FXML
    private void verwijderOefeningBtn(ActionEvent event) {
        Oefening o = oefeningList1.getSelectionModel().getSelectedItem();
        if (o != null) {
            oefeningList1.getItems().remove(o);
            oefeningList2.getItems().add(o);
        }
    }

    private void maakListeners() {
        // listener for choicebox sleect change
        soortOnderwijsChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            if (newVal == SoortOnderwijsEnum.dagonderwijs) {
                actieList1.setDisable(false);
                actieList2.setDisable(false);
                actieToevoegenBtn.setDisable(false);
                actieVerwijderenBtn.setDisable(false);
            }
            if (newVal == SoortOnderwijsEnum.afstandsonderwijs) {
                actieList1.setDisable(true);
                actieList2.setDisable(true);
                actieToevoegenBtn.setDisable(true);
                actieVerwijderenBtn.setDisable(true);
            }
        });
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(-1);
            this.fireEvent(annuleerEvent);
        });
    }
}
