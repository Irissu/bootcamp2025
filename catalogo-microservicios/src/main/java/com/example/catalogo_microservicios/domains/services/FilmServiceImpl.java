package com.example.catalogo_microservicios.domains.services;

import com.example.catalogo_microservicios.domains.contracts.repositories.FilmRepository;
import com.example.catalogo_microservicios.domains.contracts.services.FilmService;
import com.example.catalogo_microservicios.domains.entities.Film;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {
    private FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public <T> List<T> getByProjection(@NonNull Class<T> type) {
        return filmRepository.findAllBy(type);
    }

    @Override
    public <T> List<T> getByProjection(@NonNull Sort sort, @NonNull Class<T> type) {
        return filmRepository.findAllBy(sort, type);
    }

    @Override
    public <T> Page<T> getByProjection(@NonNull Pageable pageable, @NonNull Class<T> type) {
        return filmRepository.findAllBy(pageable, type);
    }

    @Override
    public List<Film> getAll(@NonNull Sort sort) {
        return filmRepository.findAll(sort);
    }

    @Override
    public Page<Film> getAll(@NonNull Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    @Override
    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> getOne(Integer id) {
        return filmRepository.findById(id);
    }

    @Override
    public Optional<Film> getOne(@NonNull Specification<Film> spec) {
        return filmRepository.findOne(spec);
    }

    @Override
    public List<Film> getAll(@NonNull Specification<Film> spec) {
        return filmRepository.findAll(spec);
    }

    @Override
    public Page<Film> getAll(@NonNull Specification<Film> spec, @NonNull Pageable pageable) {
        return filmRepository.findAll(spec, pageable);
    }

    @Override
    public List<Film> getAll(@NonNull Specification<Film> spec, @NonNull Sort sort) {
        return filmRepository.findAll(spec, sort);
    }

    @Override
    @Transactional
    public Film add(Film item) throws DuplicatedKeyException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        if(filmRepository.existsById(item.getFilmId()))
            throw new DuplicatedKeyException(item.getErrorsMessage());
        return filmRepository.save(item);
    }

    @Override
    @Transactional
    public Film modify(Film item) throws NotFoundException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
        var leido = filmRepository.findById(item.getFilmId()).orElseThrow(() -> new NotFoundException());
        return filmRepository.save(item.merge(leido));
    }

    @Override
    public void delete(Film item) throws InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        deleteById(item.getFilmId());
    }

    @Override
    public void deleteById(Integer id) {
        filmRepository.deleteById(id);
    }


    @Override
    public List<Film> novedades(@NonNull Date fecha) {
        return filmRepository.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
    }
}
