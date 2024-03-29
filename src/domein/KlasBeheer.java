package domein;

import domein.Klas;
import domein.Leerling;
import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
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
        Comparator<Klas> comparatorIgnorecase = (a,b) -> a.getNaam().toLowerCase().compareTo(b.getNaam().toLowerCase());
        return gefilterdeKlasLijst.sorted(comparatorIgnorecase);
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
//Door de Vreemde sleutels kunnen we klas niet verwijderen         
//klasRepo.delete(klasRepo.get(id));
        //System.out.println("Klas kan niet verwijderd worden door de vreemde sleutels");
        Klas klas = klasRepo.get(id);
        if (klas == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }
        klasRepo.delete(klas);
    }
 public Klas getMeestRecenteKlas(){
        return klasRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Klas::getId).reversed())
                .collect(Collectors.toList())
                .get(0);
    }
}
