package controllers;

import domein.Klas;
import domein.KlasBeheer;
import domein.Leerling;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.KlasDao;
import repository.KlasDaoJpa;

/**
 *
 * @author devri
 */
public final class KlasController {

    private KlasBeheer klasBeheer;

    public KlasController() {
        klasBeheer = new KlasBeheer();
    }

    public KlasBeheer getKlasBeheer() {
        return klasBeheer;
    }

    /**
     * Maakt nieuwe sessie aan en voegt deze toe aan de databank
     *
     * @param klasNaam String met de naam van de klas (uniek)
     * @param leerlingen Lijst van Strings met leerlingen van de klas
     * @throws IllegalArgumentException als de klasnaam reeds bestaat
     *
     */
    public void createKlas(
            String klasNaam, List<Leerling> leerlingen) {

        if (getKlasBeheer().bestaatKlasNaam(klasNaam)) {
            throw new IllegalArgumentException("Een klas met deze naam bestaat al");
        } else {
            klasBeheer.createKlas(klasNaam, leerlingen);
        }
    }

    /**
     * Geeft een klas terug
     *
     * @param id id van te op te vragen klas
     * @return klas
     */
    public Klas getKlas(int id) {
        return klasBeheer.getKlas(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle klassen
     * in de databank
     *
     * @return een ObservableList met Klassen
     */
    public ObservableList<Klas> getAllKlassen() {
        return klasBeheer.getAllKlassen();
    }

    public void deleteKlas(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
