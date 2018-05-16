package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import utils.YouTiels;

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
    @OneToOne(cascade = CascadeType.PERSIST)
    private SessiePad sessiePad;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Leerling> leerlingen;

    public Groep() {
        leerlingen = new ArrayList<>();
    }

    public Groep(String naam, SessiePad pad) {
        setNaam(naam);
        this.sessiePad = pad;
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
    
    public SimpleStringProperty getNaamProp(){
        return new SimpleStringProperty(naam);
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public List<Leerling> getLeerlingen() {
        return leerlingen;
    }
    
    public SimpleStringProperty getLeerlingenProp(){
        String leerlingenString = leerlingen.stream().map(l -> l.getVolledigeNaam()).collect(Collectors.joining(", "));
        return new SimpleStringProperty(YouTiels.cutSentence(leerlingenString, 50));
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
