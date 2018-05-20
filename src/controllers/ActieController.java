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

    /**
     * Maakt nieuwe actie aan en voegt deze toe aan de databank
     *
     * @param omschrijving String met de omschrijving van de actie
     */
    public void createActie(String omschrijving) {
        actieBeheer.createActie(omschrijving);
    }

    /**
     * Geeft een actie terug indien deze gevonden werd
     *
     * @param id id van te op te vragen actie
     * @return actie
     */
    public Actie getActie(int id) {
        return actieBeheer.getActie(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle
     * acties in de databank.
     *
     * @return een ObservableList met acties
     */
    public ObservableList<Actie> getAllActies() {
        return actieBeheer.getAllActies();
    }
    
     /**
     * Geeft de meeste recent aangemaakte actie terug
     * @return actie
     */
    public Actie getMeestRecenteActie(){
        return actieBeheer.getMeestRecenteActie();
    }

    /**
     * Wijzigt een actie via de setters en past deze wijziging toe in
     * de databank
     *
     * @param id id van de te wijzigen actie
     * @param omschrijving
     * @throws NotFoundException als de te wijzigen actie niet
     * gevonden werd
     */
    public void updateActie(int id, String omschrijving) {
        actieBeheer.updateActie(id, omschrijving);
    }

     /**
     * Verwijdert een sessie uit de databank
     *
     * @param id id van de te verwijderen actie
     * @throws NotFoundException indien er geen actie werd gevonden
     * met meegegeven id 
     */
    public void deleteActie(int id) {
        actieBeheer.deleteActie(id);
    }
    
      /**
     * Controleert of de actie in een box zit 
     *
     * @param actieId  id van de te controleren actie
     * @return true of false
     */
    public boolean zitActieInBox(int actieId){
        return actieBeheer.zitActieInBox(actieId);
    }

     /**
     * Filtert de actieLijst obv de meegegeven String. 
     * Deze filtert op de omschrijving van de actie
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        actieBeheer.applyFilter(toFilter);
    }

}
