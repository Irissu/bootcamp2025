package com.example.catalogo_microservicios.services;

import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface DomainService <E, K>{
    List<E> getAll();

    Optional<E> getOne(K id);

    E add (E item) throws DuplicateKeyException;
    E modify(E item) throws ChangeSetPersister.NotFoundException;
}
