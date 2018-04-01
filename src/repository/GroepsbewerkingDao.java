package repository;

import domein.Groepsbewerking;
import java.util.List;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author sam
 */
public interface GroepsbewerkingDao extends GenericDao<Groepsbewerking> {
    public List<Groepsbewerking> getByOmschrijvingen(List<String> omschrijvingen) throws EntityNotFoundException;
    public Groepsbewerking getByOmschrijving(String omschrijving) throws EntityNotFoundException;
    public List<String> getOmschrijvingen() throws EntityNotFoundException;
}
