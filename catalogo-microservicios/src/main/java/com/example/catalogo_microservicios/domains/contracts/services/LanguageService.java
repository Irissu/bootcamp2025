package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.DomainService;
import com.example.catalogo_microservicios.domains.entities.Language;

public interface LanguageService extends DomainService<Language, Integer> {
    void welcomeMessage();
}
