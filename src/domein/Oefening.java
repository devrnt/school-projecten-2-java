package domein;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 *
 * @author sam
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Oefening.findAll",
            query = "SELECT o FROM Oefening o")
})
public class Oefening implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String opgave;
    private int antwoord;
    private String feedback;
    @ManyToMany
    private List<Groepsbewerking> groepsbewerkingen;
    @Transient
    private SimpleStringProperty opgaveProp = new SimpleStringProperty();
    @Transient
    private SimpleIntegerProperty antwoordProp = new SimpleIntegerProperty();


    public Oefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        setOpgave(opgave);
        setAntwoord(antwoord);
        setFeedback(feedback);
        setGroepsbewerkingen(groepsbewerkingen);
    }

    public Oefening() {

    }

    public SimpleStringProperty getOpgaveProp() {
        return opgaveProp;
    }

    public SimpleIntegerProperty getAntwoordProp() {
        return antwoordProp;
    }
    
    

    public int getAntwoord() {
        return antwoord;
    }

    public String getOpgave() {
        return opgave;
    }

    public String getFeedback() {
        return feedback;
    }

    public List<Groepsbewerking> getGroepsbewerkingen() {
        return groepsbewerkingen;
    }

    public int getId() {
        return id;
    }

    public void setOpgave(String opgave) {
        this.opgave = opgave;
        opgaveProp.set(opgave);
    }

    public void setAntwoord(int antwoord) {
        this.antwoord = antwoord;
        antwoordProp.set(antwoord);
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setGroepsbewerkingen(List<Groepsbewerking> groepsbewerkingen) {
        this.groepsbewerkingen = groepsbewerkingen;
    }

}
