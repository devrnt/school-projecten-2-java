package data;

import domein.Groepsbewerking;
import domein.Oefening;
import domein.OperatorEnum;
import java.util.ArrayList;
import java.util.Arrays;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author sam
 */
public class PopulateDB {

    public void run() {
        GenericDao<Groepsbewerking> groepbwRepo = new GenericDaoJpa<>(Groepsbewerking.class);
        GenericDao<Oefening> oefeningRepo = new GenericDaoJpa<Oefening>(Oefening.class);
        GenericDaoJpa.startTransaction();
        groepbwRepo.insert(new Groepsbewerking("Tel 2 op", 2, OperatorEnum.optellen));
        groepbwRepo.insert(new Groepsbewerking("Trek 3 af", 3, OperatorEnum.aftrekken));
        groepbwRepo.insert(new Groepsbewerking("Deel door 4", 4, OperatorEnum.delen));
        groepbwRepo.insert(new Groepsbewerking("Vermeningvuldig met 5", 5, OperatorEnum.vermeningvuldigen));
        GenericDaoJpa.commitTransaction();
        GenericDaoJpa.startTransaction();
        oefeningRepo.insert(new Oefening("2 x 200 = ", 400, "Gebruik tussenstappen", groepbwRepo.findAll()));
        oefeningRepo.insert(new Oefening("5 + 20 = ", 25, "Gebruik tussenstappen",  new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(1), groepbwRepo.get(2)}))));
        oefeningRepo.insert(new Oefening("190 - 50 = ", 140, "Gebruik tussenstappen", new ArrayList<>(Arrays.asList(new Groepsbewerking[]{groepbwRepo.get(3)}))));

        GenericDaoJpa.commitTransaction();

    }
}
