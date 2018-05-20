package gui.controllers;

import controllers.BoxController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.SoortOnderwijsEnum;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
    private Label naamFoutLbl;
    @FXML
    private TextField naamTxt;
    @FXML
    private TextField omschrijvingTxt;
    @FXML
    private Label omschrijvingFoutLbl;
    @FXML
    private Button bevestigBtn;
    @FXML
    private Button annuleerBtn;
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortOnderwijsChoiceBox;
    @FXML
    private Label titelLabel;
    @FXML
    private Label oefeningenFoutLbl;
    @FXML
    private ChoiceBox<Oefening> oefeningenChoiceBox;
    @FXML
    private Button addOefeningButton;
    @FXML
    private ListView<Oefening> oefeningenListView;
    @FXML
    private Button delOefeningButton;
    @FXML
    private Label actiesFoutLbl;
    @FXML
    private ChoiceBox<Actie> actieChoiceBox;
    @FXML
    private Button addActieButton;
    @FXML
    private ListView<Actie> actiesListView;
    @FXML
    private Button delActieButton;

    private final BoxController boxController;
    private final BreakOutBox box;
    private final int type;
    private ObservableList<Oefening> oefeningen;
    private ObservableList<Actie> acties;

    public CreateBreakOutBoxController(BreakOutBox box, BoxController boxController, int type) {
        //type: 0=create, 1 = kopie, 2= update
        this.box = box;
        this.boxController = boxController;
        this.oefeningen = boxController.getOefeningen();
        this.acties = boxController.getActies();
        this.type = type;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateBreakOutBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        oefeningenChoiceBox.setItems(oefeningen);
        oefeningenChoiceBox.getSelectionModel().selectFirst();
        actieChoiceBox.setItems(acties);
        actieChoiceBox.getSelectionModel().selectFirst();
        switch (type) {
            case 0:
                titelLabel.setText("Nieuwe BreakoutBox");
                break;
            case 1:
                titelLabel.setText("Kopie van BreakoutBox '" + box.getNaam() + "'");
                break;
            case 2:
                titelLabel.setText("Wijzigen van Breakoutbox '" + box.getNaam() + "'");
                break;
        }
        //choiceBoxes vullen
        vulChoiceBoxes();
        //listeners
        maakListeners();
        if (type != 0) {
            if (type == 1) {
                naamTxt.setText(box.getNaam() + " kopie");
            } else {
                naamTxt.setText(box.getNaam());
            }
            omschrijvingTxt.setText(box.getOmschrijving());
            for (Oefening oefening : box.getOefeningen()) {
                oefeningenListView.getItems().add(oefening);
            }
            oefeningen.removeAll(box.getOefeningen());
            for (Actie actie : box.getActies()) {
                actiesListView.getItems().add(actie);
            }
            acties.removeAll(box.getOefeningen());
        }
    }

    public CreateBreakOutBoxController(BoxController bc) {
        this(null, bc, 0);
    }

    @FXML
    private void bevestigClicked(ActionEvent event) {
        SoortOnderwijsEnum onderwijs = soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem();
        List<Actie> geselecteerdeActies;
        List<Oefening> geselecteerdeOefeningen = oefeningenListView.getItems();
        if (onderwijs == SoortOnderwijsEnum.dagonderwijs) {
            geselecteerdeActies = actiesListView.getItems();
        } else {
            geselecteerdeActies = new ArrayList<>();
        }
        if (checkInput()) {
            if (this.type == 2) {
                boxController.updateBreakOutBox(box.getId(), naamTxt.getText(), omschrijvingTxt.getText(), soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem(), geselecteerdeOefeningen, geselecteerdeActies);
            } else {
                boxController.createBreakOutBox(naamTxt.getText(), omschrijvingTxt.getText(), soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem(), geselecteerdeOefeningen, geselecteerdeActies);
            }
            Event beheerEvent = new DetailsEvent(type == 1 ? box.getId() : -1);
            this.fireEvent(beheerEvent);
        } else {
            Event invalidInputEvent = new InvalidInputEvent(new ArrayList<>());
            this.fireEvent(invalidInputEvent);
        }
    }

    @FXML
    private void addOefening(ActionEvent event) {
        Oefening o = oefeningenChoiceBox.getSelectionModel().getSelectedItem();
        oefeningen.remove(o);
        oefeningenFoutLbl.setText("");
        oefeningenListView.getItems().add(o);
        if (oefeningenChoiceBox.getItems().isEmpty()) {
            oefeningenChoiceBox.setDisable(true);
            addOefeningButton.setDisable(true);
        } else {
            oefeningenChoiceBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void delOefening(ActionEvent event) {
        Oefening o = oefeningenListView.getSelectionModel().getSelectedItem();
        oefeningen.add(o);
        oefeningenListView.getItems().remove(o);
        oefeningenChoiceBox.getSelectionModel().selectFirst();
        if (oefeningenListView.getItems().isEmpty()) {
            delOefeningButton.setDisable(true);
        }
        addOefeningButton.setDisable(false);
        oefeningenChoiceBox.setDisable(false);
        oefeningenListView.getSelectionModel().clearSelection();
        delOefeningButton.setDisable(true);

    }

    @FXML
    private void addActie(ActionEvent event) {
        Actie a = actieChoiceBox.getSelectionModel().getSelectedItem();
        acties.remove(a);
        actiesListView.getItems().add(a);
        actiesFoutLbl.setText("");
        if (actieChoiceBox.getItems().isEmpty()) {
            actieChoiceBox.setDisable(true);
            addActieButton.setDisable(true);
        } else {
            actieChoiceBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void delActie(ActionEvent event) {
        Actie a = actiesListView.getSelectionModel().getSelectedItem();
        acties.add(a);
        actiesListView.getItems().remove(a);
        actieChoiceBox.getSelectionModel().selectFirst();
        if (actiesListView.getItems().isEmpty()) {
            delActieButton.setDisable(true);
        }
        addActieButton.setDisable(false);
        actieChoiceBox.setDisable(false);
        actiesListView.getSelectionModel().clearSelection();
        delActieButton.setDisable(true);
    }

    private void maakListeners() {
        // listener for choicebox sleect change
        soortOnderwijsChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            if (newVal == SoortOnderwijsEnum.dagonderwijs) {
                setActiesVisible(true);
            }
            if (newVal == SoortOnderwijsEnum.afstandsonderwijs) {
                setActiesVisible(false);
            }
        });
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(box == null ? -1 : box.getId());
            this.fireEvent(annuleerEvent);
        });
        oefeningenListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
            if (newvalue != null) {
                delOefeningButton.setDisable(false);
            }
        });
        oefeningenListView.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (oefeningenListView.getSelectionModel().getSelectedItems() == null) {
                    oefeningenFoutLbl.setText("Selecteer minstens 1 groepsbewerking");
                } else {
                    oefeningenFoutLbl.setText("");
                }
            }
        });
        actiesListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
            if (newvalue != null) {
                delActieButton.setDisable(false);
            }
        });
        actiesListView.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (actiesListView.getSelectionModel().getSelectedItems() == null) {
                    actiesFoutLbl.setText("Selecteer minstens 1 groepsbewerking");
                } else {
                    actiesFoutLbl.setText("");
                }
            }
        });

    }

    private void setActiesVisible(Boolean bool) {
        actieChoiceBox.setVisible(bool);
        actiesFoutLbl.setVisible(bool);
        actiesListView.setVisible(bool);
    }

    private void vulChoiceBoxes() {
        oefeningenChoiceBox.setItems(oefeningen.sorted());
        oefeningenChoiceBox.getSelectionModel().selectFirst();
        actieChoiceBox.setItems(acties.sorted());
        actieChoiceBox.getSelectionModel().selectFirst();
        Arrays.asList(SoortOnderwijsEnum.values()).forEach(soortOnderwijs -> soortOnderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortOnderwijsChoiceBox.getSelectionModel().selectFirst();
    }

    private boolean checkInput() {
        SoortOnderwijsEnum onderwijs = soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem();
        List<Actie> geselecteerdeActies;
        List<Oefening> geselecteerdeOefeningen = oefeningenListView.getItems();
        if (onderwijs == SoortOnderwijsEnum.dagonderwijs) {
            geselecteerdeActies = actiesListView.getItems();
        } else {
            geselecteerdeActies = new ArrayList<>();
        }
        boolean inputGeldig = true;
        if (naamTxt.getText().isEmpty()) {
            inputGeldig = false;
            naamFoutLbl.setText("Voer een naam in");
        } else {
            if (type == 1 && boxController.bestaatBoxNaam(naamTxt.getText())) {
                naamFoutLbl.setText("Er bestaat al een box met deze naam.");
                inputGeldig = false;
            } else {
                naamFoutLbl.setText("");
            }
        }
        if (omschrijvingTxt.getText().isEmpty()) {
            inputGeldig = false;
            omschrijvingFoutLbl.setText("Voer een omschrijving in");
        } else {
            omschrijvingFoutLbl.setText("");
        }
        if (geselecteerdeOefeningen.isEmpty()) {
            inputGeldig = false;
            oefeningenFoutLbl.setText("Selecteer minstens 1 oefening");
        } else {
            oefeningenFoutLbl.setText("");
        }
        if (onderwijs == SoortOnderwijsEnum.dagonderwijs && geselecteerdeActies.isEmpty()) {
            actiesFoutLbl.setText("Selecteer minstens 1 actie");
            inputGeldig = false;
        } else {
            actiesFoutLbl.setText("");
        }
        if (onderwijs == SoortOnderwijsEnum.dagonderwijs && geselecteerdeActies.size() != geselecteerdeOefeningen.size() - 1) {
            actiesFoutLbl.setText(actiesFoutLbl.getText() + "Aantal oefeningen moet gelijk zijn aan  aantal acties + 1");
            inputGeldig = false;
        }
        return inputGeldig;
    }
}
