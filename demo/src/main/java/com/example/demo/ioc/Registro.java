package com.example.catalog.ioc;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


@Component
public class Registro {
    public Registro(Configuration config) {
        this.config = config;
    }

    private Configuration config;

    //	public Registro() {
    //		System.err.println("Registro creado");
    //	}
    @PostConstruct
    private void init() {
        config.init();
    }
 }
