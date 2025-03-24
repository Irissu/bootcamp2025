package com.example.catalogo_sakila.application.contracts;

import com.example.catalogo_sakila.application.models.NovedadesDTO;

import java.util.Date;
import java.sql.Timestamp;

public interface CatalogoService {

    NovedadesDTO novedades(Date fecha);
}
