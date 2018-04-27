package gui.controllers;

import controllers.BoxController;
import domein.BreakOutBox;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.events.WijzigEvent;
import java.io.IOException;
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

public class BeheerBreakOutBoxPanelController extends AnchorPane {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button maakBoxButton;
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
    private ObservableList<Node> children;

    public BeheerBreakOutBoxPanelController(BoxController boxController) {
        this.boxController = boxController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerBreakOutBoxPanel.fxml"));
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

    }

    private void stelTableViewIn() {
        naamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        omschrijvingCol.setCellValueFactory(cell -> cell.getValue().getOmschrijvingProperty());
        boxTabel.setPlaceholder(new Label("Geen BreakOutBoxen"));
        boxTabel.setItems(boxController.getAllBreakOutBoxen());
        boxTabel.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsBreakOutBoxController(newval, boxController));
            }
        });
    }

    private void voegEventHandlersToe() {
        this.addEventHandler(WijzigEvent.WIJZIG, event -> {
            children.clear();
            children.add(new UpdateBreakOutBoxController(boxController.GeefBreakOutBox(event.getId()), boxController));
        });

        this.addEventHandler(DeleteEvent.DELETE, event -> {
            children.clear();
            boxController.deleteBreakOutBox(event.getId());
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                int size = boxController.getAllBreakOutBoxen().size();
                children.add(new DetailsBreakOutBoxController(boxController.getAllBreakOutBoxen().get(size - 1), boxController));
            } else {
                children.add(new DetailsBreakOutBoxController(boxController.GeefBreakOutBox(event.getId()), boxController));
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsBreakOutBoxController(boxController.GeefBreakOutBox(event.getId()), boxController));
            }
        });
    }

    @FXML
    private void maakBreakOutBoxButtonClicked(ActionEvent event) {
        children.clear();
        children.add(new CreateBreakOutBoxController(boxController));
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
}
