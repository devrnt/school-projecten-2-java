package main;


import controllers.OefeningController;
import controllers.SessieController;
import repository.GenericDaoJpa;


/**
 *
 * @author sam
 */
public class StartUp {
    public static void main(String[] args) {
        OefeningController controller = new OefeningController();
        controller.createOefening("oef", 0, "feed", null);
        SessieController sessieController = new SessieController();
        sessieController.createSessie("Sessie1", "omschrijving van de sessie");
        
        GenericDaoJpa.commitTransaction();
        controller.close();    
    }
}
