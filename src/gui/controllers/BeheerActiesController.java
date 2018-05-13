/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.ActieController;
import domein.Actie;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerActiesController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button maakActieButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Actie> actieTabel;
    @FXML
    private TableColumn<Actie, String> omschrijvingCol;
    @FXML
    private StackPane detailsStackPane;

    private ActieController actieController;

    private ObservableList<Actie> acties;
    private ObservableList<Node> children;

    public BeheerActiesController(ActieController actieController) {
        this.actieController = actieController;
        acties = actieController.getAllActies();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerActiesPanel.fxml"));
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

    public final void initialize() {
        // set column
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());

        // default empty table placeholder
        actieTabel.setPlaceholder(new Label("Geen acties"));

        // make column sorting possible
        SortedList<Actie> sortedActies = new SortedList<>(acties);
        sortedActies.comparatorProperty().bind(actieTabel.comparatorProperty());

        // fill the table 
        actieTabel.setItems(sortedActies);

        // add listener on click to trigger details
        actieTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsActieController(newval));
            }
        });

        // filter
        searchTextField.setOnKeyReleased(event -> actieController.applyFilter(searchTextField.getText()));
    }

    @FXML
    private void maakActieButtonClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new CreateActieController(actieController));
    }

    private void voegEventHandlersToe() {
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
            try {
                actieController.deleteActie(event.getId());
                children.clear();
            } catch (RuntimeException e) {
                showDeleteFailedAlert();
            }
        });

        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateActieController(actieController, event.getId()));
        });
        
         this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0)
                children.add(new DetailsActieController(actieController.getActie(event.getId())));
        });

    }

    private void showDeleteFailedAlert() {
        AlertCS alert = new AlertCS(Alert.AlertType.WARNING);
        alert.setTitle("Acties beheren");
        alert.setHeaderText("Actie verwijderen");
        alert.setContentText("Actie kan niet verwijderd worden omdat deze nog in een box voorkomt");
        alert.showAndWait();
    }

}
