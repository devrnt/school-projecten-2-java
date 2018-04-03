package main;

import controllers.BoxController;
import domein.Actie;
import domein.Oefening;
import domein.Toegangscode;
import gui.controllers.BeheerBreakOutBoxPanelController;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import repository.GenericDaoJpa;

public class StartUpBoxGUI extends Application {

    @Override
    public void start(Stage stage) {
        //open connectie
        GenericDaoJpa.startTransaction();
        //controllers
        BoxController boxController = new BoxController();
        for (int i = 0; i < 3; i++) {
            boxController.createBreakOutBox("box" + i, "Omschrijving" + i, new ArrayList<Oefening>(), new ArrayList<Actie>(), new ArrayList<Toegangscode>());
        }
        GenericDaoJpa.commitTransaction();

        Scene scene = new Scene(new BeheerBreakOutBoxPanelController(boxController));
        stage.setTitle("Box Lijst test");
        stage.setScene(scene);
        stage.setOnShown((WindowEvent t) -> {
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        });
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUpBoxGUI.class, args);
    }
}
