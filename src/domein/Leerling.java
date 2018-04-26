/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 *
 * @author devri
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Leerling.findAll",
            query = "SELECT l FROM Leerling l")
})
public class Leerling implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String voornaam;
    private String naam;

    @Transient
    private SimpleStringProperty voornaamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();

    protected Leerling() {

    }

    public Leerling(String voornaam, String naam) {
        setVoornaam(voornaam);
        setNaam(naam);
    }

    public int getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        if (!voornaam.isEmpty()) {
            this.voornaam = voornaam;
            voornaamProperty.set(voornaam);
        } else {
            throw new IllegalArgumentException("Voornaam mag niet leeg zijn.");
        }
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        if (!naam.isEmpty()) {
            this.naam = naam;
            naamProperty.set(naam);
        } else {
            throw new IllegalArgumentException("Naam mag niet leeg zijn.");
        }
    }

    public String getVolledigeNaam() {
        return voornaam + " " + naam;
    }

    public SimpleStringProperty getVoornaamProperty() {
        return voornaamProperty;
    }

    public SimpleStringProperty getNaamProperty() {
        return naamProperty;
    }
}
