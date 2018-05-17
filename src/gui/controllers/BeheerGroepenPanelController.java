package gui.controllers;

import controllers.GroepController;
import domein.Kleuren;
import domein.Groep;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import gui.util.ConfirmationBuilder;
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
public class BeheerGroepenPanelController extends AnchorPane implements Observer {

    @FXML
    private Button createGroepButton;

    @FXML
    private TableView<Groep> groepenTableView;

    @FXML
    private TableColumn<Groep, String> naamCol;

    @FXML
    private TableColumn<Groep, String> leerlingenCol;

    @FXML
    private TextField filterTextField;

    @FXML
    private StackPane detailsStackPane;

    private ObservableList<Node> children;
    private GroepController controller;
    private Groep laatstVerwijderd;
    private SortedList<Groep> groepen;

    public BeheerGroepenPanelController(GroepController controller) {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerGroepenPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        groepen = controller.getGroepen().sorted(Comparator.comparing(Groep::getNaam));
        children = detailsStackPane.getChildren();

//        createGroepButton.setOnAction(event -> {
//            children.clear();
//            children.add(new CreateGroepController(controller));
//        });
        
        createGroepButton.setVisible(false);

        stelTableViewIn();

        voegEventHandlersToe();
    }

    @FXML
    public void filter(KeyEvent event) {
        String toFilter = filterTextField.getText();
        controller.applyFilter(toFilter);

    }

    private void stelTableViewIn() {
        naamCol.setCellValueFactory(c -> c.getValue().getNaamProp());
        leerlingenCol.setCellValueFactory(c -> c.getValue().getLeerlingenProp());
        groepenTableView.setItems(groepen);
        groepenTableView.setPlaceholder(new Label("Geen groepen"));
        groepenTableView.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsGroepController(newval));
            }
        });
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
//            children.add(new CreateGroepController(controller, event.getId()));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            boolean inSessie = controller.zitGroepInSessie(event.getId());
            laatstVerwijderd = controller.getGroep(event.getId());
            String groepsnaam = laatstVerwijderd.getNaam();
            if (inSessie) {
                int size = children.size();
                ((DetailsGroepController) children.get(size - 1)).toggleButtons();
                if (size == 1) {
                    Node topNode = children.get(0);
                    children.set(0, new NotificatiePanelController(String.format("Groep %s zit nog in een Sessie", groepsnaam), Kleuren.GEEL));
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
                groepenTableView.getSelectionModel().select(controller.getMeestRecenteGroep());
                children.add(new NotificatiePanelController("Groep is succesvol aangemaakt", Kleuren.GROEN));
            } else {
                groepenTableView.getSelectionModel().select(controller.getGroep(event.getId()));
                children.add(new NotificatiePanelController("Groep is succesvol gewijzigd", Kleuren.GROEN));
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() > 0) {
                children.add(new DetailsGroepController(controller.getGroep(event.getId())));
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String groepsnaam = laatstVerwijderd.getNaam();
            controller.deleteGroep(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Groep %s is verwijderd", YouTiels.cutSentence(groepsnaam)), Kleuren.GROEN));
        } else {
            children.remove(children.size() - 1);
            ((DetailsOefeningController) children.get(children.size() - 1)).toggleButtons();
        }
    }

}
