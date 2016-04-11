package edu.gatech.projectThree.datamodel.dao;

/**
 * Created by dawu on 3/18/16.
 */
public interface DataAccessObject<T> {

    public void insert(T obj);

    public T update(T obj);

    public void delete(T obj);

    public T get(long index);

    public T[] getAll();
}
