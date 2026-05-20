package com.pruebas.model;

public class ItemCarrito {
    Producto producto;
    int cantidad;
    double subtotal;

    public ItemCarrito(Producto producto, int cantidad) throws Exception {
        if (cantidad <= 0){
            throw new Exception("La cantidad debe ser mayor a 0");
        }

        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calculateSubtotal();
    }

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public double getSubtotal(){
        return this.subtotal;
    }

    public void addOneProduct(){
        this.cantidad++;
        this.subtotal = calculateSubtotal();
    }

    public void removeOneProduct() throws Exception{
        if (this.cantidad == 0){
            throw new Exception("No hay productos para eliminar");
        }

        this.cantidad--;
        this.subtotal = calculateSubtotal();
    }

    private double calculateSubtotal(){
        return this.producto.getPrecio() * this.cantidad;
    }

    public void printItem(){
        System.out.println(
            "Producto: " + producto.getNombre() +
            " | Cantidad: " + cantidad +
            " | Subtotal: " + subtotal
        );
    }
}
