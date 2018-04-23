FROM openjdk:8-jdk

RUN apt-get update && apt-get install -y gradle
RUN gradle build

COPY build/libs/*.jar /app/service.jar

CMD java -jar /app/service.jar
