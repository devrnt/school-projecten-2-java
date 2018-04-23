package gui.controllers;

import controllers.BoxController;
import controllers.KlasController;
import controllers.OefeningController;
import controllers.SessieController;
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
    private Button oefeningBeheerBtn;

    @FXML
    private Button boxBeheerBtn;

    @FXML
    private Button sessieBeheerBtn;

    @FXML
    private Button beheerKlassenBtn;

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
    public void oefeningBeheerBtnClicked(ActionEvent event) {
        Stage stage = (Stage) oefeningBeheerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerOefeningenController(new OefeningController())));
        stage.setTitle("Beheer oefeningen");
        stage.show();
    }

    @FXML
    public void boxBeheerBtnClicked(ActionEvent event) {
        Stage stage = (Stage) oefeningBeheerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerBreakOutBoxPanelController(new BoxController())));
        stage.setTitle("Beheer BreakoutBoxen");
        stage.show();
    }

    @FXML
    public void sessieBeheerBtnClicked(ActionEvent event) {
        Stage stage = (Stage) oefeningBeheerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerSessiesController(new SessieController())));
        stage.setTitle("Beheer sessies");
        stage.show();
    }

    @FXML
    public void beheerKlassenBtnClicked(ActionEvent event) {
        Stage stage = (Stage) oefeningBeheerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerKlassenController(new KlasController())));
        stage.setTitle("Beheer klassen");
        stage.show();
    }

}
