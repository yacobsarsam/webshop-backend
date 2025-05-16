FROM eclipse-temurin:21-jdk
# Set the working directory
WORKDIR /app
# Copy the build artifact from Gradle build
COPY build/libs/*.jar app.jar
# Expose the port the app runs on
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]