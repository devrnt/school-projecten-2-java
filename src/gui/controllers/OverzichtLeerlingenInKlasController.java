package gui.controllers;

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

    private SessieController sessieController;
    private Klas klas;

    public OverzichtLeerlingenInKlasController(SessieController sessieController, int klasId) {
        this.sessieController = sessieController;
        this.klas = sessieController.getKlas(klasId);

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
        this.leerlingenListView.setItems(this.klas.getLeerlingen());

    }

}
