package main;

import gui.controllers.HomePanelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartUpGUI extends Application {
    
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HomePanelController.class.getResource("../panels/HomePanel.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("HOME");
        
        primaryStage.show();
    }
    
}
