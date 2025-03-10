package com.example.catalog.ioc;

import org.springframework.beans.factory.annotation.Qualifier;

//@Repository
//@Qualifier("verdad")
public class RepositoryImpl implements Repository {
    public RepositoryImpl(Configuration config, Registro registro) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void guardar() {
        System.err.println("Guardando");
    }
}
