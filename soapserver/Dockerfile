FROM adoptopenjdk:8-jdk AS builder

COPY . /code

WORKDIR /code

RUN ./gradlew clean build


FROM openjdk:8-jdk-alpine

COPY --from=builder /code/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
