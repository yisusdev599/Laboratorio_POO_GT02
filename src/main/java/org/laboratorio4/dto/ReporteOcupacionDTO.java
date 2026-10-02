package org.laboratorio4.dto;

public class ReporteOcupacionDTO {

    private int habitacionesTotales;
    private int habitacionesOcupadas;
    private double ingresoDiarioProyectado;

    public ReporteOcupacionDTO(int habitacionesTotales, int habitacionesOcupadas, double ingresoDiarioProyectado) {
        this.habitacionesTotales = habitacionesTotales;
        this.habitacionesOcupadas = habitacionesOcupadas;
        this.ingresoDiarioProyectado = ingresoDiarioProyectado;
    }

    public int getHabitacionesTotales() {
        return habitacionesTotales;
    }

    public int getHabitacionesOcupadas() {
        return habitacionesOcupadas;
    }

    public double getIngresoDiarioProyectado() {
        return ingresoDiarioProyectado;
    }

    @Override
    public String toString() {
        return "ReporteOcupacionDTO{"
                + "habitacionesTotales=" + habitacionesTotales
                + ", habitacionesOcupadas=" + habitacionesOcupadas
                + ", ingresoDiarioProyectado=" + ingresoDiarioProyectado
                + '}';
    }
}