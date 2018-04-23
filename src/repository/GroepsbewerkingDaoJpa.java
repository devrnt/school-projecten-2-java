package repository;

import domein.Groepsbewerking;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author sam
 */
public class GroepsbewerkingDaoJpa extends GenericDaoJpa<Groepsbewerking> implements GroepsbewerkingDao{

    public GroepsbewerkingDaoJpa() {
        super(Groepsbewerking.class);
    }

    @Override
    public List<Groepsbewerking> getByOmschrijvingen(List<String> omschrijvingen) throws EntityNotFoundException {
        return super.findAll()
                .stream()
                .filter(gb -> omschrijvingen.contains(gb.getOmschrijving()))
                .collect(Collectors.toList());
    }

    @Override
    public Groepsbewerking getByOmschrijving(String omschrijving) throws EntityNotFoundException {
        return super.findAll()
                .stream()
                .filter(gb -> gb.getOmschrijving().equals(omschrijving))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<String> getOmschrijvingen() throws EntityNotFoundException {
        return super.findAll()
                .stream()
                .map(Groepsbewerking::getOmschrijving)
                .collect(Collectors.toList());
    }
    
}
