FROM maven:3.8.5-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/api-monitoring-0.0.1-SNAPSHOT.jar api-monitoring.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","api-monitoring.jar"]