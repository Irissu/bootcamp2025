package com.example.catalogo_microservicios.repositories;

import com.example.catalogo_microservicios.domains.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
    List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
}
