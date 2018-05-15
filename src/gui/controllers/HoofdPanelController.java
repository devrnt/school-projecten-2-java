package gui.controllers;

import controllers.ActieController;
import controllers.BoxController;
import controllers.GroepsbewerkingController;
import controllers.KlasController;
import controllers.OefeningController;
import controllers.SessieController;
import gui.events.MenuEvent;
import java.io.IOException;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author sam
 */
public class HoofdPanelController extends AnchorPane {

    @FXML
    private StackPane menuStackPane;

    @FXML
    private StackPane beheerStackPane;

    public HoofdPanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/HoofdPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MenuPanelController home = new MenuPanelController();

        // add menu
        menuStackPane.getChildren().add(home);

        // beheercomponents
        Node[] beheerNodes = {
            new BeheerOefeningenController(new OefeningController()),
            new BeheerBreakOutBoxPanelController(new BoxController()),
            new BeheerSessiesController(new SessieController()),
            new BeheerKlassenController(new KlasController()),
            new BeheerActiesController(new ActieController()),
            new BeheerGroepsbewerkingenController(new GroepsbewerkingController())
        };

        // zet alle beheercomponents op onzichtbaar
        Arrays.stream(beheerNodes).forEach(n -> n.setVisible(false));

        beheerStackPane.getChildren().addAll(beheerNodes);

        this.addEventHandler(MenuEvent.MENU, event -> {
            Arrays.stream(beheerNodes).forEach(n -> n.setVisible(false));
            beheerNodes[event.getId()].setVisible(true);
        });

    }

}
