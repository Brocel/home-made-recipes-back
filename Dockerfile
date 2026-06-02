FROM maven:4.0.0-eclipse-temurin-21

WORKDIR /home-made-recipes-back

COPY pom.xml .
COPY api/pom.xml api/
COPY app/pom.xml app/
COPY common/pom.xml common/
COPY persistence/pom.xml persistence/
COPY service/pom.xml service/

# Source code copy
COPY . .

# Exposing springboot port
EXPOSE 8080

# Activating DevTools
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true

# Run Spring Boot from the app module
CMD ["mvn", "-f", "app/pom.xml", "spring-boot:run"]
