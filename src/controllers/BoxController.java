package controllers;

import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class BoxController {

    private GenericDao<BreakOutBox> breakOutBoxRepo;
    private ObservableList<BreakOutBox> boxLijst;
    private FilteredList<BreakOutBox> gefilterdeBoxLijst;

    public BoxController() {
        setBreakOutBoxRepo(new GenericDaoJpa<>(BreakOutBox.class));
    }

    public void setBreakOutBoxRepo(GenericDao<BreakOutBox> breakOutBoxRepo) {
        this.breakOutBoxRepo = breakOutBoxRepo;
        boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
        gefilterdeBoxLijst = new FilteredList<>(boxLijst, b -> true);
    }

    public void createBreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties, List<Toegangscode> toegangscodes) {
        breakOutBoxRepo.insert(new BreakOutBox(naam, omschrijving, oefeningen, acties, toegangscodes));
        // gebruik maken van add?
        boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return gefilterdeBoxLijst.sorted(Comparator.comparing(BreakOutBox::getNaam));
    }

    public BreakOutBox GeefBreakOutBox(int id) {
        return breakOutBoxRepo.get(id);
    }

    public ObservableList<String> getActiesByBox(int id) {
        List<Actie> acties = breakOutBoxRepo.get(id).getActies();
        return FXCollections.observableArrayList(acties.stream()
                .map(Actie::getOmschrijving)
                .collect(Collectors.toList())
        );
    }

    public ObservableList<String> getOefeningenByBox(int id) {
        List<Oefening> oefeningen = breakOutBoxRepo.get(id).getOefeningen();
        return FXCollections.observableArrayList(oefeningen.stream()
                .map(Oefening::getOpgave)
                .collect(Collectors.toList())
        );
    }

    public ObservableList<String> getToegangscodesByBox(int id) {
        List<Toegangscode> toegangscodes = breakOutBoxRepo.get(id).getToegangscodes();
        return FXCollections.observableArrayList(toegangscodes.stream()
                .map(Toegangscode::getCode)
                .collect(Collectors.toList())
        );
    }

    public void applyFilter(String toFilter) {
        gefilterdeBoxLijst.setPredicate(box -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            if (box.getNaam().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            } else if (box.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            }
            return false; // No matches
        });
    }

}
