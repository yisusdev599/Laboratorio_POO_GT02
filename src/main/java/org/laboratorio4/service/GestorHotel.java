package org.laboratorio4.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.laboratorio4.config.PoliticaHotel;
import org.laboratorio4.dto.ReporteOcupacionDTO;
import org.laboratorio4.model.Habitacion;
import org.laboratorio4.model.HabitacionEstandar;
import org.laboratorio4.model.SuiteLujo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GestorHotel implements RepositorioGenerico<Habitacion> {

    private static final String TIPO_ESTANDAR = "ESTANDAR";
    private static final String TIPO_SUITE_LUJO = "SUITE_LUJO";
    private static final String CAMPO_TIPO = "tipo";

    private static final Type TIPO_LISTA_HABITACIONES = new TypeToken<List<Habitacion>>() {
    }.getType();

    private List<Habitacion> habitaciones = new ArrayList<>();

    public GestorHotel() {
        this.habitaciones = new ArrayList<>();
    }

    @Override
    public void agregar(Habitacion elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede agregar una habitacion nula.");
        }
        habitaciones.add(elemento);
    }

    @Override
    public List<Habitacion> obtenerTodos() {
        return habitaciones;
    }

    @Override
    public void guardarEnJSON(String ruta) {
        Gson gson = crearGson();
        File archivo = new File(ruta);
        try {
            Path padre = Paths.get(ruta).toAbsolutePath().getParent();
            if (padre != null && !Files.exists(padre)) {
                Files.createDirectories(padre);
            }
            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(habitaciones, TIPO_LISTA_HABITACIONES, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el estado del hotel en: " + ruta, e);
        }
    }

    @Override
    public void cargarDesdeJSON(String ruta) {
        Gson gson = crearGson();
        File archivo = new File(ruta);
        habitaciones.clear();
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("Advertencia: el archivo JSON de origen no existe o esta vacio: " + ruta);
            return;
        }
        List<Habitacion> cargadas;
        try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
            cargadas = gson.fromJson(reader, TIPO_LISTA_HABITACIONES);
        } catch (IOException | JsonParseException e) {
            throw new RuntimeException("No se pudo leer el estado del hotel desde: " + ruta, e);
        }
        if (cargadas != null) {
            habitaciones.addAll(cargadas);
        }
    }

    public ReporteOcupacionDTO generarReporte() {
        return generarReporte(false);
    }

    public ReporteOcupacionDTO generarReporte(boolean temporadaBaja) {
        double descuento = PoliticaHotel.getInstance().getDescuentoTemporadaBaja();
        int ocupadas = 0;
        double ingresoDiario = 0.0;

        for (Habitacion habitacion : habitaciones) {
            if (habitacion.isOcupada()) {
                ocupadas++;
                double precioNoche = habitacion.calcularPrecioNoche();
                ingresoDiario += temporadaBaja ? precioNoche * (1 - descuento) : precioNoche;
            }
        }
        return new ReporteOcupacionDTO(habitaciones.size(), ocupadas, ingresoDiario);
    }

    private Gson crearGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Habitacion.class, new HabitacionAdapter())
                .create();
    }

    private static final class HabitacionAdapter implements JsonSerializer<Habitacion>, JsonDeserializer<Habitacion> {

        @Override
        public JsonElement serialize(Habitacion fuente, Type tipo, JsonSerializationContext contexto) {
            JsonObject objeto = new JsonObject();
            if (fuente instanceof SuiteLujo) {
                SuiteLujo suite = (SuiteLujo) fuente;
                objeto.addProperty(CAMPO_TIPO, TIPO_SUITE_LUJO);
                objeto.addProperty("tieneJacuzzi", suite.isTieneJacuzzi());
            } else {
                HabitacionEstandar estandar = (HabitacionEstandar) fuente;
                objeto.addProperty(CAMPO_TIPO, TIPO_ESTANDAR);
                objeto.addProperty("incluyeDesayuno", estandar.isIncluyeDesayuno());
            }
            objeto.addProperty("numero", fuente.getNumero());
            objeto.addProperty("precioBase", fuente.getPrecioBase());
            objeto.addProperty("ocupada", fuente.isOcupada());
            return objeto;
        }

        @Override
        public Habitacion deserialize(JsonElement json, Type tipo, JsonDeserializationContext contexto) {
            JsonObject objeto = json.getAsJsonObject();
            int numero = objeto.get("numero").getAsInt();
            double precioBase = objeto.get("precioBase").getAsDouble();
            String tipoHabitacion = objeto.has(CAMPO_TIPO) ? objeto.get(CAMPO_TIPO).getAsString() : TIPO_ESTANDAR;

            Habitacion habitacion;
            if (TIPO_SUITE_LUJO.equalsIgnoreCase(tipoHabitacion)) {
                boolean tieneJacuzzi = objeto.has("tieneJacuzzi") && objeto.get("tieneJacuzzi").getAsBoolean();
                habitacion = new SuiteLujo(numero, precioBase, tieneJacuzzi);
            } else {
                boolean incluyeDesayuno = objeto.has("incluyeDesayuno") && objeto.get("incluyeDesayuno").getAsBoolean();
                habitacion = new HabitacionEstandar(numero, precioBase, incluyeDesayuno);
            }
            if (objeto.has("ocupada")) {
                habitacion.setOcupada(objeto.get("ocupada").getAsBoolean());
            }
            return habitacion;
        }
    }
}