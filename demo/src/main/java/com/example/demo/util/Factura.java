package com.example.catalog.util;

import com.example.catalog.ioc.Repository;

public class Factura {
    Calculadora calculadora;
    Repository repository;

    public Factura(Calculadora calculadora, Repository repository) {
        super();
        this.calculadora = calculadora;
        this.repository = repository;
    }

    public double calcularTotal(int cantidad, int precio) {
        return calculadora.suma(cantidad, precio);
    }

    public void emitir() {
        repository.guardar();
    }

}
