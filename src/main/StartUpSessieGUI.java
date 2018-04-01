package main;

import controllers.BoxController;
import controllers.OefeningController;
import controllers.SessieController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
import gui.BreakOutBoxOverzichtController;
import gui.controllers.BeheerSessiesController;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import repository.GenericDaoJpa;

public class StartUpSessieGUI extends Application {

    @Override
    public void start(Stage stage) {
       
        //controllers
        SessieController sessieController = new SessieController();
        for (int i = 0; i < 3; i++) {
            sessieController.createSessie("sessie " + i, "Sessie " + i + " omschrijving hier...");
        }

        Scene scene = new Scene(new BeheerSessiesController(sessieController));
        stage.setTitle("Sessie Lijst");
        stage.setScene(scene);
        stage.setOnShown((WindowEvent t) -> {
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        });
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUpSessieGUI.class, args);
    }
}
