package org.laboratorio1.model;

public class Producto {
    private String sku;
    private String nombre;
    private double precio;
    private int cantidadStock;

    public Producto(String sku, String nombre, double precio, int cantidadStock) {
        this.sku = sku;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidadStock = cantidadStock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }
}
