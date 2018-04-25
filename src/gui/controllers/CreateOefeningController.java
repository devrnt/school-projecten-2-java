package gui.controllers;

import controllers.OefeningController;
import domein.Groepsbewerking;
import domein.Oefening;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
    private Label opgaveFout;

    @FXML
    private TextField antwoord;
    @FXML
    private Label antwoordFout;

    @FXML
    private Label feedbackLabel;
    @FXML
    private Button uploadFeedbackPdfBtn;
    @FXML
    private Label feedbackFout;

    @FXML
    private ListView<Groepsbewerking> groepsbewerkingen;
    @FXML
    private Label groepsbewerkingenFout;

    @FXML
    private Button bevestigBtn;

    @FXML
    private Button annuleerBtn;

    private final FileChooser filechooser = new FileChooser();
    private File opgaveFile;
    private File feedbackFile;

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

        //filechooser
        // enkel pdf's
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.PDF", "*.pdf");
        filechooser.getExtensionFilters().add(filter);

        // listview
        groepsbewerkingen.setItems(controller.getGroepsbewerkingen());
        groepsbewerkingen.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        groepsbewerkingen.getSelectionModel().selectFirst();

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

        annuleerBtn.setOnAction(event -> terugNaarLijst());

    }

    public CreateOefeningController(OefeningController controller, int id) {
        this(controller);
        Oefening oefening = controller.getOefening(id);
        opgaveLabel.setText(oefening.getOpgave());
        opgaveFile = new File(oefening.getOpgave());
        antwoord.setText(Integer.toString(oefening.getAntwoord()));
        feedbackLabel.setText(oefening.getFeedback());
        feedbackFile = new File(oefening.getFeedback());
    }

    @FXML
    protected void uploadOpgavePdf(ActionEvent event) {
        opgaveFile = uploadPdf(opgaveLabel);
    }

    @FXML
    protected void uploadFeedbackPdf(ActionEvent event) {
        feedbackFile = uploadPdf(feedbackLabel);
    }

    @FXML
    public void bevestigClicked(ActionEvent event) {
        Label[] foutLabels = {opgaveFout, antwoordFout, feedbackFout, groepsbewerkingenFout};
        List<Groepsbewerking> geselecteerdeItems = groepsbewerkingen.getSelectionModel().getSelectedItems();

        boolean inputAanwezig = !opgaveLabel.getText().isEmpty() && !feedbackLabel.getText().isEmpty() && !antwoord.getText().isEmpty();
        boolean inputGeldig = Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());

        if (inputAanwezig && inputGeldig) {
            controller.createOefening(
                    opgaveFile.getAbsolutePath(),
                    Integer.parseInt(antwoord.getText()),
                    feedbackFile.getAbsolutePath(),
                    geselecteerdeItems
            );
            showSuccessAlert();
        } else {
            showErrorAlert();
        }
    }

    private void terugNaarLijst() {
        Scene scene = new Scene(new BeheerOefeningenController(controller));
        Stage stage = (Stage) annuleerBtn.getScene().getWindow();
        stage.setTitle("Beheer Oefeningen");
        stage.setScene(scene);
        stage.show();
    }

    private File uploadPdf(Label label) {
        File file = filechooser.showOpenDialog((Stage) annuleerBtn.getScene().getWindow());
        if (file != null) {
            label.setText(file.getAbsolutePath());
        }
        return file;
    }

    private void showSuccessAlert() {
        Alert oefeningCreatedSuccess = new Alert(Alert.AlertType.INFORMATION);
        oefeningCreatedSuccess.setTitle("Oefening");
        oefeningCreatedSuccess.setHeaderText("Aanmaken van een oefening");
        oefeningCreatedSuccess.setContentText("Oefening is succesvol aangemaakt");
        oefeningCreatedSuccess.showAndWait();
        terugNaarLijst();
    }

    private void showErrorAlert() {
        Alert invalidInput = new Alert(Alert.AlertType.ERROR);
        invalidInput.setTitle("Oefening aanmaken");
        invalidInput.setHeaderText("Er zijn nog ongeldige velden");
        invalidInput.setContentText("Pas de invoer aan zodat deze geldig is");
        invalidInput.showAndWait();
    }

}
