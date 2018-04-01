package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
import java.util.List;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class OefeningController {

    private GenericDao<Oefening> oefeningRepo;

    public OefeningController() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
    }

    public void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }

    public void createOefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public List<Oefening> GeefAlleOefeningen() {
        return oefeningRepo.findAll();
    }

}
