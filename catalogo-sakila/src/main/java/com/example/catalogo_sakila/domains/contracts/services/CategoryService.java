package com.example.catalogo_sakila.domains.contracts.services;

import java.sql.Timestamp;
import java.util.List;

import com.example.catalogo_sakila.domains.core.contracts.services.DomainService;
import com.example.catalogo_sakila.domains.entities.Category;

public interface CategoryService extends DomainService<Category, Integer> {
    List<Category> novedades(Timestamp fecha);
}
