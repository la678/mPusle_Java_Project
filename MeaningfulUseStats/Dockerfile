# Use an official OpenJDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven-built JAR file into the container
COPY  target/MeaningfulUseStats.jar MeaningfulUseStats.jar

# Command to run the application
CMD ["java", "-jar", "MeaningfulUseStats.jar"]
