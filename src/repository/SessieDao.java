/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domein.Sessie;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author devri
 */
public interface SessieDao {

    public Sessie getByNaam(String naam) throws EntityNotFoundException;

}
