package gui.controllers;

import controllers.OefeningController;
import controllers.OefeningController;
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
    }
    
    @FXML
    public void createOefeningClicked(ActionEvent event) throws IOException {        
        
        Scene scene = new Scene(new CreateOefeningController(controller));
        Stage stage = (Stage) createOefening.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        
    }
    
}
