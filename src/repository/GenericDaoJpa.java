/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import java.util.Observable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author sam
 */
public class GenericDaoJpa<T> extends Observable implements GenericDao<T>{

    private static final String PU_name = "java-g16PU";
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_name);
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> type;

    public GenericDaoJpa(Class<T> type) {
        this.type = type;
    }

    public static void closePersistency() {
        em.close();
        emf.close();
    }

    private void startTransaction() {
        em.getTransaction().begin();
    }

    private void commitTransaction() {
        em.getTransaction().commit();
        setChanged();
        notifyObservers();
    }

    private void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public List<T> findAll() {
        return em.createNamedQuery(type.getSimpleName() + ".findAll", type).getResultList();
    }

    @Override
    public T get(int id) {
        T entity = em.find(type, id);
        return entity;
    }

    @Override
    public T get(String naam) {
        T entity = em.find(type, naam);
        return entity;
    }

    @Override
    public T update(T object) {
        startTransaction();
        setChanged();
        notifyObservers();
        T changed = null;
        try {
            changed = em.merge(object);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        }
        return changed;
    }

    @Override
    public void delete(T object) {
        startTransaction();
        try {
            em.remove(em.merge(object));
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        }
        commitTransaction();
    }

    @Override
    public void insert(T object) {
        startTransaction();
        try {
            em.persist(object);
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        }
        commitTransaction();
    }

    @Override
    public boolean exists(int id) {
        T entity = em.find(type, id);
        return entity != null;
    }

    @Override
    public boolean exists(String name) {
        T entity = em.find(type, name);
        return entity != null;
    }
}
