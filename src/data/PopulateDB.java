package data;

import domein.Actie;
import domein.BreakOutBox;
import domein.Groepsbewerking;
import domein.Oefening;
import domein.OperatorEnum;
import domein.Toegangscode;
import java.util.ArrayList;
import java.util.Arrays;
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

        Groepsbewerking groepsbewerking1 = new Groepsbewerking("Tel 2 op", 2, OperatorEnum.optellen);
        Groepsbewerking groepsbewerking2 = new Groepsbewerking("Trek 3 af", 3, OperatorEnum.aftrekken);
        Groepsbewerking groepsbewerking3 = new Groepsbewerking("Deel door 4", 4, OperatorEnum.delen);
        Groepsbewerking groepsbewerking4 = new Groepsbewerking("Vermeningvuldig met 5", 5, OperatorEnum.vermeningvuldigen);
        GenericDaoJpa.startTransaction();
        groepbwRepo.insert(groepsbewerking1);
        groepbwRepo.insert(groepsbewerking2);
        groepbwRepo.insert(groepsbewerking3);
        groepbwRepo.insert(groepsbewerking4);
        GenericDaoJpa.commitTransaction();

        Oefening oefening1 = new Oefening("2 x 200 = ", 400, "Gebruik tussenstappen", groepbwRepo.findAll());
        Oefening oefening2 = new Oefening("5 + 20 = ", 25, "Gebruik tussenstappen", new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)})));
        Oefening oefening3 = new Oefening("190 - 50 = ", 140, "Gebruik tussenstappen", new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(3)})));
        GenericDaoJpa.startTransaction();
        oefeningRepo.insert(oefening1);
        oefeningRepo.insert(oefening2);
        oefeningRepo.insert(oefening3);
        GenericDaoJpa.commitTransaction();

        List<Oefening> oefeningen1 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        List<Oefening> oefeningen2 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        List<Oefening> oefeningen3 = new ArrayList<>(Arrays.asList(new Oefening[]{oefening1}));
        Actie actie1 = new Actie("actie1");
        Actie actie2 = new Actie("actie2");
        Actie actie3 = new Actie("actie1");
        List<Actie> acties1 = new ArrayList<>(Arrays.asList(new Actie[]{actie1, actie2}));
        List<Actie> acties2 = new ArrayList<>(Arrays.asList(new Actie[]{actie2, actie3}));
        List<Actie> acties3 = new ArrayList<>(Arrays.asList(new Actie[]{actie1, actie2, actie3}));
        Toegangscode toegc1 = new Toegangscode("code1");
        Toegangscode toegc2 = new Toegangscode("code2");
        Toegangscode toegc3 = new Toegangscode("code3");
        List<Toegangscode> toegangscodes1 = new ArrayList<>(Arrays.asList(new Toegangscode[]{toegc1, toegc2}));
        List<Toegangscode> toegangscodes2 = new ArrayList<>(Arrays.asList(new Toegangscode[]{toegc1, toegc3}));
        List<Toegangscode> toegangscodes3 = new ArrayList<>(Arrays.asList(new Toegangscode[]{toegc1}));
        BreakOutBox box1 = new BreakOutBox("Box1", "Omsch1", oefeningen1, acties1, toegangscodes1);
        BreakOutBox box2 = new BreakOutBox("Box2", "Omsch2", oefeningen2, acties2, toegangscodes2);
        BreakOutBox box3 = new BreakOutBox("Box3", "Omsch3", oefeningen3, acties3, toegangscodes3);
        GenericDaoJpa.startTransaction();
        boxRepo.insert(box1);
        boxRepo.insert(box2);
        boxRepo.insert(box3);
        GenericDaoJpa.commitTransaction();

    }
}
