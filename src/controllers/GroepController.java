package controllers;

import domein.GroepBeheer;
import domein.Leerling;
import domein.SessiePad;
import exceptions.NotFoundException;
import java.util.List;

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
   

//    /**
//     * Wijzigt een groep via de setters en past deze wijziging toe in de
//     * databank
//     *
//     * @param oefId id van de te wijzigen groep
//     * @param opgave
//     * @param antwoord
//     * @param feedback
//     * @param groepsbewerkingen
//     */
//    public void updateGroep(int oefId, String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
//        groepBeheer.updateGroep(oefId, opgave, antwoord, feedback, vak, doelstellingen, groepsbewerkingen);
//    }
//
//    /**
//     * Verwijdert een groep uit de databank
//     *
//     * @param oefId id van de te verwijderen Groep
//     * @throws NotFoundException indien er geen Groep werd gevonden met
//     * meegegeven id
//     */
//    public void deleteGroep(int oefId) {
//        groepBeheer.deleteGroep(oefId);
//    }
//    
//    /**
//     * Controleert of de groep nog in een breakoutbox zit
//     * @param oefId
//     * @return true indien de groep nog in een breakoutbox zit, anders false
//     */
//    public boolean zitGroepInBox(int oefId){
//        return groepBeheer.zitGroepInBox(oefId);
//    }
//
//    /**
//     * Geeft een ObservableList terug van alle groepsbewerkingen in de databank
//     *
//     * @return Lijst van Groepsbewerkingen
//     */
//    public ObservableList<Groepsbewerking> getGroepsbewerkingen() {
//        return groepBeheer.getGroepsbewerkingen();
//
//    }
//
//    /**
//     * Geeft een ObservableList terug van omschrijven van groepsbewerkingen die
//     * bij de opgevraagde Groep horen
//     *
//     * @param groepId id van de te op te vragen Groep
//     * @return Lijst van omschrijvingen (String) van Groepsbewerkingen
//     */
//    public ObservableList<Groepsbewerking> getGroepsbewerkingenByGroep(int groepId) {
//        return groepBeheer.getGroepsbewerkingenByGroep(groepId);
//    }
//
//    /**
//     * Geeft een Groep terug
//     *
//     * @param id id van te op te vragen Groep
//     * @return een Groep
//     */
//    public Groep getGroep(int id) {
//        return groepBeheer.getGroep(id);
//    }
//
//    /**
//     * Geeft een ObservableList terug die gefiltered kan worden van alle
//     * Groep in de databank
//     *
//     * @return een ObservableList met Groepen
//     */
//    public ObservableList<Groep> getGroepen() {
//        return groepBeheer.getGroepen();
//    }
//    
//    /**
//     * Geeft de meeste recent aangemaakt groep terug
//     * @return 
//     */
//    public Groep getMeestRecenteGroep(){
//        return groepBeheer.getMeestRecenteGroep();
//    }
//    
//    /**
//     * Geeft een lijst terug van alle vakken die al gebruikt geweest zijn
//     * in een groep
//     * @return een lijst van vakken
//     */
//    public List<String> getVakken(){
//        return groepBeheer.getVakken();
//    }
//    
//    /**
//     * Geeft een lijst terug van alle doelstellingen die al gebruikt geweest zijn
//     * in een groep
//     * @return een lijst van doelstellingen
//     */
//    public List<String> getDoelstellingen(){
//        return groepBeheer.getDoelstellingen();
//    }
//
//    /**
//     * Sluit de persistentie
//     */
//    public void close() {
//        GenericDaoJpa.closePersistency();
//    }
//
//    /**
//     * Filtert de groepenlijst obv de meegegeven String
//     *
//     * @param toFilter Tekst waarop de lijst moet gefilterd worden
//     */
//    public void applyFilter(String toFilter) {
//        groepBeheer.applyFilter(toFilter);
//    }

    

}
