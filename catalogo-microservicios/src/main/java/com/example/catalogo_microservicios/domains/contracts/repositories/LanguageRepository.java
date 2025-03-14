package com.example.catalogo_microservicios.domains.contracts.repositories;

import com.example.catalogo_microservicios.domains.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LanguageRepository extends JpaRepository<Language, Integer>, JpaSpecificationExecutor<Language> {
}
