package main;

import controllers.BoxController;
import controllers.OefeningController;
import controllers.SessieController;
import data.PopulateDB;
import domein.SoortOnderwijsEnum;
import repository.GenericDaoJpa;

public class StartUp {

    public static void main(String[] args) {
        OefeningController controller = new OefeningController();
        new PopulateDB().run();
//        controller.createOefening("oef", 0, "feed", null);
        SessieController sessieController = new SessieController();
        
        controller.close();    
    }
}
