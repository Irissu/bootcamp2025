package com.example.catalog.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class PersonaTest {
    @Test
    void createPersona() {
        var p = new Persona(1, "Iris");

        assertNotNull(p);
        assertAll("Contructor",
                () -> assertEquals(1, p.id),
                () -> assertEquals("Iris", p.nombre, "nombre"),
                () -> assertEquals("de la Mora", p.apellidos, "apellidos")
        );
        assertEquals(1, p.id);
        assertEquals("Iris", p.nombre);
    }
}
