package com.example.catalogo_microservicios.application.models;


import com.example.catalogo_microservicios.domains.entities.Category;
import com.example.catalogo_microservicios.domains.entities.Language;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.FilmShortDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class NovedadesDTO {
    private List<FilmShortDTO> films;
    private List<ActorDTO> actors;
    private List<Category> categories;
    private List<Language> languages;
}
