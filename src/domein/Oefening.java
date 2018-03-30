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

    public Oefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen) {
        setOpgave(opgave);
        setAntwoord(antwoord);
        setFeedback(feedback);
        setGroepsbewerkingen(groepsbewerkingen);
    }

    protected Oefening() {

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
        if (opgave == null || opgave.trim().length() <= 0)
            throw new IllegalArgumentException("Opgave mag niet leeg zijn");
        this.opgave = opgave;
    }

    public void setAntwoord(int antwoord) {
        if (antwoord > Integer.MAX_VALUE || antwoord < Integer.MIN_VALUE)
            throw new IllegalArgumentException("Antwoord is buiten bereik");
        this.antwoord = antwoord;
    }

    public void setFeedback(String feedback) {
        if (feedback == null || feedback.trim().length() <= 0)
            throw new IllegalArgumentException("Opgave mag niet leeg zijn");
        this.feedback = feedback;
    }

    public void setGroepsbewerkingen(List<Groepsbewerking> groepsbewerkingen) {
        this.groepsbewerkingen = groepsbewerkingen;
    }

}
