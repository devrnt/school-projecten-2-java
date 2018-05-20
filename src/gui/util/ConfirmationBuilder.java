package gui.util;

import gui.controllers.DetailsOefeningController;
import gui.controllers.NotificatiePanelController;
import java.util.Observable;
import java.util.function.Function;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author sam
 */
public class ConfirmationBuilder extends Observable {

    private boolean confirmed;
    private int id;
    private String type;

    public ConfirmationBuilder(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public Node buildConfirmation() {
        /* === confirmation buttons === */
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        hbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(600.0);
        vbox.setMaxHeight(600.0);
        Button okButton = new Button("Ja");
        okButton.setOnAction(event -> {
            confirmed = true;
            this.setChanged();
            notifyObservers();
        });

        okButton.getStyleClass().addAll("btn", "btn-small", "btn-default");

        Button cancelButton = new Button("Nee");
        cancelButton.setOnAction(event -> {
            confirmed = false;
            this.setChanged();
            notifyObservers();
        });
        cancelButton.getStyleClass().addAll("btn", "btn-small", "btn-default");

        // show confirmation text and buttons
        hbox.getChildren().addAll(cancelButton, okButton);
        vbox.getChildren().addAll(new Label(String.format("Weet u zeker dat u deze %s wil verwijderen?", type)), hbox);
        return vbox;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getId() {
        return id;
    }
    
    

}
