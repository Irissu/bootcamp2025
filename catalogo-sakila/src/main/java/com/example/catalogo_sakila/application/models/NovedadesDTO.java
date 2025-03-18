package com.example.catalogo_sakila.application.models;

import java.util.List;

import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmShortDTO;
import com.example.catalogo_sakila.domains.entities.Category;
import com.example.catalogo_sakila.domains.entities.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class NovedadesDTO {
    private List<FilmShortDTO> films;
    private List<ActorDTO> actors;
    private List<Category> categories;
    private List<Language> languages;
}
