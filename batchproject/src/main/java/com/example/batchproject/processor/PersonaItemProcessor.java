package com.example.batchproject.processor;

import com.example.batchproject.models.Persona;
import com.example.batchproject.models.PersonaDTO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
public class PersonaItemProcessor implements ItemProcessor<PersonaDTO, Persona> {
    private static final Logger log = LoggerFactory.getLogger(PersonaItemProcessor.class);

    @Override
    public Persona process(PersonaDTO item) throws Exception {
        if(item.getId() % 2 == 0 && "Male".equals(item.getSexo())) return null;
        Persona result = new Persona(item.getId(), item.getApellidos() + ", " + item.getNombre(),
                item.getCorreo(), item.getIp());
        log.info("Procesando: " + item);
        return result;
    }
}
