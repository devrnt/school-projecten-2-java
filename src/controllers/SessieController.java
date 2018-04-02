package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.SessieDao;
import repository.SessieDaoJpa;

/**
 *
 * @author devri
 */
public class SessieController {

    private SessieDaoJpa sessieRepo;

    public SessieController() {
        setSessieRepo(new SessieDaoJpa(Sessie.class));
    }

    public void setSessieRepo(SessieDaoJpa sessieRepo) {
        this.sessieRepo = sessieRepo;
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
        return FXCollections.observableArrayList(
                sessieRepo.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Sessie::getNaam))
                        .collect(Collectors.toList())
        );
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
        GenericDaoJpa.commitTransaction();
    }

}
