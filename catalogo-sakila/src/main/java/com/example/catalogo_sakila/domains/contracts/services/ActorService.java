package com.example.catalogo_sakila.domains.contracts.services;

import com.example.catalogo_sakila.domains.core.contracts.services.ProjectionDomainService;
import com.example.catalogo_sakila.domains.entities.Actor;

import java.sql.Timestamp;
import java.util.List;

public interface ActorService extends ProjectionDomainService<Actor, Integer> {
    void repartePremios();
    List<Actor> novedades(Timestamp fecha);
}
