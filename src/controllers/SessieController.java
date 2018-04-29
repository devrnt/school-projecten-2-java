package controllers;

import domein.BreakOutBox;
import domein.Klas;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import domein.FoutAntwoordActieEnum;
import domein.ISessie;
import domein.SessieBeheer;
import exceptions.NotFoundException;
import java.util.Date;
import javafx.collections.ObservableList;

/**
 *
 * @author devri
 */
public final class SessieController {

    private SessieBeheer sessieBeheer;

    public SessieController() {
        sessieBeheer = new SessieBeheer();
    }

    public SessieBeheer getSessieBeheer() {
        return sessieBeheer;
    }

    /**
     * Maakt nieuwe sessie aan en voegt deze toe aan de databank
     *
     * @param naam String met de naam van de sessie (uniek)
     * @param omschrijving String met de omschrijving van de sessie
     * @param klas Klas met de klasnaam en de betreffende leerlingen in die klas
     * @param box
     * @param datum Date met de dag wanneer deze sessie geactiveerd kan worden
     * @param soortOnderwijs SoortOnderwijsEnum voor welke doelgroep deze sessie
     * is (afstands of dagonderwijs)
     * @param foutAntwoordActie FoutAntwoordActieEnum met wat er gebeurt als de
     * groep 3 maal een fout antwoord ingeeft
     * @param isGedaan
     */
    public void createSessie(
            String naam, String omschrijving,
            Klas klas, BreakOutBox box, Date datum,
            SoortOnderwijsEnum soortOnderwijs, FoutAntwoordActieEnum foutAntwoordActie, Boolean isGedaan) {

        sessieBeheer.createSessie(naam, omschrijving, klas, box, datum, soortOnderwijs, foutAntwoordActie, isGedaan);

    }

    /**
     * Geeft een Sessie terug
     *
     * @param id id van te op te vragen sessie
     * @return sessie
     */
    public Sessie getSessie(int id) {
        return sessieBeheer.getSessie(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle sessies
     * in de databank
     *
     * @return een ObservableList met Sessies
     */
    public ObservableList<Sessie> getAllSessies() {
        return sessieBeheer.getAllSessies();
    }

    /**
     * Verwijdert een sessie uit de databank
     *
     * @param id id van de te verwijderen sessie
     * @throws NotFoundException indien er geen sessie werd gevonden met
     * meegegeven id
     *
     */
    public void deleteSessie(int id) {
        sessieBeheer.deleteSessie(id);
    }

    /**
     * Wijzigt een sessie via de setters en past deze wijziging toe in de
     * databank
     *
     * @param id id van de te wijzigen sessie
     * @param naam
     * @param omschrijving
     * @param klas
     * @param box
     * @param datum
     * @param soortOnderwijs
     * @param foutAntwoordActie
     * @throws NotFoundException als de te wijzigen sessie niet gevonden werd
     */
    public void updateSessie(int id, String naam, String omschrijving,
            Klas klas, BreakOutBox box, Date datum,
            SoortOnderwijsEnum soortOnderwijs, FoutAntwoordActieEnum foutAntwoordActie) {

        sessieBeheer.updateSessie(id, naam, omschrijving, klas, box, datum, soortOnderwijs, foutAntwoordActie);
    }

    /**
     * Filtert de sessieLijst obv de meegegeven String
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        sessieBeheer.applyFilter(toFilter);
    }

    public ISessie getISessie() {
        return new Sessie();
    }

}
