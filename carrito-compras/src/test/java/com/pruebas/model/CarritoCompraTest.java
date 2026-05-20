package com.pruebas.model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas unitarias al carrito de compras")
public class CarritoCompraTest {

    private ArrayList<Producto> listaProductos;
    private CarritoCompra carrito;

    @BeforeEach
    void setUp() throws Exception {

        listaProductos = new ArrayList<>();

        listaProductos.add(new Producto("Escritorio", 249.9, 50));
        listaProductos.add(new Producto("Silla Oficina", 99.9, 150));
        listaProductos.add(new Producto("Silla Gamer", 369.9, 20));
        listaProductos.add(new Producto("Monitor", 899.9, 0));

        carrito = new CarritoCompra();
    }

    // =========================================================
    // TC-01 → Agregar un item nuevo al carrito
    // =========================================================
    @Test
    @DisplayName("TC-01 Agregar un item nuevo al carrito")
    void testAgregarNuevoItem() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 2);

        carrito.addItem(item);

        assertEquals(1, carrito.getCarrito().size());
        assertEquals(item, carrito.getCarrito().get(0));
    }

    // =========================================================
    // TC-02 → Agregar producto duplicado lanza excepción
    // =========================================================
    @Test
    @DisplayName("TC-02 Agregar producto duplicado")
    void testAgregarProductoDuplicado() throws Exception {

        ItemCarrito item1 = new ItemCarrito(listaProductos.get(0), 1);
        ItemCarrito item2 = new ItemCarrito(listaProductos.get(0), 1);

        carrito.addItem(item1);

        assertThrows(
            IllegalArgumentException.class,
            () -> carrito.addItem(item2)
        );
    }

    // =========================================================
    // TC-03 → Eliminar item existente
    // =========================================================
    @Test
    @DisplayName("TC-03 Eliminar item existente")
    void testEliminarItemExistente() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(1), 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertEquals(0, carrito.getCarrito().size());
    }

    // =========================================================
    // TC-04 → Eliminar item inexistente lanza excepción
    // =========================================================
    @Test
    @DisplayName("TC-04 Eliminar item inexistente")
    void testEliminarItemInexistente() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(1), 1);

        assertThrows(
            IllegalArgumentException.class,
            () -> carrito.removeItem(item)
        );
    }

    // =========================================================
    // TC-05 → Crear item con cantidad <= 0 lanza excepción
    // =========================================================
    @Test
    @DisplayName("TC-05 Crear item con cantidad inválida")
    void testCantidadInvalida() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new ItemCarrito(listaProductos.get(0), 0)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new ItemCarrito(listaProductos.get(0), -5)
        );
    }

    // =========================================================
    // TC-06 → Agregar una unidad extra a producto existente
    // =========================================================
    @Test
    @DisplayName("TC-06 Agregar unidad extra")
    void testAgregarUnidadExtra() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 1);

        carrito.addItem(item);

        carrito.addOneProduct(listaProductos.get(0));

        assertEquals(2, carrito.getCarrito().get(0).getCantidad());
    }

    // =========================================================
    // TC-07 → Agregar una unidad con stock=0 lanza excepción
    // =========================================================
    @Test
    @DisplayName("TC-07 Agregar unidad sin stock")
    void testAgregarUnidadSinStock() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(3), 0);

        assertThrows(
            IllegalArgumentException.class,
            () -> carrito.addOneProduct(listaProductos.get(3))
        );
    }

    // =========================================================
    // TC-08 → Quitar una unidad reduce cantidad
    // =========================================================
    @Test
    @DisplayName("TC-08 Quitar unidad reduce cantidad")
    void testQuitarUnidad() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 3);

        carrito.addItem(item);

        carrito.removeOneProduct(listaProductos.get(0));

        assertEquals(2, carrito.getCarrito().get(0).getCantidad());
    }

    // =========================================================
    // TC-09 → Quitar la última unidad elimina el item del carrito
    // =========================================================
    @Test
    @DisplayName("TC-09 Quitar última unidad elimina item")
    void testQuitarUltimaUnidad() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 1);

        carrito.addItem(item);

        carrito.removeOneProduct(listaProductos.get(0));

        assertEquals(0, carrito.getCarrito().size());
    }

    // =========================================================
    // TC-10 → Vaciar carrito deja lista en cero
    // =========================================================
    @Test
    @DisplayName("TC-10 Vaciar carrito")
    void testVaciarCarrito() throws Exception {

        carrito.addItem(new ItemCarrito(listaProductos.get(0), 1));
        carrito.addItem(new ItemCarrito(listaProductos.get(1), 2));

        carrito.vaciarCarrito();

        assertEquals(0, carrito.getCarrito().size());
    }

    // =========================================================
    // TC-11 → Historial registra operaciones
    // =========================================================
    @Test
    @DisplayName("TC-11 Historial registra operaciones")
    void testHistorialRegistraOperaciones() {

        carrito.vaciarCarrito();

        assertEquals(1, carrito.getHistorial().size());

        assertEquals(
            "Vaciar carrito",
            carrito.getHistorial().get(0)
        );
    }
}