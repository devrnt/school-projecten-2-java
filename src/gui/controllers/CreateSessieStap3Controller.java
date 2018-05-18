/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Groep;
import domein.Klas;
import domein.Leerling;
import domein.Sessie;
import gui.events.DetailsEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Yanis
 */
public class CreateSessieStap3Controller extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button bevestigButton;
    @FXML
    private TableView<Leerling> leerlingenTbl;
    @FXML
    private TableColumn<Leerling, String> voorNaamList;
    @FXML
    private TableColumn<Leerling, String> familieNaamList;
    @FXML
    private ChoiceBox<Groep> groepChoiceBox;
    @FXML
    private ListView<String> leerlingenListView;
    @FXML
    private Button voegLlnToeBtn;

    private final Sessie sessie;
    private final SessieController sessieController;

    public CreateSessieStap3Controller(Sessie sessie, SessieController controller) {
        this.sessie = sessie;
        this.sessieController = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateSessieStap3.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
        addEventHandlers();
    }

    private void initialize() {
        // set table palceholder
        leerlingenTbl.setPlaceholder(new Label("Geen leerlingen meer"));
        // set table and columns
        leerlingenTbl.setItems(FXCollections.observableArrayList(sessie.getKlas().getLeerlingen()));
        voorNaamList.setCellValueFactory(cell -> cell.getValue().getVoornaamProperty());
        familieNaamList.setCellValueFactory(cell -> cell.getValue().getNaamProperty());

        // converter for groups
        groepChoiceBox.setConverter(new StringConverter<Groep>() {
            @Override
            public String toString(Groep groep) {
                return groep.getNaam();
            }

            @Override
            public Groep fromString(String klasNaam) {
                return groepChoiceBox.getItems()
                        .stream()
                        .filter(k -> k.getNaam().equals(klasNaam))
                        .findFirst()
                        .orElse(null);
            }
        });

        // set choicebox with groups
        groepChoiceBox.getItems().setAll(sessie.getGroepen());

        // select default first option
        groepChoiceBox.getSelectionModel().selectFirst();

        // listview disabel 
        leerlingenListView.setMouseTransparent(true);
        leerlingenListView.setFocusTraversable(false);
        leerlingenListView.setDisable(true);

        // btn is disabled on default
        voegLlnToeBtn.setDisable(true);
    }

    private void addEventHandlers() {
        groepChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            refreshListView();
        });

        leerlingenTbl.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> {
            if (newVal == null) {
                voegLlnToeBtn.setDisable(true);
            } else {
                voegLlnToeBtn.setDisable(false);
            }
        });
    }

    @FXML
    private void voegLlnToeBtnClicked(ActionEvent event) {
        // add the selected leerling to the group 
        Groep geselecteerdeGroep = groepChoiceBox.getSelectionModel().getSelectedItem();
        Leerling geselecteerdeLeerling = leerlingenTbl.getSelectionModel().getSelectedItem();
        geselecteerdeGroep.voegLeerlingToe(geselecteerdeLeerling);

        // add the selected leerling to the listview of the selectedgroep
        refreshListView();

        // remove added leerling from the tableView
        leerlingenTbl.getItems().remove(geselecteerdeLeerling);
    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        // check if every groep has leerlingen.count > 0
        boolean valid = sessie.getGroepen().stream().allMatch(groep -> groep.getAantalLeerlingen() > 0);
        System.out.println(valid);

        sessieController.createSessie(sessie.getNaam(), sessie.getOmschrijving(), sessie.getKlas(), sessie.getBox(), sessie.getDatum(), sessie.getSoortOnderwijs(), sessie.getFoutAntwoordActie(), sessie.getIsGedaan(), sessie.getGroepen());

        sessieController.getMeestRecenteSessie().getGroepen().get(0).getLeerlingen().forEach(lln -> System.out.println(lln));
        sessieController.getMeestRecenteSessie().getGroepen().get(1).getLeerlingen().forEach(lln -> System.out.println(lln));

        Event details = new DetailsEvent(90);
        this.fireEvent(details);
    }

    private void refreshListView() {
        Groep geselecteerdeGroep = groepChoiceBox.getSelectionModel().getSelectedItem();
        List<String> leerlingenUitGroep = geselecteerdeGroep.getLeerlingen().stream()
                .map(leerling -> leerling.getVolledigeNaam())
                .collect(Collectors.toList());

        leerlingenListView.getItems().setAll(leerlingenUitGroep);
    }
}
