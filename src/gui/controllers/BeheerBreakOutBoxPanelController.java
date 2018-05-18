package gui.controllers;

import controllers.BoxController;
import domein.BreakOutBox;
import domein.Kleuren;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.DownloadEvent;
import gui.events.InvalidInputEvent;
import gui.events.WijzigEvent;
import gui.util.ConfirmationBuilder;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class BeheerBreakOutBoxPanelController extends AnchorPane implements Observer {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button maakBoxButton;
    @FXML
    private Button kopieBoxButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<BreakOutBox> boxTabel;
    @FXML
    private TableColumn<BreakOutBox, String> naamCol;
    @FXML
    private TableColumn<BreakOutBox, String> omschrijvingCol;
    @FXML
    private StackPane detailsStackPane;

    private BoxController boxController;
    private final ObservableList<Node> children;

    public BeheerBreakOutBoxPanelController(BoxController boxController) {
        this.boxController = boxController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerBreakOutBoxPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        children = detailsStackPane.getChildren();
        stelTableViewIn();
        voegEventHandlersToe();
        kopieBoxButton.setDisable(true);
    }

    private void stelTableViewIn() {
        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());
        boxTabel.setPlaceholder(new Label("Geen BreakOutBoxen"));
        boxTabel.setItems(boxController.getAllBreakOutBoxen());
        boxTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                kopieBoxButton.setDisable(false);
                children.clear();
                children.add(new DetailsBreakOutBoxController(newval, boxController));
            }
        });
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new CreateBreakOutBoxController(boxController.getBreakOutBox(event.getId()), boxController, 2));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            if (boxController.zitBoxInSessie(event.getId())) {
                String boxNaam = boxController.getBreakOutBox(event.getId()).getNaam();
                ((DetailsBreakOutBoxController) children.get(0)).toggleButtons();
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController(String.format("BreakoutBox zit nog in een Sessie", boxNaam), Kleuren.GEEL));
                children.add(topNode);
            } else {
                ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
                builder.addObserver(this);
                children.add(builder.buildConfirmation());

            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                boxTabel.getSelectionModel().select(boxController.getMeestRecenteBreakOutBox());
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("BreakoutBox is succesvol aangemaakt", Kleuren.GROEN));
                children.add(topNode);
            } else {
                boxTabel.getSelectionModel().select(boxController.getBreakOutBox(event.getId()));
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("BreakoutBox is succesvol gewijzigd", Kleuren.GROEN));
                children.add(topNode);
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsBreakOutBoxController(boxController.getBreakOutBox(event.getId()), boxController));
            }
        });

        this.addEventHandler(DownloadEvent.DOWNLOAD, event -> {
            Node topNode = children.get(0);
            children.set(0, new NotificatiePanelController("Samenvatting is succesvol gedownload", Kleuren.GROEN));
            children.add(topNode);
        });

        this.addEventHandler(InvalidInputEvent.INVALIDINPUT, event -> {
            if (children.size() == 1) {
                Node topNode = children.get(0);
                children.set(0, new NotificatiePanelController("Er zijn nog ongeldige velden", Kleuren.GROEN));
                children.add(topNode);
            }
        });
    }

    @FXML
    private void maakBreakOutBoxButtonClicked(ActionEvent event) {
        children.clear();
        children.add(new CreateBreakOutBoxController(boxController));
    }

    @FXML
    private void kopieBreakOutBoxButtonClicked(ActionEvent event) {
        if (boxTabel.getSelectionModel().getSelectedItem() != null) {
            children.clear();
            children.add(new CreateBreakOutBoxController(boxTabel.getSelectionModel().getSelectedItem(), boxController, 1));
        }
    }

    @FXML
    public void filter(KeyEvent event) {
        String toFilter = searchTextField.getText();
        boxController.applyFilter(toFilter);
    }
//    @FXML
//    private void SelecteerBox() {
//        if (boxTabel.getSelectionModel().getSelectedItem() != null) {
//            int id = boxTabel.getSelectionModel().getSelectedItem().getId();
//            Scene scene = new Scene(new DetailsBreakOutBoxController(boxController, id));
//            Stage stage = (Stage) this.getScene().getWindow();
//            stage.setScene(scene);
//            stage.setTitle("Wijzig breakoutbox");
//            stage.show();
//        }
//    }
//
//    private void terugNaarMenu() {
//        Scene scene = new Scene(new MenuPanelController());
//        Stage stage = (Stage) this.getScene().getWindow();
//        stage.setTitle("Menu");
//        stage.setScene(scene);
//        stage.show();
//    }

    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String boxNaam = boxController.getBreakOutBox(id).getNaam();
            boxController.deleteBreakOutBox(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Box %s is verwijderd", boxNaam), Kleuren.GROEN));
        } else {
            children.remove(children.size() - 1);
            ((DetailsBreakOutBoxController) children.get(children.size() - 1)).toggleButtons();
        }
    }
}
