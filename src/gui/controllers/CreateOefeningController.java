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
    private ListView<String> doelstellingenListView;
    @FXML
    private Label doelstellingFoutLabel;

    @FXML
    private ListView<Groepsbewerking> groepsbewerkingen;
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

        groepsbewerkingen.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue) {
                if (groepsbewerkingen.getSelectionModel().getSelectedItems() == null) {
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

        // acties voor buttons
        annuleerBtn.setOnAction(event -> {
            Event annuleerEvent = new AnnuleerEvent(oefening == null ? -1 : oefening.getId());
            this.fireEvent(annuleerEvent);
        });

        groepsbwButton.setOnAction(event -> {
            Groepsbewerking gbw = groepsbwChoiceBox.getSelectionModel().getSelectedItem();
            groepsbewerkingen.getItems().add(gbw);
            gbws.remove(gbw);
            groepsbwChoiceBox.getSelectionModel().selectFirst();
        });

        groepsbwRemoveButton.setOnAction(event -> {
            Groepsbewerking gbw = groepsbewerkingen.getSelectionModel().getSelectedItem();
            if (gbw != null) {
                gbws.add(gbw);
                groepsbewerkingen.getItems().remove(gbw);
            } else {
                groepsbewerkingenFout.setText("Selecteer een groepsbewerking om te verwijderen");
            }
        });

        bevestigAlert = new Alert(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer oefeningen");
        bevestigAlert.setHeaderText("Aanmaken oefening");
        bevestigAlert.setContentText("Oefening is succesvol aangemaakt");

    }

    public CreateOefeningController(OefeningController controller, int id) {
        this(controller);
        oefening = controller.getOefening(id);
        opgaveLabel.setText(oefening.getOpgave());
        opgaveFile = new File(oefening.getOpgave());
        antwoord.setText(Integer.toString(oefening.getAntwoord()));
        feedbackLabel.setText(oefening.getFeedback());
        feedbackFile = new File(oefening.getFeedback());
        vakTextField.setText(oefening.getVak());
        doelstellingenListView.getItems().addAll(FXCollections.observableArrayList(oefening.getDoelstellingen()));
        groepsbewerkingen.setItems(FXCollections.observableArrayList(oefening.getGroepsbewerkingen()));
        gbws.removeAll(oefening.getGroepsbewerkingen());
        groepsbwChoiceBox.getSelectionModel().selectFirst();
        

        bevestigAlert = new Alert(Alert.AlertType.INFORMATION);
        bevestigAlert.setTitle("Beheer oefeningen");
        bevestigAlert.setHeaderText("Wijzigen oefening");
        bevestigAlert.setContentText("Oefening is succesvol gewijzigd");
    }

    @FXML
    protected void addDoelstelling(ActionEvent event) {
        String doelstelling = doelstellingTextField.getText();
        if (!doelstelling.isEmpty()) {
            doelstellingenListView.getItems().add(doelstelling);
            doelstellingFoutLabel.setText("");
        } else {
            doelstellingFoutLabel.setText("Geef een doelstelling");
        }
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
                controller.createOefening(
                        opgaveFile.getAbsolutePath(),
                        Integer.parseInt(antwoord.getText()),
                        feedbackFile.getAbsolutePath(),
                        vakTextField.getText(),
                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
                        groepsbewerkingen.getItems().stream().collect(Collectors.toList()));
            } else {
                controller.updateOefening(
                        oefening.getId(),
                        opgaveFile.getAbsolutePath(),
                        Integer.parseInt(antwoord.getText()),
                        feedbackFile.getAbsolutePath(),
                        vakTextField.getText(),
                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
                        groepsbewerkingen.getItems().stream().collect(Collectors.toList()));
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
            textLabel.setText(file.getAbsolutePath());
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

}
