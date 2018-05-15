package gui.controllers;

import controllers.KlasController;
import domein.Klas;
import domein.Leerling;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class OverzichtLeerlingenInKlasController extends AnchorPane {

    @FXML
    private Label klasLabel;

    @FXML
    private Label aantalLlnLabel;

    @FXML
    private TableView<Leerling> leerlingenTbl;

    @FXML
    private TableColumn<Leerling, String> voornaamCol;

    @FXML
    private TableColumn<Leerling, String> naamCol;

    private KlasController klasController;
    private Klas klas;

    public OverzichtLeerlingenInKlasController(KlasController klasController, int klasId) {
        this.klasController = klasController;
        this.klas = klasController.getKlas(klasId);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/OverzichtLeerlingenInKlas.fxml"));
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
        klasLabel.setText(klas.getNaam());
        aantalLlnLabel.setText(Integer.toString(this.klas.getLeerlingen().size()));
        
        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        voornaamCol.setCellValueFactory(cell -> cell.getValue().getVoornaamProperty());
        
        leerlingenTbl.setPlaceholder(new Label("Geen leerlingen in deze klas"));
        
        leerlingenTbl.setItems(klas.getLeerlingen());
    }

}
