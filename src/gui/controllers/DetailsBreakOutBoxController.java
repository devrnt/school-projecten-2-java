package gui.controllers;

import com.itextpdf.text.DocumentException;
import controllers.BoxController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import gui.events.DeleteEvent;
import gui.events.DownloadEvent;
import gui.events.WijzigEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import utils.AlertCS;

public class DetailsBreakOutBoxController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Label naamLabel;
    @FXML
    private Label omschrijvingLabel;
    @FXML
    private Label soortOnderwijsLabel;
    @FXML
    private ListView<Actie> actieList;
    @FXML
    private ListView<Oefening> oefeningList;
    @FXML
    private Button samenvattingButton;
    @FXML
    private Button wijzigButton;
    @FXML
    private Button verwijderButton;

    private BreakOutBox box;
    private BoxController boxController;

    public DetailsBreakOutBoxController(BreakOutBox box, BoxController boxController) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/DetailsBreakOutBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.box = box;
        this.boxController = boxController;
        if (box != null) {
            init();
        }
//        if (boxController.zitBoxInSessie(box.getId())) {
//            verwijderButton.setDisable(true);
//        }
    }

    private void init() {
        naamLabel.setText(box.getNaam());
        omschrijvingLabel.setText(box.getOmschrijving());
        soortOnderwijsLabel.setText(box.getSoortOnderwijs());
        oefeningList.setItems(boxController.getOefeningenByBox(box.getId()));
        oefeningList.setDisable(true);
        actieList.setItems(boxController.getActiesByBox(box.getId()));
        actieList.setDisable(true);

    }

    @FXML
    private void samenvattingBtnClicked(ActionEvent event) throws IOException {

        try {
            boxController.createSamenvattingBox(box.getId());
            Event downloadEvent = new DownloadEvent();
            this.fireEvent(downloadEvent);
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(DetailsBreakOutBoxController.class.getName()).log(Level.SEVERE, null, ex);
        }

        

    }

    @FXML
    private void wijzigBtnClicked(ActionEvent event) {
        Event wijzigEvent = new WijzigEvent(box.getId());
        this.fireEvent(wijzigEvent);
    }

    @FXML
    private void verwijderBtnClicked(ActionEvent event) {
        toggleButtons();
        Event deleteEvent = new DeleteEvent(box.getId());
        this.fireEvent(deleteEvent);
    }

    public void toggleButtons() {
        Button[] btns = {wijzigButton, verwijderButton};
        Arrays.stream(btns).forEach(btn -> {
            btn.setVisible(!btn.isVisible());
        });
    }

}
