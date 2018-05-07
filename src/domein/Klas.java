/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import sun.java2d.pipe.SpanShapeRenderer.Simple;

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
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Leerling> leerlingen;

    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty aantalLlnProperty;

    public Klas() {
        leerlingen = new ArrayList<>();
    }

    public Klas(String naam) {
        setNaam(naam);
        leerlingen = new ArrayList<>();
    }

    public Klas(String naam, List<Leerling> leerlingen) {
        setNaam(naam);
        setLeerlingen(leerlingen);
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
        naamProperty.set(naam);
    }

    public ObservableList<Leerling> getLeerlingen() {
        return FXCollections.observableArrayList(leerlingen);
    }

    public void setLeerlingen(List<Leerling> leerlingen) {
        this.leerlingen = leerlingen;
    }

    public int getAantalLeerlingen() {
        return leerlingen.size();
    }

    public SimpleStringProperty getAantalLeerlingenProp() {
        return new SimpleStringProperty(Integer.toString(leerlingen.size()));
    }

    public SimpleStringProperty getNaamProperty() {
        return naamProperty;
    }

    // </editor-fold>
    public void voegLeerlingToe(Leerling leerling) {
        if (bevatVolledigeNaam(leerling)) {
            throw new IllegalArgumentException("Leerling met deze naam bestaat al.");
        } else {
            leerlingen.add(leerling);
        }

    }

    public void verwijderLeerling(Leerling leerling) {
        leerlingen.remove(leerling);
    }

    private boolean bevatVolledigeNaam(Leerling leerling) {
        return leerlingen
                .stream()
                .map(Leerling.class::cast)
                .anyMatch(l -> l.getNaam().equalsIgnoreCase(leerling.getNaam()) || l.getVoornaam().equalsIgnoreCase(leerling.getNaam()));
    }
}
