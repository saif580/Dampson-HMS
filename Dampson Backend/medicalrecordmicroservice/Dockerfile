# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build artifact
COPY target/medicalrecordmicroservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 7006

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
