package gui.controllers;

import domein.Groep;
import domein.Leerling;
import gui.events.DeleteEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sam
 */
public class DetailsGroepController extends AnchorPane {

    @FXML
    private Label groepsnaamLabel;

    @FXML
    private ListView<Leerling> leerlingenListView;

    @FXML
    private Button wijzigBtn;

    @FXML
    private Button verwijderBtn;

    private Groep groep;
    private SortedList<Leerling> leerlingen;

    public DetailsGroepController(Groep groep) {
        this.groep = groep;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/DetailsGroep.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        wijzigBtn.setVisible(false);
        verwijderBtn.setVisible(false);

        if (groep != null) {
            setDetails();
            setButtonActions();
        }
    }

    private void setDetails() {
        groepsnaamLabel.setText(groep.getNaam());
        leerlingen = FXCollections.observableArrayList(groep.getLeerlingen()).sorted(Comparator.comparing(Leerling::getNaam).thenComparing(Leerling::getVoornaam));
        leerlingenListView.setItems(leerlingen);
    }

    private void setButtonActions() {
        verwijderBtn.setOnAction(event -> {
            toggleButtons();
            Event deleteEvent = new DeleteEvent(groep.getId());
            this.fireEvent(deleteEvent);
        });

        wijzigBtn.setOnAction(event -> {
            Event wijzigEvent = new WijzigEvent(groep.getId());
            this.fireEvent(wijzigEvent);
        });
    }

    public void toggleButtons() {
        Button[] btns = {wijzigBtn, verwijderBtn};
        Arrays.stream(btns).forEach(btn -> {
            btn.setVisible(!btn.isVisible());
        });
    }
}
