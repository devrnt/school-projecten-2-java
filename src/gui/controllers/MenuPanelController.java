package gui.controllers;

import gui.events.MenuEvent;
import java.io.IOException;
import java.util.stream.IntStream;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private final double WIDTH = 17;
    private final double HEIGHT = 17;

    private Image oefeningIco = new Image("/assets/icons/menu/oefeningIcon.png");
    private Image boxIco = new Image("/assets/icons/menu/boxIcon.png");
    private Image sessieIco = new Image("/assets/icons/menu/sessieIcon.png");
    private Image klasIco = new Image("/assets/icons/menu/klasIcon.png");

    private Image[] icons = {oefeningIco, boxIco, sessieIco, klasIco};

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
        menuListView.getItems().addAll("   Oefeningen", "   Boxen", "   Sessies", "   Klassen");
        menuListView.setCellFactory(param -> new ListCell<String>() {
            ImageView imageVw = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (name.trim()) {
                        case "Oefeningen":
                            imageVw.setImage(icons[0]);
                            break;
                        case "Boxen":
                            imageVw.setImage(icons[1]);
                            break;
                        case "Sessies":
                            imageVw.setImage(icons[2]);
                            break;
                        case "Klassen":
                            imageVw.setImage(icons[3]);
                            break;
                    }
                    if (name.equals("Oefeningen")) {
                        imageVw.setImage(icons[0]);
                    }
                    imageVw.setFitHeight(WIDTH);
                    imageVw.setFitWidth(HEIGHT);
                    setText(name);
                    setGraphic(imageVw);
                }

            }
        });
        menuListView.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                Event menuEvent = new MenuEvent(menuListView.getSelectionModel().getSelectedIndex());
                this.fireEvent(menuEvent);
            }
        });
    }

}
