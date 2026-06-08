FROM maven:3.9.9-eclipse-temurin-17 AS build
LABEL authors="vaishnavgupta"

WORKDIR /app

COPY pom.xml .
COPY common-lib common-lib
COPY cloud cloud
COPY services services

ARG MODULE_PATH

RUN mvn -pl ${MODULE_PATH} -am clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

ARG MODULE_PATH

COPY --from=build /app/${MODULE_PATH}/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]