package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
import domein.OefeningBeheer;
import exceptions.NotFoundException;
import java.util.List;
import java.util.Observable;
import javafx.collections.ObservableList;
import repository.GenericDaoJpa;

public final class OefeningController {
    private OefeningBeheer oefeningBeheer;
    public OefeningController() {
        oefeningBeheer = new OefeningBeheer();
    }

    public OefeningBeheer getOefeningBeheer() {
        return oefeningBeheer;
    }
    
    /**
     * Maakt een nieuwe oefening aan en voegt deze toe aan de databank
     *
     * @param opgave String met de opgave
     * @param antwoord int met het antwoord van de opgave
     * @param feedback String met eventuele feedback
     * @param groepsbewerkingen Lijst met bijhorende groepsbewerkingen
     *
     */
    public void createOefening(String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
        oefeningBeheer.createOefening(opgave, antwoord, feedback, vak, doelstellingen, groepsbewerkingen);
    }

    /**
     * Wijzigt een oefening via de setters en past deze wijziging toe in de
     * databank
     *
     * @param oefId id van de te wijzigen oefening
     * @param opgave
     * @param antwoord
     * @param feedback
     * @param groepsbewerkingen
     */
    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
        oefeningBeheer.updateOefening(oefId, opgave, antwoord, feedback, vak, doelstellingen, groepsbewerkingen);
    }

    /**
     * Verwijdert een oefening uit de databank
     *
     * @param oefId id van de te verwijderen Oefening
     * @throws NotFoundException indien er geen Oefening werd gevonden met
     * meegegeven id
     */
    public void deleteOefening(int oefId) {
        oefeningBeheer.deleteOefening(oefId);
    }

    /**
     * Geeft een ObservableList terug van alle groepsbewerkingen in de databank
     *
     * @return Lijst van Groepsbewerkingen
     */
    public ObservableList<Groepsbewerking> getGroepsbewerkingen() {
        return oefeningBeheer.getGroepsbewerkingen();

    }

    /**
     * Geeft een ObservableList terug van omschrijven van groepsbewerkingen die
     * bij de opgevraagde Oefening horen
     *
     * @param oefeningId id van de te op te vragen Oefening
     * @return Lijst van omschrijvingen (String) van Groepsbewerkingen
     */
    public ObservableList<Groepsbewerking> getGroepsbewerkingenByOefening(int oefeningId) {
        return oefeningBeheer.getGroepsbewerkingenByOefening(oefeningId);
    }

    /**
     * Geeft een Oefening terug
     *
     * @param id id van te op te vragen Oefening
     * @return een Oefening
     */
    public Oefening getOefening(int id) {
        return oefeningBeheer.getOefening(id);
    }
    
    /**
     * Geeft een lijst terug van alle vakken die al gebruikt geweest zijn
     * in een oefening
     * @return een lijst van vakken
     */
    public List<String> getVakken(){
        return oefeningBeheer.getVakken();
    }
    
    

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle
     * Oefening in de databank
     *
     * @return een ObservableList met Oefeningen
     */
    public ObservableList<Oefening> getOefeningen() {
        return oefeningBeheer.getOefeningen();
    }

    /**
     * Sluit de persistentie
     */
    public void close() {
        GenericDaoJpa.closePersistency();
    }

    /**
     * Filtert de oefeningenlijst obv de meegegeven String
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        oefeningBeheer.applyFilter(toFilter);
    }

    

}
