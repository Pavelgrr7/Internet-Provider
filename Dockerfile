FROM gradle:8.5-jdk17-alpine AS builder

WORKDIR /app

COPY . .

# -x test пропускает тесты (чтобы сборка не упала без БД)
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]