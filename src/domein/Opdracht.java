package domein;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity

@NamedQueries({
    @NamedQuery(name = "Opdracht.findAll",
            query = "SELECT o FROM Opdracht o")
})
public class Opdracht implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Oefening oefening;
    private String Toegangscode;
    private int volgnr;

    protected Opdracht() {
    }

    public Opdracht(Oefening oefening, String Toegangscode, int volgnr) {
        this.oefening = oefening;
        this.Toegangscode = Toegangscode;
        this.volgnr = volgnr;
    }

    public Opdracht(Oefening oefening, String Toegangscode) {
        this.oefening = oefening;
        this.Toegangscode = Toegangscode;
        this.volgnr = 0;
    }

    public Oefening getOefening() {
        return oefening;
    }

    public String getToegangscode() {
        return Toegangscode;
    }
    
}
