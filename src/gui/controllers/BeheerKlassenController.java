/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.KlasController;
import domein.Klas;
import domein.Kleuren;
import domein.Leerling;
import gui.events.AnnuleerEvent;
import gui.events.DeleteEvent;
import gui.events.DetailsEvent;
import gui.util.ConfirmationBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import utils.AlertCS;

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerKlassenController extends AnchorPane implements Observer {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button kiesBestandBtn;
    @FXML
    private Label gekozenBestandLbl;
    @FXML
    private TableView<Klas> klassenTbl;
    @FXML
    private TableColumn<Klas, String> klasNaamCol;
    @FXML
    private TableColumn<Klas, String> aantalLlnCol;
    @FXML
    private Button keerTerugBtn;
    @FXML
    private Button nieuweKlasBtn;
    @FXML
    private StackPane detailsStackPane;

    private FileChooser fileChooser;

    private ObservableList<Klas> klassen;

    private KlasController klasController;

    private ObservableList<Node> children;

    public BeheerKlassenController(KlasController klasController) {
        this.klasController = klasController;
        klassen = klasController.getAllKlassen();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/BeheerKlassen.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        children = detailsStackPane.getChildren();

        initialize();
        configureFileChooser();
        voegEventHandlersToe();

    }

    @FXML
    private void kiesBestandBtnClicked(ActionEvent event) {
        Stage stage = (Stage) this.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                openFile(file);
            } catch (IOException | InvalidFormatException ex) {
                Logger.getLogger(BeheerKlassenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initialize() {
        klassenTbl.setPlaceholder(new Label("Geen klassen"));
        klasNaamCol.setCellValueFactory(cell -> cell.getValue().getNaamProperty());
        aantalLlnCol.setCellValueFactory(cell -> cell.getValue().getAantalLeerlingenProp());

        gekozenBestandLbl.setText("Geen");

        SortedList<Klas> sortedKlas = new SortedList<>(klassen);
        sortedKlas.comparatorProperty().bind(klassenTbl.comparatorProperty());

        klassenTbl.setItems(sortedKlas);
        klassenTbl.getSelectionModel().selectedItemProperty().addListener((ob, oldval, newval) -> {
            if (newval != null) {
                children.clear();
                children.add(new DetailsKlasController(newval, klasController));
            }
        });
//        keerTerugBtn.setOnAction(event -> terugNaarMenu());

    }

    private void openFile(File file) throws IOException, InvalidFormatException {
        gekozenBestandLbl.setText(file.getName());
        readSheet(file);

    }

    private void configureFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel Bestand");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel bestand", "*.xlsx"));
    }

    private void readSheet(File file) throws IOException, InvalidFormatException {
        // op dit moment wordt gebruik gemaakt van een vooraf gedinieerd Excel bestand
        // --> maak gebruik van file
        String sheetPath = "src/data/KlasSheet.xlsx";

        // create Workbook from file
        Workbook workbook = WorkbookFactory.create(new File(sheetPath));

        // create Sheet of Workbook
        Sheet sheet = workbook.getSheetAt(0);

        // DataFormatter to get Cell value
        DataFormatter dataFormatter = new DataFormatter();

        // rij per rij overlope 
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Klas klas = new Klas();

            // per rij kolommen overlopen
            while (cellIterator.hasNext()) {
                List<String> leerlingen = new ArrayList<>();
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                if (columnIndex == 0) {
                    // kolom van klasnaam
                    String klasNaam = dataFormatter.formatCellValue(cell);
                    klas.setNaam(klasNaam);

                } else {
                    // overige kolommen met de leerlingen
                    if (!isCellLeeg(cell)) {
                        String leerlingS = dataFormatter.formatCellValue(cell);
                        String[] volledigeNaam = leerlingS.split(",");
                        String voornaam = volledigeNaam[0];
                        String naam = volledigeNaam[1];
                        Leerling leerling = new Leerling(voornaam, naam);
                        try {
                            klas.voegLeerlingToe(leerling);

                        } catch (IllegalArgumentException e) {
                            AlertCS invalidInput = new AlertCS(Alert.AlertType.ERROR);
                            invalidInput.setTitle("Leerling bestaat al");
                            invalidInput.setHeaderText("De leerling " + leerling.getVolledigeNaam() + " bestaat reeds");
                            invalidInput.setContentText("Deze leerling werd niet toegevoegd");
                            invalidInput.showAndWait();
                        }
                    }
                }
            }

            // create klassen in database
            try {
                klasController.createKlas(klas.getNaam(), klas.getLeerlingen().stream().collect(Collectors.toList()));
            } catch (IllegalArgumentException e) {
                AlertCS invalidInput = new AlertCS(Alert.AlertType.ERROR);
                invalidInput.setTitle("Klas lezen");
                invalidInput.setHeaderText("De klas " + klas.getNaam() + " bestaat reeds");
                invalidInput.setContentText("De klas werd niet toegevoegd");
                invalidInput.showAndWait();
            }
        }
        gekozenBestandLbl.setText(file.getName());

    }

    @FXML
    private void dubbelKlik(MouseEvent event) {

    }

    @FXML
    private void nieuweKlasBtnClicked(ActionEvent event) {
        detailsStackPane.getChildren().clear();
        detailsStackPane.getChildren().add(new CreateKlasController(klasController));

    }

    // <editor-fold desc="=== Hulp methodes ===" >
    private boolean isCellLeeg(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }
        return cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty();
    }

    private void voegEventHandlersToe() {

        this.addEventHandler(DeleteEvent.DELETE, event -> {
//            ConfirmationBuilder builder = new ConfirmationBuilder(event.getId());
//            builder.addObserver(this);
//            children.add(builder.buildConfirmation());
//            children.clear();
//            klasController.deleteKlas(event.getId());
            int x = event.getId();
            boolean y = klasController.zitKlasInSessie(x);
            if (!y) {
                ConfirmationBuilder builder = new ConfirmationBuilder(event.getId(), "klas");
                builder.addObserver(this);
                children.add(builder.buildConfirmation());
            }
        });

        this.addEventHandler(DetailsEvent.DETAILS, event -> {
            children.clear();
            if (event.getId() < 0) {
                int size = klasController.getAllKlassen().size();
                children.add(new DetailsKlasController(klasController.getAllKlassen().get(size - 1), klasController));
            } else {
                children.add(new DetailsKlasController(klasController.getKlas(event.getId()), klasController));
            }
        });

        this.addEventHandler(AnnuleerEvent.ANNULEER, event -> {
            children.clear();
            if (event.getId() >= 0) {
                children.add(new DetailsKlasController(klasController.getKlas(event.getId()), klasController));
            }
        });
    }

//    private void terugNaarMenu() {
//        Scene scene = new Scene(new MenuPanelController());
//        Stage stage = (Stage) this.getScene().getWindow();
//        stage.setTitle("Menu");
//        stage.setScene(scene);
//        stage.show();
//    }
    // </editor-fold>
    @Override
    public void update(Observable o, Object arg) {
        boolean confirmed = ((ConfirmationBuilder) o).isConfirmed();
        int id = ((ConfirmationBuilder) o).getId();
        if (confirmed) {
            String klasNaam = klasController.getKlas(id).getNaam();
            klasController.deleteKlas(id);
            children.clear();
            children.add(new NotificatiePanelController(String.format("Klas %s is verwijderd", klasNaam), Kleuren.GROEN));
        } else {
            children.remove(children.size() - 1);
            ((DetailsKlasController) children.get(children.size() - 1)).toggleButton();
        }
    }
}
