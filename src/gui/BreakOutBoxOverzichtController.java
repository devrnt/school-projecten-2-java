package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import domein.BreakOutBox;
import controllers.BoxController;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;

public class BreakOutBoxOverzichtController extends GridPane {

    private BoxController boxController;
    private ObservableList<BreakOutBox> boxLijst;
    @FXML
    private TableView<BreakOutBox> tblBoxLijst;
    @FXML
    private TextField txtBoxLijst;

    public BreakOutBoxOverzichtController(BoxController boxController) {
        this.boxController = boxController;
        this.boxLijst = boxController.GeefAlleBreakOutBoxen();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BreakOutBoxOverzicht.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        TableColumn colNaam = new TableColumn("Naam");
        colNaam.setCellValueFactory(new PropertyValueFactory<>("naam"));
        TableColumn colOmschrijving = new TableColumn("Omschrijving");
        colOmschrijving.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
        tblBoxLijst.getColumns().addAll(colNaam, colOmschrijving);
        tblBoxLijst.getItems().addAll(this.boxController.GeefAlleBreakOutBoxen());
        initialize();
    }

    private void initialize() {
        FilteredList<BreakOutBox> filteredBoxLijst = new FilteredList<>(boxLijst, p -> true);
        txtBoxLijst.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBoxLijst.setPredicate(box -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (box.getNaam().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (box.getOmschrijving().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        SortedList<BreakOutBox> sortedBoxLijst = new SortedList<>(filteredBoxLijst);
        sortedBoxLijst.comparatorProperty().bind(tblBoxLijst.comparatorProperty());
        tblBoxLijst.setItems(sortedBoxLijst);

    }
}
