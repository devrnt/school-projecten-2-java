/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.ActieController;
import domein.Actie;
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
public class BeheerActiesController extends AnchorPane implements Observer {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerActiesPanel.fxml"));
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
                actieTabel.getSelectionModel().select(actieController.getMeestRecenteActie());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Actie is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            } else {
                actieTabel.getSelectionModel().select(actieController.getMeestRecenteActie());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Actie is succesvol gewijzigd", Kleuren.GROEN));
                children.add(topNode);
            }
        });

        this.addEventFilter(DeleteEvent.DELETE, event -> {
            boolean inBox = actieController.zitActieInBox(event.getId());
            String actieOmschrijving = actieController.getActie(event.getId()).getOmschrijving();
            if (inBox) {
                ((DetailsActieController) children.get(0)).toggleButton();
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController(String.format("Actie zit nog in een BreakOutBox", actieOmschrijving), Kleuren.GEEL));
                children.add(topNode);
            } else {
                ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
                builder.addObserver(this);
                children.add(builder.buildConfirmation());
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

        this.addEventFilter(InvalidInputEvent.INVALIDINPUT, event -> {
            Node topNode = children.get(0);
            children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", "#C62828"));
            children.add(topNode);
        });

    }

    private void showDeleteFailedAlert() {
        AlertCS alert = new AlertCS(Alert.AlertType.WARNING);
        alert.setTitle("Acties beheren");
        alert.setHeaderText("Actie verwijderen");
        alert.setContentText("Actie kan niet verwijderd worden omdat deze nog in een box voorkomt");
        alert.showAndWait();
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String actieOmschrijving = actieController.getActie(id).getOmschrijving();
            actieController.deleteActie(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Actie %s is verwijderd", YouTiels.cutSentence(actieOmschrijving)), Kleuren.GROEN));
        } else {
            children.remove(1);
            ((DetailsActieController) children.get(0)).toggleButton();
        }
    }

}
