package gui.controllers;

import controllers.BoxController;
import domein.BreakOutBox;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BeheerBreakOutBoxPanelController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button maakBoxButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<BreakOutBox> boxTabel;
    @FXML
    private TableColumn<BreakOutBox, String> naamCol;
    @FXML
    private TableColumn<BreakOutBox, String> omschrijvingCol;
    @FXML
    private Button detailsButton;
    private BoxController boxController;
    private ObservableList<BreakOutBox> BreakOutBoxen;
    @FXML
    private Button keerTerugBtn;

    public BeheerBreakOutBoxPanelController(BoxController boxController) {
        this.boxController = boxController;
        BreakOutBoxen = boxController.getAllBreakOutBoxen();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerBreakOutBoxPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());
        boxTabel.setPlaceholder(new Label("Geen sessies"));
        boxTabel.setItems(BreakOutBoxen);
        keerTerugBtn.setOnAction(event -> terugNaarMenu());
        initialize();
    }

    private void initialize() {
        searchTextField.setOnKeyReleased(event -> boxController.applyFilter(searchTextField.getText()));
    }

    @FXML
    private void maakSessieButtonClicked(ActionEvent event) {
        Scene scene = new Scene(new CreateBreakOutBoxController(boxController));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Aanmaken van een BreakOutBox");
        stage.show();
    }

    @FXML
    private void detailsBtnClicked(ActionEvent event) {
        if (boxTabel.getSelectionModel().getSelectedItem() != null) {
            int id = boxTabel.getSelectionModel().getSelectedItem().getId();
            Scene scene = new Scene(new DetailsBreakOutBoxController(boxController, id));
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Wijzig oefening");
            stage.show();
        }
    }

    private void terugNaarMenu() {
        Scene scene = new Scene(new MenuPanelController());
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
}
