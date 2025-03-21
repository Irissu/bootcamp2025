package com.example.catalogo_sakila.application.resources;

import com.example.catalogo_sakila.domains.contracts.services.CategoryService;
import com.example.catalogo_sakila.domains.entities.models.CategoryDTO;
import com.example.catalogo_sakila.exceptions.BadRequestException;
import com.example.catalogo_sakila.exceptions.DuplicateKeyException;
import com.example.catalogo_sakila.exceptions.InvalidDataException;
import com.example.catalogo_sakila.exceptions.NotFoundException;
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
@RequestMapping("/category/v1")
@Tag(name = "category-service", description = "Endpoint de categorías")
public class CategoryResource {

    private CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        super();
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Obtiene la lista de categorías")
    public List<CategoryDTO> getAll() {
        return  categoryService.getAll().stream().map(CategoryDTO::from).toList();
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Obtiene una categoría por su ID")
    public CategoryDTO getOne(@PathVariable int id) throws NotFoundException {
        var category = categoryService.getOne(id);
            if (category.isEmpty()) {
                throw new NotFoundException("No se encontró la categoría con id " + id);
            }
        return CategoryDTO.from(category.get());
    }

    @PostMapping
    @Operation(summary = "Crea una categoría nueva")
    @ApiResponse(responseCode = "201", description = "Categoría creada")
    public ResponseEntity<Object> create(@Valid @RequestBody CategoryDTO category) throws DuplicateKeyException, InvalidDataException {
        var newCategory = categoryService.add(CategoryDTO.from(category));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCategory.getCategoryId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica una categoría")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody CategoryDTO category) throws BadRequestException, NotFoundException, InvalidDataException {
        if (category.getCategoryId() != id) {
            throw new BadRequestException("El id del idioma no coincide con el recurso a modificar");
        }
        categoryService.modify(CategoryDTO.from(category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una categoría")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        categoryService.deleteById(id);
    }

}
