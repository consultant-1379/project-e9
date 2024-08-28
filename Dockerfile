# There are vulnerabilities in this docker image so we may want to change it
FROM openjdk:17.0.2-jdk-oracle

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
