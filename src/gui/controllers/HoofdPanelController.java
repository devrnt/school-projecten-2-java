package gui.controllers;

import controllers.BoxController;
import controllers.Controller;
import controllers.OefeningController;
import controllers.SessieController;
import domein.Oefening;
import gui.events.BeheerEvent;
import gui.events.FilterEvent;
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

    @FXML
    private StackPane detailsStackPane;
    
    private int currentIndex;
    
    private final BoxController boxController = new BoxController();
    private final SessieController sessieController = new SessieController();
    private final OefeningController oefeningController = new OefeningController();

    public HoofdPanelController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/HoofdPanel.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HomePanelController home = new HomePanelController();
        
        // controllers
        Controller[] controllers = {null, null, oefeningController};
        
        // add menu
        menuStackPane.getChildren().add(home);

        // beheer components
        Node[] beheerNodes = {
            new BeheerBreakOutBoxPanelController(boxController),
            new BeheerSessiesController(sessieController),
            new BeheerOefeningenController(oefeningController.getOefeningen())
        };

        // zet alle beheercomponents op onzichtbaar
        Arrays.stream(beheerNodes).forEach(n -> n.setVisible(false));

        beheerStackPane.getChildren().addAll(beheerNodes);
        
        this.addEventHandler(MenuEvent.MENU, event -> {
            currentIndex = event.getId();
            beheerNodes[currentIndex].setVisible(true);
        });
        
        this.addEventHandler(FilterEvent.FILTER, event -> {
            controllers[currentIndex].applyFilter(event.getFilterName());
        });
        
        
        

        Node[] detailNodes = {
            new DetailsBreakOutBoxController(boxController, 1),
            new DetailsSessieController(sessieController, 1),
            new DetailsOefeningController()
        };
        
        Arrays.stream(detailNodes).forEach(n -> n.setVisible(false));
        detailsStackPane.getChildren().addAll(detailNodes);
        
        this.addEventHandler(BeheerEvent.BEHEER, event -> {
            detailNodes[currentIndex].setVisible(true);
            ((DetailsOefeningController) detailNodes[currentIndex]).setOefening((Oefening)controllers[currentIndex].getById(event.getId()));
        });
        

    }

}
