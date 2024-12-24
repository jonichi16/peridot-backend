FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY service/core/pom.xml ./service/core/pom.xml
COPY lib/common/pom.xml ./lib/common/pom.xml
COPY service/auth/pom.xml ./service/auth/pom.xml
COPY service/budget/pom.xml ./service/budget/pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline -ntp
COPY . .
RUN mvn clean package -ntp -DskipTests

FROM openjdk:21-jdk-slim
RUN useradd -m appuser
USER appuser
WORKDIR /app
COPY --from=builder /app/service/core/target/core-0.0.1-exec.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]