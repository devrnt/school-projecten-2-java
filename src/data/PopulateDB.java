package data;

import domein.Actie;
import domein.BreakOutBox;
import domein.FoutAntwoordActieEnum;
import domein.Groep;
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
        GenericDao<Groep> groepRepo = new GenericDaoJpa<>(Groep.class);

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

        List<Oefening> oefeningen = new ArrayList<>(Arrays.asList(new Oefening[]{
            new Oefening(
            pad + "rekensommenOptellen.pdf",
            400,
            pad + "feedbackRekenen.pdf",
            "Hoofdrekenen",
            new ArrayList<>(Arrays.asList(new String[]{"D203", "C105"})),
            groepbwRepo.findAll()),
            new Oefening(
            pad + "rekensommenAftrekken.pdf",
            201,
            pad + "feedbackRekenen.pdf",
            "Hoofdrekenen",
            new ArrayList<>(Arrays.asList(new String[]{"D203", "C105", "A106"})),
            groepbwRepo.findAll()),
            new Oefening(
            pad + "rekensommenDelen.pdf",
            40,
            pad + "feedbackRekenen.pdf",
            "Hoofdrekenen",
            new ArrayList<>(Arrays.asList(new String[]{"D203", "C105", "C107"})),
            groepbwRepo.findAll()),
            new Oefening(
            pad + "rekensommenVermeningvuldigen.pdf",
            101,
            pad + "feedbackRekenen.pdf",
            "Hoofdrekenen",
            new ArrayList<>(Arrays.asList(new String[]{"D203", "C105", "C108"})),
            groepbwRepo.findAll()),
            new Oefening(
            pad + "pythagoras.pdf",
            29,
            pad + "feedbackMeetkunde.pdf",
            "Meetkunde",
            new ArrayList<>(Arrays.asList(new String[]{"D202", "C106"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)}))),
            new Oefening(
            pad + "omtrekOppervlakte.pdf",
            25,
            pad + "feedbackMeetkunde.pdf",
            "Meetkunde",
            new ArrayList<>(Arrays.asList(new String[]{"D205", "C156"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(4), groepbwRepo.get(2)}))),
            new Oefening(
            pad + "cirkels.pdf",
            130,
            pad + "feedbackMeetkunde.pdf",
            "Meetkunde",
            new ArrayList<>(Arrays.asList(new String[]{"B302", "C136"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(3)}))),
            new Oefening(
            pad + "staartdeling.pdf",
            140,
            pad + "feedbackCijferen.pdf",
            "Cijferen",
            new ArrayList<>(Arrays.asList(new String[]{"D102", "A402"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(3)}))),
            new Oefening(
            pad + "cijferenKommagetallen.pdf",
            10,
            pad + "feedbackCijferen.pdf",
            "Cijferen",
            new ArrayList<>(Arrays.asList(new String[]{"D152", "C502"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(4)}))),
            /* ==== NIET NAAR LINKEN IN BOB, MOET VERWIJDERD KUNNEN WORDEN ===== */
            new Oefening(
            pad + "problemen.pdf",
            140,
            pad + "feedbackVraagstukken.pdf",
            "Vraagstukken",
            new ArrayList<>(Arrays.asList(new String[]{"A256", "F304"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(3)}))),
            new Oefening(
            pad + "geldVraagstukken.pdf",
            140,
            pad + "feedbackVraagstukken.pdf",
            "Vraagstukken",
            new ArrayList<>(Arrays.asList(new String[]{"A256", "D324"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(4), groepbwRepo.get(2)}))),
            new Oefening(
            pad + "inhoudVraagstukken.pdf",
            140,
            pad + "feedbackVraagstukken.pdf",
            "Vraagstukken",
            new ArrayList<>(Arrays.asList(new String[]{"B706", "C204"})),
            new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)}))),}));

        oefeningen.forEach(o -> oefeningRepo.insert(o));

        /* === Data voor boxbeheer === */ /* === #Oefeningen moet 1 meer zijn dan het #Acties === */
        List<Oefening> oefeningen1 = new ArrayList<>(Arrays.asList(new Oefening[]{oefeningen.get(0), oefeningen.get(1)}));
        List<Oefening> oefeningen2 = new ArrayList<>(Arrays.asList(new Oefening[]{oefeningen.get(0), oefeningen.get(2)}));
        List<Oefening> oefeningen3 = new ArrayList<>(Arrays.asList(new Oefening[]{oefeningen.get(0), oefeningen.get(1), oefeningen.get(3)}));
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
        Sessie sessie1 = new Sessie("sessie_" + klas1.getNaam(), "Sessie voor klas " + klas1.getNaam() + " op " + sdf.format(c.getTime()), klas1, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, Boolean.FALSE, null);
        Sessie sessie2 = new Sessie("sessie_" + klas1.getNaam() + "_2", "Sessie voor klas " + klas1.getNaam() + " op " + sdf.format(c.getTime()), klas1, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, Boolean.FALSE, null);
        Sessie sessie3 = new Sessie("sessie_" + klas2.getNaam(), "Sessie voor klas " + klas2.getNaam() + " op " + sdf.format(c.getTime()), klas2, box1, c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, Boolean.FALSE, null);
        Sessie sessie4 = new Sessie("sessie_" + klas2.getNaam() + "_2", "Sessie voor klas " + klas2.getNaam() + " op " + sdf.format(c.getTime()), klas1, box2, c.getTime(), SoortOnderwijsEnum.afstandsonderwijs, FoutAntwoordActieEnum.feedback, Boolean.FALSE, null);
        sessieRepo.insert(sessie1);
        sessieRepo.insert(sessie2);
        sessieRepo.insert(sessie3);
        sessieRepo.insert(sessie4);
        List<Groep> groepen = new ArrayList<>(Arrays.asList(new Groep[]{
            new Groep("De Kampioenen", new ArrayList<>(Arrays.asList(llnKlas1))),
            new Groep("De Pottestampers", new ArrayList<>(Arrays.asList(llnKlas2)))
        }));

        groepen.forEach(g -> groepRepo.insert(g));
    }
}
