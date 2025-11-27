FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/NotificationService-1.0-SNAPSHOT.jar app.jar

RUN useradd -m -u 1000 spring
USER spring

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]