package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.entities.Category;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Date;
import java.util.List;

public interface CategoryRepository extends ListCrudRepository<Category, Integer> {
    List<Category> findAllByOrderByName();
    List<Category> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
}
