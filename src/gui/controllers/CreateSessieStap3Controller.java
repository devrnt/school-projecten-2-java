/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.Sessie;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Yanis
 */
public class CreateSessieStap3Controller extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button bevestigButton;

    private final Sessie sessie;
    private final SessieController controller;

    public CreateSessieStap3Controller(Sessie sessie, SessieController controller) {
        System.out.println("123");
        this.sessie = sessie;
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateSessieStap3.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
    }

}
