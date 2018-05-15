package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import gui.events.WijzigEvent;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
    private Oefening laatstVerwijderd;
    private SortedList<Oefening> oefeningen;

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

        oefeningen = controller.getOefeningen().sorted(Comparator.comparing(Oefening::getOpgave));
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
        oefeningenTable.setItems(oefeningen);
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
            laatstVerwijderd = controller.getOefening(event.getId());
            String opgaveNaam = new File(laatstVerwijderd.getOpgave()).getName();
            if (inBox) {
                ((DetailsOefeningController) children.get(0)).toggleButtons();
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController(String.format("Oefening zit nog in een BreakoutBox", opgaveNaam), "#C62828"));
                children.add(topNode);
            } else {
                showConfirmation(event, opgaveNaam);
            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                oefeningenTable.getSelectionModel().select(controller.getMeestRecenteOefening());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Oefening is succesvol aangemaakt", "#28BB66"));
                children.add(topNode);
            } else {
                oefeningenTable.getSelectionModel().select(controller.getOefening(event.getId()));
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Oefening is succesvol gewijzigd", "#28BB66"));
                children.add(topNode);
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() > 0) {
                children.add(new DetailsOefeningController(controller.getOefening(event.getId())));
            }
        });

        this.addEventHandler(InvalidInputEvent.INVALIDINPUT, event -> {
            Node topNode = children.get(0);
            children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", "#C62828"));
            children.add(topNode);
        });
    }

    private void showConfirmation(DeleteEvent event, String opgaveNaam) {
        /* === confirmation buttons === */
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setAlignment(Pos.CENTER);
        hbox.minHeight(50.0);
        Button okButton = new Button("Ja");
        okButton.setStyle("margin-right:7;");
        okButton.setOnAction(event2 -> {
            controller.deleteOefening(event.getId());
            children.clear();
            children.add(new NotificatiePanelController(String.format("Oefening met opgave %s is verwijderd", opgaveNaam), "#28BB66"));
        });
        okButton.getStyleClass().add("btn");
        okButton.getStyleClass().add("btn-small");

        Button cancelButton = new Button("Nee");
        cancelButton.setOnAction(event2 -> {
            children.remove(1);
            ((DetailsOefeningController) children.get(0)).toggleButtons();
        });
        cancelButton.getStyleClass().add("btn");
        cancelButton.getStyleClass().add("btn-small");

        // show confirmation text and buttons
        hbox.getChildren().addAll(cancelButton, okButton);
        vbox.getChildren().addAll(new Label("Weet u zeker dat u deze oefening wil verwijderen?"), hbox);
        children.add(vbox);
    }

    private void undoDelete(DeleteEvent event, String opgaveNaam) {
        /*=== Undo button code */
        controller.deleteOefening(event.getId());
        children.clear();
        VBox vbox = new VBox();
        vbox.alignmentProperty().setValue(Pos.CENTER);
        Button ongedaanButton = new Button("Ongedaan maken");
        ongedaanButton.setOnAction(clickEvent -> {
            controller.createOefening(
                    laatstVerwijderd.getOpgave(),
                    laatstVerwijderd.getAntwoord(),
                    laatstVerwijderd.getFeedback(),
                    laatstVerwijderd.getVak(),
                    laatstVerwijderd.getDoelstellingen(),
                    laatstVerwijderd.getGroepsbewerkingen()
            );
            oefeningenTable.getSelectionModel().select(controller.getMeestRecenteOefening());
        });
        vbox.getChildren().add(new Label(String.format("Oefening met opgave %s is verwijderd", opgaveNaam)));
        vbox.getChildren().add(ongedaanButton);
        children.add(vbox);
    }

}
