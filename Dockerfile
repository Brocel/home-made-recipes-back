FROM eclipse-temurin:21-jdk

WORKDIR /home-made-recipes-back

COPY pom.xml .
COPY api/pom.xml api/
COPY app/pom.xml app/
COPY common/pom.xml common/
COPY persistence/pom.xml persistence/
COPY service/pom.xml service/

# Maven config
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw -q -e -DskipTests dependency:go-offline || true

# Source code copy
COPY . .

# Exposing springboot port
EXPOSE 8080

# Activating DevTools
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true

# Run Spring Boot from the app module
CMD ["./mvnw", "-f", "app/pom.xml", "spring-boot:run"]
