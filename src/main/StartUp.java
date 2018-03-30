package main;


import controllers.OefeningController;
import domein.Groepsbewerking;
import repository.GenericDaoJpa;


/**
 *
 * @author sam
 */
public class StartUp {
    public static void main(String[] args) {
        OefeningController controller = new OefeningController();
        controller.createOefening("opgave", 0, "feedback", null);
        controller.close();    
    }
}
