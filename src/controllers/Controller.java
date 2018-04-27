package controllers;

/**
 *
 * @author sam
 */
public interface Controller<T> {
    
    void applyFilter(String filterName);
    
    T getById(int id);
    
}
