package com.example.catalogo_microservicios.domains.services;

import com.example.catalogo_microservicios.domains.contracts.repositories.LanguageRepository;
import com.example.catalogo_microservicios.domains.contracts.services.LanguageService;
import com.example.catalogo_microservicios.domains.entities.Language;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {
    private LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public void welcomeMessage() {
        System.out.println("Hola! Welcome! Benvingut!");
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
    public Language add(Language item) throws DuplicatedKeyException, InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El idioma no puede ser nulo");
        }
        if (item.getLanguageId() > 0 && languageRepository.existsById(item.getLanguageId())) {
            throw new DuplicatedKeyException("El idioma ya existe");
        }
        return languageRepository.save(item);
    }

    @Override
    public Language modify(Language item) throws NotFoundException, InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El idioma no puede ser nulo");
        }
        if (!languageRepository.existsById(item.getLanguageId())) {
            throw new NotFoundException("El idioma no se ha podido encontrar");
        }
        return languageRepository.save(item);
    }

    @Override
    public void delete(Language item) throws InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El idioma no puede ser nulo");
        }
        languageRepository.delete(item);
    }

    @Override
    public void deleteById(Integer id) {
        languageRepository.deleteById(id);
    }
}
