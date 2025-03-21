package com.example.catalogo_sakila.domains.services;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.example.catalogo_sakila.domains.contracts.repositories.LanguageRepository;
import com.example.catalogo_sakila.domains.contracts.services.LanguageService;
import com.example.catalogo_sakila.domains.entities.Language;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.InvalidDataException;
import com.example.catalogo_sakila.exceptions.NotFoundException;

@Service
public class LanguageServiceImpl implements LanguageService {
    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<Language> getAll() {
        return languageRepository.findAll();
    }

    @Override
    public Optional<Language> getOne(Integer id) {
        return languageRepository.findById(id);
    }

    @Override
    public Language add(Language item) throws DuplicateKeyException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        if(item.getLanguageId() != 0 && languageRepository.existsById(item.getLanguageId()))
            throw new DuplicateKeyException("Ya existe");
        return languageRepository.save(item);
    }

    @Override
    public Language modify(Language item) throws NotFoundException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        if(!languageRepository.existsById(item.getLanguageId()))
            throw new NotFoundException();
        return languageRepository.save(item);
    }

    @Override
    public void delete(Language item) throws InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        languageRepository.delete(item);
    }

    @Override
    public void deleteById(Integer id) {
        languageRepository.deleteById(id);
    }

    @Override
    public List<Language> novedades(Timestamp fecha) {
        return languageRepository.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
    }
}
