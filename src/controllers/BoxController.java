package controllers;

import domein.Actie;
import domein.BreakOutBox;
import domein.Oefening;
import domein.Toegangscode;
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

public class BoxController implements Observer {

    private GenericDao<BreakOutBox> breakOutBoxRepo;
    private GenericDao<Actie> actieRepo;
    private GenericDao<Oefening> oefeningRepo;
    private GenericDao<Toegangscode> toegangscodeRepo;

    private ObservableList<BreakOutBox> boxLijst;
    private FilteredList<BreakOutBox> gefilterdeBoxLijst;

    public BoxController() {
        setBreakOutBoxRepo(new GenericDaoJpa<>(BreakOutBox.class));
        setActieRepo(new GenericDaoJpa<>(Actie.class));
        setToegangscodeRepo(new GenericDaoJpa<>(Toegangscode.class));
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
    }

    public void setBreakOutBoxRepo(GenericDao<BreakOutBox> breakOutBoxRepo) {
        this.breakOutBoxRepo = breakOutBoxRepo;
        this.breakOutBoxRepo.addObserver(this);
        boxLijst = FXCollections.observableArrayList(breakOutBoxRepo.findAll());
        gefilterdeBoxLijst = new FilteredList<>(boxLijst, b -> true);
    }

    public void setActieRepo(GenericDao<Actie> actieRepo) {
        this.actieRepo = actieRepo;
    }

    public void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }

    public void setToegangscodeRepo(GenericDao<Toegangscode> toegangscodeRepo) {
        this.toegangscodeRepo = toegangscodeRepo;
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

    public void deleteBreakOutBox(int boxId) {
        BreakOutBox box = breakOutBoxRepo.get(boxId);
        if (box == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        breakOutBoxRepo.delete(box);
//        oefeningenLijst.remove(oefening);
    }

    @Override
    public void update(Observable o, Object arg) {
        boxLijst.clear();
        boxLijst.addAll(breakOutBoxRepo.findAll());
    }

    public ObservableList<Actie> getActies() {
        return FXCollections.observableArrayList(actieRepo.findAll());
    }

    public ObservableList<Toegangscode> getToegangscodes() {
        return FXCollections.observableArrayList(toegangscodeRepo.findAll());
    }

    public ObservableList<Oefening> getOefeningen() {
        return FXCollections.observableArrayList(oefeningRepo.findAll());
    }

    public void updateBreakOutBox(int id, String naam, String omschrijving, List<Oefening> geselecteerdeOefeningen, List<Actie> geselecteerdeActies, List<Toegangscode> geselecteerdeToegangscodes) {
        BreakOutBox box = breakOutBoxRepo.get(id);
        if (box == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        box.setNaam(naam);
        box.setOmschrijving(omschrijving);
        box.setActies(geselecteerdeActies);
        box.setOefeningen(geselecteerdeOefeningen);
        box.setToegangscodes(geselecteerdeToegangscodes);
        breakOutBoxRepo.update(box);
    }
}
