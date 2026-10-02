package org.laboratorio4.controller;

import org.laboratorio4.config.PoliticaHotel;
import org.laboratorio4.dto.ReporteOcupacionDTO;
import org.laboratorio4.model.Habitacion;
import org.laboratorio4.model.HabitacionEstandar;
import org.laboratorio4.model.SuiteLujo;
import org.laboratorio4.service.GestorHotel;

import java.util.List;

public class Main {

    private static final String RUTA_JSON = "hotel.json";

    public static void main(String[] args) {
        PoliticaHotel politica = PoliticaHotel.getInstance();
        System.out.println("=== MOTOR DE RESERVAS - HOTEL ===");
        System.out.println("Politica del hotel (Singleton):");
        System.out.println("  Descuento temporada baja: " + politica.getDescuentoTemporadaBaja());
        System.out.println("  Cargo por servicio: " + politica.getCargoServicio());

        GestorHotel gestor = new GestorHotel();

        System.out.println("\n1) Registrando inventario inicial...");
        Habitacion estandar101 = new HabitacionEstandar(101, 80.0, true);
        Habitacion estandar102 = new HabitacionEstandar(102, 75.0, false);
        SuiteLujo suite201 = new SuiteLujo(201, 250.0, true);
        SuiteLujo suite202 = new SuiteLujo(202, 230.0, false);

        gestor.agregar(estandar101);
        gestor.agregar(estandar102);
        gestor.agregar(suite201);
        gestor.agregar(suite202);

        List<Habitacion> inventario = gestor.obtenerTodos();
        for (Habitacion habitacion : inventario) {
            System.out.println("  " + habitacion + " -> Precio/noche: " + habitacion.calcularPrecioNoche());
        }

        System.out.println("\n2) Marcando reservas...");
        estandar101.setOcupada(true);
        suite201.setOcupada(true);

        System.out.println("\n3) Guardando el estado completo en " + RUTA_JSON + "...");
        gestor.guardarEnJSON(RUTA_JSON);

        System.out.println("\n4) Limpiando el estado en memoria...");
        gestor.obtenerTodos().clear();
        System.out.println("  Habitaciones en memoria tras limpiar: " + gestor.obtenerTodos().size());

        System.out.println("\n5) Cargando el estado desde " + RUTA_JSON + "...");
        gestor.cargarDesdeJSON(RUTA_JSON);
        System.out.println("  Habitaciones restauradas: " + gestor.obtenerTodos().size());
        for (Habitacion habitacion : gestor.obtenerTodos()) {
            System.out.println("  " + habitacion + " -> Precio/noche: " + habitacion.calcularPrecioNoche());
        }

        System.out.println("\n6) Reporte de ocupacion (ReporteOcupacionDTO):");
        ReporteOcupacionDTO reporte = gestor.generarReporte();
        System.out.println("  Habitaciones totales:    " + reporte.getHabitacionesTotales());
        System.out.println("  Habitaciones ocupadas:   " + reporte.getHabitacionesOcupadas());
        System.out.printf("  Ingreso diario proyectado: %.2f%n", reporte.getIngresoDiarioProyectado());
        System.out.println("  DTO: " + reporte);

        System.out.println("\n7) Proyeccion en temporada baja (aplica "
                + PoliticaHotel.getInstance().getDescuentoTemporadaBaja() + " de descuento):");
        ReporteOcupacionDTO reporteBaja = gestor.generarReporte(true);
        System.out.printf("  Ingreso diario proyectado: %.2f%n", reporteBaja.getIngresoDiarioProyectado());
    }
}