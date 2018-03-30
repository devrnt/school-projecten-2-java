package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author sam
 */
public class OefeningController {
    private GenericDao<Oefening> oefeningRepo;
    private GenericDao<Groepsbewerking> groepsbewerkingRepo;

    public OefeningController() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
        setGroepsbewerkingRepo(new GenericDaoJpa<>(Groepsbewerking.class));
    }

    public final void setGroepsbewerkingRepo(GenericDao<Groepsbewerking> groepsbewerkingRepo) {
        this.groepsbewerkingRepo = groepsbewerkingRepo;
    }
    
    public final void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }
    
    public void createOefening(String opgave, int antwoord, String feedback, int[] groepsbewerkingIds){
        List<Groepsbewerking> groepsbewerkingen = new ArrayList<>();
        Arrays.stream(groepsbewerkingIds).forEach(id -> groepsbewerkingen.add(groepsbewerkingRepo.get(id)));
        GenericDaoJpa.startTransaction();
        try {
            oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
        } catch (Exception e) {
            GenericDaoJpa.rollbackTransaction();
        }
        GenericDaoJpa.commitTransaction();
    }
    
    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, int[] groepsbewerkingIds){
        List<Groepsbewerking> groepsbewerkingen = new ArrayList<>();
        Arrays.stream(groepsbewerkingIds).forEach(id -> groepsbewerkingen.add(groepsbewerkingRepo.get(id)));
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
    
    public void close(){
        GenericDaoJpa.closePersistency();
    }
    
}
