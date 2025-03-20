package com.example.catalogo_sakila.application.resources;

import com.example.catalogo_sakila.domains.contracts.services.ActorService;
import com.example.catalogo_sakila.domains.contracts.services.FilmService;
import com.example.catalogo_sakila.domains.entities.Film;
import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmDetailsDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmEditDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmShortDTO;
import com.example.catalogo_sakila.exceptions.BadRequestException;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.InvalidDataException;
import com.example.catalogo_sakila.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/film/v1")
@Tag(name = "film-service", description = "Endpoint de películas")
public class FilmResource {
    private FilmService filmService;

    public FilmResource(FilmService filmService) {
        super();
        this.filmService = filmService;
    }

    @GetMapping
    @Operation(summary = "Obtiene la lista de películas sin paginar")
    public List<FilmShortDTO> getAll() {
        return filmService.getByProjection(FilmShortDTO.class);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Obtiene una pelicula y toda su información detallada por su ID")
    public FilmDetailsDTO getOne(@PathVariable int id) throws NotFoundException {
        var film = filmService.getOne(id);
        if (film.isEmpty()) {
            throw new NotFoundException("No se pudo encontrar la pelicula con el ID: " + id);
        }
        return FilmDetailsDTO.from(film.get());
    }

    @PostMapping
    @Operation(summary = "Crea una película nueva")
    @ApiResponse(responseCode = "201", description = "película creada")
    public ResponseEntity<Object> create(@Valid @RequestBody FilmEditDTO item) throws BadRequestException, DuplicateKeyException, InvalidDataException {
        var newItem = filmService.add(FilmEditDTO.from(item));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newItem.getFilmId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica una película")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody FilmEditDTO item) throws BadRequestException, NotFoundException, InvalidDataException {
        if (item.getFilmId() != id) {
            throw new BadRequestException("el id de la película no coincide con el recurso a modificar");
        }
        filmService.modify(FilmEditDTO.from(item));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una película")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        filmService.deleteById(id);
    }
}
