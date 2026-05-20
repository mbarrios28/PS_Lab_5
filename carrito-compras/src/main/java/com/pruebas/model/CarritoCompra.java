package com.pruebas.model;

import java.util.ArrayList;

public class CarritoCompra {
    ArrayList <ItemCarrito> carrito;
    double total;

    public CarritoCompra() {
        this.carrito = new ArrayList<>();
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

    public void resumenCompra(){
        this.total = 0;

        for (ItemCarrito e : carrito){
            e.printItem();
            this.total += e.getSubtotal();
        }

        System.out.println("______________________\nTotal: " + this.total);
    }
}