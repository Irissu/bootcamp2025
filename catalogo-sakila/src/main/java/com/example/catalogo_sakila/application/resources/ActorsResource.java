package com.example.catalogo_sakila.application.resources;


import com.example.catalogo_sakila.domains.contracts.services.ActorService;
import com.example.catalogo_sakila.domains.entities.Actor;
import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.exceptions.BadRequestException;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.InvalidDataException;
import com.example.catalogo_sakila.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/actor/v1")
@Tag(name = "actor-service", description = "Endpoint de actores")
public class ActorsResource {
    private ActorService actorService;

    public ActorsResource(ActorService actorService) {
        super();
        this.actorService = actorService;
    }

    @GetMapping
    @Hidden
    public List<ActorDTO> getAll() {
        return actorService.getByProjection(ActorDTO.class);
    }

    @GetMapping(params = { "page" } )
    @Operation(summary = "Obtiene la lista de actores paginada")
    public Page<ActorDTO> getAll(Pageable pageable) {
        return actorService.getByProjection(pageable, ActorDTO.class);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Obtiene un actor por su ID")
    public ActorDTO getOne(@PathVariable int id) throws NotFoundException {
        var item = actorService.getOne(id);
        if (item.isEmpty()) {
            throw new NotFoundException("No se encontró al actor con id " + id);
        }
        return ActorDTO.from(item.get());
    }

    record Titulo(int id, String titulo) {	}

    @GetMapping(path = "/{id}/pelis")
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


    @PostMapping
    @Operation(summary = "Crea un actor nuevo")
    @ApiResponse(responseCode = "201", description = "Actor creado")
    public ResponseEntity<Object> create(@Valid @RequestBody ActorDTO item) throws BadRequestException, DuplicateKeyException, InvalidDataException {
        var newItem = actorService.add(ActorDTO.from(item));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newItem.getActorId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica un actor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody ActorDTO item) throws BadRequestException, NotFoundException, InvalidDataException {
        if (item.getActorId() != id) {
            throw new BadRequestException("el id del actor no coincide con el recurso a modificar");
        }
        actorService.modify(ActorDTO.from(item));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un actor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        actorService.deleteById(id);
    }
}
