package controllers;

import domein.Sessie;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.SessieDaoJpa;

/**
 *
 * @author devri
 */
public class SessieController {

    private SessieDaoJpa sessieRepo;
    private ObservableList<Sessie> sessieLijst;
    private FilteredList<Sessie> gefilterdeSessieLijst;

    public SessieController() {
        setSessieRepo(new SessieDaoJpa(Sessie.class));
    }

    public void setSessieRepo(SessieDaoJpa sessieRepo) {
        this.sessieRepo = sessieRepo;
        sessieLijst = FXCollections.observableArrayList(sessieRepo.findAll());
        gefilterdeSessieLijst = new FilteredList<>(sessieLijst, s -> true);
    }

    public void createSessie(
            String naam, String omschrijving,
            String klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie) {

        if (bestaatSessieNaam(naam)) {
            throw new IllegalArgumentException("Een sessie met deze naam bestaat al");
        } else {
            GenericDaoJpa.startTransaction();

            try {
                sessieRepo.insert(new Sessie(naam, omschrijving, klas, lesuur, datum, soortOnderwijs, foutAntwActie));
            } catch (Exception e) {
                GenericDaoJpa.rollbackTransaction();
                throw e;
            }
            sessieLijst.add(sessieRepo.findAll().get(sessieRepo.findAll().size() - 1));
            GenericDaoJpa.commitTransaction();
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
        GenericDaoJpa.startTransaction();
        try {
            sessieRepo.delete(sessie);
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
        }
        sessieLijst.remove(sessie);
        GenericDaoJpa.commitTransaction();
    }

    public boolean bestaatSessieNaam(String naam) {
        Sessie sessie = sessieRepo.getByNaam(naam.toLowerCase());
        return sessie != null;
    }

    public void updateSessie(int id, String naam, String omschrijving,
            String klas, int lesuur, Date datum,
            SoortOnderwijsEnum soortOnderwijs, String foutAntwActie
    ) {
        Sessie sessie = sessieRepo.get(id);
        if (sessie == null) {
            throw new NotFoundException("De sessie werd niet gevonden");
        }
        int index = sessieLijst.indexOf(sessie);
        sessie.setNaam(naam);
        sessie.setOmschrijving(omschrijving);
        sessie.setKlas(klas);
        sessie.setLesuur(lesuur);

        sessie.setDatum(datum);
        sessie.setSoortOnderwijs(soortOnderwijs);
        sessie.setFoutAntwActie(foutAntwActie);

        GenericDaoJpa.startTransaction();
        try {
            sessieRepo.update(sessie);
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
            throw e;
        }
        sessieLijst.set(index, sessie);
        GenericDaoJpa.commitTransaction();
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

}
