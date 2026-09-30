#!/bin/bash
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0;0m'

echo "🚀 Iniciando validación de Laboratorio 2..."

# 1. Verificar build.gradle
echo "✅ Verificando configuración de Gradle..."
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}❌ ERROR: No se encontró el archivo build.gradle en la raíz del proyecto.${NC}"
    exit 1
fi

if ! grep -qi "gson" "build.gradle"; then
    echo -e "${RED}❌ ERROR: El archivo build.gradle no declara la dependencia de 'gson'.${NC}"
    exit 1
fi
echo -e "${GREEN}✔️ Archivo build.gradle y dependencia de Gson verificados.${NC}"

# 2. Verificación de Clases Obligatorias y Estructura POO
echo "✅ Verificando clases obligatorias y sintaxis POO..."
FILES=(
    "src/main/java/org/laboratorio2/config/AjustesPlataforma.java"
    "src/main/java/org/laboratorio2/model/Contenido.java"
    "src/main/java/org/laboratorio2/model/Pelicula.java"
    "src/main/java/org/laboratorio2/model/Serie.java"
    "src/main/java/org/laboratorio2/dto/EstadisticasCatalogoDTO.java"
    "src/main/java/org/laboratorio2/service/RepositorioGenerico.java"
    "src/main/java/org/laboratorio2/service/GestorStreaming.java"
    "src/main/java/org/laboratorio2/controller/Main.java"
)

for file in "${FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo -e "${RED}❌ ERROR: Falta el archivo obligatorio '$file'.${NC}"
        exit 1
    fi
done

if ! grep -q "abstract class" "src/main/java/org/laboratorio2/model/Contenido.java"; then
    echo -e "${RED}❌ ERROR: Contenido debe ser una clase abstracta ('abstract class').${NC}"
    exit 1
fi

if ! grep -q "extends Contenido" "src/main/java/org/laboratorio2/model/Pelicula.java" || \
   ! grep -q "extends Contenido" "src/main/java/org/laboratorio2/model/Serie.java"; then
    echo -e "${RED}❌ ERROR: Pelicula y Serie deben utilizar 'extends Contenido'.${NC}"
    exit 1
fi

if ! grep -q "implements RepositorioGenerico" "src/main/java/org/laboratorio2/service/GestorStreaming.java"; then
    echo -e "${RED}❌ ERROR: GestorStreaming debe utilizar 'implements RepositorioGenerico'.${NC}"
    exit 1
fi

if ! grep -q "@Override" "src/main/java/org/laboratorio2/model/Pelicula.java" || \
   ! grep -q "@Override" "src/main/java/org/laboratorio2/service/GestorStreaming.java"; then
    echo -e "${RED}❌ ERROR: Faltan anotaciones '@Override' en la implementación/sobrescritura de métodos.${NC}"
    exit 1
fi
echo -e "${GREEN}✔️ Clases obligatorias, herencia (extends), interfaz (implements) y @Override verificados.${NC}"

# 3. Compilación y ejecuciones
if [ ! -f "gson.jar" ]; then wget -q https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar -O gson.jar; fi

mkdir -p bin
echo "✅ Compilando proyecto..."
javac -cp gson.jar -d bin $(find src -name "*.java") 2>/dev/null
if [ $? -ne 0 ]; then echo -e "${RED}❌ ERROR DE COMPILACIÓN.${NC}"; exit 1; fi

cat <<EOF > TestRunner.java
import org.laboratorio2.model.*;
public class TestRunner {
    public static void main(String[] args) {
        Contenido c1 = new Pelicula(1, "A", 120, true);
        Contenido c2 = new Serie(1, "B", 60, 10);
        if(!c1.equals(c2)) { System.out.println("❌ ERROR: equals() no compara por ID"); System.exit(1); }
        if(c1.calcularCostoLicencia() != 500.0) { System.out.println("❌ ERROR: Costo licencia estreno fallido"); System.exit(1); }
    }
}
EOF
javac -cp bin TestRunner.java
java -cp bin:. TestRunner
if [ $? -ne 0 ]; then exit 1; fi

echo "✅ Ejecutando Main.java..."
java -cp bin:gson.jar org.laboratorio2.controller.Main > /dev/null

echo "✅ Verificando persistencia JSON..."
if [ ! -s "catalogo.json" ]; then
    echo -e "${RED}❌ ERROR: El archivo 'catalogo.json' no existe o está vacío. Revisa la serialización con Gson.${NC}"
    exit 1
else
    echo -e "${GREEN}✔️ Archivo 'catalogo.json' generado exitosamente con datos.${NC}"
fi

echo -e "${GREEN}✅ Todos los tests del Laboratorio 2 aprobados.${NC}"
exit 0