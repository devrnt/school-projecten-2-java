package controllers;

import domein.Groepsbewerking;
import domein.Oefening;
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

public final class OefeningController implements Observer {

    private GenericDao<Oefening> oefeningRepo;
    private ObservableList<Oefening> oefeningenLijst;
    private FilteredList<Oefening> gefilterdeOefeningenLijst;
    private GroepsbewerkingDao groepsbewerkingRepo;

    public OefeningController() {
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

    /**
     * Maakt een nieuwe oefening aan en voegt deze toe aan de databank
     *
     * @param opgave String met de opgave
     * @param antwoord int met het antwoord van de opgave
     * @param feedback String met eventuele feedback
     * @param groepsbewerkingen Lijst met bijhorende groepsbewerkingen
     *
     */
    public void createOefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        GenericDaoJpa.startTransaction();
        oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
//        oefeningenLijst.add(oefeningRepo.get(oefeningRepo.findAll().size()));
        GenericDaoJpa.commitTransaction();

    }

    /**
     * Wijzigt een oefening via de setters en past deze wijziging toe in de
     * databank
     *
     * @param oefId id van de te wijzigen oefening
     * @param opgave
     * @param antwoord
     * @param feedback
     * @param groepsbewerkingen
     */
    public void updateOefening(int oefId, String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
//        int index = oefeningenLijst.indexOf(oefening);
        oefening.setOpgave(opgave);
        oefening.setAntwoord(antwoord);
        oefening.setFeedback(feedback);
        oefening.setGroepsbewerkingen(groepsbewerkingen);

        GenericDaoJpa.startTransaction();
        oefeningRepo.update(oefening);
//        oefeningenLijst.set(index, oefening);    
        GenericDaoJpa.commitTransaction();
    }

    /**
     * Verwijdert een oefening uit de databank
     *
     * @param oefId id van de te verwijderen Oefening
     * @throws NotFoundException indien er geen Oefening werd gevonden met
     * meegegeven id
     */
    public void deleteOefening(int oefId) {
        Oefening oefening = oefeningRepo.get(oefId);
        if (oefening == null) {
            throw new NotFoundException("De oefening werd niet gevonden");
        }
        GenericDaoJpa.startTransaction();
        oefeningRepo.delete(oefening);
//        oefeningenLijst.remove(oefening);
        GenericDaoJpa.commitTransaction();
    }

    /**
     * Geeft een ObservableList terug van alle groepsbewerkingen in de databank
     *
     * @return Lijst van Groepsbewerkingen
     */
    public ObservableList<Groepsbewerking> getGroepsbewerkingen() {
        return FXCollections.observableArrayList(groepsbewerkingRepo.findAll());

    }

    /**
     * Geeft een ObservableList terug van omschrijven van groepsbewerkingen die
     * bij de opgevraagde Oefening horen
     *
     * @param oefeningId id van de te op te vragen Oefening
     * @return Lijst van omschrijvingen (String) van Groepsbewerkingen
     */
    public ObservableList<String> getGroepsbewerkingenByOefening(int oefeningId) {
        List<Groepsbewerking> groepsbewerkingen = oefeningRepo.get(oefeningId).getGroepsbewerkingen();
        return FXCollections.observableArrayList(groepsbewerkingen.stream()
                .map(Groepsbewerking::getOmschrijving)
                .collect(Collectors.toList())
        );
    }

    /**
     * Geeft een Oefening terug
     *
     * @param id id van te op te vragen Oefening
     * @return een Oefening
     */
    public Oefening getOefening(int id) {
        return oefeningRepo.get(id);
    }

    /**
     * Geeft een ObservableList terug die gefiltered kan worden van alle
     * Oefening in de databank
     *
     * @return een ObservableList met Oefeningen
     */
    public ObservableList<Oefening> getOefeningen() {
        return gefilterdeOefeningenLijst;
    }

    /**
     * Sluit de persistentie
     */
    public void close() {
        GenericDaoJpa.closePersistency();
    }

    /**
     * Filtert de oefeningenlijst obv de meegegeven String
     *
     * @param toFilter Tekst waarop de lijst moet gefilterd worden
     */
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

}
