package com.example.catalog.util;

public class Factura {
    Calculadora calculadora;

    public Factura(Calculadora calculadora) {
        super();
        this.calculadora = calculadora;
    }

    public double calcularTotal(int cantidad, int precio) {
        return calculadora.suma(cantidad, precio);
    }
}
