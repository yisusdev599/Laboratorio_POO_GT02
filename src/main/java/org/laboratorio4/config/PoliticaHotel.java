package org.laboratorio4.config;

public class PoliticaHotel {

    private static PoliticaHotel instance;
    private double descuentoTemporadaBaja;
    private double cargoServicio;

    private PoliticaHotel() {
        this.descuentoTemporadaBaja = 0.15;
        this.cargoServicio = 20.0;
    }

    public static synchronized PoliticaHotel getInstance() {
        if (instance == null) {
            instance = new PoliticaHotel();
        }
        return instance;
    }

    public double getDescuentoTemporadaBaja() {
        return descuentoTemporadaBaja;
    }

    public void setDescuentoTemporadaBaja(double descuentoTemporadaBaja) {
        this.descuentoTemporadaBaja = descuentoTemporadaBaja;
    }

    public double getCargoServicio() {
        return cargoServicio;
    }

    public void setCargoServicio(double cargoServicio) {
        this.cargoServicio = cargoServicio;
    }
}