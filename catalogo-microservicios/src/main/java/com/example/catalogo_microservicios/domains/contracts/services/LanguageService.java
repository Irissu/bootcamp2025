package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.DomainService;
import com.example.catalogo_microservicios.domains.entities.Language;

import java.util.Date;
import java.util.List;

public interface LanguageService extends DomainService<Language, Integer> {
    void welcomeMessage();
    List<Language> novedades(Date fecha);
}
