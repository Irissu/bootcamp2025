package com.example.catalogo_sakila.domains.contracts.repositories;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.example.catalogo_sakila.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.catalogo_sakila.domains.entities.Actor;
import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.domains.entities.models.ActorShort;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

public interface ActorRepository extends ProjectionsAndSpecificationJpaRepository<Actor, Integer> {
    List<Actor> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
    List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
    List<Actor> findTop5ByFirstNameStartingWith(String prefijo, Sort orderBy);

    default Actor insert(Actor item) throws DuplicateKeyException {
        if(existsById(item.getActorId()))
            throw new DuplicateKeyException();
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
