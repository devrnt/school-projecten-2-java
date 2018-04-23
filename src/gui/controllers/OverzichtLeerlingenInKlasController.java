package gui.controllers;

import controllers.KlasController;
import controllers.SessieController;
import domein.Klas;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class OverzichtLeerlingenInKlasController extends AnchorPane {

    @FXML
    private Label klasLabel;

    @FXML
    private Label aantalLlnLabel;

    @FXML
    private ListView<String> leerlingenListView;

    private KlasController klasController;
    private Klas klas;

    public OverzichtLeerlingenInKlasController(KlasController klasController, int klasId) {
        this.klasController = klasController;
        this.klas = klasController.getKlas(klasId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/OverzichtLeerlingenInKlas.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        klasLabel.setText(klas.getNaam());
        aantalLlnLabel.setText(Integer.toString(this.klas.getLeerlingen().size()));
        this.leerlingenListView.setPlaceholder(new Label("Geen leerlingen in deze klas"));
        this.leerlingenListView.setItems(this.klas.getLeerlingen());

    }

}
