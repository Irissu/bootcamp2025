package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer>, JpaSpecificationExecutor<Language> {
    List<Language> findByNameStartingWith(String letra);
    List<Language> findTop3By();
    List<Language> findAllByOrderByName();
    List<Language> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
}
