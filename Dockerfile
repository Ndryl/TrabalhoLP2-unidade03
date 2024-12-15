FROM openjdk:21-jdk-slim

WORKDIR /app

# usar arquivo gerado após ./mvnw clean package
COPY target/MongoSpring-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]