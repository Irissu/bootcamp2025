package com.example.catalogo_microservicios.domains.services;

import com.example.catalogo_microservicios.domains.contracts.repositories.LanguageRepository;
import com.example.catalogo_microservicios.domains.contracts.services.LanguageService;
import com.example.catalogo_microservicios.domains.entities.Language;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public class LanguageServiceImpl implements LanguageService {
    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public void welcomeMessage() {

    }

    @Override
    public List<Language> getAll() {
        return List.of();
    }

    @Override
    public Optional<Language> getOne(Integer id) {
        return Optional.empty();
    }

    @Override
    public Language add(Language item) throws DuplicatedKeyException, InvalidDataException {
        return null;
    }

    @Override
    public Language modify(Language item) throws NotFoundException, InvalidDataException {
        return null;
    }

    @Override
    public void delete(Language item) throws InvalidDataException {

    }

    @Override
    public void deleteById(Integer id) {

    }
}
