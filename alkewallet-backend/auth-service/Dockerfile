# Etapa 1: Construcción
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copiamos solo el pom.xml y el src de este servicio
COPY pom.xml .
COPY src ./src
# Compilamos
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Cloud Run inyecta la variable $PORT automáticamente
# Usamos -Dserver.port=$PORT para que Spring sepa en qué puerto escuchar
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]