package gui.controllers;

import controllers.BoxController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

public class UpdateBreakOutBoxController extends AnchorPane {

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
    private Label wijzigLbl;
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
    private Button annuleerBtn;

    private final BoxController boxController;
    private final BreakOutBox box;

    public UpdateBreakOutBoxController(BreakOutBox box, BoxController boxController) {
        this.boxController = boxController;
        this.box = box;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateBreakOutBox.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //waarde vullen
        naamTxt.setText(box.getNaam());
        omschrijvingTxt.setText(box.getOmschrijving());
        //listviews vullen
        actieList2.setItems(boxController.getActies());
        for (Actie a : box.getActies()) {
            actieList2.getItems().remove(a);
            actieList1.getItems().add(a);
        }
        oefeningList2.setItems(boxController.getOefeningen());
        for (Oefening o : box.getOefeningen()) {
            oefeningList2.getItems().remove(o);
            oefeningList1.getItems().add(o);
        }
        //listeners
        maakListeners();
    }

    @FXML
    private void bevestigClicked(ActionEvent event) {
        boolean inputGeldig = true;
        List<Actie> geselecteerdeActies = actieList1.getItems();
        List<Oefening> geselecteerdeOefeningen = oefeningList1.getItems();
        if (geselecteerdeActies.isEmpty() || geselecteerdeOefeningen.isEmpty() || naamTxt.getText().isEmpty() || omschrijvingTxt.getText().isEmpty()) {
            inputGeldig = false;
        }
        if (geselecteerdeActies.size() != geselecteerdeOefeningen.size() - 1) {
            inputGeldig = false;
        }

        if (inputGeldig) {
            boxController.updateBreakOutBox(box.getId(), naamTxt.getText(), omschrijvingTxt.getText(), geselecteerdeOefeningen, geselecteerdeActies);
            AlertCS BreakOutBoxCreatedSucces = new AlertCS(Alert.AlertType.INFORMATION);
            BreakOutBoxCreatedSucces.setTitle("BreakOutBox");
            BreakOutBoxCreatedSucces.setHeaderText("Wijzigen van een box");
            BreakOutBoxCreatedSucces.setContentText("BreakOutBox is succesvol gewijwigd");
            BreakOutBoxCreatedSucces.showAndWait();
            terugNaarDetails();

        } else {
            AlertCS invalidInput = new AlertCS(Alert.AlertType.ERROR);
            invalidInput.setTitle("Box aanmaken");
            invalidInput.setHeaderText("Er zijn nog ongeldige velden");
            invalidInput.setContentText("Pas de invoer aan zodat deze geldig worden");
            invalidInput.showAndWait();
        }
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
        naamTxt.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (naamTxt.getText() == null || naamTxt.getText().trim().length() == 0) {
                    naamFoutLbl.setText("Geef een naam in");
                } else {
                    naamFoutLbl.setText("");
                }
            }
        });
        omschrijvingTxt.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (omschrijvingTxt.getText() == null || omschrijvingTxt.getText().trim().length() == 0) {
                    omschrijvingFoutLbl.setText("Geef een omschrijving in");
                } else {
                    omschrijvingFoutLbl.setText("");
                }
            }
        });
        actieList1.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (actieList1.getItems().isEmpty()) {
                    actiesFoutLbl.setText("Selecteer minstens 1 actie");
                } else {
                    actiesFoutLbl.setText("");
                }
            }
        });
        oefeningList1.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (oefeningList1.getItems().isEmpty()) {
                    oefeningenFoutLbl.setText("Selecteer minstens 1 oefening");
                } else {
                    oefeningenFoutLbl.setText("");
                }
            }
        });
        annuleerBtn.setOnAction(event -> {
            terugNaarDetails();

        });
    }

    private void terugNaarDetails() {
        Event annuleerEvent = new AnnuleerEvent(box.getId());
        this.fireEvent(annuleerEvent);
    }
}
