package domein;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

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

    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();

    public Actie(String omschrijving) {
        setOmschrijving(omschrijving);
    }

    protected Actie() {
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        omschrijvingProperty.set(omschrijving);
    }

    public int getId() {
        return id;
    }

    public SimpleStringProperty getOmschrijvingProperty() {
        return omschrijvingProperty;
    }

    @Override
    public String toString() {
        return omschrijving;
    }
}
