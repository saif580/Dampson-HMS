# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project's build output (JAR file) to the working directory
COPY target/notificationmicroservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the application will run
EXPOSE 7003

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
