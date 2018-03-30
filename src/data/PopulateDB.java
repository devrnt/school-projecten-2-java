package data;

import domein.Groepsbewerking;
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
        GenericDaoJpa.startTransaction();
        groepbwRepo.insert(new Groepsbewerking("bewerking1", 2, OperatorEnum.optellen));
        groepbwRepo.insert(new Groepsbewerking("bewerking2", 3, OperatorEnum.aftrekken));
        groepbwRepo.insert(new Groepsbewerking("bewerking3", 4, OperatorEnum.delen));
        groepbwRepo.insert(new Groepsbewerking("bewerking4", 1, OperatorEnum.vermeningvuldigen));
        GenericDaoJpa.commitTransaction();

    }
}
