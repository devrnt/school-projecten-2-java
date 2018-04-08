/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Klas;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GenericDaoJpa;
import repository.KlasDao;
import repository.KlasDaoJpa;

/**
 *
 * @author devri
 */
public class KlasController implements Observer {

    private KlasDao klasRepo;
    private ObservableList<Klas> klasLijst;
    private FilteredList<Klas> gefilterdeKlasLijst;

    public KlasController() {
        setKlasRepo(new KlasDaoJpa(Klas.class));
    }

    public void setKlasRepo(KlasDaoJpa klasRepo) {
        this.klasRepo = klasRepo;
        this.klasRepo.addObserver(this);
        klasLijst = FXCollections.observableArrayList(klasRepo.findAll());
        gefilterdeKlasLijst = new FilteredList<>(klasLijst, s -> true);
    }

    public ObservableList<Klas> getAllKlassen() {
        return gefilterdeKlasLijst.sorted(Comparator.comparing(Klas::getNaam));
    }

    public Klas getKlas(int id) {
        return klasRepo.get(id);
    }

    public void createKlas(
            String klasNaam, List<String> leerlingen) {

        if (bestaatSessieNaam(klasNaam)) {
            throw new IllegalArgumentException("Een klas met deze naam bestaat al");
        } else {
            klasRepo.insert(new Klas(klasNaam, leerlingen));

        }
    }

    public void close() {
        GenericDaoJpa.closePersistency();
    }

    public boolean bestaatSessieNaam(String naam) {
        Klas klas = klasRepo.getByNaam(naam.toLowerCase());
        return klas != null;
    }

    @Override
    public void update(Observable o, Object arg) {
        klasLijst.clear();
        klasLijst.addAll(klasRepo.findAll());
    }

}
