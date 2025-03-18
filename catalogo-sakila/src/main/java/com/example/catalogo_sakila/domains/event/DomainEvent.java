package com.example.catalogo_sakila.domains.event;

public record DomainEvent(String entity, int pk, String property, Object old, Object current) {
}
