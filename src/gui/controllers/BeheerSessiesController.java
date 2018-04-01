/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerSessiesController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
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

        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());
        sessieTabel.setItems(sessieController.getAllSessies());
        initialize();
    }

    public void initialize() {
        FilteredList<Sessie> filteredSessieLijst = new FilteredList<>(sessies, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredSessieLijst.setPredicate(sessie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

                if (sessie.getNaam().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                    return true;
                } else if (sessie.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // No matches
            });
        });

        SortedList<Sessie> sortedSessies = new SortedList<>(filteredSessieLijst);
        sortedSessies.comparatorProperty().bind(sessieTabel.comparatorProperty());
        sessieTabel.setItems(sortedSessies);

        detailsBtn.setDisable(true);
        sessieTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                detailsBtn.setDisable(false);
            }
        });
    }

    @FXML
    private void detailsBtnClicked(ActionEvent event) {
        int id = sessieTabel.getSelectionModel().getSelectedItem().getId();
        Scene scene = new Scene(new DetailsSessieController(sessieController, id));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Wijzig oefening");
        stage.show();
    }

    @FXML
    private void maakSessieButtonClicked(ActionEvent event) {
        Scene scene = new Scene(new CreateSessieController(sessieController));
        Stage stage = (Stage) maakSessieButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Aanmaken van een nieuwe sessie");
        stage.show();
    }

}
