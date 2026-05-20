package com.pruebas.model;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Pruebas unitarias al carrito de compras")
public class CarritoCompraTest {
    private ArrayList<Producto> listaProductos;

    @BeforeEach
    void setUp(){
        listaProductos = new ArrayList<>();
        listaProductos.add(new Producto("Escritorio", 249.9, 50));
        listaProductos.add(new Producto("Silla de Oficina", 99.9, 150));
        listaProductos.add(new Producto("Silla de escritorio", 99.9, 100));
        listaProductos.add(new Producto("Silla gamer", 369.9, 20));
    }
}