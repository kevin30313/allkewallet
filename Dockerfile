# --- Etapa 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar las dependencias para cachearlas y acelerar futuros builds
RUN mvn dependency:go-offline -B

# Copiar el código fuente del microservicio
COPY src ./src

# Compilar y empaquetar el JAR saltándose los tests para agilizar
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de Ejecución (Runtime) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto dinámico de Cloud Run (dejamos 8080 por compatibilidad por defecto)
EXPOSE 8080

# Comando definitivo pasándole la variable PORT de Google a la máquina virtual de Java
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]