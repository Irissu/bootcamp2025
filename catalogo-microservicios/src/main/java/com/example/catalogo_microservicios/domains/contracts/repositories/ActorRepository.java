package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.catalogo_microservicios.domains.entities.Actor;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorShort;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>, ProjectionsAndSpecificationJpaRepository<Actor, Integer> {
    List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
    List<Actor> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
    List<Actor> findTop5ByFirstNameStartingWith(String prefijo, Sort orderBy);

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


    List<Actor> findByActorIdGreaterThan(int id);
    @Query(value = "SELECT a FROM Actor a WHERE a.actorId > ?1")
    List<Actor> findNovedadesJPQL(int id);
    @Query(value = "SELECT * FROM actor a WHERE a.actor_id > :id", nativeQuery = true)
    List<Actor> findNovedadesSQL(int id);

    List<ActorDTO> queryByActorIdGreaterThan(int id);
    List<ActorShort> getByActorIdGreaterThan(int id);

    <T> List<T> findByActorIdGreaterThan(int id, Class<T> type);
}