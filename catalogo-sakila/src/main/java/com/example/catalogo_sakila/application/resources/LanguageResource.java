package com.example.catalogo_sakila.application.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/language/v1")
@Tag(name = "language-service", description = "Endpoint de idiomas")
public class LanguageResource {
}
