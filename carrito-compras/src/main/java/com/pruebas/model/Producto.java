package com.pruebas.model;

public class Producto {
    private static int contador = 1;

    String id;
    String nombre;
    double precio;
    int disponibilidad;

    public Producto(String nombre, double precio, int disponibilidad) throws Exception {
        if (verifyProducto(precio, disponibilidad)){
            this.id = generateId();
            this.nombre = nombre;
            this.precio = precio;
            this.disponibilidad = disponibilidad;
        }
    }

    private String generateId(){
        return "PROD-" + contador++;
    }

    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public double getPrecio() {
        return this.precio;
    }

    public int getDisponibilidad() {
        return this.disponibilidad;
    }

    public boolean isDisponible() {
        return this.disponibilidad > 0;
    }

    public void removeOneProduct() throws Exception{
        if (this.disponibilidad == 0){
            throw new Exception("No hay stock disponible");
        }

        this.disponibilidad--;
    }

    public void addOneProduct() {
        this.disponibilidad++;
    }

    private boolean verifyProducto(double precio, int cantidad) throws Exception{
        if (precio < 0){
            throw new Exception ("El producto no puede tener precio negativo");
        }
        if (cantidad < 0){
            throw new Exception ("El producto no puede tener cantidad negativa");
        }
        return true;
    }
}