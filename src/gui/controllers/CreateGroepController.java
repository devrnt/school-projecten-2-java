//package gui.controllers;
//
//import controllers.GroepController;
//import domein.Groepsbewerking;
//import domein.Groep;
//import domein.Leerling;
//import gui.events.AnnuleerEvent;
//import gui.events.DetailsEvent;
//import gui.events.InvalidInputEvent;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.collections.transformation.FilteredList;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
///**
// *
// * @author sam
// */
//public class CreateGroepController extends AnchorPane {
//
//    private GroepController controller;
//    
//    @FXML
//    private Label titelLabel;
//    
//    @FXML
//    private TextField naamTextField;   
//    @FXML
//    private Label naamFoutLabel;
//    
//    @FXML
//    private ListView<Leerling> leerlingenListView;
//    @FXML
//    private Label leerlingenFoutLabel;
//    
//    @FXML
//    private Button bevestigBtn;
//
//    @FXML
//    private Button annuleerBtn;
//
//    private Groep groep;
//
//    public CreateGroepController(GroepController controller) {
//        this.controller = controller;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/CreateGroep.fxml"));
//
//        loader.setRoot(this);
//        loader.setController(this);
//
//        try {
//            loader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        titelLabel.setText("Nieuwe groep");
//
//        // listeners voor validatie
//        addListeners();
//
//        // acties voor buttons
//        setButtonActions();
//    }
//
//    public CreateGroepController(GroepController controller, int id) {
//        this(controller);
//        titelLabel.setText("Wijzig groep");
//        groep = controller.getGroep(id);
//
//        opgaveFile = new File(groep.getOpgave());
//        opgaveLabel.setText(opgaveFile.getName());
//        feedbackFile = new File(groep.getFeedback());
//        feedbackLabel.setText(feedbackFile.getName());
//
//        antwoord.setText(Integer.toString(groep.getAntwoord()));
//        vakTextField.setText(groep.getVak());
//
//        doelstellingenListView.getItems().addAll(FXCollections.observableArrayList(groep.getDoelstellingen()));
//
//        groepsbewerkingenListView.setItems(FXCollections.observableArrayList(groep.getGroepsbewerkingen()));
//        gbws.removeAll(groep.getGroepsbewerkingen());
//        groepsbwButton.setDisable(gbws.isEmpty());
//        groepsbwChoiceBox.setDisable(gbws.isEmpty());
//        groepsbwChoiceBox.getSelectionModel().selectFirst();
//
//    }
//
//    @FXML
//    protected void addDoelstelling(ActionEvent event) {
//        String doelstelling = doelstellingTextField.getText();
//        doelstellingenListView.getItems().add(doelstelling);
//        doelstellingTextField.setText("");
//        doelstellingFoutLabel.setText("");
//        addDoelstellingBtn.setDisable(true);
//    }
//
//    @FXML
//    protected void uploadOpgavePdf(ActionEvent event) {
//        if (opgaveFile != null) {
//            filechooser.setInitialDirectory(opgaveFile.getParentFile());
//        }
//        opgaveFile = uploadPdf(opgaveLabel, opgaveFoutLabel);
//
//    }
//
//    @FXML
//    protected void uploadFeedbackPdf(ActionEvent event) {
//        if (feedbackFile != null) {
//            filechooser.setInitialDirectory(feedbackFile);
//        }
//        feedbackFile = uploadPdf(feedbackLabel, feedbackFoutLabel);
//    }
//
//    @FXML
//    public void bevestigClicked(ActionEvent event) {
//        Label[] foutLabels = {opgaveFoutLabel, antwoordFout, feedbackFoutLabel, groepsbewerkingenFout};
//
//        checkInputs();
//
//        boolean inputGeldig = Arrays.stream(foutLabels).allMatch(l -> l.getText().isEmpty());
//
//        if (inputGeldig) {
//            if (groep == null) {
//                controller.createGroep(opgaveFile.getAbsolutePath(),
//                        Integer.parseInt(antwoord.getText()),
//                        feedbackFile.getAbsolutePath(),
//                        vakTextField.getText(),
//                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
//                        groepsbewerkingenListView.getItems().stream().collect(Collectors.toList()));
//            } else {
//                controller.updateGroep(groep.getId(),
//                        opgaveFile.getAbsolutePath(),
//                        Integer.parseInt(antwoord.getText()),
//                        feedbackFile.getAbsolutePath(),
//                        vakTextField.getText(),
//                        doelstellingenListView.getItems().stream().collect(Collectors.toList()),
//                        groepsbewerkingenListView.getItems().stream().collect(Collectors.toList()));
//            }
//            toonDetails();
//        } else {
//            showErrorAlert();
//        }
//    }
//
//    private File uploadPdf(Label textLabel, Label foutLabel) {
//        File file = filechooser.showOpenDialog((Stage) annuleerBtn.getScene().getWindow());
//        if (file == null || !file.getName().toLowerCase().endsWith(".pdf")) {
//            textLabel.setText("");
//            foutLabel.setText("Selecteer een opgave");
//        } else {
//            textLabel.setText(file.getName());
//            foutLabel.setText("");
//        }
//        return file;
//    }
//
//    private void showErrorAlert() {
//        List<String> velden = new ArrayList<>();
//        velden.add("Er zijn nog ongeldige velden: \n");
//        Label[] foutLabels = {opgaveFoutLabel, antwoordFout, feedbackFoutLabel, groepsbewerkingenFout};
//        Arrays.stream(foutLabels).filter(l -> !l.getText().isEmpty()).forEach(l -> {
//            velden.add(l.getText());
//        });
//        Event inputInvalidEvent = new InvalidInputEvent(velden);
//        this.fireEvent(inputInvalidEvent);
//    }
//
//    private void checkInputs() {
//        String antwoordText = antwoord.getText();
//        String vakText = vakTextField.getText();
//        int aantalDoelstelling = doelstellingenListView.getItems().size();
//        int aantalGroepsbewerkingen = groepsbewerkingenListView.getItems().size();
//
//        if (antwoordText == null || antwoordText.trim().isEmpty()) {
//            antwoordFout.setText("Geef een antwoord in");
//        }
//        try {
//            Integer.parseInt(antwoordText);
//        } catch (NumberFormatException e) {
//            antwoordFout.setText("Antwoord moet een getal zijn");
//        }
//        if (vakText == null || vakText.trim().isEmpty()) {
//            vakFoutLabel.setText("Geef een vak in");
//        }
//        if (aantalDoelstelling == 0) {
//            doelstellingFoutLabel.setText("Geef minstens 1 doelstelling in");
//        }
//
//        if (aantalGroepsbewerkingen == 0) {
//            groepsbewerkingenFout.setText("Voeg minstens 1 groepsbewerking toe");
//        }
//
//        if (opgaveFile == null || !opgaveFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
//            opgaveFoutLabel.setText("Bestand moet in PDF formaat zijn");
//        }
//
//        if (feedbackFile == null || !feedbackFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
//            feedbackFoutLabel.setText("Bestand moet in PDF formaat zijn");
//        }
//    }
//
//    private void toonDetails() {
//        Event beheerEvent = new DetailsEvent(groep == null ? -1 : groep.getId());
//        this.fireEvent(beheerEvent);
//    }
//
//    private void addListeners() {
//        antwoord.focusedProperty().addListener((ob, oldValue, newValue) -> {
//            if (!newValue) {
//                if (antwoord.getText() == null || antwoord.getText().trim().length() == 0) {
//                    antwoordFout.setText("Geef een antwoord in");
//                } else {
//                    try {
//                        Integer.parseInt(antwoord.getText());
//                        antwoordFout.setText("");
//                    } catch (NumberFormatException e) {
//                        antwoordFout.setText("Antwoord moet een getal zijn");
//                    }
//                }
//            }
//        });
//
//        groepsbewerkingenListView.focusedProperty().addListener((ob, oldValue, newValue) -> {
//            if (!newValue) {
//                if (groepsbewerkingenListView.getSelectionModel().getSelectedItems() == null) {
//                    groepsbewerkingenFout.setText("Selecteer minstens 1 groepsbewerking");
//                } else {
//                    groepsbewerkingenFout.setText("");
//                }
//            }
//        });
//
//        vakTextField.focusedProperty().addListener((ob, oldValue, newValue) -> {
//            if (!newValue) {
//                String text = vakTextField.getText();
//                if (text == null || text.trim().length() == 0) {
//                    vakFoutLabel.setText("Geef een vak in");
//                } else {
//                    vakFoutLabel.setText("");
//                }
//            }
//        });
//
//        doelstellingTextField.setOnKeyReleased(event -> {
//            String text = doelstellingTextField.getText().trim().toLowerCase();
//            addDoelstellingBtn.setDisable(text == null || text.isEmpty());
//            if (text == null || text.isEmpty()) {
//                doelstellingen.setPredicate(d -> true);
//                doelstellingComboBox.hide();
//            } else {
//                int matches = (int) controller.getDoelstellingen().stream().filter(d -> d.toLowerCase().startsWith(text)).count();
//                doelstellingen.setPredicate(d -> d.toLowerCase().startsWith(text));
//                doelstellingComboBox.show();
//                
//            }
//        });
//
//        groepsbewerkingenListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
//            if (newvalue != null) {
//                groepsbwRemoveButton.setDisable(false);
//            }
//        });
//    }
//
//    private void setButtonActions() {
//        annuleerBtn.setOnAction(event -> {
//            Event annuleerEvent = new AnnuleerEvent(groep == null ? -1 : groep.getId());
//            this.fireEvent(annuleerEvent);
//        });
//
//        //<editor-fold defaultstate="collapsed" desc="doelstellingen">
//        addDoelstellingBtn.setDisable(true);
//        doelstellingRemoveBtn.setDisable(true);
//
//        doelstellingRemoveBtn.setOnAction(event -> {
//            int index = doelstellingenListView.getSelectionModel().getSelectedIndex();
//            doelstellingenListView.getItems().remove(index);
//            if (doelstellingenListView.getItems().isEmpty()) {
//                doelstellingRemoveBtn.setDisable(true);
//            }
//            doelstellingenListView.getSelectionModel().clearSelection();
//            doelstellingRemoveBtn.setDisable(true);
//        });
//
//        doelstellingenListView.getSelectionModel().selectedItemProperty().addListener((ob, oldvalue, newvalue) -> {
//            if (newvalue != null) {
//                doelstellingRemoveBtn.setDisable(false);
//            }
//        });
////</editor-fold>
//
//        //<editor-fold defaultstate="collapsed" desc="groepsbewerkingen">
//        groepsbwButton.setOnAction(event -> {
//            Groepsbewerking gbw = groepsbwChoiceBox.getSelectionModel().getSelectedItem();
//            groepsbewerkingenListView.getItems().add(gbw);
//            groepsbewerkingenFout.setText("");
//            gbws.remove(gbw);
//            if (groepsbwChoiceBox.getItems().isEmpty()) {
//                groepsbwButton.setDisable(true);
//                groepsbwChoiceBox.setDisable(true);
//            } else {
//                groepsbwChoiceBox.getSelectionModel().selectFirst();
//            }
//        });
//
//        groepsbwRemoveButton.setDisable(true);
//
//        groepsbwRemoveButton.setOnAction(event -> {
//            Groepsbewerking gbw = groepsbewerkingenListView.getSelectionModel().getSelectedItem();
//            gbws.add(gbw);
//            groepsbwChoiceBox.getSelectionModel().selectFirst();
//            groepsbewerkingenListView.getItems().remove(gbw);
//            if (groepsbewerkingenListView.getItems().isEmpty()) {
//                groepsbwRemoveButton.setDisable(true);
//            }
//            groepsbwButton.setDisable(false);
//            groepsbwChoiceBox.setDisable(false);
//            groepsbewerkingenListView.getSelectionModel().clearSelection();
//            groepsbwRemoveButton.setDisable(true);
//
//        });
////</editor-fold>
//    }
//
//}
