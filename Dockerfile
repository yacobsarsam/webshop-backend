# Stage 1: Build
FROM gradle:8.3-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle clean bootJar

# Stage 2: Run
FROM eclipse-temurin:21-jre
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
