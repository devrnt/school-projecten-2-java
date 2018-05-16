/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.GroepsbewerkingController;
import domein.Groepsbewerking;
import domein.Kleuren;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import gui.events.WijzigEvent;
import gui.util.ConfirmationBuilder;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import utils.AlertCS;
import utils.YouTiels;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerGroepsbewerkingenController extends AnchorPane implements Observer {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button maakGroepsbewBtn;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Groepsbewerking> groepsbewTbl;
    @FXML
    private TableColumn<Groepsbewerking, String> omschrijvingCol;
    @FXML
    private TableColumn<Groepsbewerking, String> factorCol;
    @FXML
    private TableColumn<Groepsbewerking, String> operatorCol;
    @FXML
    private StackPane detailsStackPane;

    private GroepsbewerkingController groepsbewerkingController;

    private ObservableList<Groepsbewerking> groepsbewerkingen;
    private ObservableList<Node> children;

    public BeheerGroepsbewerkingenController(GroepsbewerkingController groepsbewerkingController) {
        this.groepsbewerkingController = groepsbewerkingController;
        groepsbewerkingen = groepsbewerkingController.getAllGroepsbewerking();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerGroepsbewerkingenPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        children = detailsStackPane.getChildren();

        initialize();

        voegEventHandlersToe();
    }

    private void initialize() {
        // default empty table placeholder
        groepsbewTbl.setPlaceholder(new Label("Geen groepsbewerkingen"));

        // set columns
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());
        factorCol.setCellValueFactory(cell -> cell.getValue().getFactorProperty());
        operatorCol.setCellValueFactory(cell -> cell.getValue().getOperatorProperty());

        // make column sorting possible
        SortedList<Groepsbewerking> sortedGroepsbewerkingen = new SortedList<>(groepsbewerkingen);
        sortedGroepsbewerkingen.comparatorProperty().bind(groepsbewTbl.comparatorProperty());

        // fill the table
        groepsbewTbl.setItems(sortedGroepsbewerkingen);

        // add listener on click to trigger details
        groepsbewTbl.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsGroepsbewerkingController(newval));
            }
        });

        // filter
        searchTextField.setOnKeyReleased(event -> groepsbewerkingController.applyFilter(searchTextField.getText()));
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                groepsbewTbl.getSelectionModel().select(groepsbewerkingController.getMeestRecenteGroepsbewerking());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Groepsbewerking is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            } else {
                groepsbewTbl.getSelectionModel().select(groepsbewerkingController.getMeestRecenteGroepsbewerking());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Groepsbewerking is succesvol gewijzigd", Kleuren.GROEN));
                children.add(topNode);
            }
        });

        this.addEventFilter(DeleteEvent.DELETE, event -> {
            boolean zitGroepsbewInOef = groepsbewerkingController.zitGroepsbewerkingInOefening(event.getId());
            String groepsBewOmschrijving = groepsbewerkingController.getGroepsbewerking(event.getId()).getOmschrijving();
            if (zitGroepsbewInOef) {
                ((DetailsGroepsbewerkingController) children.get(0)).toggleButton();
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController(String.format("Groepsbewerking %s zit nog in een oefening", YouTiels.cutSentence(groepsBewOmschrijving)), Kleuren.GEEL));
                children.add(topNode);
            } else {
                ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
                builder.addObserver(this);
                children.add(builder.buildConfirmation());
            }
        });

        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateGroepsbewerkingController(groepsbewerkingController, event.getId()));
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsGroepsbewerkingController(groepsbewerkingController.getGroepsbewerking(event.getId())));
            }
        });

        this.addEventFilter(InvalidInputEvent.INVALIDINPUT, event -> {
            if (children.size() == 1) {
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", Kleuren.ROOD));
                children.add(topNode);
            }
        });

    }

    private void showDeleteFailedAlert() {
        AlertCS alert = new AlertCS(Alert.AlertType.WARNING);
        alert.setTitle("Groepsbewerking beheren");
        alert.setHeaderText("Groepsbewerking verwijderen");
        alert.setContentText("Groepsbewerking kan niet verwijderd worden omdat deze nog in een oefening voorkomt");
        alert.showAndWait();
    }

    @FXML
    private void maakGroepsbewBtnClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new CreateGroepsbewerkingController(groepsbewerkingController));
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String groepsbewOmschr = groepsbewerkingController.getGroepsbewerking(id).getOmschrijving();
            groepsbewerkingController.deleteGroepsbewerking(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Groepsbewerking %s is verwijderd", YouTiels.cutSentence(groepsbewOmschr)), Kleuren.GROEN));
        } else {
            children.remove(1);
            ((DetailsGroepsbewerkingController) children.get(0)).toggleButton();
        }
    }

}
