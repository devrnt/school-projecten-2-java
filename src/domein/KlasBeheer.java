package domein;

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
public final class KlasBeheer implements Observer {

    private KlasDao klasRepo;
    private ObservableList<Klas> klasLijst;
    private FilteredList<Klas> gefilterdeKlasLijst;

    public KlasBeheer() {
        setKlasRepo(new KlasDaoJpa(Klas.class));
    }

    public void setKlasRepo(KlasDaoJpa klasRepo) {
        this.klasRepo = klasRepo;
        this.klasRepo.addObserver(this);
        klasLijst = FXCollections.observableArrayList(klasRepo.findAll());
        gefilterdeKlasLijst = new FilteredList<>(klasLijst, s -> true);
    }

    public void createKlas(
            String klasNaam, List<Leerling> leerlingen) {

        if (bestaatKlasNaam(klasNaam)) {
            throw new IllegalArgumentException("Een klas met deze naam bestaat al");
        } else {
            klasRepo.insert(new Klas(klasNaam, leerlingen));

        }
    }

    public Klas getKlas(int id) {
        return klasRepo.get(id);
    }

    public ObservableList<Klas> getAllKlassen() {
        return gefilterdeKlasLijst.sorted(Comparator.comparing(Klas::getNaam));
    }

    public boolean bestaatKlasNaam(String naam) {
        Klas klas = klasRepo.getByNaam(naam);
        return klas != null;
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    @Override
    public void update(Observable o, Object arg) {
        klasLijst.clear();
        klasLijst.addAll(klasRepo.findAll());
    }

    public void verwijderKlas(int id) {
         klasRepo.delete(klasRepo.get(id));
    }

}
