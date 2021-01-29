FROM maven:3.6-jdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn -e -B package

FROM amazoncorretto:11
COPY --from=builder /app/target/intercorp-0.0.1-SNAPSHOT.jar /app/target/