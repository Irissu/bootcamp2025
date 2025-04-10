package com.example.catalogo_sakila.domains.services;

import com.example.catalogo_sakila.domains.contracts.services.ActorService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.catalogo_sakila.domains.contracts.repositories.ActorRepository;
import com.example.catalogo_sakila.domains.entities.Actor;
import com.example.catalogo_sakila.domains.event.EmitEntityDeleted;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.InvalidDataException;
import com.example.catalogo_sakila.exceptions.NotFoundException;


@Service
public class ActorServiceImpl implements ActorService {
    private ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public <T> List<T> getByProjection(Class<T> type) {
        return actorRepository.findAllBy(type);
    }

    @Override
    public <T> Iterable<T> getByProjection(Sort sort, Class<T> type) {
        return actorRepository.findAllBy(sort, type);
    }

    @Override
    public <T> Page<T> getByProjection(Pageable pageable, Class<T> type) {
        return actorRepository.findAllBy(pageable, type);
    }

    @Override
    public Iterable<Actor> getAll(Sort sort) {
        return actorRepository.findAll(sort);
    }

    @Override
    public Page<Actor> getAll(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    @Override
    public List<Actor> getAll() {
        return actorRepository.findAll();
    }

    @Override
    public Optional<Actor> getOne(Integer id) {
        return actorRepository.findById(id);
    }

//    @Override
//    public Actor add(Actor item) throws DuplicateKeyException, InvalidDataException {
//        if(item == null)
//            throw new InvalidDataException("No puede ser nulo");
//        if(item.isInvalid())
//            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
//		if(item.getActorId() != 0 && dao.existsById(item.getActorId()))
//			throw new DuplicateKeyException("Ya existe");
//		return dao.save(item);
//        return actorRepository.insert(item);
//    }

    @Override
    public Actor add(Actor item) throws DuplicateKeyException, InvalidDataException {
        if(item == null) {
            throw new InvalidDataException("El actor no puede ser nulo");
        }
        if(item.isInvalid()) {
            throw new InvalidDataException(item.getErrorsMessage());
        }

        if(item.getActorId() > 0 && actorRepository.existsById(item.getActorId())) {
            throw new DuplicateKeyException("El actor ya existe");
        }
        return actorRepository.save(item);
    }

    @Override
    public Actor modify(Actor item) throws NotFoundException, InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        if(item.isInvalid())
            throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
//		if(!dao.existsById(item.getActorId()))
//			throw new NotFoundException();
//		return dao.save(item);
        return actorRepository.update(item);
    }

    @Override
    public void delete(Actor item) throws InvalidDataException {
        if(item == null)
            throw new InvalidDataException("No puede ser nulo");
        actorRepository.delete(item);
    }

    @Override
    //@EmitEntityDeleted(entityName = "Actores")
    public void deleteById(Integer id) {
        actorRepository.deleteById(id);
    }

    @Override
    public void repartePremios() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Actor> novedades(Date fecha) {
        return actorRepository.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
    }
}
