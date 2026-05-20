package com.pruebas.model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.pruebas.service.ServicioPrecio;
import com.pruebas.service.ServicioPrecioImpl;

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

    @Nested
    @DisplayName("Integración ServicioPrecio + Mockito")
    class IntegracionServicioPrecio {

        private ServicioPrecio mockServicio;
        private CarritoCompra carritoServicio;

        @BeforeEach
        void setUpServicio() {

            mockServicio = mock(ServicioPrecio.class);

            carritoServicio = new CarritoCompra(mockServicio);
        }

        // =====================================================
        // TC-18: calcularTotal llama descuento e impuesto
        // =====================================================

        @Test
        @DisplayName("TC-18 calcularTotal llama descuento e impuesto")
        void testCalcularTotalLlamaMetodosServicio() throws Exception {

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(100.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenReturn(162.0);

            carritoServicio.addItem(
                new ItemCarrito(listaProductos.get(0), 4)
            );

            carritoServicio.calcularTotal();

            verify(mockServicio, times(1))
                    .calcularDescuento(999.6);

            verify(mockServicio, times(1))
                    .calcularImpuesto(899.6);
        }

        // =====================================================
        // TC-19: calcularTotal correcto
        // =====================================================

        @Test
        @DisplayName("TC-19 calcularTotal correcto")
        void testCalcularTotalCorrecto() throws Exception {

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(100.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenReturn(162.0);

            carritoServicio.addItem(
                new ItemCarrito(listaProductos.get(0), 4)
            );

            double total = carritoServicio.calcularTotal();

            assertEquals(1061.6, total, 0.01);
        }

        // =====================================================
        // TC-20: carrito vacío retorna 0
        // =====================================================

        @Test
        @DisplayName("TC-20 carrito vacío retorna 0")
        void testCarritoVacio() {

            when(mockServicio.calcularDescuento(0.0))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(0.0))
                    .thenReturn(0.0);

            double total = carritoServicio.calcularTotal();

            assertEquals(0.0, total);
        }

        // =====================================================
        // TC-21: resumen contiene producto y total
        // =====================================================

        @Test
        @DisplayName("TC-21 resumen contiene producto y total")
        void testResumenCompra() throws Exception {

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenReturn(179.98);

            carritoServicio.addItem(
                new ItemCarrito(listaProductos.get(0), 2)
            );

            String resumen =
                    carritoServicio.obtenerResumenCompra();

            assertTrue(resumen.contains("Escritorio"));

            assertTrue(resumen.contains("TOTAL"));
        }

        // =====================================================
        // TC-22: sin ServicioPrecio lanza excepción
        // =====================================================

        @Test
        @DisplayName("TC-22 sin ServicioPrecio")
        void testSinServicioPrecio() throws Exception {

            CarritoCompra carritoNormal =
                    new CarritoCompra();

            carritoNormal.addItem(
                new ItemCarrito(listaProductos.get(0), 1)
            );

            assertThrows(
                IllegalStateException.class,
                carritoNormal::calcularTotal
            );
        }

        // =====================================================
        // TC-23: verify carrito vacío
        // =====================================================

        @Test
        @DisplayName("TC-23 verify carrito vacío")
        void testVerifyCarritoVacio() {

            when(mockServicio.calcularDescuento(0.0))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(0.0))
                    .thenReturn(0.0);

            carritoServicio.calcularTotal();

            verify(mockServicio)
                    .calcularDescuento(0.0);

            verify(mockServicio)
                    .calcularImpuesto(0.0);

            verifyNoMoreInteractions(mockServicio);
        }
    }

    @Nested
    @DisplayName("Parametrizadas")
    class Parametrizadas {

        // =====================================================
        // TC-24: descuento parametrizado
        // =====================================================
        @ParameterizedTest
        @CsvSource({
            "0.0,0.0",
            "100.0,0.0",
            "200.0,0.0",
            "201.0,20.1",
            "500.0,50.0",
            "1000.0,100.0"
        })
        @DisplayName("TC-24 descuento parametrizado")
        void testDescuentoParametrizado(
                double subtotal,
                double esperado
        ) {

            ServicioPrecio servicio =
                    new ServicioPrecioImpl();

            double descuento =
                    servicio.calcularDescuento(subtotal);

            assertEquals(
                    esperado,
                    descuento,
                    0.001
            );
        }

        // =====================================================
        // TC-25: impuesto parametrizado
        // =====================================================

        @ParameterizedTest
        @CsvSource({
            "0.0,0.0",
            "100.0,18.0",
            "500.0,90.0",
            "1000.0,180.0"
        })
        @DisplayName("TC-25 impuesto parametrizado")
        void testImpuestoParametrizado(
                double subtotal,
                double esperado
        ) {

            ServicioPrecio servicio =
                    new ServicioPrecioImpl();

            double impuesto =
                    servicio.calcularImpuesto(subtotal);

            assertEquals(
                    esperado,
                    impuesto,
                    0.001
            );
        }

        // =====================================================
        // TC-26: distintos precios
        // =====================================================

        @ParameterizedTest
        @ValueSource(doubles = {
            10.0,
            100.0,
            500.0,
            1500.0,
            5000.0
        })
        @DisplayName("TC-26 distintos precios")
        void testDistintosPrecios(
                double precio
        ) throws Exception {

            ServicioPrecio mockServicio =
                    mock(ServicioPrecio.class);

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenReturn(precio * 0.18);

            CarritoCompra carrito =
                    new CarritoCompra(mockServicio);

            Producto producto =
                    new Producto(
                        "Producto Test",
                        precio,
                        10
                    );

            carrito.addItem(
                    new ItemCarrito(producto, 1)
            );

            assertDoesNotThrow(
                    carrito::calcularTotal
            );
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        // =====================================================
        // TC-27: carrito con un producto
        // =====================================================

        @Test
        @DisplayName("TC-27 carrito con un producto")
        void testCarritoUnProducto() throws Exception {

            ServicioPrecio mockServicio =
                    mock(ServicioPrecio.class);

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenReturn(9.0);

            CarritoCompra carrito =
                    new CarritoCompra(mockServicio);

            Producto producto =
                    new Producto(
                        "Cuaderno",
                        50.0,
                        10
                    );

            carrito.addItem(
                    new ItemCarrito(producto, 1)
            );

            double total =
                    carrito.calcularTotal();

            assertEquals(59.0, total);
        }

        // =====================================================
        // TC-28: carrito con 100 productos
        // =====================================================

        @Test
        @DisplayName("TC-28 carrito con 100 productos")
        void testCarrito100Productos() throws Exception {

            ServicioPrecio mockServicio =
                    mock(ServicioPrecio.class);

            when(mockServicio.calcularDescuento(anyDouble()))
                    .thenReturn(0.0);

            when(mockServicio.calcularImpuesto(anyDouble()))
                    .thenAnswer(
                        inv -> (double)
                        inv.getArgument(0) * 0.18
                    );

            CarritoCompra carrito =
                    new CarritoCompra(mockServicio);

            double subtotal = 0;

            for (int i = 0; i < 100; i++) {

                Producto producto =
                        new Producto(
                            "Producto " + i,
                            10.0,
                            5
                        );

                carrito.addItem(
                        new ItemCarrito(producto, 1)
                );

                subtotal += 10.0;
            }

            double totalEsperado =
                    subtotal * 1.18;

            double total =
                    carrito.calcularTotal();

            assertEquals(
                    totalEsperado,
                    total,
                    0.01
            );

            assertEquals(
                    100,
                    carrito.getCarrito().size()
            );
        }

        // =====================================================
        // TC-29: subtotal negativo
        // =====================================================

        @Test
        @DisplayName("TC-29 subtotal negativo")
        void testSubtotalNegativo() {

            ServicioPrecio servicio =
                    new ServicioPrecioImpl();

            assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularDescuento(-1.0)
            );

            assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularImpuesto(-1.0)
            );
        }
      }
}
