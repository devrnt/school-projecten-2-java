package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author devri
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Groep.findAll",
            query = "SELECT g FROM Groep g")
})
public class Groep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    private Pad pad;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Leerling> leerlingen;

    public Groep() {
        leerlingen = new ArrayList<>();
    }

    public Groep(String naam, Pad pad) {
        setNaam(naam);
        this.pad = pad;
        leerlingen = new ArrayList<>();
    }

    public Groep(String naam, List<Leerling> leerlingen) {
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
    }

    public List<Leerling> getLeerlingen() {
        return leerlingen;
    }

    public void setLeerlingen(List<Leerling> leerlingen) {
        this.leerlingen = leerlingen;
    }

    public int getAantalLeerlingen() {
        return leerlingen.size();
    }

    // </editor-fold>
    public void voegLeerlingToe(Leerling leerling) {
        leerlingen.add(leerling);
    }

    public void verwijderLeerling(Leerling leerling) {
        leerlingen.remove(leerling);
    }

}
