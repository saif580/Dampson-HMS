# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the target directory to the container
COPY target/usersmicroservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 7000

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
