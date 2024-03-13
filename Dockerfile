FROM openjdk:17-slim
# Update package lists (optional for some base images)
RUN apt-get update && apt-get upgrade -y

# Install OpenJDK 17
RUN apt-get install -y openjdk-17-jdk

ENV JAVA_HOME /usr/java/openjdk-17

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
