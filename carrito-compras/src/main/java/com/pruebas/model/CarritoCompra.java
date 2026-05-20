package com.pruebas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pruebas.service.ServicioPrecio;

public class CarritoCompra {

    private ArrayList<ItemCarrito> carrito;
    private double total;

    private ServicioPrecio servicioPrecio;
    private List<String> historial;

    public CarritoCompra() {
        this.carrito = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    public CarritoCompra(ServicioPrecio servicioPrecio) {
        this.carrito = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.servicioPrecio = servicioPrecio;
    }

    public List<ItemCarrito> getCarrito() {
        return Collections.unmodifiableList(carrito);
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    private void registrarOperacion(String operacion) {
        historial.add(
            "[" + LocalDateTime.now() + "] " + operacion
        );
    }

    public void addItem(ItemCarrito item) throws Exception {

        for (ItemCarrito e : carrito) {

            if (e.getProducto().getId().equals(item.getProducto().getId())) {

                registrarOperacion(
                    "ERROR_ADD_ITEM -> Producto duplicado: "
                    + item.getProducto().getNombre()
                );

                throw new Exception("El producto ya existe en el carrito");
            }
        }

        carrito.add(item);

        registrarOperacion(
            String.format(
                "ADD_ITEM -> %s | cantidad=%d | subtotal=%.2f",
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.getSubtotal()
            )
        );
    }

    public void removeItem(ItemCarrito item) throws Exception {

        int index = carrito.indexOf(item);

        if (index == -1) {

            registrarOperacion(
                "ERROR_REMOVE_ITEM -> Producto inexistente"
            );

            throw new Exception("El producto seleccionado no existe");
        }

        carrito.remove(index);

        registrarOperacion(
            "REMOVE_ITEM -> " + item.getProducto().getNombre()
        );
    }

    public void addOneProduct(Producto producto) throws Exception {

        int index = 0;

        for (ItemCarrito e : carrito) {

            if (e.getProducto().getId().equals(producto.getId())) {
                break;
            }

            index++;
        }

        if (index >= carrito.size()) {

            registrarOperacion(
                "ERROR_ADD_ONE_PRODUCT -> Producto inexistente"
            );

            throw new Exception("El producto seleccionado no existe");
        }

        if (!producto.isDisponible()) {

            registrarOperacion(
                "ERROR_ADD_ONE_PRODUCT -> Sin stock: "
                + producto.getNombre()
            );

            throw new Exception("No hay stock disponible");
        }

        ItemCarrito aux = carrito.get(index);

        aux.addOneProduct();
        producto.removeOneProduct();

        carrito.set(index, aux);

        registrarOperacion(
            "ADD_ONE_PRODUCT -> "
            + producto.getNombre()
            + " | nuevaCantidad=" + aux.getCantidad()
        );
    }

    public void removeOneProduct(Producto producto) throws Exception {

        int index = 0;

        for (ItemCarrito e : carrito) {

            if (e.getProducto().getId().equals(producto.getId())) {
                break;
            }

            index++;
        }

        if (index >= carrito.size()) {

            registrarOperacion(
                "ERROR_REMOVE_ONE_PRODUCT -> Producto inexistente"
            );

            throw new Exception("El producto seleccionado no existe");
        }

        ItemCarrito aux = carrito.get(index);

        aux.removeOneProduct();
        producto.addOneProduct();

        carrito.set(index, aux);

        registrarOperacion(
            "REMOVE_ONE_PRODUCT -> "
            + producto.getNombre()
            + " | nuevaCantidad=" + aux.getCantidad()
        );

        if (aux.getCantidad() == 0) {
            removeItem(aux);
        }
    }

    public void vaciarCarrito() {

        int cantidadItems = carrito.size();

        carrito.clear();
        total = 0;

        registrarOperacion(
            "CLEAR_CART -> itemsEliminados=" + cantidadItems
        );
    }

    public double calcularTotal() {

        if (servicioPrecio == null) {

            registrarOperacion(
                "ERROR_CALCULAR_TOTAL -> ServicioPrecio no configurado"
            );

            throw new IllegalStateException(
                "ServicioPrecio no configurado"
            );
        }

        double subtotal = 0;

        for (ItemCarrito e : carrito) {
            subtotal += e.getSubtotal();
        }

        double descuento = servicioPrecio.calcularDescuento(subtotal);
        double baseConDescuento = subtotal - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(baseConDescuento);

        total = baseConDescuento + impuesto;

        registrarOperacion(
            String.format(
                "CALCULAR_TOTAL -> subtotal=%.2f descuento=%.2f impuesto=%.2f total=%.2f",
                subtotal,
                descuento,
                impuesto,
                total
            )
        );

        return total;
    }

    public String obtenerResumenCompra() {

        if (servicioPrecio == null) {

            registrarOperacion(
                "ERROR_RESUMEN_COMPRA -> ServicioPrecio no configurado"
            );

            throw new IllegalStateException(
                "ServicioPrecio no configurado. Use el constructor con ServicioPrecio."
            );
        }

        double subtotal = 0;

        StringBuilder sb = new StringBuilder();

        sb.append("RESUMEN DE COMPRA\n");

        for (ItemCarrito e : carrito) {

            sb.append(
                String.format(
                    "  %-20s x%d  S/ %.2f%n",
                    e.getProducto().getNombre(),
                    e.getCantidad(),
                    e.getSubtotal()
                )
            );

            subtotal += e.getSubtotal();
        }

        double descuento = servicioPrecio.calcularDescuento(subtotal);
        double baseConDescuento = subtotal - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(baseConDescuento);
        double totalFinal = baseConDescuento + impuesto;

        sb.append(
            String.format(
                "  Subtotal  : S/ %.2f%n",
                subtotal
            )
        );

        sb.append(
            String.format(
                "  Descuento : S/ %.2f%n",
                descuento
            )
        );

        sb.append(
            String.format(
                "  IGV (18%%) : S/ %.2f%n",
                impuesto
            )
        );

        sb.append(
            String.format(
                "  TOTAL     : S/ %.2f%n",
                totalFinal
            )
        );

        sb.append("\n");

        total = totalFinal;

        registrarOperacion(
            String.format(
                "GENERAR_RESUMEN -> total=%.2f",
                totalFinal
            )
        );

        return sb.toString();
    }

    public void resumenCompra() {

        total = 0;

        for (ItemCarrito e : carrito) {

            e.printItem();

            total += e.getSubtotal();
        }

        System.out.println(
            "______________________\nTotal: " + total
        );

        registrarOperacion(
            String.format(
                "RESUMEN_COMPRA -> total=%.2f",
                total
            )
        );
    }
}