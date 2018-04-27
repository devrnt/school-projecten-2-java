package gui.controllers;

import controllers.OefeningController;
import domein.Oefening;
import gui.events.BeheerEvent;
import gui.events.FilterEvent;
import gui.events.MenuEvent;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Button createOefening;

    @FXML
    private Button detailsBtn;

    @FXML
    private TableView<Oefening> oefeningenTable;

    @FXML
    private TableColumn<Oefening, String> opgaveCol;

    @FXML
    private TableColumn<Oefening, String> vakCol;
    
    @FXML
    private TableColumn<Oefening, String> doelstellingenCol;

    @FXML
    private TextField filterText;

    @FXML
    private Button keerTerugBtn;

    public BeheerOefeningenController(ObservableList<Oefening> oefeningen) {
        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerOefeningenPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        keerTerugBtn.setOnAction(event -> terugNaarMenu());

        opgaveCol.setCellValueFactory(c -> c.getValue().getOpgaveProp());
        vakCol.setCellValueFactory(c -> c.getValue().getVakProp());
        doelstellingenCol.setCellValueFactory(c -> c.getValue().getDoelstellingenProp());
        oefeningenTable.setItems(oefeningen);
        oefeningenTable.setPlaceholder(new Label("Geen oefeningen"));
        detailsBtn.setDisable(true);
        oefeningenTable.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                detailsBtn.setDisable(false);
            }
        });

    }

    @FXML
    public void createOefeningClicked(ActionEvent event) {

//        Scene scene = new Scene(new CreateOefeningController(controller));
//        Stage stage = (Stage) createOefening.getScene().getWindow();
//        stage.setScene(scene);
//        stage.setTitle("Aanmaken van een oefening");
//        stage.show();

    }

    @FXML
    public void detailsBtnClicked(ActionEvent event) {
        int id = oefeningenTable.getSelectionModel().getSelectedItem().getId();
        Event beheerEvent = new BeheerEvent(id);
        this.fireEvent(beheerEvent);
    }

    @FXML
    public void filter(KeyEvent event) {
        Event filterEvent = new FilterEvent(filterText.getText());
        this.fireEvent(filterEvent);
    }

    private void terugNaarMenu() {
        Scene scene = new Scene(new HomePanelController());
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }

}
