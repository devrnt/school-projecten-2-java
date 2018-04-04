package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class BeheerOefeningenController extends AnchorPane {

    private OefeningController controller;

    @FXML
    private Button createOefening;

    @FXML
    private Button detailsBtn;

    @FXML
    private TableView<Oefening> oefeningenTable;

    @FXML
    private TableColumn<Oefening, String> opgaveCol;

    @FXML
    private TableColumn<Oefening, Number> antwoordCol;
    
    @FXML
    private TextField filterText;

    public BeheerOefeningenController(OefeningController controller) {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerOefeningenPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        opgaveCol.setCellValueFactory(c -> c.getValue().getOpgaveProp());
        antwoordCol.setCellValueFactory(c -> c.getValue().getAntwoordProp());
        oefeningenTable.setItems(controller.getOefeningen());
        detailsBtn.setDisable(true);
        oefeningenTable.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                detailsBtn.setDisable(false);
            }
        });

    }

    @FXML
    public void createOefeningClicked(ActionEvent event) {

        Scene scene = new Scene(new CreateOefeningController(controller));
        Stage stage = (Stage) createOefening.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Aanmaken van een oefening");
        stage.show();

    }

    @FXML
    public void detailsBtnClicked(ActionEvent event) {
        int id = oefeningenTable.getSelectionModel().getSelectedItem().getId();
        Scene scene = new Scene(new DetailsOefeningController(controller, id));
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Oefening details");
        stage.show();
    }
    
    @FXML
    public void filter(KeyEvent event){
        String toFilter = filterText.getText();
        controller.applyFilter(toFilter);
    }

}
