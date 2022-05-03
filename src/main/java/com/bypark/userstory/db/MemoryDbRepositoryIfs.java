package com.bypark.userstory.db;

import java.util.List;
import java.util.Optional;

public interface MemoryDbRepositoryIfs<T> {

    Optional<T> findById(Long index);
    T save(T entity);
    void deleteById(Long index);
    List<T> listAll();

}
