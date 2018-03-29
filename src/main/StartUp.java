package main;


import controllers.OefeningController;
import repository.GenericDaoJpa;


/**
 *
 * @author sam
 */
public class StartUp {
    public static void main(String[] args) {
        GenericDaoJpa.startTransaction();
        OefeningController controller = new OefeningController();
        controller.createOefening("oef", 0, "feed", null);
        GenericDaoJpa.commitTransaction();
        controller.close();    
    }
}
