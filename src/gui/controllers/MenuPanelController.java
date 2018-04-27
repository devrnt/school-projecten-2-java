package gui.controllers;

import controllers.BoxController;
import controllers.KlasController;
import controllers.OefeningController;
import controllers.SessieController;
import gui.controllers.BeheerOefeningenController;
import gui.events.MenuEvent;
import java.io.IOException;
import java.util.stream.IntStream;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class MenuPanelController extends AnchorPane {

    @FXML
    private Button oefeningBeheerBtn;

    @FXML
    private Button boxBeheerBtn;

    @FXML
    private Button sessieBeheerBtn;

    @FXML
    private Button beheerKlassenBtn;

    public MenuPanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/MenuPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Button[] menuButtons = {oefeningBeheerBtn, boxBeheerBtn, sessieBeheerBtn, beheerKlassenBtn};
        
        IntStream.range(0, menuButtons.length).forEach(i -> {
            menuButtons[i].setOnAction(event -> {
                Event menuEvent = new MenuEvent(i);
                this.fireEvent(menuEvent);
            });
        });
    }

}
