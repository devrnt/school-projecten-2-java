package domein;

import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author devri
 */
public final class GroepBeheer implements Observer {

    private GenericDao<Groep> groepRepo;
    private ObservableList<Groep> groepLijst;
    private FilteredList<Groep> gefilterdeGroepLijst;
    private final SessieBeheer sessieBeheer;

    public GroepBeheer() {
        setGroepRepo(new GenericDaoJpa<>(Groep.class));
        sessieBeheer = new SessieBeheer();
    }

    public void setGroepRepo(GenericDao<Groep> groepRepo) {
        this.groepRepo = groepRepo;
        this.groepRepo.addObserver(this);
        groepLijst = FXCollections.observableArrayList(groepRepo.findAll());
        gefilterdeGroepLijst = new FilteredList<>(groepLijst, s -> true);
    }

    public void createGroep(String groepNaam, List<Leerling> leerlingen) {
        if (bestaatGroepsnaam(groepNaam)) {
            throw new IllegalArgumentException("Een groep met deze naam bestaat al");
        } else {
            groepRepo.insert(new Groep(groepNaam, leerlingen));
        }
    }

    public void updateGroep(int groepId, String groepNaam, List<Leerling> leerlingen) {
        Groep groep = groepRepo.get(groepId);
        if (groep == null) {
            throw new NotFoundException("Geen groep gevonden met id " + groepId);
        }
        groep.setNaam(groepNaam);
        groep.setLeerlingen(leerlingen);

        groepRepo.update(groep);
    }

    public void deleteGroep(int groepId) {
        Groep groep = groepRepo.get(groepId);
        if (groep == null) {
            throw new NotFoundException("Geen groep gevonden met id " + groepId);
        }
        groepRepo.delete(groep);    
    }

    public boolean zitGroepInSessie(int groepId) {
        //return sessieBeheer.getAllSessies().stream().anyMatch(s -> s.getGroepen())
        return false;
    }

    public Groep getGroep(int id) {
        return groepRepo.get(id);
    }

    public ObservableList<Groep> getGroepen() {
        Comparator<Groep> comparatorIgnorecase = (a,b) -> a.getNaam().toLowerCase().compareTo(b.getNaam().toLowerCase());
        return gefilterdeGroepLijst.sorted(comparatorIgnorecase);
    }
    
    public Groep getMeestRecenteGroep(){
        return groepRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Groep::getId).reversed())
                .findFirst()
                .orElse(null);
    }

    public boolean bestaatGroepsnaam(String naam) {
        return groepRepo.findAll()
                .stream()
                .anyMatch(g
                        -> g.getNaam()
                        .toLowerCase().equals(naam));
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    @Override
    public void update(Observable o, Object arg) {
        groepLijst.clear();
        groepLijst.addAll(groepRepo.findAll());
    }

    public void applyFilter(String name) {
        gefilterdeGroepLijst.setPredicate(g -> {
            if (name == null || name.trim().isEmpty()) {
                return true;
            }
            return g.getNaam().toLowerCase().contains(name.trim().toLowerCase())
                    || g.getLeerlingen().stream()
                            .anyMatch(l
                                    -> l.getNaam()
                                    .toLowerCase()
                                    .contains(name.trim().toLowerCase()))
                    || g.getLeerlingen().stream()
                            .anyMatch(l
                                    -> l.getVoornaam()
                                    .toLowerCase()
                                    .contains(name.trim().toLowerCase()));
                                   
        });
    }

}
