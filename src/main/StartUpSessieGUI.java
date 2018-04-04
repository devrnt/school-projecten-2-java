/*=================================================*/
/* ============== Gebruik StartUpGUI ==============*/
/*=================================================*/


//package main;
//
//import controllers.BoxController;
//import controllers.OefeningController;
//import controllers.SessieController;
//import domein.Actie;
//import domein.BreakOutBox;
//import domein.Oefening;
//import domein.SoortOnderwijsEnum;
//import domein.Toegangscode;
//import gui.controllers.BeheerSessiesController;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.stage.WindowEvent;
//import repository.GenericDaoJpa;
//
//public class StartUpSessieGUI extends Application {
//
//    @Override
//    public void start(Stage stage) {
//
//        //controllers
//        SessieController sessieController = new SessieController();
//        //tijdelijk
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.DAY_OF_YEAR, 1);
//        for (int i = 0; i < 3; i++) {
//            sessieController.createSessie(
//                    "sessie " + i, "Sessie " + i + " omschrijving hier...",
//                    "2A1", 2, c.getTime(), SoortOnderwijsEnum.dagonderwijs, "Feedback");
//        }
//
//        Scene scene = new Scene(new BeheerSessiesController(sessieController));
//        stage.setTitle("Sessie Lijst");
//        stage.setScene(scene);
//        stage.setOnShown((WindowEvent t) -> {
//            stage.setMinWidth(stage.getWidth());
//            stage.setMinHeight(stage.getHeight());
//        });
//        stage.show();
//    }
//
//    public static void main(String... args) {
//        Application.launch(StartUpSessieGUI.class, args);
//    }
//}
