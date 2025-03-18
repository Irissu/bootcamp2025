package com.example.catalogo_sakila.domains.contracts.repositories;

import java.sql.Timestamp;
import java.util.List;

import com.example.catalogo_sakila.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.catalogo_sakila.domains.entities.Film;

public interface FilmRepository extends ProjectionsAndSpecificationJpaRepository<Film, Integer> {
    List<Film> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Timestamp fecha);
}
