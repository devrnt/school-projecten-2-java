/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.KlasController;
import controllers.SessieController;
import domein.Klas;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

/**
 * FXML Controller class
 *
 * @author devri
 */
public class BeheerKlassenController extends AnchorPane {

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

    private FileChooser fileChooser;

    private ObservableList<Klas> klassen;

    private KlasController klasController;

    public BeheerKlassenController(KlasController klasController) {
        this.klasController = klasController;
        klassen = klasController.getAllKlassen();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/BeheerKlassen.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
        configureFileChooser();

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
        keerTerugBtn.setOnAction(event -> terugNaarMenu());

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
                        String leerling = dataFormatter.formatCellValue(cell);
                        try {
                            klas.voegLeerlingToe(leerling);

                        } catch (IllegalArgumentException e) {
                            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
                            invalidInput.setTitle("Leerling bestaat al");
                            invalidInput.setHeaderText("De leerling " + leerling + " bestaat reeds");
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
                Alert invalidInput = new Alert(Alert.AlertType.ERROR);
                invalidInput.setTitle("Klas lezen");
                invalidInput.setHeaderText("De klas " + klas.getNaam() + " bestaat reeds");
                invalidInput.setContentText("Deze klas werd niet toegevoegd");
                invalidInput.showAndWait();
            }
        }
        gekozenBestandLbl.setText("Geen");

    }

    @FXML
    private void dubbelKlik(MouseEvent event) {
        if (event.getClickCount() == 2) {
            int klasId = klassenTbl.getSelectionModel().getSelectedItem().getId();
            Scene scene = new Scene(new OverzichtLeerlingenInKlasController(klasController, klasId));
            Stage stage = new Stage();
            stage.setTitle("Bekijk leerlingen");
            stage.setScene(scene);
            stage.show();
        }
    }

    // <editor-fold desc="=== Hulp methodes ===" >
    private boolean isCellLeeg(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }
        return cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty();
    }

    private void terugNaarMenu() {
        Scene scene = new Scene(new HomePanelController());
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
    // </editor-fold>
}