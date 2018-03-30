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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class BeheerOefeningenController implements Initializable {
    private OefeningController controller;

    @FXML
    private Button createOefening;

    @FXML
    private TextField opgave;
    
    @FXML
    private TextField antwoord;
    
    @FXML
    private TextField feedback;
    
    @FXML
    private Button bevestig;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = new OefeningController();
    }

    @FXML
    public void createOefeningClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("../panels/CreateOefening.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) createOefening.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void bevestigClicked(ActionEvent event){
        controller.createOefening(opgave.getText(), Integer.parseInt(antwoord.getText()), feedback.getText(), new int[]{});
    }

}
