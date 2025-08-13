FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -U

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar /app/cbhi.jar

ENV PORT=3021

EXPOSE 3021

ENTRYPOINT ["java", "-jar", "cbhi.jar"]

