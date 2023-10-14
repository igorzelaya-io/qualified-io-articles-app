package com.example.articles.repository;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrudRepository<T> {
    public List<T> findAll();
    public T findById(int id);
    public T save(T entity);
    public T update(int id, T entity);
    public void delete(int id);
}
