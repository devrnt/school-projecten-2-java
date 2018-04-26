package data;

import domein.Actie;
import domein.BreakOutBox;
import domein.FoutAntwoordActieEnum;
import domein.Groepsbewerking;
import domein.Klas;
import domein.Oefening;
import domein.OperatorEnum;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
//import domein.Toegangscode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author sam
 */
public class PopulateDB {

    public void run() {
        GenericDao<Groepsbewerking> groepbwRepo = new GenericDaoJpa<>(Groepsbewerking.class);
        GenericDao<Oefening> oefeningRepo = new GenericDaoJpa<>(Oefening.class);
        GenericDao<BreakOutBox> boxRepo = new GenericDaoJpa<>(BreakOutBox.class);
        GenericDao<Sessie> sessieRepo = new GenericDaoJpa<>(Sessie.class);

        /* === Data voor oefeningenbeheer === */
        Groepsbewerking groepsbewerking1 = new Groepsbewerking("Tel 2 op", 2, OperatorEnum.optellen);
        Groepsbewerking groepsbewerking2 = new Groepsbewerking("Trek 3 af", 3, OperatorEnum.aftrekken);
        Groepsbewerking groepsbewerking3 = new Groepsbewerking("Deel door 4", 4, OperatorEnum.delen);
        Groepsbewerking groepsbewerking4 = new Groepsbewerking("Vermeningvuldig met 5", 5, OperatorEnum.vermeningvuldigen);
        groepbwRepo.insert(groepsbewerking1);
        groepbwRepo.insert(groepsbewerking2);
        groepbwRepo.insert(groepsbewerking3);
        groepbwRepo.insert(groepsbewerking4);

        Oefening oefening1 = new Oefening("~/Documents/Rekenen/hoofdrekenen1.pdf", 400, "~/Documents/Feedback/feedbackRekenen.pdf", groepbwRepo.findAll());
        Oefening oefening2 = new Oefening("~/Documents/Meetkunde/meetkunde2.pdf", 25, "~/Documents/Feedback/feedbackMeetkunde.pdf", new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)})));
        Oefening oefening3 = new Oefening("~/Documents/Cijferen/cijferen2.pdf", 140, "~/Documents/Feedback/feedbackCijferen.pdf", new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(3)})));
        oefeningRepo.insert(oefening1);
        oefeningRepo.insert(oefening2);
        oefeningRepo.insert(oefening3);

        /* === Data voor boxbeheer === */
        List<Oefening> oefeningen1 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        List<Oefening> oefeningen2 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        List<Oefening> oefeningen3 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        Actie actie1 = new Actie("actie1");
        Actie actie2 = new Actie("actie2");
        Actie actie3 = new Actie("actie1");
        List<Actie> acties1 = new ArrayList<>(Arrays.asList(new Actie[]{actie1, actie2}));
        List<Actie> acties2 = new ArrayList<>(Arrays.asList(new Actie[]{actie2, actie3}));
        List<Actie> acties3 = new ArrayList<>(Arrays.asList(new Actie[]{actie1, actie2, actie3}));
        BreakOutBox box1 = new BreakOutBox("Box1", "Omsch1", oefeningen1, acties1);
        BreakOutBox box2 = new BreakOutBox("Box2", "Omsch2", oefeningen2, acties2);
        BreakOutBox box3 = new BreakOutBox("Box3", "Omsch3", oefeningen3, acties3);
        boxRepo.insert(box1);
        boxRepo.insert(box2);
        boxRepo.insert(box3);

        /*=== Data voor sessiebeheer === */
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);

        Klas klas1 = new Klas("2A1");
        Klas klas2 = new Klas("2B1");

        String[] llnKlas1 = {"Jan", "Piet", "Joris", "Corneel"};
        for (String lln : llnKlas1) {
            klas1.voegLeerlingToe(lln);
        }

        String[] llnKlas2 = {"Niels", "Pepijn", "Robin", "Petra"};
        for (String lln : llnKlas2) {
            klas2.voegLeerlingToe(lln);
        }

        for (int i = 0; i < 3; i++) {
            sessieRepo.insert(new Sessie(
                    "sessie " + i, "Sessie " + i + " omschrijving hier...",
                    klas1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback));
        }
        sessieRepo.insert(new Sessie(
                "sessie " + 4, "Sessie " + 4 + " omschrijving hier...",
                klas2, c.getTime(), SoortOnderwijsEnum.afstandsonderwijs, FoutAntwoordActieEnum.feedback));

    }
}
