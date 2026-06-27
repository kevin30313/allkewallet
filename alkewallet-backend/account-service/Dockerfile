# --- Etapa 1: Construcción (Build) ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -Dmaven.test.skip=true
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de Ejecución (Runtime) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Asegúrate de que el jar esté en la ruta correcta
COPY --from=build /app/target/*.jar app.jar
# Cloud Run inyecta la variable $PORT. Por defecto usaremos 8080
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]