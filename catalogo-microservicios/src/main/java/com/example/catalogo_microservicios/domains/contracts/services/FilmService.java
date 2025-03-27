package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.ProjectionDomainService;
import com.example.catalogo_microservicios.domains.core.contracts.services.SpecificationDomainService;
import com.example.catalogo_microservicios.domains.entities.Film;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FilmService extends ProjectionDomainService<Film, Integer>, SpecificationDomainService<Film, Integer> {
    List<Film> novedades(Date fecha);
}
