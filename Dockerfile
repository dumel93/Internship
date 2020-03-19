FROM openjdk:8u201-jdk-alpine3.9
ADD target/pgs-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar pgs-1.0-SNAPSHOT.jar
