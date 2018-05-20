package gui.controllers;

import controllers.OefeningController;
import domein.Kleuren;
import domein.Oefening;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import utils.YouTiels;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class BeheerOefeningenController extends AnchorPane implements Observer {

    @FXML
    private Button createButton;

    @FXML
    private Button kopieButton;

    @FXML
    private TableView<Oefening> oefeningenTableView;

    @FXML
    private TableColumn<Oefening, String> opgaveCol;

    @FXML
    private TableColumn<Oefening, String> vakCol;

    @FXML
    private TableColumn<Oefening, String> doelstellingenCol;

    @FXML
    private TextField filterTextField;

    @FXML
    private StackPane detailsStackPane;

    private ObservableList<Node> children;

    private OefeningController controller;
    private ConfirmationBuilder confirmationBuilder;

    public BeheerOefeningenController(OefeningController controller) {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerOefeningenPanel.fxml"));

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

        setButtonActions();

        filterTextField.setOnKeyReleased(event -> {
            String toFilter = filterTextField.getText();
            controller.applyFilter(toFilter);
        });

    }

    private void stelTableViewIn() {
        opgaveCol.setCellValueFactory(c -> c.getValue().getOpgaveProp());
        opgaveCol.setSortable(false);
        vakCol.setCellValueFactory(c -> c.getValue().getVakProp());
        vakCol.setSortable(false);
        doelstellingenCol.setCellValueFactory(c -> c.getValue().getDoelstellingenProp());
        doelstellingenCol.setSortable(false);
        oefeningenTableView.setItems(controller.getOefeningen());
        oefeningenTableView.setPlaceholder(new Label("Geen oefeningen"));
        oefeningenTableView.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                kopieButton.setDisable(false);
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
            String opgaveNaam = controller.getOefening(event.getId()).getOpgaveNaam();
            if (inBox) {
                int size = children.size();
                children.get(size - 1).setDisable(false);
                Node topNode = children.remove(size - 1);
                children.set(0, new NotificatiePanelController(String.format("Oefening %s kan niet verwijderd worden."
                        + "%nReden: Zit nog in een BreakoutBox", YouTiels.cutSentence(opgaveNaam)), Kleuren.ROOD));
                children.add(topNode);
            } else {
                if (children.size() == 2) {
                    children.remove(0);
                }
                confirmationBuilder = new ConfirmationBuilder(event.getId(), "oefening");
                confirmationBuilder.addObserver(this);
                children.add(confirmationBuilder.buildConfirmation());
            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                oefeningenTableView.getSelectionModel().select(controller.getMeestRecenteOefening());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Oefening is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            } else {
                oefeningenTableView.getSelectionModel().select(controller.getOefening(event.getId()));
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Oefening is succesvol gewijzigd", Kleuren.GROEN));
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
            if (children.size() == 1) {
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", Kleuren.ROOD));
                children.add(topNode);
            }
        });
    }

    private void setButtonActions() {
        kopieButton.setDisable(true);
        kopieButton.setOnAction(event -> {
            children.clear();
            Oefening oef = oefeningenTableView.getSelectionModel().getSelectedItem();
            children.add(new CreateOefeningController(controller, oef.getId(), true));
        });

        createButton.setOnAction(event -> {
            oefeningenTableView.getSelectionModel().clearSelection();
            kopieButton.setDisable(true);
            children.clear();
            children.add(new CreateOefeningController(controller));
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = confirmationBuilder.isConfirmed();
        int id = confirmationBuilder.getId();
        if (confirmed) {
            String opgaveNaam = controller.getOefening(id).getOpgaveNaam();
            controller.deleteOefening(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Oefening met opgave %s is verwijderd", YouTiels.cutSentence(opgaveNaam)), Kleuren.GROEN));
        } else {
            children.remove(children.size() - 1);
            children.get(children.size() - 1).setDisable(false);
        }
    }

    //    private void undoDelete(DeleteEvent event, String opgaveNaam) {
//        /*=== Undo button code */
//        controller.deleteOefening(event.getId());
//        children.clear();
//        VBox vbox = new VBox();
//        vbox.alignmentProperty().setValue(Pos.CENTER);
//        Button ongedaanButton = new Button("Ongedaan maken");
//        ongedaanButton.setOnAction(clickEvent -> {
//            controller.createOefening(
//                    laatstVerwijderd.getOpgave(),
//                    laatstVerwijderd.getAntwoord(),
//                    laatstVerwijderd.getFeedback(),
//                    laatstVerwijderd.getVak(),
//                    laatstVerwijderd.getDoelstellingen(),
//                    laatstVerwijderd.getGroepsbewerkingen()
//            );
//            oefeningenTable.getSelectionModel().select(controller.getMeestRecenteOefening());
//        });
//        vbox.getChildren().add(new Label(String.format("Oefening met opgave %s is verwijderd", opgaveNaam)));
//        vbox.getChildren().add(ongedaanButton);
//        children.add(vbox);
//    }
}
