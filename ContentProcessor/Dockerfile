FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY target/ContentProcessor-0.0.1-SNAPSHOT.jar app.jar

RUN useradd -m -u 1000 spring
USER spring

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]