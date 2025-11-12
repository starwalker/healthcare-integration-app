# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline || true
COPY src ./src
RUN mvn -q -DskipTests package || true

# Run stage
FROM eclipse-temurin:17-jre
ENV JAVA_OPTS=""
WORKDIR /opt/app
COPY --from=build /app/target/health-integration-service-0.1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
