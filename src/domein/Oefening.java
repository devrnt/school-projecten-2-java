package domein;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private String vak;
    private List<String> doelstellingen;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Groepsbewerking> groepsbewerkingen;
//    @Transient
//    private SimpleStringProperty opgaveProp = new SimpleStringProperty();
//    @Transient
//    private SimpleIntegerProperty antwoordProp = new SimpleIntegerProperty();

    public Oefening(String opgave, int antwoord, String feedback, String vak, List<String> doelstellingen, List<Groepsbewerking> groepsbewerkingen) {
        setOpgave(opgave);
        setAntwoord(antwoord);
        setFeedback(feedback);
        setVak(vak);
        setDoelstellingen(doelstellingen);
        setGroepsbewerkingen(groepsbewerkingen);
    }

    public Oefening() {

    }

    public SimpleStringProperty getOpgaveProp() {
        return new SimpleStringProperty(opgave);
    }

    public SimpleIntegerProperty getAntwoordProp() {
        return new SimpleIntegerProperty(antwoord);
    }
    
    public SimpleStringProperty getVakProp(){
        return new SimpleStringProperty(vak);
    }
    
    public SimpleStringProperty getDoelstellingenProp(){
        return new SimpleStringProperty(doelstellingen.toString().substring(1, doelstellingen.toString().length() - 1));
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
//        opgaveProp.set(opgave);
    }

    public void setAntwoord(int antwoord) {
        this.antwoord = antwoord;
//        antwoordProp.set(antwoord);
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getVak() {
        return vak;
    }

    public void setVak(String vak) {
        this.vak = vak;
    }

    public List<String> getDoelstellingen() {
        return doelstellingen;
    }

    public void setDoelstellingen(List<String> doelstellingen) {
        this.doelstellingen = doelstellingen;
    }
    
    

    public void setGroepsbewerkingen(List<Groepsbewerking> groepsbewerkingen) {
        this.groepsbewerkingen = groepsbewerkingen;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Oefening other = (Oefening) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return opgave;
    }

}
