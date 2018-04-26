package controllers;

import domein.Klas;
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
public final class KlasController implements Observer {

    private KlasDao klasRepo;
    private ObservableList<Klas> klasLijst;
    private FilteredList<Klas> gefilterdeKlasLijst;

    public KlasController() {
        setKlasRepo(new KlasDaoJpa(Klas.class));
    }

    public void setKlasRepo(KlasDaoJpa klasRepo) {
        this.klasRepo = klasRepo;
        this.klasRepo.addObserver(this);
        klasLijst = FXCollections.observableArrayList(klasRepo.findAll());
        gefilterdeKlasLijst = new FilteredList<>(klasLijst, s -> true);
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

        if (bestaatKlasNaam(klasNaam)) {
            throw new IllegalArgumentException("Een klas met deze naam bestaat al");
        } else {
            klasRepo.insert(new Klas(klasNaam, leerlingen));

        }
    }

    /**
     * Geeft een klas terug
     *
     * @param id id van te op te vragen klas
     * @return klas
     */
    public Klas getKlas(int id) {
        return klasRepo.get(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle klassen
     * in de databank
     *
     * @return een ObservableList met Klassen
     */
    public ObservableList<Klas> getAllKlassen() {
        return gefilterdeKlasLijst.sorted(Comparator.comparing(Klas::getNaam));
    }

    /**
     * Valideert of een klas reeds bestaat
     *
     * @param naam van de te valideren klas
     * @return true als een klas reeds bestaat anders false
     *
     */
    public boolean bestaatKlasNaam(String naam) {
        Klas klas = klasRepo.getByNaam(naam);
        return klas != null;
    }

    /**
     * Sluit de persistentie
     */
    public void close() {
        GenericDaoJpa.closePersistency();
    }

    @Override
    public void update(Observable o, Object arg) {
        klasLijst.clear();
        klasLijst.addAll(klasRepo.findAll());
    }

}
