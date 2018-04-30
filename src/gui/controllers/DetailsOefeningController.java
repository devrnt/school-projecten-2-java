package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import gui.events.DeleteEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    @FXML
    private Button terugBtn;

    private Oefening oefening;

    public DetailsOefeningController(Oefening oefening) {
        this.oefening = oefening;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsOefening.fxml"));

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
        }
    }

    private void setDetails() {
        opgaveLabel.setText(oefening.getOpgave());
        feedbackLabel.setText(oefening.getFeedback());
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

    private void setTooltips() {
        // add tooltips to display full document path
        Tooltip opgaveTt = new Tooltip();
        opgaveTt.setText(oefening.getOpgave());

        Tooltip feedbackTt = new Tooltip();
        feedbackTt.setText(oefening.getFeedback());

        opgaveLabel.setTooltip(opgaveTt);
        feedbackLabel.setTooltip(feedbackTt);

    }

    @FXML
    public void wijzigBtnClicked(ActionEvent event) {
        Event wijzigEvent = new WijzigEvent(oefening.getId());
        this.fireEvent(wijzigEvent);
    }

    @FXML
    public void verwijderBtnClicked(ActionEvent event) {
        Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen oefening");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze oefening wil verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                Event deleteEvent = new DeleteEvent(oefening.getId());
                this.fireEvent(deleteEvent);
            }
        });
    }

    private void terugNaarLijst() {
//        Scene scene = new Scene(new BeheerOefeningenController(controller));
//        Stage stage = (Stage) wijzigBtn.getScene().getWindow();
//        stage.setTitle("Beheer Oefeningen");
//        stage.setScene(scene);
//        stage.show();
    }

//    @FXML
//    public void bevestigClicked(ActionEvent event) {
//
//        controller.deleteOefening(1);
//
//        Alert oefeningCreatedSuccess = new Alert(Alert.AlertType.INFORMATION);
//        oefeningCreatedSuccess.setTitle("Oefening");
//        oefeningCreatedSuccess.setHeaderText("Verwijderen van een oefening");
//        oefeningCreatedSuccess.setContentText("Oefening is succesvol verwijderd");
//        oefeningCreatedSuccess.showAndWait();
//        Scene scene = new Scene(new BeheerOefeningenController(controller));
//        Stage stage = (Stage) opgaveLabel.getScene().getWindow();
//        stage.setTitle("Beheer Oefeningen");
//        stage.setScene(scene);
//        stage.show();
//
//    }
}
