package data;

import domein.Actie;
import domein.BreakOutBox;
import domein.FoutAntwoordActieEnum;
import domein.Groepsbewerking;
import domein.Klas;
import domein.Leerling;
import domein.Oefening;
import domein.OperatorEnum;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import java.text.SimpleDateFormat;
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
        GenericDao<Actie> actieRepo = new GenericDaoJpa<>(Actie.class);
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

        String pad = "/Users/sam/NetBeansProjects/java-g16/src/data/";

        Oefening oefening1 = new Oefening(
                pad + "rekensommen.pdf",
                400,
                pad + "feedbackRekenen.pdf",
                "Hoofdrekenen",
                new ArrayList<String>(Arrays.asList(new String[]{"D203", "C105"})),
                groepbwRepo.findAll());
        Oefening oefening11 = new Oefening(
                pad + "rekensommen.pdf",
                400,
                pad + "feedbackRekenen.pdf",
                "Hoofdrekenen",
                new ArrayList<String>(Arrays.asList(new String[]{"D203", "C105", "c106"})),
                groepbwRepo.findAll());
        Oefening oefening12 = new Oefening(
                pad + "rekensommen.pdf",
                400,
                pad + "feedbackRekenen.pdf",
                "Hoofdrekenen",
                new ArrayList<String>(Arrays.asList(new String[]{"D203", "C105", "C107"})),
                groepbwRepo.findAll());
        Oefening oefening13 = new Oefening(
                pad + "rekensommen.pdf",
                400,
                pad + "feedbackRekenen.pdf",
                "Hoofdrekenen",
                new ArrayList<String>(Arrays.asList(new String[]{"D203", "C105", "C108"})),
                groepbwRepo.findAll());
        Oefening oefening2 = new Oefening(
                pad + "pythagoras.pdf",
                25,
                pad + "feedbackMeetkunde.pdf",
                "Meetkunde",
                new ArrayList<String>(Arrays.asList(new String[]{"D202", "C106"})),
                new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)})));
        Oefening oefening3 = new Oefening(
                pad + "staartdeling.pdf",
                140,
                pad + "feedbackCijferen.pdf",
                "Cijferen",
                new ArrayList<>(Arrays.asList(new String[]{"D102", "A402"})),
                new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(3)})));
        /* ==== NIET NAAR LINKEN IN BOB, MOET VERWIJDERD KUNNEN WORDEN ===== */
        Oefening oefening4 = new Oefening(
                pad + "problemen.pdf",
                140,
                pad + "feedbackVraagstukken.pdf",
                "Vraagstukken",
                new ArrayList<>(Arrays.asList(new String[]{"A256", "F304"})),
                new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(3)})));
        oefeningRepo.insert(oefening1);
        oefeningRepo.insert(oefening2);
        oefeningRepo.insert(oefening3);
        oefeningRepo.insert(oefening4);
        oefeningRepo.insert(oefening11);
        oefeningRepo.insert(oefening12);
        oefeningRepo.insert(oefening13);

        /* === Data voor boxbeheer === */
 /* === #Oefeningen moet 1 meer zijn dan het #Acties === */
        List<Oefening> oefeningen1 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1, oefening2}));
        List<Oefening> oefeningen2 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1, oefening3}));
        List<Oefening> oefeningen3 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1, oefening2, oefening3}));
        Actie actie1 = new Actie("actie1");
        Actie actie2 = new Actie("actie2");
        Actie actie3 = new Actie("actie3");
        actieRepo.insert(actie1);
        actieRepo.insert(actie2);
        actieRepo.insert(actie3);

        List<Actie> acties1 = new ArrayList<>(Arrays.asList(new Actie[]{actie1}));
        List<Actie> acties2 = new ArrayList<>(Arrays.asList(new Actie[]{actie2}));
        List<Actie> acties3 = new ArrayList<>(Arrays.asList(new Actie[]{actie1, actie2}));
        BreakOutBox box1 = new BreakOutBox("Box vermenigvuldigen en delen", "Vermenigvuldigen en delen", SoortOnderwijsEnum.dagonderwijs, oefeningen1, acties1);
        BreakOutBox box2 = new BreakOutBox("Box optellen en aftrekken", "Optellen en aftrekken", SoortOnderwijsEnum.dagonderwijs, oefeningen2, acties2);
        BreakOutBox box3 = new BreakOutBox("Box alle opdrachten", "Alle wiskunde met vragen over optellen en aftrekken, vermenigvuldigen en delen", SoortOnderwijsEnum.dagonderwijs, oefeningen3, acties3);
        boxRepo.insert(box1);
        boxRepo.insert(box2);
        boxRepo.insert(box3);


        /*=== Data voor sessiebeheer === */
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);

        Klas klas1 = new Klas("2A1");
        Klas klas2 = new Klas("2B1");

        Leerling[] llnKlas1 = {
            new Leerling("Jan", "De Timmerman"),
            new Leerling("Pieter", "Vansteenkiste"),
            new Leerling("Joris", "De Praeter"),
            new Leerling("Corneel", "De Grote")
        };

        for (Leerling lln : llnKlas1) {
            klas1.voegLeerlingToe(lln);
        }

        Leerling[] llnKlas2 = {
            new Leerling("Anja", "De Neef"),
            new Leerling("Pepijn", "Waershot"),
            new Leerling("Robin", "De Kleine"),
            new Leerling("Petra", "De Vos")};

        for (Leerling lln : llnKlas2) {
            klas2.voegLeerlingToe(lln);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        sessieRepo.insert(new Sessie(
                "sessie_" + klas1.getNaam(), "Sessie voor klas " + klas1.getNaam() + " op " + sdf.format(c.getTime()),
                klas1, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false));
        sessieRepo.insert(new Sessie(
                "sessie_" + klas2.getNaam(), "Sessie voor klas " + klas1.getNaam() + " op 28/12",
                klas1, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false));
        sessieRepo.insert(new Sessie(
                "sessie_" + klas2.getNaam(), "Sessie voor klas " + klas1.getNaam() + " op " + sdf.format(c.getTime()),
                klas1, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false));
        sessieRepo.insert(new Sessie(
                "sessie_" + 4, "Sessie voor klas " + klas1.getNaam() + " op " + sdf.format(c.getTime()),
                klas2, box2, c.getTime(), SoortOnderwijsEnum.afstandsonderwijs, FoutAntwoordActieEnum.feedback, false));

    }
}
