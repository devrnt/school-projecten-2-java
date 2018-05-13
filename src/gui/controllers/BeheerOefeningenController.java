package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import java.io.File;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import utils.AlertCS;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class BeheerOefeningenController extends AnchorPane {

    @FXML
    private Button createOefening;

    @FXML
    private Button detailsBtn;

    @FXML
    private TableView<Oefening> oefeningenTable;

    @FXML
    private TableColumn<Oefening, String> opgaveCol;

    @FXML
    private TableColumn<Oefening, String> vakCol;

    @FXML
    private TableColumn<Oefening, String> doelstellingenCol;

    @FXML
    private TextField filterText;

    @FXML
    private StackPane detailsStackPane;

    private ObservableList<Node> children;

    private OefeningController controller;

    public BeheerOefeningenController(OefeningController controller) {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerOefeningenPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        children = detailsStackPane.getChildren();

        stelTableViewIn();

        voegEventHandlersToe();
    }

    @FXML
    public void createOefeningClicked(ActionEvent event) {
        children.clear();
        children.add(new CreateOefeningController(controller));
    }

    @FXML
    public void filter(KeyEvent event) {
        String toFilter = filterText.getText();
        controller.applyFilter(toFilter);

    }

    private void stelTableViewIn() {
        opgaveCol.setCellValueFactory(c -> c.getValue().getOpgaveProp());
        vakCol.setCellValueFactory(c -> c.getValue().getVakProp());
        doelstellingenCol.setCellValueFactory(c -> c.getValue().getDoelstellingenProp());
        oefeningenTable.setItems(controller.getOefeningen());
        oefeningenTable.setPlaceholder(new Label("Geen oefeningen"));
        oefeningenTable.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsOefeningController(newval));
            }
        });
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateOefeningController(controller, event.getId()));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            boolean inBox = controller.zitOefeningInBox(event.getId());
            String opgaveNaam = new File(controller.getOefening(event.getId()).getOpgave()).getName();
            if (inBox) {
                showDeleteFailedAlert(opgaveNaam);
            } else {
                AlertCS verwijderAlert = new AlertCS(Alert.AlertType.CONFIRMATION);
                verwijderAlert.setTitle("Verwijderen oefening");
                verwijderAlert.setHeaderText("Bevestigen");
                verwijderAlert.setContentText("Weet u zeker dat u deze oefening wil verwijderen?");
                verwijderAlert.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        controller.deleteOefening(event.getId());
                        children.clear();
                        VBox vbox = new VBox();
                        vbox.alignmentProperty().setValue(Pos.CENTER);
                        vbox.getChildren().add(new Label(String.format("Oefening met opgave %s is verwijderd", opgaveNaam)));
                        children.add(vbox);
                    }
                });
            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                int size = controller.getOefeningen().size();
                children.add(new DetailsOefeningController(controller.getOefeningen().get(size - 1)));
            } else {
                children.add(new DetailsOefeningController(controller.getOefening(event.getId())));
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() > 0) {
                children.add(new DetailsOefeningController(controller.getOefening(event.getId())));
            }
        });
    }

    private void showDeleteFailedAlert(String opgaveNaam) {
        AlertCS alert = new AlertCS(Alert.AlertType.WARNING);
        alert.setTitle("Oefening beheren");
        alert.setHeaderText("Oefening verwijderen");
        alert.setContentText(String.format("Oefening met opgave %s kan niet verwijderd worden omdat deze nog in een box voorkomt", opgaveNaam));
        alert.showAndWait();
    }

}
