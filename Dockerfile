########################
# Stage 1 – Build JAR  #
########################
FROM gradle:8.3-jdk21 AS build

# ── Workdir for the project
WORKDIR /home/gradle/project

# ── Copy only files that rarely change first (better layer‑caching)
COPY --chown=gradle:gradle build.gradle* settings.gradle* gradle.properties gradlew gradle/ ./

# ── Copy sources last – only rebuild when code changes
COPY --chown=gradle:gradle src ./src

# ── Build the fat‑jar (no daemon → smaller image)
RUN ./gradlew clean bootJar --no-daemon


########################
# Stage 2 – Runtime    #
########################
FROM eclipse-temurin:21-jre

# ── Conventional runtime workdir
WORKDIR /app

# ── Copy jar built in previous stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# ── Expose Spring Boot’s default port
EXPOSE 8080

# ── Allow runtime JVM tweaks (e.g. on Render: JAVA_OPTS="-Xmx256m")
ENV JAVA_OPTS=""

# ── Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
