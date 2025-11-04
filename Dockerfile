# Etapa 1: construir el jar con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: ejecutar la app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/reactive-postgres-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8033
ENTRYPOINT ["java", "-jar", "app.jar"]
