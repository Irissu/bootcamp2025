package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.DomainService;
import com.example.catalogo_microservicios.domains.core.contracts.services.ProjectionDomainService;
import com.example.catalogo_microservicios.domains.entities.Actor;

import java.util.Date;
import java.util.List;

public interface ActorService extends ProjectionDomainService<Actor, Integer>  {
    void repartePremios();
    List<Actor> novedades(Date fecha);
}
