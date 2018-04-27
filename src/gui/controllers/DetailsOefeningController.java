package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import java.io.IOException;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author sam
 */
public class DetailsOefeningController extends AnchorPane {

    private OefeningController controller;

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

    public DetailsOefeningController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        terugBtn.setOnAction(event -> terugNaarLijst());

    }
    
    public void setOefening(Oefening oefening){
        oefening = oefening;
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

    @FXML
    public void wijzigBtnClicked(ActionEvent event) {
        Scene scene = new Scene(new CreateOefeningController(controller, oefening.getId()));
        Stage stage = (Stage) wijzigBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Wijzig oefening");
        stage.show();
    }

    @FXML
    public void verwijderBtnClicked(ActionEvent event) {
        Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen oefening");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze oefening wil verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                controller.deleteOefening(oefening.getId());
                terugNaarLijst();
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
