FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar secondhand.jar

ENTRYPOINT ["java", "-jar", "secondhand.jar"]
