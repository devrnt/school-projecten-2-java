/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.SessieController;
import domein.FoutAntwoordActieEnum;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import gui.events.DetailsEvent;
import gui.events.InvalidInputEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AnchorType;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class CreateSessieStap2Controller extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextField aantalGroepenInput;
    @FXML
    private Label aantalGroepenFout;
    @FXML
    private Button bevestigButton;
    @FXML
    private Button terugBtn;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton zelfKiezenRadio;
    @FXML
    private RadioButton genererenRadio;
    @FXML
    private RadioButton leegRadio;

    private SessieController sessieController;
    private Sessie sessie;

    public CreateSessieStap2Controller(SessieController sessieController, Sessie sessie) {
        this.sessieController = sessieController;
        this.sessie = sessie;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateSessieStap2.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
        addInputValidation();
    }

    private void initialize() {
        if (sessie.getSoortOnderwijs() == SoortOnderwijsEnum.afstandsonderwijs) {
            genererenRadio.setSelected(true);
            zelfKiezenRadio.setDisable(true);
            leegRadio.setDisable(true);
        } else {
            zelfKiezenRadio.setSelected(true);
        }
    }

    @FXML
    private void bevestigButtonClicked(ActionEvent event) {
        if (aantalGroepenFout.getText().isEmpty() && !aantalGroepenInput.getText().trim().isEmpty()) {
            RadioButton chk = (RadioButton) toggleGroup.getSelectedToggle(); // Cast object to radio button
            String text = chk.getText();
            if (text.equalsIgnoreCase("Zelf kiezen")) {
                System.out.println("ja dees werkt ng nie");
            }
            if (text.equalsIgnoreCase("Genereren")) {
                sessie.genereerPaden(Integer.parseInt(aantalGroepenInput.getText()));
                sessieController.createSessie(sessie.getNaam(), sessie.getOmschrijving(), sessie.getKlas(), sessie.getBox(), sessie.getDatum(), sessie.getSoortOnderwijs(), sessie.getFoutAntwoordActie(), sessie.getIsGedaan());
                System.out.println("DONE");
                Event detailsEvent = new DetailsEvent(90);
                this.fireEvent(detailsEvent);
            }
            if (text.equalsIgnoreCase("Leeg")) {
                System.out.println("Ja leeg uw");
            }
            /*if (sessie.getSoortOnderwijs() == SoortOnderwijsEnum.dagonderwijs) {
                sessie.genereerPadenDag(Integer.parseInt(aantalGroepenInput.getText()));
                
            }*/
        } else {
            System.out.println("to bad");
            Event invalidInputEvent = new InvalidInputEvent(new ArrayList<>());
            this.fireEvent(invalidInputEvent);
        }
    }

    // todo
    @FXML
    private void terugBtnClicked(ActionEvent event) {
        Event beheerEvent = new DetailsEvent(sessie == null ? -1 : sessie.getId());
        this.fireEvent(beheerEvent);
    }

    private void addInputValidation() {
        aantalGroepenInput.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                aantalGroepenFout.setText("Vul aantal groepen in");
            } else {
                try {
                    int aantal = Integer.parseInt(aantalGroepenInput.getText());
                    if (aantal < sessie.getMinimumAantalGroepen()) {
                        aantalGroepenFout.setText("Minimaal aanta groepen is " + sessie.getMinimumAantalGroepen());
                    } else {
                        aantalGroepenFout.setText("");
                    }
                } catch (NumberFormatException e) {
                    aantalGroepenFout.setText("Aantal groepen moet een getal zijn");
                }
            }
        });
    }

}
