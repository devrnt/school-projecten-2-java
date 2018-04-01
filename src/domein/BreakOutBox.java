package domein;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "BreakOutBox.findAll",
            query = "SELECT o FROM BreakOutBox o")
})

public class BreakOutBox implements Serializable {

//Een Bob bestaat uit oefeningen, acties en toegangscodes die uit vooraf gedefinieerde lijsten worden geselecteerd.
//Het aantal oefeningen en acties is gelijk binnen 1 BoB, maar de laatste actie ligt vast als “zoeken naar schatkist”. 
//(dus de te kiezen acties is minstens # oefeningen -1, want de laatste ligt vast)
    public BreakOutBox(String naam, String omschrijving, List<Oefening> oefeningen, List<Actie> acties, List<Toegangscode> toegangscodes) {
        this.naam = naam;
        this.omschrijving = omschrijving;
        this.oefeningen = oefeningen;
        this.acties = acties;
        this.toegangscodes = toegangscodes;
    }

    protected BreakOutBox() {
    }

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

    public String getNaam() {
        return naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

}
