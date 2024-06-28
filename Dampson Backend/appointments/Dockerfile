# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/appointments-0.0.1-SNAPSHOT.jar app.jar

# Make port 7001 available to the world outside this container
EXPOSE 7001

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
