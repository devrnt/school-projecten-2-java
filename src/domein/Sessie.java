
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author devri
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "domein.Sessie.findAll",
            query = "SELECT s FROM Sessie s")
})
public class Sessie implements Serializable {

    @Id
    private String naam;
    private String omschrijving;
    private String klas;
    private double lesuur;
    @Temporal(TemporalType.DATE)
    private Date datum;

    public Sessie() { }

    public Sessie(String naam, String omschrijving, String klas, double lesuur, Date datum) {
        setNaam(naam);
        setOmschrijving(omschrijving);
        setKlas(klas);
        setLesuur(lesuur);
        setDatum(datum);
    }

    // <editor-fold desc="Getters and Setters" >
    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getKlas() {
        return klas;
    }

    //klas wordt van type Klas
    public void setKlas(String klas) {
        //controle 
        this.klas = klas;
    }

    public double getLesuur() {
        return lesuur;
    }

    public void setLesuur(double lesuur) {
        // controle 
        if (lesuur <= 0 || lesuur > 10) {
            throw new IllegalArgumentException("Geef een geldig lesuur in, tussen 1 en 10");
        }
        this.lesuur = lesuur;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        // controle: datum moet na vandaag zijn
        Calendar cal = Calendar.getInstance();

        Date vandaag = cal.getTime();
        if (datum.before(vandaag)) {
            throw new IllegalArgumentException("Datum moet in de toekomst liggen");
        } else {
            this.datum = datum;
        }
    }
    // </editor-fold>

}
