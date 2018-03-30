package main;


import controllers.OefeningController;
import controllers.SessieController;
import data.PopulateDB;
import repository.GenericDaoJpa;


/**
 *
 * @author sam
 */
public class StartUp {
    public static void main(String[] args) {
        OefeningController controller = new OefeningController();
        new PopulateDB().run();
        controller.createOefening("oef", 0, "feed", null);
        SessieController sessieController = new SessieController();
        sessieController.createSessie("Sessie1", "omschrijving van de sessie");
        
        controller.close();    
    }
}
