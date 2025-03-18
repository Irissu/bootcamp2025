package com.example.catalogo_sakila.domains.contracts.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.example.catalogo_sakila.domains.entities.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Integer> {
    List<Category> findAllByOrderByName();
    List<Category> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Timestamp fecha);
}
