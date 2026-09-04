package org.laboratorio1.service;

import org.laboratorio1.model.Producto;
import java.util.ArrayList;

public class ServicioInventario {
    private static final double IVA = 0.13;
    private ArrayList<Producto> productos;

    public ServicioInventario() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public boolean venderProducto(String sku, int cantidad) {
        for (Producto producto : productos) {
            if (producto.getSku().equals(sku)) {
                if (producto.getCantidadStock() >= cantidad) {
                    producto.setCantidadStock(producto.getCantidadStock() - cantidad);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public double calcularValorTotalInventario() {
        double subtotal = 0.0;
        for (Producto producto : productos) {
            subtotal += producto.getPrecio() * producto.getCantidadStock();
        }
        return subtotal + (subtotal * IVA);
    }
}
