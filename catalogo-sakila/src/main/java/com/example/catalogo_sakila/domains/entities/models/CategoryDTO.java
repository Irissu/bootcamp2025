package com.example.catalogo_sakila.domains.entities.models;

import com.example.catalogo_sakila.domains.entities.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(name = "Category", description = "Información sobre las categorías de películas en el catálogo")
public class CategoryDTO {

    @JsonProperty("id")
    private int categoryId;

    @NotBlank
    @Size(max = 25)
    @Schema(description = "Nombre de la categoría", example = "Acción", required = true, maxLength = 25)
    @JsonProperty("categoria")
    private String name;

    public static CategoryDTO from(Category source) {
        return new CategoryDTO(
                source.getCategoryId(),
                source.getName()
        );
    }

    public static Category from(CategoryDTO source) {
        return new Category(
                source.getCategoryId(),
                source.getName()
        );
    }
}