FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/*.jar /usr/app
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "redidit-clone-0.0.1-SNAPSHOT.jar"]
