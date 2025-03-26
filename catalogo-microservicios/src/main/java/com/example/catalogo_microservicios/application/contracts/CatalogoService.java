package com.example.catalogo_microservicios.application.contracts;

import com.example.catalogo_microservicios.application.models.NovedadesDTO;

import java.util.Date;

public interface CatalogoService {
    NovedadesDTO novedades(Date fecha);
}
