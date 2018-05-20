package controllers;

import domein.Groep;
import domein.GroepBeheer;
import domein.Leerling;
import domein.SessiePad;
import exceptions.NotFoundException;
import java.util.List;
import javafx.collections.ObservableList;
import repository.GenericDaoJpa;

public final class GroepController {
    private GroepBeheer groepBeheer;
    public GroepController() {
        groepBeheer = new GroepBeheer();
    }

    public GroepBeheer getGroepBeheer() {
        return groepBeheer;
    }
    
    /**
     * Maakt een nieuwe groep aan en voegt deze toe aan de databank
     *
     * @param groepsnaam Naam van de groep
     * @param leerlingen Lijst van leerlingen
     *
     */
    public void createGroep(String groepsnaam, List<Leerling> leerlingen) {
        groepBeheer.createGroep(groepsnaam, leerlingen);
    }
   

    /**
     * Wijzigt een groep via de setters en past deze wijziging toe in de
     * databank
     *
     * @param oefId id van de te wijzigen groep
     * @param opgave
     * @param antwoord
     * @param feedback
     * @param groepsbewerkingen
     */
    public void updateGroep(int groepId, String groepsnaam, List<Leerling> leerlingen) {
        groepBeheer.updateGroep(groepId, groepsnaam, leerlingen);
    }

    /**
     * Verwijdert een groep uit de databank
     *
     * @param groepId id van de te verwijderen Groep
     * @throws NotFoundException indien er geen Groep werd gevonden met
     * meegegeven id
     */
    public void deleteGroep(int groepId) {
        try {
            groepBeheer.deleteGroep(groepId);
        } catch (NotFoundException e) {
            throw(e);
        }
    }
    
    /**
     * Controleert of de groep nog in een sessie zit
     * @param groepId
     * @return true indien de groep nog in een sessie zit, anders false
     */
    public boolean zitGroepInSessie(int groepId){
        return groepBeheer.zitGroepInSessie(groepId);
    }


    /**
     * Geeft een Groep terug
     *
     * @param id id van te op te vragen Groep
     * @return een Groep
     */
    public Groep getGroep(int id) {
        return groepBeheer.getGroep(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle
     * Groep in de databank
     *
     * @return een ObservableList met Groepen
     */
    public ObservableList<Groep> getGroepen() {
        return groepBeheer.getGroepen();
    }
    
    /**
     * Geeft de meeste recent aangemaakt groep terug
     * @return 
     */
    public Groep getMeestRecenteGroep(){
        return groepBeheer.getMeestRecenteGroep();
    }

    /**
     * Sluit de persistentie
     */
    public void close() {
        GenericDaoJpa.closePersistency();
    }

    /**
     * Filtert de groepenlijst obv de meegegeven String
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        groepBeheer.applyFilter(toFilter);
    }

    

}
