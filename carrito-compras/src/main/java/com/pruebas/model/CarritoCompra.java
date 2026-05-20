package com.pruebas.model;
import java.util.ArrayList;
import java.util.List;

import com.pruebas.service.ServicioPrecio;

public class CarritoCompra {
    ArrayList <ItemCarrito> carrito;
    double total;

    // Para el servicio
    private ServicioPrecio servicioPrecio;
    private List<String> historial;

    public CarritoCompra() {
        this.carrito = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    // Constructor con el servicio
    public CarritoCompra(ServicioPrecio servicioPrecio) {
        this.carrito = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.servicioPrecio = servicioPrecio;  
    }

    public List<ItemCarrito> getCarrito() {
        return carrito;
    }
    
    public void addItem(ItemCarrito item) throws Exception{
        for (ItemCarrito e : carrito){
            if (e.getProducto().getId().equals(item.getProducto().getId())){
                throw new Exception("El producto ya existe en el carrito");
            }
        }

        this.carrito.add(item);
    }

    public void removeItem(ItemCarrito item) throws Exception{
        int index = carrito.indexOf(item);

        if (index == -1){
            throw new Exception ("El producto seleccionado no existe");
        }

        this.carrito.remove(index);
    }

    public void addOneProduct(Producto producto) throws Exception {
        int index = 0;

        for (ItemCarrito e : this.carrito){
            if (e.getProducto().getId().equals(producto.getId())){
                break;
            }
            index++;
        }

        if (index >= carrito.size()){
            throw new Exception ("El producto seleccionado no existe");
        }

        if (!producto.isDisponible()){
            throw new Exception ("No hay stock disponible");
        }

        ItemCarrito aux = carrito.get(index);

        aux.addOneProduct();
        producto.removeOneProduct();

        carrito.set(index, aux);
    }

    public void removeOneProduct(Producto producto) throws Exception {
        int index = 0;

        for (ItemCarrito e : this.carrito){
            if (e.getProducto().getId().equals(producto.getId())){
                break;
            }
            index++;
        }

        if (index >= carrito.size()){
            throw new Exception ("El producto seleccionado no existe");
        }

        ItemCarrito aux = carrito.get(index);

        aux.removeOneProduct();
        producto.addOneProduct();

        carrito.set(index, aux);
        
        if (aux.getCantidad() == 0){
            removeItem(aux);
        }
    }

    public void vaciarCarrito(){
        this.carrito.clear();
        this.total = 0;
        historial.add("Vaciar carrito");
    }

    public double calcularTotal(){
        if (servicioPrecio == null) {
           throw new IllegalStateException( "ServicioPrecio no configurado"); 
        }

        double subtotal = 0;
        for (ItemCarrito e: carrito) {
            subtotal += e.getSubtotal();
        }
        double descuento = servicioPrecio.calcularDescuento(subtotal);
        double baseConDescuento = subtotal - descuento;
        double impuesto   = servicioPrecio.calcularImpuesto(baseConDescuento);

        this.total = baseConDescuento + impuesto;

        historial.add(String.format(
            "CALCULAR_TOTAL: subtotal=%.2f descuento=%.2f impuesto=%.2f total=%.2f",
            subtotal, descuento, impuesto, this.total));
        
        return this.total;
    }

    public String obtenerResumenCompra() {
        if (servicioPrecio == null) {
            throw new IllegalStateException(
                "ServicioPrecio no configurado. Use el constructor con ServicioPrecio.");
        }
 
        double subtotal = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("RESUMEN DE COMPRA\n");
 
        for (ItemCarrito e : carrito) {
            sb.append(String.format("  %-20s x%d  S/ %.2f%n",
                e.getProducto().getNombre(),
                e.getCantidad(),
                e.getSubtotal()));
            subtotal += e.getSubtotal();
        }
 
        double descuento        = servicioPrecio.calcularDescuento(subtotal);
        double baseConDescuento = subtotal - descuento;
        double impuesto         = servicioPrecio.calcularImpuesto(baseConDescuento);
        double totalFinal       = baseConDescuento + impuesto;
 
        sb.append(String.format("  Subtotal  : S/ %.2f%n", subtotal));
        sb.append(String.format("  Descuento : S/ %.2f%n", descuento));
        sb.append(String.format("  IGV (18%%) : S/ %.2f%n", impuesto));
        sb.append(String.format("  TOTAL     : S/ %.2f%n", totalFinal));
        sb.append("\n");
 
        this.total = totalFinal;
        return sb.toString();
    }

    public List<String> getHistorial() {
        return historial;
    }

    public void resumenCompra(){
        this.total = 0;

        for (ItemCarrito e : carrito){
            e.printItem();
            this.total += e.getSubtotal();
        }

        System.out.println("______________________\nTotal: " + this.total);
    }
}