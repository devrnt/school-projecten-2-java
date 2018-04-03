package domein;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({
    @NamedQuery(name = "BreakOutBox.findAll",
            query = "SELECT o FROM BreakOutBox o")
})

public class BreakOutBox implements Serializable {

//Een Bob bestaat uit oefeningen, acties en toegangscodes die uit vooraf gedefinieerde lijsten worden geselecteerd.
//Het aantal oefeningen en acties is gelijk binnen 1 BoB, maar de laatste actie ligt vast als “zoeken naar schatkist”. 
//(dus de te kiezen acties is minstens # oefeningen -1, want de laatste ligt vast)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    private String omschrijving;
    @ManyToMany
    private List<Oefening> oefeningen;
    @ManyToMany
    private List<Actie> acties;
    @ManyToMany
    private List<Toegangscode> toegangscodes;
    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();

    public BreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties, List<Toegangscode> toegangscodes) {
        setNaam(naam);
        setOmschrijving(omschrijving);
        setOefeningen(oefeningen);
        setActies(acties);
        setToegangscodes(toegangscodes);
    }

    protected BreakOutBox() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaam(String naam) {
        this.naam = naam;
        naamProperty.set(naam);
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        omschrijvingProperty.set(naam);
    }

    public void setOefeningen(List<Oefening> oefeningen) {
        this.oefeningen = oefeningen;
    }

    public void setActies(List<Actie> acties) {
        this.acties = acties;
    }

    public void setToegangscodes(List<Toegangscode> toegangscodes) {
        this.toegangscodes = toegangscodes;
    }

    public SimpleStringProperty getNaamProperty() {
        return naamProperty;
    }

    public SimpleStringProperty getOmschrijvingProperty() {
        return omschrijvingProperty;
    }

    public String getNaam() {
        return naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

}
