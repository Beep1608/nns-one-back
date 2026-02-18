FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar


ENV SPRING_PORT=8081
EXPOSE ${SPRING_PORT}

ENTRYPOINT ["java", "-Dserver.port=${SPRING_PORT}", "-jar", "app.jar"]