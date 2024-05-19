
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/authentication-service-0.0.1-SNAPSHOT.jar /app/authentication-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/authentication-service.jar"]
