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
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
    }

    @FXML
    public void createOefeningClicked(ActionEvent event) {
        oefeningenTable.getSelectionModel().clearSelection();
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
            laatstVerwijderd = controller.getOefening(event.getId());
            String opgaveNaam = new File(laatstVerwijderd.getOpgave()).getName();
            if (inBox) {
                int size = children.size();
                ((DetailsOefeningController)children.get(size - 1)).toggleButtons();
                if (size == 1) {
                    Node topNode = children.get(0);
                    children.set(0, new NotificatiePanelController(String.format("Oefening %s zit nog in een BreakoutBox", opgaveNaam), Kleuren.GEEL));
                    children.add(topNode);
                }
            } else {
                ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
                builder.addObserver(this);
                children.add(builder.buildConfirmation());
            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                oefeningenTable.getSelectionModel().select(controller.getMeestRecenteOefening());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Oefening is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            } else {
                oefeningenTable.getSelectionModel().select(controller.getOefening(event.getId()));
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
    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String opgaveNaam = new File(laatstVerwijderd.getOpgave()).getName();
            controller.deleteOefening(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Oefening met opgave %s is verwijderd", YouTiels.cutSentence(opgaveNaam)), Kleuren.GROEN));
        } else {
            children.remove(1);
            ((DetailsOefeningController) children.get(children.size() - 1)).toggleButtons();
        }
    }

}
