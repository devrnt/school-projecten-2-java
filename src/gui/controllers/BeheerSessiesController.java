/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerSessiesController extends AnchorPane {
    @FXML
    private StackPane detailsStackPane; 

    @FXML
    private TableView<Sessie> sessieTabel;
    @FXML
    private TableColumn<Sessie, String> naamCol;
    @FXML
    private TableColumn<Sessie, String> omschrijvingCol;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button maakSessieButton;

    private SessieController sessieController;
    private ObservableList<Sessie> sessies;
    private ObservableList<Node> children;

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
        
        children = detailsStackPane.getChildren();

        initialize();
        
        voegEventHandlersToe();
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


        // toont de geselecteerde sessie meteen in het detailvenster
        sessieTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsSessieController(newval));
            }
        });

        searchTextField.setOnKeyReleased(event -> sessieController.applyFilter(searchTextField.getText()));
        
        // eventhandlers
        this.addEventHandler(DeleteEvent.DELETE, event -> {
            detailsStackPane.getChildren().clear();
            sessieController.deleteSessie(event.getId());
        });
    }

    @FXML
    private void maakSessieButtonClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new CreateSessieController(sessieController));
    }

    // <editor-fold desc="=== Help methodes ===" >
    private void toonSessieDetails() {
        int sessieId = sessieTabel.getSelectionModel().getSelectedItem().getId();
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new DetailsSessieController(sessieController.getSessie(sessieId)));
    }

    private void terugNaarMenu() {
        Scene scene = new Scene(new MenuPanelController());
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
    
    private void voegEventHandlersToe(){
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateSessieController(sessieController));
        });
        
        this.addEventHandler(DeleteEvent.DELETE, event -> {
            children.clear();
            sessieController.deleteSessie(event.getId());
        });
        
        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0){
                int size = sessies.size();
                children.add(new DetailsSessieController(sessies.get(size - 1)));
            } else {
                children.add(new DetailsSessieController(sessieController.getSessie(event.getId())));
            }
        });
        
        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0)
                children.add(new DetailsSessieController(sessieController.getSessie(event.getId())));
        });
    }
    // </editor-fold>

    
    
}
