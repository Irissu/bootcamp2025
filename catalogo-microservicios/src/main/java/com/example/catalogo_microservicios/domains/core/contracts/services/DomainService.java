package com.example.catalogo_microservicios.domains.core.contracts.services;

import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

public interface DomainService<E, K> {
    List<E> getAll();

    Optional<E> getOne(K id);

    E add(E item) throws DuplicatedKeyException, InvalidDataException;

    E modify(E item) throws NotFoundException, InvalidDataException;

    void delete(E item) throws InvalidDataException;
    void deleteById(K id);
}
