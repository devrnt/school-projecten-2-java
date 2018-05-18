
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import javax.persistence.OneToMany;
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
public class Sessie implements Serializable, ISessie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String naam;
    private String omschrijving;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Klas klas;
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Enumerated(EnumType.STRING)
    private SoortOnderwijsEnum soortOnderwijs;
    @Enumerated(EnumType.STRING)
    private FoutAntwoordActieEnum foutAntwoordActie;
    @Transient
    private SimpleStringProperty naamProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();
    private String sessieCode;
    private Boolean isGedaan;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private BreakOutBox box;
    //private List<Groep> groepen;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Groep> groepen;
    
    public Sessie() {
    }
    
    public Sessie(String naam, String omschrijving, Klas klas, BreakOutBox box,
            Date datum, SoortOnderwijsEnum soortOnderwijs, FoutAntwoordActieEnum foutAntwoordActie, Boolean isgedaan, List<Groep> groepen) {
        setNaam(naam);
        setOmschrijving(omschrijving);
        setKlas(klas);
        setBox(box);
        setDatum(datum);
        setSessieCode();
        setSoortOnderwijs(soortOnderwijs);
        setFoutAntwoordActie(foutAntwoordActie);
        setIsGedaan(isgedaan);
        setGroepen(groepen);
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
        cal.setTime(datum);
        
        int gekozenDag = cal.get(Calendar.DAY_OF_MONTH);
        Calendar huidig = Calendar.getInstance();
        
        int huidigeDag = huidig.get(Calendar.DAY_OF_MONTH);
        
        if (gekozenDag < huidigeDag) {
            throw new IllegalArgumentException("Datum moet in de toekomst liggen");
        } else {
            this.datum = datum;
        }


        /*  Date vandaag = cal.getTime();

    if (datum.before (vandaag) 
        ) {
            throw new IllegalArgumentException("Datum moet in de toekomst liggen");
    }

    
        else {
            this.datum = datum;
    }*/
    }
    
    public void setGroepen(List<Groep> groepen) {
        this.groepen = groepen;
    }
    
    public Boolean getIsGedaan() {
        return isGedaan;
    }
    
    public void setIsGedaan(Boolean isGedaan) {
        this.isGedaan = isGedaan;
    }
    
    public void setBox(BreakOutBox box) {
        this.box = box;
    }
    
    public String getBoxNaam() {
        return this.box.getNaam();
    }
    
    public BreakOutBox getBox() {
        return box;
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
    
    @Override
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

    public void genereerLegeGroepen(int aantal) {
        if (soortOnderwijs == SoortOnderwijsEnum.dagonderwijs) {
            genereerLegeGroepenDag(aantal);
        } else {
            genereerLegeGroepenAfstand(aantal);
        }
    }
    
    public void genereerRandomGroepen(int aantal) {
        genereerLegeGroepen(aantal);
        List<Leerling> leerlingen = this.klas.getLeerlingen();
        Collections.shuffle(leerlingen);
        for (int i = 0; i < leerlingen.size(); i++) {
            int x = i % aantal;
            this.groepen.get(x).voegLeerlingToe(leerlingen.get(i));
        }
    }
    
    private void genereerLegeGroepenDag(int aantalGroepen) {
        //int aantalGroep = (int) Math.ceil(this.klas.getAantalLeerlingen() / 4);
        int aantalOef = this.box.getOefeningen().size();
        Random rand = new Random();
        List<List<Integer>> randomOef = genereerDubbeleArray(aantalOef, aantalGroepen);
        
        int aantalAct = this.box.getActies().size();
        List<List<Integer>> randomAct = genereerDubbeleArray(aantalAct, aantalGroepen);
        
        List<SessiePad> paden = new ArrayList<>();
        List<Groep> groeplijst = new ArrayList<>();
        List<Actie> acties = box.getActies();
        List<Oefening> oefeningen = box.getOefeningen();
        
        for (int i = 0; i < aantalGroepen; i++) {
            List<Integer> oefRandom = randomOef.get(i);
            //List<Oefening> randomOefeningen = new ArrayList<>();
            List<Opdracht> randomOpdrachten = new ArrayList<>();
            for (Integer it : oefRandom) {
                //randomOefeningen.add(oefeningen.get(it));
                randomOpdrachten.add(new Opdracht(oefeningen.get(it), Integer.toString(rand.nextInt(1000))));
            }
            //  oefRandom.forEach((it) -> {
            //    
            //  });
            List<Integer> actRandom = randomAct.get(i);
            List<Actie> randomActies = new ArrayList<>();
            for (Integer it : actRandom) {
                randomActies.add(acties.get(it));
                
            }
            actRandom.forEach((it) -> {
            });
            SessiePad pad = new SessiePad(randomOpdrachten, randomActies, soortOnderwijs);
            groeplijst.add(new Groep("groep" + (i + 1), pad));
        }
        this.groepen = groeplijst;
    }
    
    private void genereerLegeGroepenAfstand(int aantalGroepen) {
        // int aantalGroep = (int) Math.ceil(this.klas.getAantalLeerlingen() / 4.0);
        //this.groepen = new ArrayList<>();
        int aantalOef = this.box.getOefeningen().size();
        Random rand = new Random();
        List<List<Integer>> randomOef = genereerDubbeleArray(aantalOef, aantalGroepen);
        List<Groep> groeplijst = new ArrayList<>();
        List<Oefening> oefeningen = box.getOefeningen();
        
        for (int i = 0; i < aantalGroepen; i++) {
            List<Integer> oefRandom = randomOef.get(i);
            List<Opdracht> randomOpdrachten = new ArrayList<>();
            oefRandom.forEach((it) -> {
                randomOpdrachten.add(new Opdracht(oefeningen.get(it), Integer.toString(rand.nextInt(1000))));
            });
            SessiePad pad = new SessiePad(randomOpdrachten, soortOnderwijs);
            groeplijst.add(new Groep("groep" + i, pad));
        }
        this.groepen = groeplijst;
    }
    
    private List<List<Integer>> genereerDubbeleArray(int breedte, int diepte) {
        Random rand = new Random();
        List<Integer> startposities = new ArrayList<>();
        for (int a = 0; a < breedte; a++) {
            startposities.add(a);
        }
        List<List<Integer>> dubbelleArray = new ArrayList<>();
        for (int b = 0; b < diepte; b++) {
            dubbelleArray.add(new ArrayList<>());
        }
        for (int i = 1; i <= breedte; i++) {
            int start = startposities.remove(rand.nextInt(startposities.size()));
            for (int j = 0; j < diepte; j++) {
                dubbelleArray.get(j).add((start + j) % breedte);
            }
        }
        return dubbelleArray;
    }
    
    public List<Groep> getGroepen() {
        return groepen;
    }
    
    public int getMinimumAantalGroepen() {
        return (int) Math.ceil(this.klas.getAantalLeerlingen() / 4.0);
    }
    
    public String getAantalLeerlingen() {
        return Integer.toString(this.klas.getAantalLeerlingen());
    }
    
}
