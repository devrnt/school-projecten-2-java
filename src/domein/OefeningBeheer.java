package domein;

import exceptions.NotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.GroepsbewerkingDao;
import repository.GroepsbewerkingDaoJpa;

/**
 *
 * @author sam
 */
public final class OefeningBeheer implements Observer {
    private GenericDao<Oefening> oefeningRepo;
    private ObservableList<Oefening> oefeningenLijst;
    private FilteredList<Oefening> gefilterdeOefeningenLijst;
    private GroepsbewerkingDao groepsbewerkingRepo;

    public OefeningBeheer() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
        setGroepsbewerkingRepo(new GroepsbewerkingDaoJpa());
    }
    
    public void setGroepsbewerkingRepo(GroepsbewerkingDao groepsbewerkingRepo) {
        this.groepsbewerkingRepo = groepsbewerkingRepo;
    }

    public void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
        this.oefeningRepo.addObserver(this);
        oefeningenLijst = FXCollections.observableArrayList(oefeningRepo.findAll());
        gefilterdeOefeningenLijst = new FilteredList<>(oefeningenLijst, o -> true);
    }

    public void createOefening(String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
        oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, vak, doelstellingen, groepsbewerkingen));
    }
    
    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        oefening.setOpgave(opgave);
        oefening.setAntwoord(antwoord);
        oefening.setFeedback(feedback);
        oefening.setVak(vak);
        oefening.setDoelstellingen(doelstellingen);
        oefening.setGroepsbewerkingen(groepsbewerkingen);
        

        oefeningRepo.update(oefening);
    }
    
    public void deleteOefening(int oefId) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        oefeningRepo.delete(oefening);
    }
    
    public Oefening getOefening(int id) {
        return oefeningRepo.get(id);
    }
    
    public ObservableList<Oefening> getOefeningen() {
        return gefilterdeOefeningenLijst;
    }
    
    public void applyFilter(String toFilter) {
        gefilterdeOefeningenLijst.setPredicate(o -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            return o.getOpgave().replaceAll("\\s+", "").toLowerCase().contains(toFilter.replaceAll("\\s+", "").toLowerCase());
        });

    }
    
    @Override
    public void update(Observable o, Object arg) {
        oefeningenLijst.clear();
        oefeningenLijst.addAll(oefeningRepo.findAll());
    }
    
    public ObservableList<Groepsbewerking> getGroepsbewerkingen() {
        return FXCollections.observableArrayList(groepsbewerkingRepo.findAll());
    }

    public ObservableList<Groepsbewerking> getGroepsbewerkingenByOefening(int oefeningId) {
        List<Groepsbewerking> groepsbewerkingen = oefeningRepo.get(oefeningId).getGroepsbewerkingen();
        return FXCollections.observableArrayList(groepsbewerkingen);
    }
    
}
