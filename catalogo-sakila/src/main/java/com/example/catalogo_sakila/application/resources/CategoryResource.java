package com.example.catalogo_sakila.application.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category/v1")
@Tag(name = "category-service", description = "Endpoint de categorías")
public class CategoryResource {
}
