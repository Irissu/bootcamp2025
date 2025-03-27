package com.example.catalogo_microservicios.domains.event;

public record DomainEvent(String entity, int pk, String property, Object old, Object current) {
}
