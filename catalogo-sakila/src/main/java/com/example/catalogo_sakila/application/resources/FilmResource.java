package com.example.catalogo_sakila.application.resources;

import com.example.catalogo_sakila.domains.contracts.services.ActorService;
import com.example.catalogo_sakila.domains.contracts.services.FilmService;
import com.example.catalogo_sakila.domains.entities.Film;
import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmDetailsDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmShortDTO;
import com.example.catalogo_sakila.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "Obtiene una pelicula por su ID")
    public FilmDetailsDTO getOne(@PathVariable int id) throws NotFoundException {
        var film = filmService.getOne(id);
        if (film.isEmpty()) {
            throw new NotFoundException("No se pudo encontrar la pelicula con el ID: " + id);
        }
        return FilmDetailsDTO.from(film.get());
    }
}
