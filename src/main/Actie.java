/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.ActieController;
import data.PopulateDB;
import domein.ActieBeheer;

/**
 *
 * @author devri
 */
public class Actie {

    public static void main(String[] args) {
        ActieController actieController = new ActieController();
        actieController.createActie("actie whatever");
        actieController.getAllActies().stream().forEach(actie -> System.out.println(actie));
                System.out.println("====");

        actieController.deleteActie(1);
        actieController.getAllActies().stream().forEach(actie -> System.out.println(actie));

    }
}
