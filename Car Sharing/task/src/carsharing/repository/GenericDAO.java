package carsharing.repository;

import carsharing.entity.Company;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    T get(int id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);
}