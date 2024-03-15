# Stage 1: Build Environment
FROM gradle:jdk17 AS builder

# Copy Gradle wrapper and project files
COPY . /app

# Adjust working directory
WORKDIR /app

# Build the application using Gradle wrapper
RUN ./gradlew clean build

# Stage 2: Final Image
FROM openjdk:17-slim

# Copy the application JAR
COPY --from=builder /app/build/libs/*.jar app.jar

# Set environment variable
ENV JAVA_HOME /usr/lib/jvm/java-17

# Entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
