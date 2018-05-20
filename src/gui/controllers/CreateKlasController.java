/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.KlasController;
import domein.Leerling;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.AlertCS;

/**
 *
 * @author ili
 */
public class CreateKlasController extends AnchorPane {

    private KlasController klasController;
    @FXML
    private TextField klasNaamTxt;
    @FXML
    private TextField voorNaamTxt;
    @FXML
    private TextField familieNaamTxt;
    @FXML
    private Button llnToevoegnBtn;
    @FXML
    private Button klasAanmakenBtn;
    @FXML
    private Button annuleerBtn;
    @FXML
    private Label fouteKlasnaamLbl;
    @FXML
    private Label leerlingNaamFoutLabel;
    @FXML
    private TableView<Leerling> leerlingenTbl;
    @FXML
    private TableColumn<Leerling, String> voorNaamList;
    @FXML
    private TableColumn<Leerling, String> familieNaamList;

    private List<Leerling> tempList;

    public CreateKlasController(KlasController klasController) {
        this.klasController = klasController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateKlas.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempList = new ArrayList<>();
        fouteKlasnaamLbl.setTextFill(Color.RED);
        leerlingNaamFoutLabel.setTextFill(Color.RED);
        klasAanmakenBtn.setDisable(true);

        leerlingenTbl.setItems(FXCollections.observableArrayList(tempList));
        voorNaamList.setCellValueFactory(cell -> cell.getValue().getVoornaamProperty());
        familieNaamList.setCellValueFactory(cell -> cell.getValue().getNaamProperty());

        klasNaamTxt.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            String sessieNaam = klasNaamTxt.getText();
            checkKlasNaam(sessieNaam);
        });

        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(-1);
            this.fireEvent(annuleerEvent);
        });

    }

    @FXML
    private void llnToevoegenBtnClicked(ActionEvent event) {

        String geldigeNaam = "^[ A-Za-z-]+$";
        if (!voorNaamTxt.getText().trim().matches(geldigeNaam) || !familieNaamTxt.getText().trim().matches(geldigeNaam)) {
            leerlingNaamFoutLabel.setText("Vul een geldige voor- en familienaam in");
        } else {
            leerlingNaamFoutLabel.setText("");
            Leerling leerling = new Leerling(voorNaamTxt.getText(), familieNaamTxt.getText());

            if (tempList.stream().anyMatch(n -> n.getVolledigeNaam().equals(leerling.getVolledigeNaam()))) {
                leerlingNaamFoutLabel.setText("Deze leerling is al toegevoegd");

            } else {
                leerlingNaamFoutLabel.setText("");

                tempList.add(leerling);
                leerlingenTbl.getItems().add(leerling);
                voorNaamTxt.clear();
                familieNaamTxt.clear();
                checkKlasNaam(klasNaamTxt.getText().toString());
            }
        }
    }

    @FXML
    private void klasAanmakenBtnClicked(ActionEvent event) {
        klasController.createKlas(klasNaamTxt.getText(), tempList);
        klasController.getMeestRecenteKlas().getId();
        AlertCS sessieSuccesvolGewijzigd = new AlertCS(Alert.AlertType.INFORMATION);
        Event beheerEvent = new DetailsEvent(        klasController.getMeestRecenteKlas().getId());
        this.fireEvent(beheerEvent);

    }

    @FXML
    private void annuleerBtnClicked(ActionEvent event) {
  Event annuleerEvent = new AnnuleerEvent(-1);
        this.fireEvent(annuleerEvent);
    }

    @FXML
    private void checkKlasNaam(String naam) {
        if (naam.length() == 0) {
            klasAanmakenBtn.setDisable(Boolean.TRUE);
            fouteKlasnaamLbl.setText("Klasnaam verplicht");

        } else if (klasController.bestaatKlas(naam)) {
            klasAanmakenBtn.setDisable(Boolean.TRUE);
            fouteKlasnaamLbl.setText("Klasnaam bestaat al");
        } else {
            if (tempList.size() != 0) {
                klasAanmakenBtn.setDisable(Boolean.FALSE);
                fouteKlasnaamLbl.setText("");
            } else {
                fouteKlasnaamLbl.setText("");
                klasAanmakenBtn.setDisable(Boolean.TRUE);

            }

        }
    }

}
