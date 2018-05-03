/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import controllers.BoxController;
import controllers.KlasController;
import domein.Actie;
import domein.Leerling;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author ili
 */
public class CreateKlasController extends AnchorPane{
    
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
   private ListView<String> voorNaamList;
   @FXML
   private ListView <String> familieNaamList;
   
   private List<Leerling> tempList;
   
  
   
    
     public CreateKlasController(KlasController klasController) {
        this.klasController = klasController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../panels/CreateKlas.fxml"));

        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tempList =  new ArrayList<>();
       
            klasNaamTxt.textProperty().addListener((ObservableValue<? extends String> ob, String oldValue, String newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                fouteKlasnaamLbl.setText("Vul sessienaam in");
            } else {
                String sessieNaam = klasNaamTxt.getText();
               checkKlasNaam(sessieNaam);
            }
        });
        
     }
     @FXML
     private void llnToevoegenBtnClicked(ActionEvent event){
         Leerling leerling = new Leerling(voorNaamTxt.getText(),familieNaamTxt.getText());
         tempList.add(leerling);
         voorNaamList.getItems().add(leerling.getVoornaam());
         familieNaamList.getItems().add(leerling.getNaam());
         System.out.println(tempList.size());
     }
     
     @FXML
     private void klasAanmakenBtnClicked(ActionEvent event){
        klasController.createKlas(klasNaamTxt.toString(), tempList);
         Stage stage = (Stage) annuleerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerKlassenController(new KlasController())));
        stage.setTitle("Beheer klassen");
        stage.show();
         
     }
     @FXML
     private void annuleerBtnClicked (ActionEvent event){
            Stage stage = (Stage) annuleerBtn.getScene().getWindow();
        stage.setScene(new Scene(new BeheerKlassenController(new KlasController())));
        stage.setTitle("Beheer klassen");
        stage.show();
    }
     
    @FXML
    private void checkKlasNaam(String naam){
        if (klasController.bestaatKlas(naam)) {
            klasAanmakenBtn.setDisable(Boolean.TRUE);
            fouteKlasnaamLbl.setText("Klasnaam bestaat al");
            System.out.println(klasNaamTxt.toString()+"True");
        } else{
            klasAanmakenBtn.setDisable(Boolean.FALSE);
             fouteKlasnaamLbl.setText("");
             System.out.println(klasNaamTxt.toString()+"False");
        }
    }
    

     
     }
     
     
