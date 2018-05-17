/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Kleuren;
import domein.Sessie;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.GeefSessieDoorEvent;
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
public class BeheerSessiesController extends AnchorPane implements Observer {

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerSessiesPanel.fxml"));
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
                children.add(new DetailsSessieController(newval, sessieController));
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
        detailsStackPane.getChildren().add(new DetailsSessieController(sessieController.getSessie(sessieId), sessieController));
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateSessieController(sessieController));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
            builder.addObserver(this);
            children.add(builder.buildConfirmation());
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            Sessie ses = sessieController.getMeestRecenteSessie();

            if (event.getId() == 90) {
                sessieTabel.getSelectionModel().select(sessieController.getMeestRecenteSessie());

                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Sessie is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            }
            //sessieTabel.getSelectionModel().select(sessieController.getMeestRecenteSessie());

            //Node topNode = children.get(0);
            //children.set(0, new NotificatiePanelController("Sessie is succesvol aangemaakt", Kleuren.GROEN));
            //children.add(topNode);
        });
        this.addEventHandler(GeefSessieDoorEvent.GEEFSESSIEDOOR, event -> {
            children.clear();
            children.add(new CreateSessieStap2Controller(sessieController, event.getSessie()));
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsSessieController(sessieController.getSessie(event.getId()), sessieController));
            }
        });

        this.addEventHandler(InvalidInputEvent.INVALIDINPUT, event -> {
            if (children.size() == 1) {
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", "#C62828"));
                children.add(topNode);
            }
        });
    }
    // </editor-fold>

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String sessieNaam = sessieController.getSessie(id).getNaam();
            sessieController.deleteSessie(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Sessie %s is verwijderd", sessieNaam), Kleuren.GROEN));
        } else {
            children.remove(children.size() - 1);
            ((DetailsSessieController) children.get(children.size() - 1)).toggleButton();
        }
    }

}
