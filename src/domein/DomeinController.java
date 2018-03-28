package domein;

import java.util.List;
import repository.GenericDao;
import repository.GenericDaoJpa;

/**
 *
 * @author sam
 */
public class DomeinController {
    private GenericDao<Oefening> oefeningRepo;

    public DomeinController() {
        setOefeningRepo(new GenericDaoJpa<>(Oefening.class));
    }

    public void setOefeningRepo(GenericDao<Oefening> oefeningRepo) {
        this.oefeningRepo = oefeningRepo;
    }
    
    public void createOefening(String opgave, int antwoord, String feedback, List<Groepsbewerking> groepsbewerkingen){
        GenericDaoJpa.startTransaction();
        oefeningRepo.insert(new Oefening(opgave, antwoord, feedback, groepsbewerkingen));
        GenericDaoJpa.commitTransaction();
    }
    
    public void close(){
        GenericDaoJpa.closePersistency();
    }
    
}
