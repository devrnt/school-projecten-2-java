package gui.controllers;

import gui.events.MenuEvent;
import java.io.IOException;
import java.util.stream.IntStream;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class MenuPanelController extends AnchorPane {
      @FXML
      private ListView<String> menuListView;

//    @FXML
//    private Button oefeningBeheerBtn;
//
//    @FXML
//    private Button boxBeheerBtn;
//
//    @FXML
//    private Button sessieBeheerBtn;
//
//    @FXML
//    private Button beheerKlassenBtn;

    public MenuPanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/MenuPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
//        Button[] menuButtons = {oefeningBeheerBtn, boxBeheerBtn, sessieBeheerBtn, beheerKlassenBtn};
        
//        IntStream.range(0, menuButtons.length).forEach(i -> {
//            menuButtons[i].setOnAction(event -> {
//                Event menuEvent = new MenuEvent(i);
//                this.fireEvent(menuEvent);
//            });
//        });

        menuListView.getItems().addAll("Oefeningen", "Boxen", "Sessies", "Klassen");
        menuListView.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                Event menuEvent = new MenuEvent(menuListView.getSelectionModel().getSelectedIndex());
                this.fireEvent(menuEvent);
            }
        });
    }

}
