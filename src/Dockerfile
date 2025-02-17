FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/api-monitoring-0.0.1.jar
COPY ${JAR_FILE} api-monitoring.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-monitoring.jar"]