/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Klas;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author devri
 */
public class KlasDaoJpa extends GenericDaoJpa<Klas> implements KlasDao {

    public KlasDaoJpa(Class<Klas> type) {
        super(type);
    }

    @Override
    public Klas getByNaam(String naam) throws EntityNotFoundException {
        return super.findAll()
                .stream()
                .filter(s -> s.getNaam().equals(naam))
                .findFirst()
                .orElse(null);
    }

}
