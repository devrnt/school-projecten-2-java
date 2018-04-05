package gui.controllers;

import controllers.BoxController;
import domein.BreakOutBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DetailsBreakOutBoxController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label naamLabel;
    @FXML
    private Label omschrijvingLabel;
    @FXML
    private ListView<String> actieList;
    @FXML
    private ListView<String> oefeningList;
    @FXML
    private ListView<String> toegangscodeList;
    @FXML
    private Button wijzigButton;
    @FXML
    private Button verwijderButton;

    private BoxController boxController;
    private BreakOutBox breakOutBox;
    private int id;

    public DetailsBreakOutBoxController(BoxController boxController, int id) {
        this.boxController = boxController;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsBreakOutBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.id = id;
        this.breakOutBox = boxController.GeefBreakOutBox(id);
        naamLabel.setText(breakOutBox.getNaam());
        omschrijvingLabel.setText(breakOutBox.getOmschrijving());

        oefeningList.setItems(boxController.getOefeningenByBox(id));
        oefeningList.setDisable(true);
        actieList.setItems(boxController.getActiesByBox(id));
        actieList.setDisable(true);
        toegangscodeList.setItems(boxController.getToegangscodesByBox(id));
        toegangscodeList.setDisable(true);
    }

    @FXML
    private void wijzigBtnClicked(ActionEvent event) {
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
         Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen BreakOutBox");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze BreakOutBox wil verwijderen?");
        verwijderAlert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                System.out.println("test");
                boxController.deleteBreakOutBox(breakOutBox.getId());
                terugNaarLijst();
            }
        });
    }
    
     private void terugNaarLijst() {
        Scene scene = new Scene(new BeheerBreakOutBoxPanelController(boxController));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Beheer Oefeningen");
        stage.setScene(scene);
        stage.show();
    }
}
