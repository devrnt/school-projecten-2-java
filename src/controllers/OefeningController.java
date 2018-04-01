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
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.GroepsbewerkingDao;
import repository.GroepsbewerkingDaoJpa;

/**
 *
 * @author sam
 */
public class OefeningController {
    private GenericDao<Oefening> oefeningRepo;
    private GroepsbewerkingDao groepsbewerkingRepo;

    public OefeningController() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
        setGroepsbewerkingRepo(new GroepsbewerkingDaoJpa());
    }

    public void setGroepsbewerkingRepo(GroepsbewerkingDao groepsbewerkingRepo) {
        this.groepsbewerkingRepo = groepsbewerkingRepo;
    }
    
    public final void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }
    
    public void createOefening(String opgave, int antwoord, String feedback, List<String> omschrijvingen){
        List<Groepsbewerking> groepsbewerkingen = groepsbewerkingRepo.getByOmschrijvingen(omschrijvingen);
        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
            throw e;
        }
        GenericDaoJpa.commitTransaction();
    }
    
    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, List<String> omschrijvingen){
        List<Groepsbewerking> groepsbewerkingen = groepsbewerkingRepo.getByOmschrijvingen(omschrijvingen);
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null)
            throw new NotFoundException("De oefening werd niet gevonden");
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
    
    public void deleteOefening(int oefId){
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null)
            throw new NotFoundException("De oefening werd niet gevonden");
        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.delete(oefening);
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
        }
        GenericDaoJpa.commitTransaction();
    }
    
    public ObservableList<String> getGroepsbewerkingen(){
        List<String> omschrijvingen = groepsbewerkingRepo.getOmschrijvingen();
        ObservableList<String> list = FXCollections.observableArrayList(omschrijvingen);
        
        return list;
    }
    
    public ObservableList<String> getGroepsbewerkingenByOefening(int oefeningId){
        List<Groepsbewerking> groepsbewerkingen = oefeningRepo.get(oefeningId).getGroepsbewerkingen();
        return FXCollections.observableArrayList(groepsbewerkingen.stream()
                .map(Groepsbewerking::getOmschrijving)
                .collect(Collectors.toList())
        );
    }
    
    public void close(){
        GenericDaoJpa.closePersistency();
    }

    public Oefening getOefening(int id) {
        return oefeningRepo.get(id);
    }
    
    public ObservableList<Oefening> getOefeningen(){
        return FXCollections.observableArrayList(
                oefeningRepo.findAll()
                        .stream()
                        .sorted(Comparator.comparing(o -> o.getOpgave()))
                        .collect(Collectors.toList())
        );
    }
    
}
