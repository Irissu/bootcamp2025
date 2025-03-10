package com.example.catalog.ioc;

//@Qualifier("mentira")
//@Repository
//@Primary
public class RepositoryMock implements Repository {
    @Override
    public void guardar() {
        System.err.println("Guardando de mentira");
    }
}
