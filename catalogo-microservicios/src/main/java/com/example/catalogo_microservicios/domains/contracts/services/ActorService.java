package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.DomainService;
import com.example.catalogo_microservicios.domains.entities.Actor;

public interface ActorService extends DomainService<Actor, Integer> {
    void repartePremios();
}
