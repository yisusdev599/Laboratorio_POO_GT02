package org.laboratorio4.model;

import org.laboratorio4.config.PoliticaHotel;

public class SuiteLujo extends Habitacion {

    private boolean tieneJacuzzi;

    public SuiteLujo(int numero, double precioBase, boolean tieneJacuzzi) {
        super(numero, precioBase);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    public boolean isTieneJacuzzi() {
        return tieneJacuzzi;
    }

    public boolean getTieneJacuzzi() {
        return tieneJacuzzi;
    }

    public void setTieneJacuzzi(boolean tieneJacuzzi) {
        this.tieneJacuzzi = tieneJacuzzi;
    }

    @Override
    public double calcularPrecioNoche() {
        double precio = precioBase + PoliticaHotel.getInstance().getCargoServicio();
        if (tieneJacuzzi) {
            precio += 50.0;
        }
        return precio;
    }

    @Override
    public String toString() {
        return super.toString() + " (Suite de Lujo, jacuzzi: " + tieneJacuzzi + ")";
    }
}