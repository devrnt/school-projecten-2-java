package gui.controllers;

import controllers.OefeningController;
import domein.Groepsbewerking;
import domein.Oefening;
import gui.events.AnnuleerEvent;
import gui.events.DetailsEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author sam
 */
public class CreateOefeningController extends AnchorPane {

    private OefeningController controller;

    @FXML
    private Label opgaveLabel;
    @FXML
    private Button uploadOpgavePdfBtn;
    @FXML
    private Label opgaveFoutLabel;

    @FXML
    private TextField antwoord;
    @FXML
    private Label antwoordFout;

    @FXML
    private Label feedbackLabel;
    @FXML
    private Button uploadFeedbackPdfBtn;
    @FXML
    private Label feedbackFoutLabel;

    @FXML
    private TextField vakTextField;
    @FXML
    private Label vakFoutLabel;

    @FXML
    private TextField doelstellingTextField;
    @FXML
    private Button addDoelstellingBtn;
    @FXML
    private Button doelstellingRemoveBtn;
    @FXML
    private ListView<String> doelstellingenListView;
    @FXML
    private Label doelstellingFoutLabel;

    @FXML
    private ListView<Groepsbewerking> groepsbewerkingenListView;
    @FXML
    private Label groepsbewerkingenFout;
    @FXML
    private ChoiceBox<Groepsbewerking> groepsbwChoiceBox;
    @FXML
    private Button groepsbwButton;
    @FXML
    private Button groepsbwRemoveButton;

    @FXML
    private Button bevestigBtn;

    @FXML
    private Button annuleerBtn;

    private Oefening oefening;

    private final FileChooser filechooser = new FileChooser();
    private File opgaveFile;
    private File feedbackFile;
    private Alert bevestigAlert;
    private ObservableList<Groepsbewerking> gbws;

