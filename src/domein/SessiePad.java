package domein;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author sam
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "SessiePad.findAll",
            query = "SELECT p FROM SessiePad p")
})
public class SessiePad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Actie> acties;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Opdracht> opdrachten;
    @Enumerated(EnumType.STRING)
    private SoortOnderwijsEnum onderwijs;
    private int voortgang;

    protected SessiePad() {
    }

    public SessiePad(List<Opdracht> opdrachten, List<Actie> acties, SoortOnderwijsEnum onderwijs) {
        this.opdrachten = opdrachten;
        this.acties = acties;
        this.onderwijs = onderwijs;
        this.voortgang = 0;
    }

    public SessiePad(List<Opdracht> opdrachten, SoortOnderwijsEnum onderwijs) {
        this.opdrachten = opdrachten;
        this.acties = null;
        this.onderwijs = onderwijs;
        this.voortgang = 0;
    }

    public List<Actie> getActies() {
        return acties;
    }

    public List<Opdracht> getOpdrachten() {
        return opdrachten;
    }
    
}
