FROM openjdk:17-alpine
EXPOSE 8080
ADD target/Weather_Service-0.0.1-SNAPSHOT.jar Weather_Service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Weather_Service-0.0.1-SNAPSHOT.jar"]