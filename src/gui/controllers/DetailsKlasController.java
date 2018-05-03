/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;
import domein.Klas;
import domein.Leerling;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author ili
 */
public class DetailsKlasController extends AnchorPane {

    @FXML
    private Label klasNaamLbl;

    @FXML
    private ListView<String> voorNaamList;
    @FXML
    private ListView<String> familieNaamList;

    private Klas klas;

    public DetailsKlasController(Klas klas) {
        this.klas = klas;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsKlas.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    private void initialize() {
        klasNaamLbl.setText(klas.getNaam());
        for (Leerling leerling : klas.getLeerlingen()) {
            voorNaamList.getItems().add(leerling.getVoornaam());
            familieNaamList.getItems().add(leerling.getNaam());
        }
    }

}
