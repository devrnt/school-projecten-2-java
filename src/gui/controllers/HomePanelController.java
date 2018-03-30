package gui.controllers;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class HomePanelController implements Initializable {
    @FXML
    private Button oefeningBeheer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void oefeningBeheerClicked(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BeheerOefeningenController.class.getResource("../panels/BeheerOefeningenPanel.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) oefeningBeheer.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
}
