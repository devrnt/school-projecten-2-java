package gui.controllers;

import controllers.OefeningController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sam
 */
public class CreateOefeningController extends AnchorPane {
    private OefeningController controller;

    @FXML
    private TextField opgave;

    @FXML
    private Label opgaveFout;

    @FXML
    private TextField antwoord;
    @FXML
    private Label antwoordFout;

    @FXML
    private TextField feedback;
    @FXML
    private Label feedbackFout;

    @FXML
    private ListView<String> groepsbewerkingen;

    @FXML
    private Label groepsbewerkingenFout;

    @FXML
    private Button bevestig;

    public CreateOefeningController(OefeningController controller) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateOefening.fxml"));
        
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        groepsbewerkingen.setItems(controller.getGroepsbewerkingen());
    }
    
    @FXML
    public void bevestigClicked(ActionEvent event) {
        controller.createOefening(opgave.getText(), Integer.parseInt(antwoord.getText()), feedback.getText(), null);
    }
    
    
}
