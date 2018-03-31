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
        groepbwRepo.insert(new Groepsbewerking("bewerking1", 2, OperatorEnum.optellen));
        groepbwRepo.insert(new Groepsbewerking("bewerking2", 3, OperatorEnum.aftrekken));
        groepbwRepo.insert(new Groepsbewerking("bewerking3", 4, OperatorEnum.delen));
        groepbwRepo.insert(new Groepsbewerking("bewerking4", 1, OperatorEnum.vermeningvuldigen));
        oefeningRepo.insert(new Oefening("opgave1", 2, "feedback", groepbwRepo.findAll()));
        GenericDaoJpa.commitTransaction();

    }
}
