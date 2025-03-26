package com.example.catalogo_microservicios.domains.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.catalogo_microservicios.domains.entities.Film;

//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

//@Schema(name = "Pelicula (Corto)", description = "Version corta de las peliculas")
public record FilmShortDTO(
        @JsonProperty("id")
//		@Schema(description = "Identificador de la pelicula", accessMode = AccessMode.READ_ONLY)
        int filmId,
        @JsonProperty("titulo")
//		@Schema(description = "Titulo de la pelicula")
        String title) {
    public static FilmShortDTO from(Film source) {
        return new FilmShortDTO(source.getFilmId(), source.getTitle());
    }
}
