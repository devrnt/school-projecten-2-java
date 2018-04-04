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

    public static void startTransaction() {
        em.getTransaction().begin();
    }

    public static void commitTransaction() {
        em.getTransaction().commit();
    }

    public static void rollbackTransaction() {
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
        setChanged();
        notifyObservers();
        return em.merge(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
        setChanged();
        notifyObservers();
    }

    @Override
    public void insert(T object) {
        em.persist(object);
        setChanged();
        notifyObservers();
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
