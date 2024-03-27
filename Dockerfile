# Stage 1: Build Environment
FROM gradle:jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the source code
COPY . .

RUN ls -laRt

RUN gradle wrapper --gradle-version 8.5

RUN chmod +x gradlew
# Build the application using Gradle wrapper
RUN ./gradlew clean build

# Stage 2: Final Image
FROM openjdk:17-slim

# Update the package lists
RUN apt-get update && apt-get upgrade -y

# Install OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

# Copy the application JAR
COPY --from=builder /app/build/libs/*.jar app.jar

# Set environment variable
ENV JAVA_HOME /usr/lib/jvm/java-17

# Entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
