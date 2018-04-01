package controllers;

import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class BoxController {

    private GenericDao<BreakOutBox> BreakOutBoxRepo;

    public BoxController() {
        setBreakOutBoxRepo(new GenericDaoJpa<>(BreakOutBox.class));
    }

    public void setBreakOutBoxRepo(GenericDao<BreakOutBox> breakOutBoxRepo) {
        this.BreakOutBoxRepo = breakOutBoxRepo;
    }

    public void createBreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties, List<Toegangscode> toegangscodes) {
        BreakOutBoxRepo.insert(new BreakOutBox(naam, omschrijving, oefeningen, acties, toegangscodes));
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public ObservableList<BreakOutBox> GeefAlleBreakOutBoxen() {
        return FXCollections.observableArrayList(BreakOutBoxRepo.findAll());
    }

    public BreakOutBox GeefBreakOutBox(int id) {
        return BreakOutBoxRepo.get(id);
    }

}
