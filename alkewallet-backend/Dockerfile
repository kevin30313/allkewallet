# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM amazoncorretto:17-alpine
COPY --from=build /target/*.jar app.jar

# Dejamos que Render maneje el puerto mediante la variable de entorno
ENTRYPOINT java -Dserver.port=${PORT} -jar app.jar