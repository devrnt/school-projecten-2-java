/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Groepsbewerking;
import domein.GroepsbewerkingBeheer;
import domein.OperatorEnum;
import javafx.collections.ObservableList;

/**
 *
 * @author devri
 */
public class GroepsbewerkingController {

    private GroepsbewerkingBeheer groepsbewerkingBeheer;

    public GroepsbewerkingController() {
        groepsbewerkingBeheer = new GroepsbewerkingBeheer();
    }

    public GroepsbewerkingBeheer getGroepsbewerkingBeheer() {
        return groepsbewerkingBeheer;
    }

    /**
     * Maakt nieuwe groepsbewerking aan en voegt deze toe aan de databank
     *
     * @param omschrijving String met de omschrijving van de groepsbewerking
     * @param factor Integer met de factor dat moet gebeuren op de operator
     * @param operator Operator van het type OperatorEnum
     */
    public void createGroepsbewerking(String omschrijving, int factor, OperatorEnum operator) {
        groepsbewerkingBeheer.createGroepsbewerking(omschrijving, factor, operator);
    }

    /**
     * Geeft een Groepsbewerking terug indien deze gevonden werd
     *
     * @param id id van te op te vragen groepsbewerking
     * @return groepsbewerking
     */
    public Groepsbewerking getGroepsbewerking(int id) {
        return groepsbewerkingBeheer.getGroepsbewerking(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle
     * groepsbewerkingen in de databank.
     *
     * @return een ObservableList met Groepsbewerkingen
     */
    public ObservableList<Groepsbewerking> getAllGroepsbewerking() {
        return groepsbewerkingBeheer.getAllGroepsbewerkingen();
    }

    /**
     * Wijzigt een groepsbewerking via de setters en past deze wijziging toe in
     * de databank
     *
     * @param id id van de te wijzigen groepsbewerking
     * @param omschrijving
     * @param factor
     * @param operator
     * @throws NotFoundException als de te wijzigen groepsbewerking niet
     * gevonden werd
     */
    public void updateGroepsbewerking(int id, String omschrijving, int factor, OperatorEnum operator) {
        groepsbewerkingBeheer.updateGroepsbewerking(id, omschrijving, factor, operator);
    }

    /**
     * Verwijdert een sessie uit de databank
     *
     * @param id id van de te verwijderen groepsbewerking
     * @throws NotFoundException indien er geen groepsbewerking werd gevonden
     * met meegegeven id * Voert de actie niet uit indien de groepsbewerking nog
     * in een oefening zit
     */
    public void deleteGroepsbewerking(int id) {
        groepsbewerkingBeheer.deleteGroepsbewerking(id);
    }

    /**
     * Controleert of de groepsbewerking in een oefening
     *
     * @param groepsbewId id van de te controleren groepsbewerking
     * @return
     */
    public boolean zitGroepsbewerkingInOefening(int groepsbewId) {
        return groepsbewerkingBeheer.zitGroepsbewerkingInOef(groepsbewId);
    }

    /**
     * Filtert de groepsbewerkingLijst obv de meegegeven String Deze filtert op
     * de omschrijving, operator en factor van de groepsbewerking
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        groepsbewerkingBeheer.applyFilter(toFilter);
    }
}
