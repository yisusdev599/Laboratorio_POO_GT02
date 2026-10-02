package org.laboratorio4.model;

public abstract class Habitacion {

    protected int numero;
    protected double precioBase;
    protected boolean ocupada;

    public Habitacion(int numero, double precioBase) {
        this.numero = numero;
        this.precioBase = precioBase;
        this.ocupada = false;
    }

    public int getNumero() {
        return numero;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public boolean getOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public abstract double calcularPrecioNoche();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Habitacion)) {
            return false;
        }
        Habitacion otra = (Habitacion) obj;
        return numero == otra.numero;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numero);
    }

    @Override
    public String toString() {
        return "Habitacion N° " + numero + " - Ocupada: " + ocupada;
    }
}