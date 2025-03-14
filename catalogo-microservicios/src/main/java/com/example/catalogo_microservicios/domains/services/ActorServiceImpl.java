package com.example.catalogo_microservicios.domains.services;

import com.example.catalogo_microservicios.domains.contracts.repositories.ActorRepository;
import com.example.catalogo_microservicios.domains.contracts.services.ActorService;
import com.example.catalogo_microservicios.domains.entities.Actor;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public class ActorServiceImpl implements ActorService {
    private ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) { this.actorRepository = actorRepository; } // mejor que autowired

    @Override
    public void repartePremios() {
        System.out.println("Felicidades, ha ganado usted un premio!");
    }

    @Override
    public List<Actor> getAll() {
        return actorRepository.findAll();
    }

    @Override
    public Optional<Actor> getOne(Integer id) {
        return actorRepository.findById(id);
    }

    @Override
    public Actor add(Actor item) throws DuplicatedKeyException, InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El actor no puede ser nulo");
        }
        if (item.getActorId() > 0 && actorRepository.existsById(item.getActorId())) {
            throw new DuplicatedKeyException("El actor ya existe");
        }

        return actorRepository.save(item);
    }

    @Override
    public Actor modify(Actor item) throws NotFoundException, InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El actor no puede ser nulo");
        }
        if (!actorRepository.existsById(item.getActorId())) {
            throw new NotFoundException("El actor no se ha podido encontrar");
        }
         return actorRepository.save(item);
    }

    @Override
    public void delete(Actor item) throws InvalidDataException {
        if (item == null) {
            throw new InvalidDataException("El actor no puede ser nulo");
        }
        actorRepository.delete(item);
    }

    @Override
    public void deleteById(Integer id) {
        actorRepository.deleteById(id);

    }
}
