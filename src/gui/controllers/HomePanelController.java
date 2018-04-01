package gui.controllers;

import controllers.OefeningController;
import gui.controllers.BeheerOefeningenController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class HomePanelController extends AnchorPane {
    @FXML
    private Button oefeningBeheer;

    public HomePanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/HomePanel.fxml"));
        
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    

    @FXML
    public void oefeningBeheerClicked(ActionEvent event){
        Stage stage = (Stage) oefeningBeheer.getScene().getWindow();
        stage.setScene(new Scene(new BeheerOefeningenController(new OefeningController())));
        stage.setTitle("Beheer oefeningen");
        stage.show();
    }
    
}
