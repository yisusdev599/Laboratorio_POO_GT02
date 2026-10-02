package org.laboratorio4.model;

public class HabitacionEstandar extends Habitacion {

    private boolean incluyeDesayuno;

    public HabitacionEstandar(int numero, double precioBase, boolean incluyeDesayuno) {
        super(numero, precioBase);
        this.incluyeDesayuno = incluyeDesayuno;
    }

    public boolean isIncluyeDesayuno() {
        return incluyeDesayuno;
    }

    public boolean getIncluyeDesayuno() {
        return incluyeDesayuno;
    }

    public void setIncluyeDesayuno(boolean incluyeDesayuno) {
        this.incluyeDesayuno = incluyeDesayuno;
    }

    @Override
    public double calcularPrecioNoche() {
        double precio = precioBase;
        if (incluyeDesayuno) {
            precio += 15.0;
        }
        return precio;
    }

    @Override
    public String toString() {
        return super.toString() + " (Estandar, desayuno: " + incluyeDesayuno + ")";
    }
}