    public CreateOefeningController(OefeningController controller) {
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateOefening.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gbws = controller.getGroepsbewerkingen();

        //filechooser
        // enkel pdf's
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.PDF", "*.pdf");
        filechooser.getExtensionFilters().add(filter);

        // choicebox
        groepsbwChoiceBox.setItems(gbws.sorted());
        groepsbwChoiceBox.getSelectionModel().selectFirst();

        // listeners voor validatie
        addListeners();

        // acties voor buttons
        setButtonActions();

        bevestigAlert = new Alert(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer oefeningen");
        bevestigAlert.setHeaderText("Aanmaken oefening");
        bevestigAlert.setContentText("Oefening is succesvol aangemaakt");

    }

    public CreateOefeningController(OefeningController controller, int id) {
        this(controller);
        oefening = controller.getOefening(id);
        
        opgaveFile = new File(oefening.getOpgave());
        opgaveLabel.setText(opgaveFile.getName());
        feedbackFile = new File(oefening.getFeedback());
        feedbackLabel.setText(feedbackFile.getName());
        
        antwoord.setText(Integer.toString(oefening.getAntwoord()));         
        vakTextField.setText(oefening.getVak());
        
        doelstellingenListView.getItems().addAll(FXCollections.observableArrayList(oefening.getDoelstellingen()));
        
        groepsbewerkingenListView.setItems(FXCollections.observableArrayList(oefening.getGroepsbewerkingen()));
        gbws.removeAll(oefening.getGroepsbewerkingen());
        groepsbwButton.setDisable(gbws.isEmpty());        
        groepsbwChoiceBox.setDisable(gbws.isEmpty());
        groepsbwChoiceBox.getSelectionModel().selectFirst();

        bevestigAlert = new Alert(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer oefeningen");
        bevestigAlert.setHeaderText("Wijzigen oefening");
        bevestigAlert.setContentText("Oefening is succesvol gewijzigd");
    }

    @FXML
    protected void addDoelstelling(ActionEvent event) {
        String doelstelling = doelstellingTextField.getText();
        doelstellingenListView.getItems().add(doelstelling);
        doelstellingTextField.setText("");
        doelstellingFoutLabel.setText("");
        addDoelstellingBtn.setDisable(true);
    }

    @FXML
    protected void uploadOpgavePdf(ActionEvent event) {
        opgaveFile = uploadPdf(opgaveLabel, opgaveFoutLabel);

    }

    @FXML
    protected void uploadFeedbackPdf(ActionEvent event) {
        feedbackFile = uploadPdf(feedbackLabel, feedbackFoutLabel);
    }

    @FXML
    public void bevestigClicked(ActionEvent event) {
        Label[] foutLabels = {opgaveFoutLabel, antwoordFout, feedbackFoutLabel, groepsbewerkingenFout};

        checkInputs();

        boolean inputGeldig = Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());

        if (inputGeldig) {
            if (oefening == null) {
                controller.createOefening(opgaveFile.getAbsolutePath(),
                        Integer.parseInt(antwoord.getText()),
                        feedbackFile.getAbsolutePath(),
                        vakTextField.getText(),
                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
                        groepsbewerkingenListView.getItems().stream().collect(Collectors.toList()));
            } else {
                controller.updateOefening(oefening.getId(),
                        opgaveFile.getAbsolutePath(),
                        Integer.parseInt(antwoord.getText()),
                        feedbackFile.getAbsolutePath(),
                        vakTextField.getText(),
                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
                        groepsbewerkingenListView.getItems().stream().collect(Collectors.toList()));
            }
            showSuccessAlert();
        } else {
            showErrorAlert();
        }
    }

    private File uploadPdf(Label textLabel, Label foutLabel) {
        File file = filechooser.showOpenDialog((Stage) annuleerBtn.getScene().getWindow());
        if (file == null || !file.getName().toLowerCase().endsWith(".pdf")) {
            foutLabel.setText("Selecteer een opgave in PDF formaat");
        } else {
            textLabel.setText(file.getName());
            foutLabel.setText("");
        }
        return file;
    }

    private void showSuccessAlert() {
        bevestigAlert.showAndWait();
        toonDetails();
    }

    private void showErrorAlert() {
        Alert invalidInput = new Alert(Alert.AlertType.ERROR);
        invalidInput.setTitle("Oefening aanmaken");
        invalidInput.setHeaderText("Er zijn nog ongeldige velden");
        invalidInput.setContentText("Pas de invoer aan zodat deze geldig is");
        invalidInput.showAndWait();
    }

    private void checkInputs() {
        String antwoordText = antwoord.getText();
        String vakText = vakTextField.getText();
        int aantalDoelstelling = doelstellingenListView.getItems().size();
        int aantalGroepsbewerkingen = groepsbewerkingenListView.getItems().size();

        if (antwoordText == null || antwoordText.trim().isEmpty()) {
            antwoordFout.setText("Geef een antwoord in");
        }
        try {
            Integer.parseInt(antwoordText);
        } catch (NumberFormatException e) {
            antwoordFout.setText("Antwoord moet een getal zijn");
        }
        if (vakText == null || vakText.trim().isEmpty()) {
            vakFoutLabel.setText("Geef een vak in");
        }
        if (aantalDoelstelling == 0) {
            doelstellingFoutLabel.setText("Geef minstens 1 doelstelling in");
        }

        if (aantalGroepsbewerkingen == 0) {
            groepsbewerkingenFout.setText("Voeg minstens 1 groepsbewerking toe");
        }

        if (opgaveFile == null || !opgaveFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
            opgaveFoutLabel.setText("Bestand moet in PDF formaat zijn");
        }

        if (feedbackFile == null || !feedbackFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
            feedbackFoutLabel.setText("Bestand moet in PDF formaat zijn");
        }
    }

    private void toonDetails() {
        Event beheerEvent = new DetailsEvent(oefening == null ? -1 : oefening.getId());
        this.fireEvent(beheerEvent);
    }

    private void addListeners() {
        antwoord.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (antwoord.getText() == null || antwoord.getText().trim().length() == 0) {
                    antwoordFout.setText("Geef een antwoord in");
                } else {
                    try {
                        Integer.parseInt(antwoord.getText());
                        antwoordFout.setText("");
                    } catch (NumberFormatException e) {
                        antwoordFout.setText("Antwoord moet een getal zijn");
                    }
                }
            }
        });

        groepsbewerkingenListView.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (groepsbewerkingenListView.getSelectionModel().getSelectedItems() == null) {
                    groepsbewerkingenFout.setText("Selecteer minstens 1 groepsbewerking");
                } else {
                    groepsbewerkingenFout.setText("");
                }
            }
        });

        vakTextField.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                String text = vakTextField.getText();
                if (text == null || text.trim().length() == 0) {
                    vakFoutLabel.setText("Geef een vak in");
                } else {
                    vakFoutLabel.setText("");
                }
            }
        });

        doelstellingTextField.setOnKeyReleased(event -> {
            String text = doelstellingTextField.getText();
            addDoelstellingBtn.setDisable(text == null || text.trim().length() == 0);
        });

        groepsbewerkingenListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
            if (newvalue != null) {
                groepsbwRemoveButton.setDisable(false);
            }
        });
    }

    private void setButtonActions() {
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(oefening == null ? -1 : oefening.getId());
            this.fireEvent(annuleerEvent);
        });
        
        //<editor-fold defaultstate="collapsed" desc="doelstellingen">
        addDoelstellingBtn.setDisable(true);
        doelstellingRemoveBtn.setDisable(true);
        
        doelstellingRemoveBtn.setOnAction(event -> {
            int index = doelstellingenListView.getSelectionModel().getSelectedIndex();
            doelstellingenListView.getItems().remove(index);
            if (doelstellingenListView.getItems().isEmpty()) {
                doelstellingRemoveBtn.setDisable(true);
            }
            doelstellingenListView.getSelectionModel().clearSelection();
            doelstellingRemoveBtn.setDisable(true);
        });
        
        doelstellingenListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
            if (newvalue != null) {
                doelstellingRemoveBtn.setDisable(false);
            }
        });
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="groepsbewerkingen">
        groepsbwButton.setOnAction(event -> {
            Groepsbewerking gbw = groepsbwChoiceBox.getSelectionModel().getSelectedItem();
            groepsbewerkingenListView.getItems().add(gbw);
            groepsbewerkingenFout.setText("");
            gbws.remove(gbw);
            if (groepsbwChoiceBox.getItems().isEmpty()) {
                groepsbwButton.setDisable(true);
                groepsbwChoiceBox.setDisable(true);
            } else {
                groepsbwChoiceBox.getSelectionModel().selectFirst();
            }
        });
        
        groepsbwRemoveButton.setDisable(true);
        
        groepsbwRemoveButton.setOnAction(event -> {
            Groepsbewerking gbw = groepsbewerkingenListView.getSelectionModel().getSelectedItem();
            gbws.add(gbw);
            groepsbwChoiceBox.getSelectionModel().selectFirst();
            groepsbewerkingenListView.getItems().remove(gbw);
            if (groepsbewerkingenListView.getItems().isEmpty()) {
                groepsbwRemoveButton.setDisable(true);
            }
            groepsbwButton.setDisable(false);
            groepsbwChoiceBox.setDisable(false);
            groepsbewerkingenListView.getSelectionModel().clearSelection();
            groepsbwRemoveButton.setDisable(true);
            
        });
//</editor-fold>
    }

}
