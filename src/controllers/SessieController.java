package controllers;

import domein.Sessie;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author devri
 */
public class SessieController {

    private GenericDao<Sessie> sessieRepo;

    public SessieController() {

        setSessieRepo(new GenericDaoJpa<>(Sessie.class));

    }

    public void setSessieRepo(GenericDao<Sessie> sessieRepo) {
        this.sessieRepo = sessieRepo;
    }

    public void createSessie(String naam, String omschrijving) {
        if (sessieRepo.exists(naam)) {
            throw new IllegalArgumentException("Een sessie met deze naam bestaat al");
        }
        sessieRepo.insert(new Sessie(naam, omschrijving, "1", 2.0, new Date()));
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }
    
    
     public Sessie getSessie(String naam) {
        return sessieRepo.get(naam);
    }

    public ObservableList<Sessie> getAllSessies() {
        return FXCollections.observableArrayList(
                sessieRepo.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Sessie::getNaam))
                        .collect(Collectors.toList())
        );
    }

}
