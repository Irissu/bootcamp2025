package com.example.catalogo_microservicios.domains.entities.dtos;

import org.springframework.beans.factory.annotation.Value;

public interface ActorShort {
    @Value("#{target.ActorId}")
    int getId();
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getNombre();
}
