package controllers;

import com.itextpdf.text.DocumentException;
import domein.Actie;
import domein.BreakOutBox;

import domein.BreakOutBoxBeheer;
import domein.Oefening;
import domein.SessieBeheer;
import domein.SoortOnderwijsEnum;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import repository.GenericDao;

public final class BoxController {

    private final BreakOutBoxBeheer boxBeheer;

    public BoxController() {
        this.boxBeheer = new BreakOutBoxBeheer();
    }

    /**
     * Geeft de BreakOutBoxBeheer terug die gebruikt kan worden voor
     * verschillende bewerkingen
     *
     * @return een BreakOutBoxBeheer
     */
    public BreakOutBoxBeheer getBoxBeheer() {
        return boxBeheer;
    }

    /**
     * Crieert een nieuwe BreakOutBox en voegt deze toe aan de databank
     *
     * @param naam naam van de nieuwe BoB
     * @param omschrijving beknopte omschrijving van de BoB
     * @param soortOnderwijs enumeration van soort onderwijs
     * @param oefeningen de lijst van oefeningen voor de BoB
     * @param acties de lijst van acties voor de BoB
     */
    public void createBreakOutBox(String naam, String omschrijving, SoortOnderwijsEnum soortOnderwijs, List<Oefening> oefeningen, List<Actie> acties) {
        boxBeheer.createBreakOutBox(naam, omschrijving, soortOnderwijs, oefeningen, acties);
    }

    /**
     * Sluit de JPA zodat de applicatie geen BoB's meer kan opvragen of
     * wegschrijven
     */
    public void close() {
        boxBeheer.close();
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle BoB's in
     * de databank.
     *
     * @return een ObservableList met BoB's
     */
    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return boxBeheer.getAllBreakOutBoxen();
    }

    /**
     * Geeft een BoB terug indien deze gevonden werd
     *
     * @param id id van te op te vragen Bob
     * @return BreakOutBox als id correct is
     */
    public BreakOutBox getBreakOutBox(int id) {
        return boxBeheer.getBreakOutBox(id);
    }

    /**
     * Geeft de meeste recent aangemaakte BoB terug
     *
     * @return BreakOutBox
     */
    public BreakOutBox getMeestRecenteBreakOutBox() {
        return boxBeheer.getMeestRecenteBreakOutBox();
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle Acties
     * in de BoB
     *
     * @param id id van de op te vragen BoB
     * @return een ObservableList met acties van de BoB
     */
    public ObservableList<Actie> getActiesByBox(int id) {
        return boxBeheer.getActiesByBox(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle
     * Oefeningen in de BoB
     *
     * @param id id van de op te vragen BoB
     * @return een ObservableList met oefeningen van de BoB
     */
    public ObservableList<Oefening> getOefeningenByBox(int id) {
        return boxBeheer.getOefeningenByBox(id);
    }

    /**
     * Filtert de BoB-lijst obv de meegegeven String. Deze filtert op de naam en
     * omschrijving van de BoB
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
    public void applyFilter(String toFilter) {
        boxBeheer.applyFilter(toFilter);
    }

    /**
     * Verwijdert een BoB uit de databank
     *
     * @param boxId id van de te verwijderen BoB
     * @throws NotFoundException indien er geen BoB werd gevonden met meegegeven
     * id
     */
    public void deleteBreakOutBox(int boxId) {
        boxBeheer.deleteBreakOutBox(boxId);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle Acties
     * in de databank
     *
     * @return een ObservableList met alle acties in de databank
     */
    public ObservableList<Actie> getActies() {
        return boxBeheer.getActies();
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden op alle
     * Oefeningen in de databank
     *
     * @return een ObservableList met alle oefeningen in de databank
     */
    public ObservableList<Oefening> getOefeningen() {
        return boxBeheer.getOefeningen();
    }

    /**
     * Wijzigt een BoB via setters en past deze wijziging toe in de databank
     *
     * @param id de onveranderbare id van de te wijzigen BoB
     * @param naam de geupdate naam
     * @param omschrijving de geupdate omschrijving
     * @param onderwijs de geupdate enumeration van soort onderwijs
     * @param geselecteerdeOefeningen de geupdate List van Oefeningen
     * @param geselecteerdeActies de geupdate List van Acties
     *
     * @throws NotFoundException als de te wijzigen BoB niet gevonden werd
     */
    public void updateBreakOutBox(int id, String naam, String omschrijving, SoortOnderwijsEnum onderwijs, List<Oefening> geselecteerdeOefeningen, List<Actie> geselecteerdeActies) {
        boxBeheer.updateBreakOutBox(id, naam, omschrijving, onderwijs, geselecteerdeOefeningen, geselecteerdeActies);
    }

    /**
     * Crieert een PDF samenvatting van de geselecteerde BoB 
     *
     * @param id id van de BoB die samengevat moet worden
     *      *
     * @throws IOException als er een probleem is tijdens het wegschrijven
     * @throws FileNotFoundException als de opgevraagde File niet gevonden werd
     * @throws DocumentException als er een fout optreed tijdens het opstellen van de Pdf
     */
    public void createSamenvattingBox(int id) throws IOException, FileNotFoundException, DocumentException {
        boxBeheer.createSamenvattingBox(id);
    }
    /**
     * Controleert of de BoB in een sessie zit
     *
     * @param boxId id van de te controleren Bob
     * @return true of false
     */
    public boolean zitBoxInSessie(int boxId) {
        SessieBeheer sb = new SessieBeheer();
        return sb.zitBoxInSessie(boxId);
    }

    public boolean bestaatBoxNaam(String text) {
        return boxBeheer.bestaatBoxNaam(text);
    }
}
