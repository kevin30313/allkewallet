# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
# Cambia la línea 7 de tu Dockerfile por esta:
FROM amazoncorretto:17-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
