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
public interface KlasDao extends GenericDao<Klas> {

    public Klas getByNaam(String naam) throws EntityNotFoundException;

}
