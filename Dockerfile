FROM openjdk:8-alpine3.9

ADD build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]