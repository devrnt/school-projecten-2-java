package controllers;

import domein.Groepsbewerking;
import domein.Klas;
import domein.Oefening;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;

/**
 *
 * @author devri
 */
public class SessieController implements Observer {

    private SessieDao sessieRepo;
    private GenericDao<Klas> klasRepo;
    private ObservableList<Sessie> sessieLijst;
    private FilteredList<Sessie> gefilterdeSessieLijst;

    public SessieController() {
        setSessieRepo(new SessieDaoJpa(Sessie.class));
        setKlasRepo(new GenericDaoJpa(Klas.class));
    }

    public void setSessieRepo(SessieDao sessieRepo) {
        this.sessieRepo = sessieRepo;
        this.sessieRepo.addObserver(this);
        sessieLijst = FXCollections.observableArrayList(sessieRepo.findAll());
        gefilterdeSessieLijst = new FilteredList<>(sessieLijst, s -> true);
    }

    public void setKlasRepo(GenericDaoJpa klasRepo) {
        this.klasRepo = klasRepo;
    }

    public void createSessie(
            String naam, String omschrijving,
            Klas klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie) {

        if (bestaatSessieNaam(naam)) {
            throw new IllegalArgumentException("Een sessie met deze naam bestaat al");
        } else {

            sessieRepo.insert(new Sessie(naam, omschrijving, klas,
                    lesuur, datum, soortOnderwijs, foutAntwActie));

        }
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public Sessie getSessie(int id) {
        return sessieRepo.get(id);
    }

    public ObservableList<Sessie> getAllSessies() {
        return gefilterdeSessieLijst.sorted(Comparator.comparing(Sessie::getNaam));
    }

    public void deleteSessie(int id) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }
        sessieRepo.delete(sessie);
    }

    public boolean bestaatSessieNaam(String naam) {
        Sessie sessie = sessieRepo.getByNaam(naam.toLowerCase());
        return sessie != null;
    }

    public void updateSessie(int id, String naam, String omschrijving,
            Klas klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie
    ) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }

        sessie.setNaam(naam);
        sessie.setOmschrijving(omschrijving);
        sessie.setKlas(klas);
        sessie.setLesuur(lesuur);
        sessie.setDatum(datum);
        sessie.setSoortOnderwijs(soortOnderwijs);
        sessie.setFoutAntwActie(foutAntwActie);

        sessieRepo.update(sessie);
    }

    public List<Klas> getAllKlassen() {
        return FXCollections.observableArrayList(
                klasRepo.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Klas::getNaam))
                        .collect(Collectors.toList())
        );
    }

    public Klas getKlas(int id) {
        return klasRepo.get(id);
    }

    public void applyFilter(String toFilter) {
        gefilterdeSessieLijst.setPredicate(sessie -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            if (sessie.getNaam().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            } else if (sessie.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            }
            return false; // No matches
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        sessieLijst.clear();
        sessieLijst.addAll(sessieRepo.findAll());
    }

}
