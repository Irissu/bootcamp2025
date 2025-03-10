package com.example.catalog.ioc;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
    public Configuration() {
        System.err.println("Configuracion creada");
    }
    public void init() {
        System.err.println("Configuracion inicializada");
    }
}
