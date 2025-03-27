package com.example.catalogo_microservicios.domains.contracts.services;

import com.example.catalogo_microservicios.domains.core.contracts.services.DomainService;
import com.example.catalogo_microservicios.domains.entities.Category;

import java.util.Date;
import java.util.List;

public interface CategoryService  extends DomainService<Category, Integer> {
    List<Category> novedades(Date fecha);
}
