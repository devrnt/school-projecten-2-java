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
    private Label titelLabel;
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
    @FXML
    private Button annuleerBtn;
    @FXML
    private ChoiceBox<SoortOnderwijsEnum> soortOnderwijsChoiceBox;
    private final BoxController boxController;
    private final BreakOutBox box;
    private final Boolean isUpdate;

    public CreateBreakOutBoxController(BoxController boxController) {
        this.boxController = boxController;
        this.box = null;
        this.isUpdate = false;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateBreakOutBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        titelLabel.setText("Nieuwe BreakoutBox");
        
        //listviews vullen
        actieList2.setItems(boxController.getActies());
        oefeningList2.setItems(boxController.getOefeningen());
        //choicebox
        Arrays.asList(SoortOnderwijsEnum.values()).forEach(soortOnderwijs -> soortOnderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortOnderwijsChoiceBox.getSelectionModel().selectFirst();

        //listeners
        maakListeners();
    }

    public CreateBreakOutBoxController(BreakOutBox box, BoxController boxController, Boolean isUpdate) {
        this.boxController = boxController;
        this.box = box;
        this.isUpdate = isUpdate;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateBreakOutBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        titelLabel.setText("Wijzig BreakoutBox");
        
        
        //waarde vullen
        if (isUpdate) {
            naamTxt.setText(box.getNaam());
        } else {
            naamTxt.setText(box.getNaam() + " kopie");
        }
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
        Arrays.asList(SoortOnderwijsEnum.values()).forEach(soortOnderwijs -> soortOnderwijsChoiceBox.getItems().add(soortOnderwijs));
        soortOnderwijsChoiceBox.setValue(this.box.getSoortOnderwijsEnum());
        if (this.box.getSoortOnderwijsEnum() == SoortOnderwijsEnum.afstandsonderwijs) {
            setActiesDisabled(true);
        }
        //listeners
        maakListeners();
    }

    @FXML
    private void bevestigClicked(ActionEvent event) {
        SoortOnderwijsEnum onderwijs = soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem();
        List<Actie> geselecteerdeActies = actieList1.getItems();
        List<Oefening> geselecteerdeOefeningen = oefeningList1.getItems();
        if (checkInput()) {
            if (this.isUpdate) {
                boxController.updateBreakOutBox(box.getId(), naamTxt.getText(), omschrijvingTxt.getText(), soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem(), geselecteerdeOefeningen, geselecteerdeActies);
            } else {
                boxController.createBreakOutBox(naamTxt.getText(), omschrijvingTxt.getText(), soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem(), geselecteerdeOefeningen, geselecteerdeActies);
            }
            Event beheerEvent = new DetailsEvent(isUpdate ? box.getId() : -1);
            this.fireEvent(beheerEvent);
        } else {
            Event invalidInputEvent = new InvalidInputEvent(new ArrayList<>());
            this.fireEvent(invalidInputEvent);
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
        // listener for choicebox sleect change
        soortOnderwijsChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            if (newVal == SoortOnderwijsEnum.dagonderwijs) {
                setActiesDisabled(false);
            }
            if (newVal == SoortOnderwijsEnum.afstandsonderwijs) {
                setActiesDisabled(true);
            }
        });
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(-1);
            this.fireEvent(annuleerEvent);
        });
    }

    private void setActiesDisabled(Boolean bool) {
        actieList1.setDisable(bool);
        actieList2.setDisable(bool);
        actieToevoegenBtn.setDisable(bool);
        actieVerwijderenBtn.setDisable(bool);
    }

    private boolean checkInput() {
        SoortOnderwijsEnum onderwijs = soortOnderwijsChoiceBox.getSelectionModel().getSelectedItem();
        List<Actie> geselecteerdeActies = actieList1.getItems();
        List<Oefening> geselecteerdeOefeningen = oefeningList1.getItems();
        boolean inputGeldig = true;
        if (naamTxt.getText().isEmpty()) {
            inputGeldig = false;
            naamFoutLbl.setText("Voer een naam in");
        } else {
            naamFoutLbl.setText("");
        }
        if (omschrijvingTxt.getText().isEmpty()) {
            inputGeldig = false;
            naamFoutLbl.setText("Voer een naam in");
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
