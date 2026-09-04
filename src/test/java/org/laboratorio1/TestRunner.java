package org.laboratorio1;

import org.laboratorio1.model.Producto;
import org.laboratorio1.service.ServicioInventario;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TestRunner {
    public static void main(String[] args) {
        boolean allTestsPassed = true;

        // Prueba 1: Verificar la clase Producto (constructor, getters, setters básicos)
        try {
            Producto p = new Producto("SKU01", "Laptop", 1200.50, 10);
            p.setCantidadStock(15);
            if (!p.getSku().equals("SKU01") || !p.getNombre().equals("Laptop") || p.getPrecio() != 1200.50 || p.getCantidadStock() != 15) {
                System.out.println("❌ TEST 1 FALLIDO: La clase Producto (constructor, getters o setters) no funciona como se esperaba.");
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 1 APROBADO: La clase Producto funciona correctamente (Constructor y getters).");
            }
        } catch (Exception e) {
            System.out.println("❌ TEST 1 FALLIDO: Error crítico al usar la clase Producto. " + e.getMessage());
            allTestsPassed = false;
        }

        // Prueba 1.5: Verificar el resto de los Setters requeridos
        try {
            Producto p = new Producto("TEMP", "Temp", 1.0, 1);
            p.setSku("NUEVO-SKU");
            p.setNombre("Nuevo Nombre");
            p.setPrecio(99.99);
            
            if (!p.getSku().equals("NUEVO-SKU") || !p.getNombre().equals("Nuevo Nombre") || p.getPrecio() != 99.99) {
                System.out.println("❌ TEST 1.5 FALLIDO: Los setters (setSku, setNombre, setPrecio) no actualizan los valores correctamente.");
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 1.5 APROBADO: Todos los métodos setters de Producto existen y funcionan.");
            }
        } catch (Exception e) {
            System.out.println("❌ TEST 1.5 FALLIDO: Faltan setters en Producto (setSku, setNombre o setPrecio). " + e.getMessage());
            allTestsPassed = false;
        }

        // Prueba 2: Verificar constante IVA en ServicioInventario
        try {
            Field field = ServicioInventario.class.getDeclaredField("IVA");
            field.setAccessible(true);
            double ivaValue = field.getDouble(null);
            if (Math.abs(ivaValue - 0.13) > 0.001) {
                System.out.println("❌ TEST 2 FALLIDO: La constante IVA existe pero su valor no es 0.13.");
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 2 APROBADO: La constante IVA = 0.13 está definida correctamente.");
            }
        } catch (NoSuchFieldException e) {
            System.out.println("❌ TEST 2 FALLIDO: No se encontró el atributo 'IVA' en ServicioInventario.");
            allTestsPassed = false;
        } catch (Exception e) {
            System.out.println("❌ TEST 2 FALLIDO: Error al intentar acceder a la constante IVA.");
            allTestsPassed = false;
        }

        // Prueba 3: Verificar ServicioInventario.venderProducto() - Venta Exitosa
        try {
            ServicioInventario servicio = new ServicioInventario();
            Producto pVenta = new Producto("SKU02", "Mouse", 25.0, 20);
            servicio.agregarProducto(pVenta);
            boolean ventaExitosa = servicio.venderProducto("SKU02", 5);
            if (!ventaExitosa || pVenta.getCantidadStock() != 15) {
                System.out.println("❌ TEST 3 FALLIDO: Una venta exitosa no retornó 'true' o no actualizó el stock correctamente.");
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 3 APROBADO: El método venderProducto() (venta exitosa) funciona.");
            }
        } catch (Exception e) {
            System.out.println("❌ TEST 3 FALLIDO: Error en venderProducto() en una venta exitosa. " + e.getMessage());
            allTestsPassed = false;
        }

        // Prueba 4: Verificar ServicioInventario.venderProducto() - Venta Fallida (sin stock)
        try {
            ServicioInventario servicio = new ServicioInventario();
            Producto pVenta = new Producto("SKU03", "Teclado", 80.0, 5);
            servicio.agregarProducto(pVenta);
            boolean ventaFallida = servicio.venderProducto("SKU03", 10);
            if (ventaFallida || pVenta.getCantidadStock() != 5) {
                System.out.println("❌ TEST 4 FALLIDO: Una venta sin stock suficiente no retornó 'false' o modificó el stock indebidamente.");
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 4 APROBADO: El método venderProducto() (venta fallida) funciona.");
            }
        } catch (Exception e) {
            System.out.println("❌ TEST 4 FALLIDO: Error en venderProducto() en una venta fallida. " + e.getMessage());
            allTestsPassed = false;
        }

        // Prueba 5: Verificar ServicioInventario.calcularValorTotalInventario()
        try {
            ServicioInventario servicio = new ServicioInventario();
            servicio.agregarProducto(new Producto("P1", "Prod A", 100.0, 10)); // Subtotal: 1000
            servicio.agregarProducto(new Producto("P2", "Prod B", 50.0, 20));  // Subtotal: 1000
            // Total sin IVA = 2000. Total con IVA (13%) = 2000 + (2000 * 0.13) = 2260.0
            double valorTotal = servicio.calcularValorTotalInventario();
            if (Math.abs(valorTotal - 2260.0) > 0.001) { 
                System.out.println("❌ TEST 5 FALLIDO: El cálculo del valor total del inventario (con IVA) es incorrecto. Se esperaba 2260.0 pero se obtuvo " + valorTotal);
                allTestsPassed = false;
            } else {
                System.out.println("✔️ TEST 5 APROBADO: El método calcularValorTotalInventario() funciona.");
            }
        } catch (Exception e) {
            System.out.println("❌ TEST 5 FALLIDO: Error en calcularValorTotalInventario(). " + e.getMessage());
            allTestsPassed = false;
        }

        if (!allTestsPassed) {
            System.exit(1);
        }
    }
}
