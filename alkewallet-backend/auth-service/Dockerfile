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

# Exponer el puerto en el que corre el servicio (auth-service usa el 8081)
EXPOSE 8081

# Comando definitivo para arrancar la aplicación en Cloud Run
ENTRYPOINT ["java", "-jar", "app.jar"]