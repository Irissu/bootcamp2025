package com.example.catalogo_microservicios.domains.core.contracts.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ProjectionsAndSpecificationJpaRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E>, RepositoryWithProjections {
    <T> List<T> findAllBy(Class<T> tipo);
    <T> List<T> findAllBy(Sort orden, Class<T> tipo);
    <T> Page<T> findAllBy(Pageable page, Class<T> tipo);
}
