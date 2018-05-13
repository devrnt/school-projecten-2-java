/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Actie;
import domein.ActieBeheer;
import javafx.collections.ObservableList;

/**
 *
 * @author devri
 */
public final class ActieController {

    private ActieBeheer actieBeheer;

    public ActieController() {
        actieBeheer = new ActieBeheer();
    }

    public ActieBeheer getActieBeheer() {
        return actieBeheer;
    }

    public void createActie(String omschrijving) {
        actieBeheer.createActie(omschrijving);
    }

    public Actie getActie(int id) {
        return actieBeheer.getActie(id);
    }

    public ObservableList<Actie> getAllActies() {
        return actieBeheer.getAllActies();
    }

    public void updateActie(int id, String omschrijving) {
        actieBeheer.updateActie(id, omschrijving);
    }

    public void deleteActie(int id) {
        actieBeheer.deleteActie(id);
    }
    
    public boolean zitActieInBox(int actieId){
        return actieBeheer.zitActieInBox(actieId);
    }

    public void applyFilter(String toFilter) {
        actieBeheer.applyFilter(toFilter);
    }

}
