package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.catalogo_microservicios.domains.entities.Film;

import java.util.Date;
import java.util.List;

public interface FilmRepository extends ProjectionsAndSpecificationJpaRepository<Film, Integer> {
    List<Film> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
}
