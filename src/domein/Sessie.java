
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author devri
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Sessie.findAll",
            query = "SELECT s FROM Sessie s")
})
public class Sessie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    private String omschrijving;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Klas klas;
    @Temporal(TemporalType.DATE)
    private Date datum;
    private String sessieCode;
    @Enumerated(EnumType.STRING)
    private SoortOnderwijsEnum soortOnderwijs;
    @Enumerated(EnumType.STRING)
    private FoutAntwoordActieEnum foutAntwoordActie;

    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();

    public Sessie() {
    }

    public Sessie(String naam, String omschrijving,
            Klas klas,
            Date datum, SoortOnderwijsEnum soortOnderwijs, FoutAntwoordActieEnum foutAntwoordActie) {
        setNaam(naam);
        setOmschrijving(omschrijving);
        setKlas(klas);
        setDatum(datum);
        setSessieCode();
        setSoortOnderwijs(soortOnderwijs);
        setFoutAntwoordActie(foutAntwoordActie);
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
        naamProperty.set(naam);
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        omschrijvingProperty.set(omschrijving);
    }

    public Klas getKlas() {
        return klas;
    }

    //klas wordt van type Klas
    public void setKlas(Klas klas) {
        //controle 
        this.klas = klas;
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

    public void setSessieCode() {
        int min = 1;
        int max = 10;
        int lengte = 10;
        String temp = "";
        for (int i = 0; i < lengte; i++) {
            int random = ThreadLocalRandom.current().nextInt(min, max + 1);
            temp += String.valueOf(random);
        }
        this.sessieCode = temp;
    }

    public String getSessieCode() {
        return sessieCode;
    }

    public SoortOnderwijsEnum getSoortOnderwijs() {
        return soortOnderwijs;
    }

    public void setSoortOnderwijs(SoortOnderwijsEnum soortOnderwijs) {
        if (Arrays.asList(SoortOnderwijsEnum.values()).contains(soortOnderwijs)) {
            this.soortOnderwijs = soortOnderwijs;
        } else {
            throw new IllegalArgumentException("Geen geldig onderwijstype");
        }
    }

    public SimpleStringProperty getNaamProperty() {
        return naamProperty;
    }

    public SimpleStringProperty getOmschrijvingProperty() {
        return omschrijvingProperty;
    }

    public void setFoutAntwoordActie(FoutAntwoordActieEnum foutAntwoordActie) {

        this.foutAntwoordActie = foutAntwoordActie;
    }

    public FoutAntwoordActieEnum getFoutAntwoordActie() {
        return foutAntwoordActie;
    }
    // </editor-fold>

}
