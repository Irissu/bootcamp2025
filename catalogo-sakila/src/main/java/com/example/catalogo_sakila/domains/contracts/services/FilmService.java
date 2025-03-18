package com.example.catalogo_sakila.domains.contracts.services;

import java.sql.Timestamp;
import java.util.List;

import com.example.catalogo_sakila.domains.core.contracts.services.ProjectionDomainService;
import com.example.catalogo_sakila.domains.core.contracts.services.SpecificationDomainService;
import com.example.catalogo_sakila.domains.entities.Film;

public interface FilmService extends ProjectionDomainService<Film, Integer>, SpecificationDomainService<Film, Integer> {
    List<Film> novedades(Timestamp fecha);
}
