# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven Project Object Model (POM) and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy Spring Boot application sources
COPY src ./src

# Build JAR package
RUN mvn package -DskipTests -B

# Execution stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Non-root security group config
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy built artifact from prior stage
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
