package gui.controllers;

import domein.Oefening;
import gui.events.DeleteEvent;
import gui.events.WijzigEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sam
 */
public class DetailsOefeningController extends AnchorPane {

    @FXML
    private Label opgaveLabel;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label antwoordLabel;

    @FXML
    private Label vakLabel;

    @FXML
    private ListView<String> doelstellingenListView;

    @FXML
    private ListView<String> groepsbewerkingen;

    @FXML
    private Button wijzigBtn;

    @FXML
    private Button verwijderBtn;

    private Oefening oefening;

    public DetailsOefeningController(Oefening oefening) {
        this.oefening = oefening;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/DetailsOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (oefening != null) {
            setDetails();
            setTooltips();
            setButtonActions();
        }
    }

    private void setDetails() {
        opgaveLabel.setText(new File(oefening.getOpgave()).getName());
        feedbackLabel.setText(new File(oefening.getFeedback()).getName());
        antwoordLabel.setText(Integer.toString(oefening.getAntwoord()));
        vakLabel.setText(oefening.getVak());

        doelstellingenListView.getItems().addAll(FXCollections.observableArrayList(oefening.getDoelstellingen()));
        doelstellingenListView.setDisable(true);

        groepsbewerkingen.getItems().addAll(FXCollections.observableArrayList(
                oefening.getGroepsbewerkingen()
                        .stream()
                        .map(gb -> gb.getOmschrijving())
                        .collect(Collectors.toList())
        ));
        groepsbewerkingen.setDisable(true);
    }

    private void setButtonActions() {
        verwijderBtn.setOnAction(event -> {
//            toggleButtons();
            this.setDisable(true);
            Event deleteEvent = new DeleteEvent(oefening.getId());
            this.fireEvent(deleteEvent);
        });

        wijzigBtn.setOnAction(event -> {
            Event wijzigEvent = new WijzigEvent(oefening.getId());
            this.fireEvent(wijzigEvent);
        });
    }

    private void setTooltips() {
        // add tooltips to display full document path
        Tooltip opgaveTt = new Tooltip();
        opgaveTt.setText(oefening.getOpgave());

        Tooltip feedbackTt = new Tooltip();
        feedbackTt.setText(oefening.getFeedback());

        opgaveLabel.setTooltip(opgaveTt);
        feedbackLabel.setTooltip(feedbackTt);

    }
    public void toggleButtons() {
        Button[] btns = {wijzigBtn, verwijderBtn};
        Arrays.stream(btns).forEach(btn -> {
            btn.setVisible(!btn.isVisible());
        });
    }
}
