/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerSessiesController extends AnchorPane {

    @FXML
    private TableView<Sessie> sessieTabel;
    @FXML
    private TableColumn<Sessie, String> naamCol;
    @FXML
    private TableColumn<Sessie, String> omschrijvingCol;
    @FXML
    private Button detailsBtn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button maakSessieButton;

    private SessieController sessieController;
    private ObservableList<Sessie> sessies;

    public BeheerSessiesController(SessieController sessieController) {
        this.sessieController = sessieController;
        sessies = sessieController.getAllSessies();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerSessiesPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
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

        detailsBtn.setDisable(true);

        // als er geen sessie is geselecteerd in de table disable details knop
        sessieTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                detailsBtn.setDisable(false);
            }
        });

        searchTextField.setOnKeyReleased(event -> sessieController.applyFilter(searchTextField.getText()));
    }

    @FXML
    private void dubbelKlik(MouseEvent event) {
        if (event.getClickCount() == 2) {
            gaNaarSessieDetails();
        }
    }

    @FXML
    private void detailsBtnClicked(ActionEvent event) {
        gaNaarSessieDetails();
    }

    @FXML
    private void maakSessieButtonClicked(ActionEvent event) {
        Scene scene = new Scene(new CreateSessieController(sessieController));
        Stage stage = (Stage) maakSessieButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Aanmaken van een nieuwe sessie");
        stage.show();
    }

    // <editor-fold desc="=== Help methodes ===" >
    private void gaNaarSessieDetails() {
        int sessieId = sessieTabel.getSelectionModel().getSelectedItem().getId();
        Scene scene = new Scene(new DetailsSessieController(sessieController, sessieId));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Details sessie");
        stage.show();
    }
    // </editor-fold>

}
