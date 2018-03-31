package data;

import domein.Groepsbewerking;
import domein.Oefening;
import domein.OperatorEnum;
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
        oefeningRepo.insert(new Oefening("Hoofdrekenen 1", 100, "Gebruik tussenstappen", groepbwRepo.findAll()));
        GenericDaoJpa.commitTransaction();

    }
}
