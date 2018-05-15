/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerSessiesController extends AnchorPane {

    @FXML
    private StackPane detailsStackPane;

    @FXML
    private TableView<Sessie> sessieTabel;
    @FXML
    private TableColumn<Sessie, String> naamCol;
    @FXML
    private TableColumn<Sessie, String> omschrijvingCol;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button maakSessieButton;

    private SessieController sessieController;
    private ObservableList<Sessie> sessies;
    private ObservableList<Node> children;

    public BeheerSessiesController(SessieController sessieController) {
        this.sessieController = sessieController;
        sessies = sessieController.getAllSessies();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerSessiesPanel.fxml"));
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
        /* ==== Code is verhuisd naar SessieController methode -> testbaar ==== */
        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());

        sessieTabel.setPlaceholder(new Label("Geen sessies"));

        // zorg dat de kolom gesorteerd kan worden
        SortedList<Sessie> sortedSessies = new SortedList<>(sessies);
        sortedSessies.comparatorProperty().bind(sessieTabel.comparatorProperty());

        sessieTabel.setItems(sortedSessies);

        // toont de geselecteerde sessie meteen in het detailvenster
        sessieTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsSessieController(newval));
            }
        });

        searchTextField.setOnKeyReleased(event -> sessieController.applyFilter(searchTextField.getText()));
    }

    @FXML
    private void maakSessieButtonClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new CreateSessieController(sessieController));
    }

    // <editor-fold desc="=== Help methodes ===" >
    private void toonSessieDetails() {
        int sessieId = sessieTabel.getSelectionModel().getSelectedItem().getId();
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new DetailsSessieController(sessieController.getSessie(sessieId)));
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateSessieController(sessieController));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            showConfirmation(event, sessieController.getSessie(event.getId()).getNaam());
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            sessieTabel.getSelectionModel().select(sessieController.getMeestRecenteSessie());
            Node topNode = children.get(0);
            children.set(0, new NotificatiePanelController("Sessie is succesvol aangemaakt", "#28BB66"));
            children.add(topNode);
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsSessieController(sessieController.getSessie(event.getId())));
            }
        });

        this.addEventHandler(InvalidInputEvent.INVALIDINPUT, event -> {
            Node topNode = children.get(0);
            children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", "#C62828"));
            children.add(topNode);
        });
    }
    // </editor-fold>

    private void showConfirmation(DeleteEvent event, String sessieNaam) {
        /* === confirmation buttons === */
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setAlignment(Pos.CENTER);
        hbox.minHeight(50.0);
        Button okButton = new Button("Ja");
        okButton.setStyle("margin-right:7;");
        okButton.setOnAction(event2 -> {
            sessieController.deleteSessie(event.getId());
            children.clear();
            children.add(new NotificatiePanelController(String.format("Sessie %s is verwijderd", sessieNaam), "#28BB66"));
        });
        okButton.getStyleClass().add("btn");
        okButton.getStyleClass().add("btn-small");
        okButton.getStyleClass().add("btn-default");

        Button cancelButton = new Button("Nee");
        cancelButton.setOnAction(event2 -> {
            children.remove(1);
            ((DetailsSessieController) children.get(0)).toggleButton();
        });
        cancelButton.getStyleClass().add("btn");
        cancelButton.getStyleClass().add("btn-small");
        cancelButton.getStyleClass().add("btn-default");

        // show confirmation text and buttons
        hbox.getChildren().addAll(cancelButton, okButton);
        vbox.getChildren().addAll(new Label("Weet u zeker dat u deze sessie wil verwijderen?"), hbox);
        children.add(vbox);
    }

}
