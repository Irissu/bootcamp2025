package com.example.catalogo_sakila.application.contracts;

import com.example.catalogo_sakila.application.models.NovedadesDTO;

import java.sql.Timestamp;

public interface CatalogoService {

    NovedadesDTO novedades(Timestamp fecha);
}
