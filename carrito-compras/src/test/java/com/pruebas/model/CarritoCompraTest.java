package com.pruebas.model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    @DisplayName("TC-01 Agregar un item nuevo al carrito")
    void testAgregarNuevoItem() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 2);

        carrito.addItem(item);

        assertEquals(1, carrito.getCarrito().size());
        assertEquals(item, carrito.getCarrito().get(0));
    }

    @Test
    @DisplayName("TC-02 Agregar producto duplicado")
    void testAgregarProductoDuplicado() throws Exception {

        ItemCarrito item1 = new ItemCarrito(listaProductos.get(0), 1);
        ItemCarrito item2 = new ItemCarrito(listaProductos.get(0), 1);

        carrito.addItem(item1);

        Exception ex = assertThrows(
            Exception.class,
            () -> carrito.addItem(item2)
        );

        assertEquals(
            "El producto ya existe en el carrito",
            ex.getMessage()
        );
    }

    @Test
    @DisplayName("TC-03 Eliminar item existente")
    void testEliminarItemExistente() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(1), 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertEquals(0, carrito.getCarrito().size());
    }

    @Test
    @DisplayName("TC-04 Eliminar item inexistente")
    void testEliminarItemInexistente() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(1), 1);

        Exception ex = assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );

        assertEquals(
            "El producto seleccionado no existe",
            ex.getMessage()
        );
    }

    @Test
    @DisplayName("TC-05 Crear item con cantidad inválida")
    void testCantidadInvalida() {

        Exception ex1 = assertThrows(
            Exception.class,
            () -> new ItemCarrito(listaProductos.get(0), 0)
        );

        assertEquals(
            "La cantidad debe ser mayor a 0",
            ex1.getMessage()
        );

        Exception ex2 = assertThrows(
            Exception.class,
            () -> new ItemCarrito(listaProductos.get(0), -5)
        );

        assertEquals(
            "La cantidad debe ser mayor a 0",
            ex2.getMessage()
        );
    }

    @Test
    @DisplayName("TC-06 Agregar unidad extra")
    void testAgregarUnidadExtra() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        carrito.addItem(item);

        int stockInicial = producto.getDisponibilidad();

        carrito.addOneProduct(producto);

        assertEquals(2, carrito.getCarrito().get(0).getCantidad());

        assertEquals(
            stockInicial - 1,
            producto.getDisponibilidad()
        );
    }

    @Test
    @DisplayName("TC-07 Agregar unidad sin stock")
    void testAgregarUnidadSinStock() throws Exception {

        Producto producto = listaProductos.get(3);

        carrito.addItem(new ItemCarrito(producto, 1));

        Exception ex = assertThrows(
            Exception.class,
            () -> carrito.addOneProduct(producto)
        );

        assertEquals(
            "No hay stock disponible",
            ex.getMessage()
        );
    }

    @Test
    @DisplayName("TC-08 Quitar unidad reduce cantidad")
    void testQuitarUnidad() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 3);

        carrito.addItem(item);

        int stockInicial = producto.getDisponibilidad();

        carrito.removeOneProduct(producto);

        assertEquals(2, carrito.getCarrito().get(0).getCantidad());

        assertEquals(
            stockInicial + 1,
            producto.getDisponibilidad()
        );
    }

    @Test
    @DisplayName("TC-09 Quitar última unidad elimina item")
    void testQuitarUltimaUnidad() throws Exception {

        ItemCarrito item = new ItemCarrito(listaProductos.get(0), 1);

        carrito.addItem(item);

        carrito.removeOneProduct(listaProductos.get(0));

        assertEquals(0, carrito.getCarrito().size());
    }

    @Test
    @DisplayName("TC-10 Vaciar carrito")
    void testVaciarCarrito() throws Exception {

        carrito.addItem(new ItemCarrito(listaProductos.get(0), 1));
        carrito.addItem(new ItemCarrito(listaProductos.get(1), 2));

        carrito.vaciarCarrito();

        assertEquals(0, carrito.getCarrito().size());
    }

    @Test
    @DisplayName("TC-11A Historial registra ADD_ITEM")
    void testHistorialAddItem() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertEquals(1, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(0)
                .contains("ADD_ITEM")
        );

        assertTrue(
            carrito.getHistorial().get(0)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-11B Historial registra ADD_ONE_PRODUCT")
    void testHistorialAddOneProduct() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        carrito.addOneProduct(producto);

        assertEquals(2, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("ADD_ONE_PRODUCT")
        );

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }
    
    @Test
    @DisplayName("TC-11C Historial registra REMOVE_ONE_PRODUCT")
    void testHistorialRemoveOneProduct() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 2)
        );

        carrito.removeOneProduct(producto);

        assertEquals(2, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("REMOVE_ONE_PRODUCT")
        );

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-11D Historial registra REMOVE_ITEM")
    void testHistorialRemoveItem() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertEquals(2, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("REMOVE_ITEM")
        );

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-11E Historial registra CLEAR_CART")
    void testHistorialClearCart() throws Exception {

        carrito.addItem(
            new ItemCarrito(listaProductos.get(0), 1)
        );

        carrito.vaciarCarrito();

        assertEquals(2, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("CLEAR_CART")
        );
    }

    @Test
    @DisplayName("TC-11F Historial registra ERROR_ADD_ITEM")
    void testHistorialErrorAddItem() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertThrows(
            Exception.class,
            () -> carrito.addItem(
                new ItemCarrito(producto, 1)
            )
        );

        assertEquals(2, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("ERROR_ADD_ITEM")
        );

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-11G Historial registra ERROR_REMOVE_ITEM")
    void testHistorialErrorRemoveItem() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );

        assertEquals(1, carrito.getHistorial().size());

        assertTrue(
            carrito.getHistorial().get(0)
                .contains("ERROR_REMOVE_ITEM")
        );
    }
}