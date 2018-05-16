package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

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

    public SessiePad getSessiePad() {
        return sessiePad;
    }

    // </editor-fold>
    public void voegLeerlingToe(Leerling leerling) {
        leerlingen.add(leerling);
    }

    public void verwijderLeerling(Leerling leerling) {
        leerlingen.remove(leerling);
    }
}
