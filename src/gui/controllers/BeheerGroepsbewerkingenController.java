/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.GroepsbewerkingController;
import domein.Groepsbewerking;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerGroepsbewerkingenController extends AnchorPane {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerGroepsbewerkingenPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        children = detailsStackPane.getChildren();

        initialize();

       // voegEventHandlersToe();

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
                //children.add(new DetailsGroepsbewerkingController(newval));
            }
        });

        // filter
        searchTextField.setOnKeyReleased(event -> groepsbewerkingController.applyFilter(searchTextField.getText()));
    }

    /*private void voegEventHandlersToe() {
        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                int size = acties.size();
                children.add(new DetailsActieController(acties.get(size - 1)));
            } else {
                children.add(new DetailsActieController(actieController.getActie(event.getId())));
            }
        });

        this.addEventFilter(DeleteEvent.DELETE, event -> {
            boolean zitActieInBox = actieController.zitActieInBox(event.getId());
            System.out.println(zitActieInBox);
            if (zitActieInBox) {
                showDeleteFailedAlert();
            } else {
                actieController.deleteActie(event.getId());
                children.clear();
            }
        });

        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateActieController(actieController, event.getId()));
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsActieController(actieController.getActie(event.getId())));
            }
        });
    }*/

    @FXML
    private void maakGroepsbewBtnClicked(ActionEvent event) {
    }

}
