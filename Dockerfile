# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Run stage (smaller base image with just JRE)
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

COPY --from=builder /app/target/shop-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 9193

ENTRYPOINT ["java", "-jar", "app.jar"]
