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
public class ActieBeheer implements Observer {

    private GenericDao<Actie> actieRepo;
    private ObservableList<Actie> actieLijst;
    private FilteredList<Actie> gefilterdeActieLijst;

    public ActieBeheer() {
        setActieRepo(new GenericDaoJpa(Actie.class));
    }

    private void setActieRepo(GenericDaoJpa actieRepo) {
        this.actieRepo = actieRepo;
        this.actieRepo.addObserver(this);
        actieLijst = FXCollections.observableArrayList(this.actieRepo.findAll());
        gefilterdeActieLijst = new FilteredList<>(actieLijst, s -> true);
    }

    public void createActie(String omschrijving) {
        // are there any checks for this?
        actieRepo.insert(new Actie(omschrijving));
    }

    public Actie getActie(int id) {
        return this.actieRepo.get(id);
    }

    public ObservableList<Actie> getAllActies() {
        return gefilterdeActieLijst.sorted(Comparator.comparing(Actie::getOmschrijving));
    }

    public void updateActie(int id, String omschrijving) {
        Actie actie = actieRepo.get(id);
        if (actie == null) {
            throw new NotFoundException("De actie werd niet gevonden");
        }

        actie.setOmschrijving(omschrijving);

        actieRepo.update(actie);
    }

    public void deleteActie(int id) {
        Actie actie = actieRepo.get(id);
        if (actie == null) {
            throw new NotFoundException("De actie werd niet gevonden");
        } else {
            actieRepo.delete(actie);
        }
    }

    public void applyFilter(String toFilter) {
        gefilterdeActieLijst.setPredicate(actie -> {
            if (toFilter == null || toFilter.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = toFilter.toLowerCase();
            lowerCaseFilter = lowerCaseFilter.trim().replaceAll("\\s+", "");

            if (actie.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            } else if (actie.getOmschrijving().toLowerCase().trim().replaceAll("\\s+", "").contains(lowerCaseFilter)) {
                return true;
            }
            return false; // No matches
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        actieLijst.clear();
        actieLijst.addAll(actieRepo.findAll());
    }

}
