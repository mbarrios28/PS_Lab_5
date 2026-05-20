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
    @DisplayName("TC-01A Agregar item incrementa size")
    void testAgregarNuevoItemSize() throws Exception {

        carrito.addItem(
            new ItemCarrito(listaProductos.get(0), 2)
        );

        assertEquals(
            1,
            carrito.getCarrito().size()
        );
    }

    @Test
    @DisplayName("TC-01B Agregar item almacena item correcto")
    void testAgregarNuevoItemContenido() throws Exception {

        ItemCarrito item =
            new ItemCarrito(listaProductos.get(0), 2);

        carrito.addItem(item);

        assertEquals(
            item,
            carrito.getCarrito().get(0)
        );
    }

    @Test
    @DisplayName("TC-02A Producto duplicado lanza exception")
    void testAgregarProductoDuplicadoException() throws Exception {

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
    }

    @Test
    @DisplayName("TC-02B Producto duplicado lanza mensaje correcto")
    void testAgregarProductoDuplicadoMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        Exception ex = assertThrows(
            Exception.class,
            () -> carrito.addItem(
                new ItemCarrito(producto, 1)
            )
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
    @DisplayName("TC-04A Eliminar item inexistente lanza exception")
    void testEliminarItemInexistenteException() throws Exception {

        ItemCarrito item =
            new ItemCarrito(listaProductos.get(1), 1);

        assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );
    }

    @Test
    @DisplayName("TC-04B Eliminar item inexistente lanza mensaje correcto")
    void testEliminarItemInexistenteMensaje() throws Exception {

        ItemCarrito item =
            new ItemCarrito(listaProductos.get(1), 1);

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
    @DisplayName("TC-05A Cantidad 0 lanza exception")
    void testCantidadCeroException() {

        assertThrows(
            Exception.class,
            () -> new ItemCarrito(
                listaProductos.get(0),
                0
            )
        );
    }

    @Test
    @DisplayName("TC-05B Cantidad 0 lanza mensaje correcto")
    void testCantidadCeroMensaje() {

        Exception ex = assertThrows(
            Exception.class,
            () -> new ItemCarrito(
                listaProductos.get(0),
                0
            )
        );

        assertEquals(
            "La cantidad debe ser mayor a 0",
            ex.getMessage()
        );
    }

    @Test
    @DisplayName("TC-05C Cantidad negativa lanza exception")
    void testCantidadNegativaException() {

        assertThrows(
            Exception.class,
            () -> new ItemCarrito(
                listaProductos.get(0),
                -5
            )
        );
    }

    @Test
    @DisplayName("TC-05D Cantidad negativa lanza mensaje correcto")
    void testCantidadNegativaMensaje() {

        Exception ex = assertThrows(
            Exception.class,
            () -> new ItemCarrito(
                listaProductos.get(0),
                -5
            )
        );

        assertEquals(
            "La cantidad debe ser mayor a 0",
            ex.getMessage()
        );
    }

    @Test
    @DisplayName("TC-06A Agregar unidad incrementa cantidad")
    void testAgregarUnidadExtraCantidad() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        carrito.addOneProduct(producto);

        assertEquals(
            2,
            carrito.getCarrito().get(0).getCantidad()
        );
    }

    @Test
    @DisplayName("TC-06B Agregar unidad reduce stock")
    void testAgregarUnidadExtraStock() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        int stockInicial =
            producto.getDisponibilidad();

        carrito.addOneProduct(producto);

        assertEquals(
            stockInicial - 1,
            producto.getDisponibilidad()
        );
    }

    @Test
    @DisplayName("TC-07A Agregar unidad sin stock lanza exception")
    void testAgregarUnidadSinStockException() throws Exception {

        Producto producto = listaProductos.get(3);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertThrows(
            Exception.class,
            () -> carrito.addOneProduct(producto)
        );
    }

    @Test
    @DisplayName("TC-07B Agregar unidad sin stock lanza mensaje correcto")
    void testAgregarUnidadSinStockMensaje() throws Exception {

        Producto producto = listaProductos.get(3);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

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
    @DisplayName("TC-08A Quitar unidad reduce cantidad")
    void testQuitarUnidadCantidad() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 3)
        );

        carrito.removeOneProduct(producto);

        assertEquals(
            2,
            carrito.getCarrito().get(0).getCantidad()
        );
    }

    @Test
    @DisplayName("TC-08B Quitar unidad aumenta stock")
    void testQuitarUnidadStock() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 3)
        );

        int stockInicial =
            producto.getDisponibilidad();

        carrito.removeOneProduct(producto);

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
    @DisplayName("TC-11A Historial registra ADD_ITEM verificar size")
    void testHistorialAddItemSize() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertEquals(1, carrito.getHistorial().size());
    }

    @Test
    @DisplayName("TC-11B Historial registra ADD_ITEM verificar mensaje")
    void testHistorialAddItemMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertTrue(
            carrito.getHistorial().get(0)
                .contains("ADD_ITEM")
        );
    }

    @Test
    @DisplayName("TC-11C Historial registra ADD_ITEM verificar producto")
    void testHistorialAddItemProducto() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        assertTrue(
            carrito.getHistorial().get(0)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-12A Historial registra ADD_ONE_PRODUCT verificar size")
    void testHistorialAddOneProductSize() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        carrito.addOneProduct(producto);

        assertEquals(2, carrito.getHistorial().size());
    }

    @Test
    @DisplayName("TC-12B Historial registra ADD_ONE_PRODUCT verificar mensaje")
    void testHistorialAddOneProductMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        carrito.addOneProduct(producto);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("ADD_ONE_PRODUCT")
        );
    }

    @Test
    @DisplayName("TC-12C Historial registra ADD_ONE_PRODUCT verificar producto")
    void testHistorialAddOneProductProducto() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 1)
        );

        carrito.addOneProduct(producto);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }
    
    @Test
    @DisplayName("TC-13A Historial registra REMOVE_ONE_PRODUCT verificar size")
    void testHistorialRemoveOneProductSize() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 2)
        );

        carrito.removeOneProduct(producto);

        assertEquals(2, carrito.getHistorial().size());
    }

    @Test
    @DisplayName("TC-13B Historial registra REMOVE_ONE_PRODUCT verificar mensaje")
    void testHistorialRemoveOneProductMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 2)
        );

        carrito.removeOneProduct(producto);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("REMOVE_ONE_PRODUCT")
        );
    }


    @Test
    @DisplayName("TC-13C Historial registra REMOVE_ONE_PRODUCT verificar producto")
    void testHistorialRemoveOneProductProducto() throws Exception {

        Producto producto = listaProductos.get(0);

        carrito.addItem(
            new ItemCarrito(producto, 2)
        );

        carrito.removeOneProduct(producto);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-14A Historial registra REMOVE_ITEM verificar size")
    void testHistorialRemoveItemSize() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertEquals(2, carrito.getHistorial().size());
    }

    @Test
    @DisplayName("TC-14B Historial registra REMOVE_ITEM verificar mensaje")
    void testHistorialRemoveItemMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("REMOVE_ITEM")
        );
    }

    @Test
    @DisplayName("TC-14C Historial registra REMOVE_ITEM verificar producto")
    void testHistorialRemoveItemProducto() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        carrito.addItem(item);

        carrito.removeItem(item);

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-15A Historial registra CLEAR_CART verificar size")
    void testHistorialClearCartSize() throws Exception {

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
    @DisplayName("TC-15B Historial registra CLEAR_CART verificar mensaje")
    void testHistorialClearCartMensaje() throws Exception {

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
    @DisplayName("TC-16A Historial registra ERROR_ADD_ITEM verificar Exception")
    void testHistorialErrorAddItemException() throws Exception {

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
    }

    @Test
    @DisplayName("TC-16B Historial registra ERROR_ADD_ITEM verificar size")
    void testHistorialErrorAddItemSize() throws Exception {

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
    }

    @Test
    @DisplayName("TC-16C Historial registra ERROR_ADD_ITEM verificar mensaje")
    void testHistorialErrorAddItemMensaje() throws Exception {

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

        assertTrue(
            carrito.getHistorial().get(1)
                .contains("ERROR_ADD_ITEM")
        );
    }

    @Test
    @DisplayName("TC-16D Historial registra ERROR_ADD_ITEM verificar producto")
    void testHistorialErrorAddItemProducto() throws Exception {

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

        assertTrue(
            carrito.getHistorial().get(1)
                .contains(producto.getNombre())
        );
    }

    @Test
    @DisplayName("TC-17A Historial registra ERROR_REMOVE_ITEM verificar Exception")
    void testHistorialErrorRemoveItemException() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );
    }

    @Test
    @DisplayName("TC-17B Historial registra ERROR_REMOVE_ITEM verificar size")
    void testHistorialErrorRemoveItemSize() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );

        assertEquals(1, carrito.getHistorial().size());
    }

    @Test
    @DisplayName("TC-17C Historial registra ERROR_REMOVE_ITEM verificar mensaje")
    void testHistorialErrorRemoveItemMensaje() throws Exception {

        Producto producto = listaProductos.get(0);

        ItemCarrito item = new ItemCarrito(producto, 1);

        assertThrows(
            Exception.class,
            () -> carrito.removeItem(item)
        );

        assertTrue(
            carrito.getHistorial().get(0)
                .contains("ERROR_REMOVE_ITEM")
        );
    }
}