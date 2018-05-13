package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@NamedQueries({
    @NamedQuery(name = "BreakOutBox.findAll",
            query = "SELECT o FROM BreakOutBox o")
})

public class BreakOutBox implements Serializable {

//Een Bob bestaat uit oefeningen en acties die uit vooraf gedefinieerde lijsten worden geselecteerd.
//Het aantal oefeningen en acties is gelijk binnen 1 BoB, maar de laatste actie ligt vast als “zoeken naar schatkist”. 
//(dus de te kiezen acties is minstens # oefeningen -1, want de laatste ligt vast)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    private String omschrijving;
    private SoortOnderwijsEnum soortOnderwijs;
    @ManyToMany
    private List<Oefening> oefeningen;
    @ManyToMany
    private List<Actie> acties;
    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();

    public BreakOutBox(String naam, String omschrijving, SoortOnderwijsEnum soortOnderwijs, List<Oefening> oefeningen, List<Actie> acties) {
        if (soortOnderwijs == SoortOnderwijsEnum.dagonderwijs && oefeningen.size() > 0 && oefeningen.size() != acties.size() + 1) {
            throw new IllegalArgumentException("#Oefeningen is niet 1 meer dan #Acties");
        }
        setNaam(naam);
        setOmschrijving(omschrijving);
        setSoortOnderwijs(soortOnderwijs);
        setOefeningen(oefeningen);
        setActies(acties);

    }

    public BreakOutBox(String naam, String omschrijving, SoortOnderwijsEnum soortOnderwijs, List<Oefening> oefeningen) {
        this(naam, omschrijving, soortOnderwijs, oefeningen, new ArrayList<Actie>());
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
        omschrijvingProperty.set(omschrijving);
    }

    public void setSoortOnderwijs(SoortOnderwijsEnum soortOnderwijs) {
        this.soortOnderwijs = soortOnderwijs;
    }

    public void setOefeningen(List<Oefening> oefeningen) {
        this.oefeningen = oefeningen;
    }

    public void setActies(List<Actie> acties) {
        this.acties = acties;
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

    public SoortOnderwijsEnum getSoortOnderwijsEnum() {
        return soortOnderwijs;
    }

    public String getSoortOnderwijs() {
        return soortOnderwijs.name();
    }

    public int getId() {
        return id;
    }

    public List<Oefening> getOefeningen() {
        return oefeningen;
    }

    public List<Actie> getActies() {
        return acties;
    }
}
