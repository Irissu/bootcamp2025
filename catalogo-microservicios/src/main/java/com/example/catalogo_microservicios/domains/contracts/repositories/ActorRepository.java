package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.entities.Actor;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
    List<Actor> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Timestamp fecha);

    default Actor insert(Actor item) throws DuplicatedKeyException {
        if(existsById(item.getActorId()))
            throw new DuplicatedKeyException();
        return save(item);
    }

    default Actor update(Actor item) throws NotFoundException {
        if(!existsById(item.getActorId()))
            throw new NotFoundException();
        return save(item);
    }
}