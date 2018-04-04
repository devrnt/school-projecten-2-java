/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author devri
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Klas.findAll",
            query = "SELECT k FROM Klas k")
})
public class Klas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    //tijdelijk een list van strings
    private List<String> leerlingen;

    public Klas() {
    }

    public Klas(String naam) {
        this.naam = naam;
        leerlingen = new ArrayList<>();
    }

    public Klas(String naam, List<String> leerlingen) {
        this.naam = naam;
        this.leerlingen = leerlingen;
    }

    // <editor-fold desc="Getters and Setters" >
    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public ObservableList<String> getLeerlingen() {
        return FXCollections.observableArrayList(leerlingen);
    }

    public void setLeerlingen(List<String> leerlingen) {
        this.leerlingen = leerlingen;
    }

    // </editor-fold>
    public void voegLeerlingToe(String leerling) {
        if (leerlingen.stream().anyMatch(l -> l.trim().equalsIgnoreCase(leerling))) {
            throw new IllegalArgumentException("Leerling bestaat al.");
        } else {
            leerlingen.add(leerling);
        }
    }

    public void verwijderLeerling(String leerling) {
        leerlingen.remove(leerling);
    }
}
