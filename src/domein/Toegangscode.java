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
    @NamedQuery(name = "Toegangscode.findAll",
            query = "SELECT o FROM Toegangscode o")
})
public class Toegangscode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;

    public Toegangscode(String code) {
        this.code = code;
    }

    protected Toegangscode() {
    }

    public String getCode() {
        return code;
    }

}
