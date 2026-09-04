package org.laboratorio1.controller;

import org.laboratorio1.model.Producto;
import org.laboratorio1.service.ServicioInventario;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServicioInventario servicio = new ServicioInventario();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- SISTEMA DE INVENTARIO ---");
            System.out.println("1. Agregar producto");
            System.out.println("2. Vender producto");
            System.out.println("3. Calcular valor total del inventario");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("SKU: ");
                    String sku = scanner.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Precio: ");
                    double precio = scanner.nextDouble();
                    System.out.print("Cantidad en stock: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();
                    servicio.agregarProducto(new Producto(sku, nombre, precio, stock));
                    System.out.println("Producto agregado.");
                    break;
                case 2:
                    System.out.print("SKU del producto a vender: ");
                    String skuVenta = scanner.nextLine();
                    System.out.print("Cantidad a vender: ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine();
                    boolean exito = servicio.venderProducto(skuVenta, cantidad);
                    if (exito) {
                        System.out.println("Venta realizada con éxito.");
                    } else {
                        System.out.println("Venta fallida: stock insuficiente o producto no encontrado.");
                    }
                    break;
                case 3:
                    System.out.printf("El valor total del inventario (con IVA) es: %.2f%n", servicio.calcularValorTotalInventario());
                    break;
                case 4:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 4);

        scanner.close();
    }
}
