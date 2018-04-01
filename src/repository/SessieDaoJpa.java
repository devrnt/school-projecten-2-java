/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Groepsbewerking;
import domein.Sessie;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author devri
 */
public class SessieDaoJpa extends GenericDaoJpa<Sessie> implements SessieDao {

    public SessieDaoJpa(Class<Sessie> type) {
        super(type);
    }

    @Override
    public Sessie getByNaam(String naam) throws EntityNotFoundException {
        return super.findAll()
                .stream()
                .filter(s -> s.getNaam().equals(naam))
                .findFirst()
                .orElse(null);
    }

}
