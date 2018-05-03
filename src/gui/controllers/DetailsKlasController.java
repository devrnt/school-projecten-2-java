/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;
import domein.Klas;
import domein.Leerling;
import gui.events.DeleteEvent;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author ili
 */
public class DetailsKlasController extends AnchorPane {

    @FXML
    private Label klasNaamLbl;
       @FXML
    private TableView<Leerling> leerlingenTbl;
   @FXML
    private TableColumn<Leerling, String> voorNaamList;
    @FXML
    private TableColumn<Leerling, String> familieNaamList;
    @FXML
    private Button verwijderButton;
    

    private Klas klas;

    public DetailsKlasController(Klas klas) {
        this.klas = klas;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsKlas.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    private void initialize() {
                  leerlingenTbl.setItems(FXCollections.observableArrayList( klas.getLeerlingen()));
        voorNaamList.setCellValueFactory(cell -> cell.getValue().getVoornaamProperty());
        familieNaamList.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        klasNaamLbl.setText(klas.getNaam());
//        for (Leerling leerling : klas.getLeerlingen()) {
//            voorNaamList.getItems().add(leerling.getVoornaam());
//            familieNaamList.getItems().add(leerling.getNaam());
//        }
    }
       @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        Alert verwijderAlert = new Alert(Alert.AlertType.CONFIRMATION);
        verwijderAlert.setTitle("Verwijderen klas");
        verwijderAlert.setHeaderText("Bevestigen");
        verwijderAlert.setContentText("Weet u zeker dat u deze klas wilt verwijderen?");
        Optional<ButtonType> result = verwijderAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Event deleteEvent = new DeleteEvent(klas.getId());
            this.fireEvent(deleteEvent);
        }
    }

}
