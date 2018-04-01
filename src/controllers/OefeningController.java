package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.GroepsbewerkingDao;
import repository.GroepsbewerkingDaoJpa;

public class OefeningController {

    private GenericDao<Oefening> oefeningRepo;
    private ObservableList<Oefening> oefeningenLijst;
    private FilteredList<Oefening> gefilterdeOefeningenLijst;
    private GroepsbewerkingDao groepsbewerkingRepo;

    public OefeningController() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
        setGroepsbewerkingRepo(new GroepsbewerkingDaoJpa());
        oefeningenLijst = FXCollections.observableArrayList(oefeningRepo.findAll());
        gefilterdeOefeningenLijst = new FilteredList<>(oefeningenLijst, o -> true);
    }

    public void setGroepsbewerkingRepo(GroepsbewerkingDao groepsbewerkingRepo) {
        this.groepsbewerkingRepo = groepsbewerkingRepo;
    }

    public final void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }

    public void createOefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
            throw e;
        }
        GenericDaoJpa.commitTransaction();
    }

    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        oefening.setOpgave(opgave);
        oefening.setAntwoord(antwoord);
        oefening.setFeedback(feedback);
        oefening.setGroepsbewerkingen(groepsbewerkingen);

        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.update(oefening);
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
        }
        GenericDaoJpa.commitTransaction();
    }

    public void deleteOefening(int oefId) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.delete(oefening);
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
        }
        GenericDaoJpa.commitTransaction();
    }

    public ObservableList<Groepsbewerking> getGroepsbewerkingen() {
        return FXCollections.observableArrayList(groepsbewerkingRepo.findAll());

    }

    public ObservableList<String> getGroepsbewerkingenByOefening(int oefeningId) {
        List<Groepsbewerking> groepsbewerkingen = oefeningRepo.get(oefeningId).getGroepsbewerkingen();
        return FXCollections.observableArrayList(groepsbewerkingen.stream()
                .map(Groepsbewerking::getOmschrijving)
                .collect(Collectors.toList())
        );
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public Oefening getOefening(int id) {
        return oefeningRepo.get(id);
    }

    public ObservableList<Oefening> getOefeningen() {
        return gefilterdeOefeningenLijst;
    }

    public void applyFilter(String toFilter) {
        gefilterdeOefeningenLijst.setPredicate(o -> {
            if (toFilter == null || toFilter.isEmpty())
                return true;
            return o.getOpgave().toLowerCase().contains(toFilter.toLowerCase());
        });
        
    }

}
