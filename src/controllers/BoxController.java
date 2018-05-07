package controllers;

import com.itextpdf.text.DocumentException;
import domein.Actie;
import domein.BreakOutBox;

import domein.BreakOutBoxBeheer;
import domein.Oefening;
import domein.SessieBeheer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import repository.GenericDao;

public final class BoxController {

    private final BreakOutBoxBeheer boxBeheer;

    public BoxController() {
        this.boxBeheer = new BreakOutBoxBeheer();
    }

    public BreakOutBoxBeheer getBoxBeheer() {
        return boxBeheer;
    }

    public void createBreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties) {
        boxBeheer.createBreakOutBox(naam, omschrijving, oefeningen, acties);
    }

    public void close() {
        boxBeheer.close();
    }

    public ObservableList<BreakOutBox> getAllBreakOutBoxen() {
        return boxBeheer.getAllBreakOutBoxen();
    }

    public BreakOutBox GeefBreakOutBox(int id) {
        return boxBeheer.GeefBreakOutBox(id);
    }

    public ObservableList<Actie> getActiesByBox(int id) {
        return boxBeheer.getActiesByBox(id);
    }

    public ObservableList<Oefening> getOefeningenByBox(int id) {
        return boxBeheer.getOefeningenByBox(id);
    }

    public void applyFilter(String toFilter) {
        boxBeheer.applyFilter(toFilter);
    }

    public void deleteBreakOutBox(int boxId) {
        boxBeheer.deleteBreakOutBox(boxId);
    }

    public ObservableList<Actie> getActies() {
        return boxBeheer.getActies();
    }

    public ObservableList<Oefening> getOefeningen() {
        return boxBeheer.getOefeningen();
    }

    public void updateBreakOutBox(int id, String naam, String omschrijving, List<Oefening> geselecteerdeOefeningen, List<Actie> geselecteerdeActies) {
        boxBeheer.updateBreakOutBox(id, naam, omschrijving, geselecteerdeOefeningen, geselecteerdeActies);
    }

    public void createSamenvattingBox(int id) throws IOException, FileNotFoundException, DocumentException {
        boxBeheer.createSamenvattingBox(id);
    }

    public boolean isBoxGedaan(BreakOutBox box) {
        SessieBeheer sb = new SessieBeheer();
        return sb.isBoxGedaan(box);
    }
}
