package com.example.catalogo_sakila.application.resources;

import com.example.catalogo_sakila.domains.contracts.services.LanguageService;
import com.example.catalogo_sakila.domains.entities.Language;
import com.example.catalogo_sakila.domains.entities.models.LanguageDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/language/v1")
@Tag(name = "language-service", description = "Endpoint de idiomas")
public class LanguageResource {
    private LanguageService languageService;

    public LanguageResource(LanguageService languageService) {
        super();
        this.languageService = languageService;
    }

    @GetMapping
    @Hidden
    public List<LanguageDTO> getAll() {
        return languageService.getAll().stream()
                .map(LanguageDTO::from)
                .toList();
    }

    @GetMapping(params = { "page" } )
    @Operation(summary = "Obtiene la lista de idiomas paginada")
    public Page<LanguageDTO> getAll(Pageable pageable) {
        return languageService.getAll().stream()
                .map(LanguageDTO::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> new PageImpl<>(list, pageable, list.size())));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Obtiene un idioma por su ID")
    public LanguageDTO getOne(@PathVariable int id) throws NotFoundException {
        var item = languageService.getOne(id);
        if (item.isEmpty()) {
            throw new NotFoundException("No se encontró el idioma con id " + id);
        }
        return LanguageDTO.from(item.get());
    }

    @PostMapping
    @Operation(summary = "Crea un idioma nuevo")
    @ApiResponse(responseCode = "201", description = "Idioma creado")
    public ResponseEntity<Object> create(@Valid @RequestBody LanguageDTO item) throws BadRequestException, DuplicateKeyException, InvalidDataException {
        var newItem = languageService.add(LanguageDTO.from(item));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newItem.getLanguageId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica un idioma")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody LanguageDTO item) throws BadRequestException, NotFoundException, InvalidDataException {
        if (item.getLanguageId() != id) {
            throw new BadRequestException("El id del idioma no coincide con el recurso a modificar");
        }
        languageService.modify(LanguageDTO.from(item));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un idioma")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        languageService.deleteById(id);
    }
}