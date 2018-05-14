/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import exceptions.NotFoundException;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author devri
 */
public class GroepsbewerkingBeheer implements Observer {

    private GenericDao<Groepsbewerking> groepsbewerkingRepo;
    private final OefeningBeheer oefeningBeheer;
    private ObservableList<Groepsbewerking> groepsbewerkingLijst;
    private FilteredList<Groepsbewerking> gefilterdeGroepsbewerkingLijst;

    public GroepsbewerkingBeheer() {
        setGroepsbewerkingRepo(new GenericDaoJpa(Groepsbewerking.class));
        oefeningBeheer = new OefeningBeheer();
    }

    private void setGroepsbewerkingRepo(GenericDaoJpa groepsbewerkingRepo) {
        this.groepsbewerkingRepo = groepsbewerkingRepo;
        this.groepsbewerkingRepo.addObserver(this);
        groepsbewerkingLijst = FXCollections.observableArrayList(this.groepsbewerkingRepo.findAll());
        gefilterdeGroepsbewerkingLijst = new FilteredList<>(groepsbewerkingLijst, s -> true);
    }

    public void createGroepsbewerking(String omschrijving, int factor, OperatorEnum operator) {
        // are there any checks for this?
        groepsbewerkingRepo.insert(new Groepsbewerking(omschrijving, factor, operator));
    }

    public Groepsbewerking getGroepsbewerking(int id) {
        return this.groepsbewerkingRepo.get(id);
    }

    public ObservableList<Groepsbewerking> getAllGroepsbewerkingen() {
        return gefilterdeGroepsbewerkingLijst.sorted(Comparator.comparing(Groepsbewerking::getOmschrijving));
    }

    public void updateGroepsbewerking(int id, String omschrijving, int factor, OperatorEnum operator) {
        Groepsbewerking groepsBewerking = groepsbewerkingRepo.get(id);
        if (groepsBewerking == null) {
            throw new NotFoundException("De groepsbewerking werd niet gevonden");
        }

        groepsBewerking.setOmschrijving(omschrijving);
        groepsBewerking.setFactor(factor);
        groepsBewerking.setOperator(operator);

        groepsbewerkingRepo.update(groepsBewerking);
    }

    public void deleteGroepsbewerking(int id) {
        Groepsbewerking groepsBewerking = groepsbewerkingRepo.get(id);
        if (groepsBewerking == null) {
            throw new NotFoundException("De groepsbewerking werd niet gevonden");
        } else {
            groepsbewerkingRepo.delete(groepsBewerking);
        }
    }

    public void applyFilter(String toFilter) {
        gefilterdeGroepsbewerkingLijst.setPredicate(groepsBew -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            return groepsBew.getOmschrijving().trim().toLowerCase().replaceAll("\\s+","").contains(lowerCaseFilter)
                    || groepsBew.getOperator().toString().trim().toLowerCase().contains(lowerCaseFilter)
                    || Integer.toString(groepsBew.getFactor()).equals(lowerCaseFilter);
        });
    }

    public boolean zitGroepsbewerkingInOef(int groepsbewId) {
        Groepsbewerking groepsBewerking = groepsbewerkingRepo.get(groepsbewId);
        if (groepsBewerking == null) {
            throw new NotFoundException("De groepsbewerking werd niet gevonden");
        }
        return oefeningBeheer.getOefeningen()
                .stream()
                .anyMatch(oef -> oef.getGroepsbewerkingen().contains(groepsBewerking));
    }

    @Override
    public void update(Observable o, Object arg) {
        groepsbewerkingLijst.clear();
        groepsbewerkingLijst.addAll(groepsbewerkingRepo.findAll());
    }

}
