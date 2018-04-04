/*=================================================*/
/* ============== Gebruik StartUpGUI ==============*/
/*=================================================*/

//package main;
//
//import controllers.BoxController;
//import data.PopulateDB;
//import gui.controllers.BeheerBreakOutBoxPanelController;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class StartUpBoxGUI extends Application {
//
//    private BoxController boxController;
//
//    @Override
//    public void start(Stage stage) {
//        new PopulateDB().run();
//        boxController = new BoxController();
//        Scene scene = new Scene(new BeheerBreakOutBoxPanelController(boxController));
//        stage.setScene(scene);
//        stage.setTitle("Beheer Boxen");
//        stage.show();
//    }
//
//    public static void main(String... args) {
//        Application.launch(StartUpBoxGUI.class, args);
//    }
//}
