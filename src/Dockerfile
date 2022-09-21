FROM maven:3.8.6-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY checkstyle.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:11
RUN mkdir /opt/results
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/app/target/*.jar /app/billboard.jar

ENTRYPOINT ["java", "-jar", "/app/billboard.jar"]
