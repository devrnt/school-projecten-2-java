package gui.controllers;

import controllers.OefeningController;
import controllers.OefeningController;
import domein.Oefening;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    }

//    @FXML
//    public void updateOefeningClicked(ActionEvent event) throws IOException {        
//        
//        Scene scene = new Scene(new UpdateOefeningController(controller));
//        Stage stage = (Stage) updateOefening.getScene().getWindow();
//        stage.setScene(scene);
//        stage.setTitle("Wijzigen van een oefening");
//        stage.show();
//        
//    }
//    
//    @FXML
//    public void deleteOefeningClicked(ActionEvent event) throws IOException {        
//        
//        Scene scene = new Scene(new DeleteOefeningController(controller));
//        Stage stage = (Stage) deleteOefening.getScene().getWindow();
//        stage.setScene(scene);
//        stage.setTitle("Verwijderen van een oefening");
//        stage.show();
//        
//    }
}
