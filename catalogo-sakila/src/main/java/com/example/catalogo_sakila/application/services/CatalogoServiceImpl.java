package com.example.catalogo_sakila.application.services;

import com.example.catalogo_sakila.application.contracts.CatalogoService;
import com.example.catalogo_sakila.application.models.NovedadesDTO;
import com.example.catalogo_sakila.domains.contracts.services.ActorService;
import com.example.catalogo_sakila.domains.contracts.services.CategoryService;
import com.example.catalogo_sakila.domains.contracts.services.FilmService;
import com.example.catalogo_sakila.domains.contracts.services.LanguageService;
import com.example.catalogo_sakila.domains.entities.models.ActorDTO;
import com.example.catalogo_sakila.domains.entities.models.FilmShortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class CatalogoServiceImpl implements CatalogoService {
    @Autowired
    private FilmService filmSrv;
    @Autowired
    private ActorService artorSrv;
    @Autowired
    private CategoryService categorySrv;
    @Autowired
    private LanguageService languageSrv;

    @Override
    public NovedadesDTO novedades(Timestamp fecha) {
        // Timestamp fecha = Timestamp.valueOf("2019-01-01 00:00:00");
        if(fecha == null)
            fecha = Timestamp.from(Instant.now().minusSeconds(36000));
        return new NovedadesDTO(
                filmSrv.novedades(fecha).stream().map(item -> new FilmShortDTO(item.getFilmId(), item.getTitle())).toList(),
                artorSrv.novedades(fecha).stream().map(item -> ActorDTO.from(item)).toList(),
                categorySrv.novedades(fecha),
                languageSrv.novedades(fecha)
        );
    }
}
