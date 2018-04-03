package controllers;

import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
import java.util.List;
import java.util.stream.Collectors;
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

    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return FXCollections.observableArrayList(BreakOutBoxRepo.findAll());
    }

    public BreakOutBox GeefBreakOutBox(int id) {
        return BreakOutBoxRepo.get(id);
    }

    public ObservableList<String> getActiesByBox(int id) {
        List<Actie> acties = BreakOutBoxRepo.get(id).getActies();
        return FXCollections.observableArrayList(acties.stream()
                .map(Actie::getOmschrijving)
                .collect(Collectors.toList())
        );
    }

    public ObservableList<String> getOefeningenByBox(int id) {
        List<Oefening> oefeningen = BreakOutBoxRepo.get(id).getOefeningen();
        return FXCollections.observableArrayList(oefeningen.stream()
                .map(Oefening::getOpgave)
                .collect(Collectors.toList())
        );
    }

    public ObservableList<String> getToegangscodesByBox(int id) {
        List<Toegangscode> toegangscodes = BreakOutBoxRepo.get(id).getToegangscodes();
        return FXCollections.observableArrayList(toegangscodes.stream()
                .map(Toegangscode::getCode)
                .collect(Collectors.toList())
        );
    }

}
