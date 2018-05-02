package main;

import data.PopulateDB;
import gui.controllers.MenuPanelController;
import gui.controllers.HoofdPanelController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUpGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new PopulateDB().run();
//        Scene scene = new Scene(new HomePanelController());
        Scene scene = new Scene(new HoofdPanelController());

        // font family
        //scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Noto+Sans");
        
        // global stylesheet
        scene.getStylesheets().add(getClass().getResource("/gui/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("BreakoutBox");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

}
