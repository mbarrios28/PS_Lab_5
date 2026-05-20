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
    }
}