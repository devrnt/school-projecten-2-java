/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.ActieController;
import domein.Actie;
import gui.events.DeleteEvent;
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

        // filter
        searchTextField.setOnKeyReleased(event -> actieController.applyFilter(searchTextField.getText()));
    }

    @FXML
    private void maakActieButtonClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        //detailsStackPane.getChildren().add(new CreateActieController(actieController));
    }

    private void voegEventHandlersToe() {
       
    }

}
