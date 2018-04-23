package domein;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Actie.findAll",
            query = "SELECT o FROM Actie o")
})
public class Actie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String omschrijving;

    public Actie(String Omschrijving) {
        this.omschrijving = Omschrijving;
    }

    protected Actie() {
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    @Override
    public String toString() {
        return omschrijving;
    }
}