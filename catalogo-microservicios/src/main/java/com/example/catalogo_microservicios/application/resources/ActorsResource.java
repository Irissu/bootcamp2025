package com.example.catalogo_microservicios.application.resources;

import com.example.catalogo_microservicios.domains.contracts.services.ActorService;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorShort;
import com.example.catalogo_microservicios.exceptions.BadRequestException;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
//@RequestMapping("/actor/v1")
@RequestMapping("/actores")
@Tag(name = "actor-service", description = "Endpoint de actores")
public class ActorsResource {
    private ActorService actorService;

    public ActorsResource(ActorService actorService) {
        super();
        this.actorService = actorService;
    }

//    @GetMapping(path = "/v1")
//    @Hidden
//    public List<ActorDTO> getAll() {
//        return actorService.getByProjection(ActorDTO.class);
//    }
//
//    @GetMapping(path = { "/v1", "/v2" }, params = "page")
//    @Operation(summary = "Obtiene la lista de actores paginada")
//    public Page<ActorDTO> getAll(Pageable pageable) {
//        return actorService.getByProjection(pageable, ActorDTO.class);
//    }

    @GetMapping(path = "/v1")
    public List<?> getAll(@RequestParam(required = false, defaultValue = "largo") String modo) {
        if("short".equals(modo))
            return (List<?>) actorService.getByProjection(Sort.by("firstName", "lastName"), ActorShort.class);
        else
            return (List<?>) actorService.getByProjection(Sort.by("firstName", "lastName"), ActorDTO.class); // srv.getAll();;
    }

    @GetMapping(path = "/v2")
    public List<ActorDTO> getAllv2(@RequestParam(required = false) String sort) {
        if (sort != null)
            return (List<ActorDTO>) actorService.getByProjection(Sort.by(sort), ActorDTO.class);
        return actorService.getByProjection(ActorDTO.class);
    }

    @GetMapping(path = { "/v1/{id}", "/v2/{id}" })
    @Operation(summary = "Obtiene un actor por su ID")
    public ActorDTO getOne(@PathVariable int id) throws NotFoundException {
        var item = actorService.getOne(id);
        if (item.isEmpty()) {
            throw new NotFoundException("No se encontró al actor con id " + id);
        }
        return ActorDTO.from(item.get());
    }

    record Titulo(int id, String titulo) {	}

    @GetMapping(path = {"/{id}/pelis", "/v1/{id}/pelis","/v2/{id}/pelis"} )
    @Operation(summary = "Obtiene un las peliculas de un actor por su ID")
    @Transactional
    public List<Titulo> getPeliculas(@PathVariable int id) throws NotFoundException {
        var item = actorService.getOne(id);
        if (item.isEmpty()) {
            throw new NotFoundException("No se encontró el actor con id " + id);
        }
        return item.get().getFilmActors().stream()
                .map(o -> new Titulo(o.getFilm().getFilmId(), o.getFilm().getTitle()))
                .toList();
    }


    @PostMapping(path = { "/v1", "/v2" })
    @Operation(summary = "Crea un actor nuevo")
    @ApiResponse(responseCode = "201", description = "Actor creado")
    public ResponseEntity<Object> create(@Valid @RequestBody ActorDTO item) throws BadRequestException, DuplicatedKeyException, InvalidDataException {
        var newItem = actorService.add(ActorDTO.from(item));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newItem.getActorId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = { "/v1/{id}", "/v2/{id}" })
    @Operation(summary = "Modifica un actor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody ActorDTO item) throws BadRequestException, NotFoundException, InvalidDataException {
        if (item.getActorId() != id) {
            throw new BadRequestException("el id del actor no coincide con el recurso a modificar");
        }
        actorService.modify(ActorDTO.from(item));
    }

    @DeleteMapping(path = "/v1/{id}/jubilacion")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void jubilar(@PathVariable int id) throws NotFoundException {
        var item = actorService.getOne(id);
        if(item.isEmpty())
            throw new NotFoundException();
        item.get().jubilate();
    }

    @DeleteMapping(path = { "/v1/{id}", "/v2/{id}" })
    @Operation(summary = "Elimina un actor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        actorService.deleteById(id);
    }
}
