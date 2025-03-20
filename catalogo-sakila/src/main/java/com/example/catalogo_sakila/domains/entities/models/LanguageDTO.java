package com.example.catalogo_sakila.domains.entities.models;

import com.example.catalogo_sakila.domains.entities.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(name = "Language", description = "Información sobre los idiomas disponibles en el catálogo de peliculass")
public class LanguageDTO {

    @JsonProperty("id")
    private int languageId;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Nombre del idioma", example = "Inglés", required = true, maxLength = 20)
    @JsonProperty("idioma")
    private String name;

    public static LanguageDTO from(Language source) {
        return new LanguageDTO(
                source.getLanguageId(),
                source.getName()
        );
    }

    public static Language from(LanguageDTO source) {
        return new Language(
                source.getLanguageId(),
                source.getName()
        );
    }
}