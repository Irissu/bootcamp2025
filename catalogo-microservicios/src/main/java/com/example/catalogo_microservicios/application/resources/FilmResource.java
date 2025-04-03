package com.example.catalogo_microservicios.application.resources;

import com.example.catalogo_microservicios.application.proxies.MeGustaProxy;
import com.example.catalogo_microservicios.domains.contracts.services.FilmService;
import com.example.catalogo_microservicios.domains.entities.Category;
import com.example.catalogo_microservicios.domains.entities.Film;
import com.example.catalogo_microservicios.domains.entities.dtos.ActorDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.FilmDetailsDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.FilmEditDTO;
import com.example.catalogo_microservicios.domains.entities.dtos.FilmShortDTO;
import com.example.catalogo_microservicios.exceptions.BadRequestException;
import com.example.catalogo_microservicios.exceptions.DuplicatedKeyException;
import com.example.catalogo_microservicios.exceptions.InvalidDataException;
import com.example.catalogo_microservicios.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Hidden
    @Operation(summary = "Obtiene la lista de películas sin paginar")
    public List<FilmShortDTO> getAll() {
        return filmService.getByProjection(FilmShortDTO.class);
    }

    @GetMapping(params = {"page"})
    @Operation(summary = "Obtiene la lista de películas paginada")
    public Page<FilmShortDTO> getAll(Pageable pageable) {
        return filmService.getByProjection(pageable, FilmShortDTO.class);
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

    @GetMapping(path = "/contenido-adicional")
    @Operation(summary = "Listado del contenido adicional posible")
    public List<String> getSpecialFeatures() {
        return List.of(Film.SpecialFeature.values()).stream().map(o -> o.getValue()).toList();
    }

    @PostMapping
    @Operation(summary = "Crea una película nueva")
    @ApiResponse(responseCode = "201", description = "película creada")
    public ResponseEntity<Object> create(@Valid @RequestBody FilmEditDTO item) throws BadRequestException, DuplicatedKeyException, InvalidDataException {
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

    @Hidden
    @GetMapping(params = "mode=details")
    public List<FilmDetailsDTO> getAllDetails(
//			@Parameter(allowEmptyValue = true, required = false, schema = @Schema(type = "string", allowableValues = {"details","short"}, defaultValue = "short"))
            @RequestParam(defaultValue = "short") String mode) {
        return filmService.getAll().stream().map(item -> FilmDetailsDTO.from(item)).toList();
    }

    // CONSULTAS FILTRADAS Y CON SPECIFICATIONS
    record Search(
            @Schema(description = "Que el titulo contenga")
            String title,
            @Schema(description = "Duración mínima de la pelicula")
            Integer minlength,
            @Schema(description = "Duración máxima de la pelicula")
            Integer maxlength,
            @Schema(description = "La clasificación por edades asignada a la película", allowableValues = {"G", "PG", "PG-13", "R", "NC-17"})
            @Pattern(regexp = "^(G|PG|PG-13|R|NC-17)$")
            String rating,
            @Schema(description = "Formato de la respuesta", type = "string", allowableValues = {
                    "details", "short" }, defaultValue = "short")
            String mode
    ) {}



    @Operation(summary = "Consulta filtrada de peliculas")
    @GetMapping("/filtro")
    public List<?> search(@ParameterObject @Valid Search filter) throws BadRequestException {
        if(filter.minlength != null && filter.maxlength != null && filter.minlength > filter.maxlength)
            throw new BadRequestException("la duración máxima debe ser superior a la mínima");
        Specification<Film> spec = null;
        if(filter.title != null && !"".equals(filter.title)) {
            Specification<Film> cond = (root, query, builder) -> builder.like(root.get("title"), "%" + filter.title.toUpperCase() + "%");
            spec = spec == null ? cond : spec.and(cond);
        }
        if(filter.rating != null && !"".equals(filter.rating)) {
            if(!List.of(Film.Rating.VALUES).contains(filter.rating))
                throw new BadRequestException("rating desconocido");
            Specification<Film> cond = (root, query, builder) -> builder.equal(root.get("rating"), Film.Rating.getEnum(filter.rating));
            spec = spec == null ? cond : spec.and(cond);
        }
        if(filter.minlength != null) {
            Specification<Film> cond = (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("length"), filter.minlength);
            spec = spec == null ? cond : spec.and(cond);
        }
        if(filter.maxlength != null) {
            Specification<Film> cond = (root, query, builder) -> builder.lessThanOrEqualTo(root.get("length"), filter.maxlength);
            spec = spec == null ? cond : spec.and(cond);
        }
        if(spec == null)
            throw new BadRequestException("Faltan los parametros de filtrado");
        var query = filmService.getAll(spec).stream();
        if("short".equals(filter.mode))
            return query.map(e -> FilmShortDTO.from(e)).toList();
        else {
            return query.map(e -> FilmDetailsDTO.from(e)).toList();
        }
    }
 // ..
    @GetMapping(path = "/{id}", params = "mode=short")
    public FilmShortDTO getOneCorto(
            @Parameter(description = "Identificador de la pelicula", required = true)
            @PathVariable
            int id,
            @Parameter(required = false, allowEmptyValue = true, schema = @Schema(type = "string", allowableValues = {
                    "details", "short", "edit" }, defaultValue = "edit"))
            @RequestParam(required = false, defaultValue = "edit")
            String mode)
            throws Exception {
        Optional<Film> rslt = filmService.getOne(id);
        if (rslt.isEmpty())
            throw new NotFoundException();
        return FilmShortDTO.from(rslt.get());
    }

    @GetMapping(path = "/{id}", params = "mode=details")
    public FilmDetailsDTO getOneDetalle(
            @Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id,
            @Parameter(required = false, schema = @Schema(type = "string", allowableValues = { "details", "short",
                    "edit" }, defaultValue = "edit")) @RequestParam(required = false, defaultValue = "edit") String mode)
            throws Exception {
        Optional<Film> rslt = filmService.getOne(id);
        if (rslt.isEmpty())
            throw new NotFoundException();
        return FilmDetailsDTO.from(rslt.get());
    }

    @Operation(summary = "Recupera una pelicula", description = "Están disponibles una versión corta, una versión con los detalles donde se han transformado las dependencias en cadenas y una versión editable donde se han transformado las dependencias en sus identificadores.")
    @GetMapping(path = "/{id}")
    @ApiResponse(responseCode = "200", description = "Pelicula encontrada", content = @Content(schema = @Schema(oneOf = {
            FilmShortDTO.class, FilmDetailsDTO.class, FilmEditDTO.class })))
    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public FilmEditDTO getOneEditar(
            @Parameter(description = "Identificador de la pelicula", required = true)
            @PathVariable
            int id,
            @Parameter(required = false, schema = @Schema(type = "string", allowableValues = { "details", "short",
                    "edit" }, defaultValue = "edit"))
            @RequestParam(required = false, defaultValue = "edit")
            String mode)
            throws Exception {
        Optional<Film> rslt = filmService.getOne(id);
        if (rslt.isEmpty())
            throw new NotFoundException();
        return FilmEditDTO.from(rslt.get());
    }

    @Operation(summary = "Listado de los actores de la pelicula")
    @ApiResponse(responseCode = "200", description = "Pelicula encontrada")
    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
    @GetMapping(path = "/{id}/reparto")
    @Transactional
    public List<ActorDTO> getFilms(
            @Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id)
            throws Exception {
        Optional<Film> rslt = filmService.getOne(id);
        if (rslt.isEmpty())
            throw new NotFoundException();
        return rslt.get().getActors().stream().map(item -> ActorDTO.from(item)).toList();
    }

    @Operation(summary = "Listado de las categorias de la pelicula")
    @ApiResponse(responseCode = "200", description = "Pelicula encontrada")
    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
    @GetMapping(path = "/{id}/categorias")
    @Transactional
    public List<Category> getCategories(
            @Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id)
            throws Exception {
        Optional<Film> rslt = filmService.getOne(id);
        if (rslt.isEmpty())
            throw new NotFoundException();
        return rslt.get().getCategories();
    }

    @GetMapping(path = "/clasificaciones")
    @Operation(summary = "Listado de las clasificaciones por edades")
    public List<Map<String, String>> getClasificaciones() {
        return List.of(Map.of("key", "G", "value", "Todos los públicos"),
                Map.of("key", "PG", "value", "Guía paternal sugerida"),
                Map.of("key", "PG-13", "value", "Guía paternal estricta"),
                Map.of("key", "R", "value", "Restringido"),
                Map.of("key", "NC-17", "value", "Prohibido para audiencia de 17 años y menos"));
    }

    @Operation(summary = "Añadir una nueva pelicula")
    @ApiResponse(responseCode = "201", description = "Pelicula añadida")
    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Object> add(@RequestBody() FilmEditDTO item) throws Exception {
        Film newItem = filmService.add(FilmEditDTO.from(item));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newItem.getFilmId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Modificar una pelicula existente", description = "Los identificadores deben coincidir")
    @ApiResponse(responseCode = "200", description = "Pelicula encontrada")
    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
//	@Transactional
    @PutMapping(path = "/{id}")
    public FilmEditDTO modify(
            @Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id,
            @Valid @RequestBody FilmEditDTO item) throws Exception {
        if (item.getFilmId() != id)
            throw new BadRequestException("No coinciden los identificadores");
        return FilmEditDTO.from(filmService.modify(FilmEditDTO.from(item)));
    }

//    @Operation(summary = "Borrar una pelicula existente")
//    @ApiResponse(responseCode = "204", description = "Pelicula borrada")
//    @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
//    @DeleteMapping(path = "/{id}")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT)
//    public void delete(@Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id)
//            throws Exception {
//        filmService.deleteById(id);
//    }

    @Autowired
    MeGustaProxy proxy;

//	@Operation(summary = "Enviar un me gusta")
//	@ApiResponse(responseCode = "200", description = "Like enviado")
//	@PostMapping(path = "{id}/like")
//	public String like(@Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id)
//			throws Exception {
//		return proxy.sendLike(id);
//	}

    //	@PreAuthorize("hasRole('ADMINISTRADORES')")
    @Operation(summary = "Enviar un me gusta")
    @ApiResponse(responseCode = "200", description = "Like enviado")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "{id}/like")
    public String like(@Parameter(description = "Identificador de la pelicula", required = true) @PathVariable int id,
                       @Parameter(hidden = true) @RequestHeader(required = false) String authorization) throws Exception {
        if (authorization == null)
            return proxy.sendLike(id);
        return proxy.sendLike(id, authorization);
    }


}
