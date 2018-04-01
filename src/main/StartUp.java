package main;

import controllers.BoxController;
import controllers.OefeningController;
import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
import java.util.ArrayList;
import java.util.List;
import repository.GenericDaoJpa;

public class StartUp {

    public static void main(String[] args) {
        //open connectie
        GenericDaoJpa.startTransaction();
        //controllers
        OefeningController oefController = new OefeningController();
        BoxController boxController = new BoxController();
        for (int i = 0; i < 3; i++) {
            boxController.createBreakOutBox("box" + i, "Omschrijving" + i, new ArrayList<Oefening>(), new ArrayList<Actie>(), new ArrayList<Toegangscode>());
        }
        GenericDaoJpa.commitTransaction();
        System.out.println(boxController.GeefBreakOutBox(1).getNaam());
        oefController.close();
    }
}
