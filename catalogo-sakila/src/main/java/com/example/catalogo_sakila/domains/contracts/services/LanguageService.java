package com.example.catalogo_sakila.domains.contracts.services;

import java.sql.Timestamp;
import java.util.List;

import com.example.catalogo_sakila.domains.core.contracts.services.DomainService;
import com.example.catalogo_sakila.domains.entities.Language;

public interface LanguageService extends DomainService<Language, Integer> {
    List<Language> novedades(Timestamp fecha);
}
