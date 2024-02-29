# # Use the Amazon Corretto 17 base image
# FROM amazoncorretto:17
# 
# # Define an argument for the JAR file path
# # ARG JAR_FILE=target/*.jar
# ARG JAR_FILE=*.jar
# 
# # Copy the JAR file from the build context into the Docker image
# COPY ${JAR_FILE} application.jar
# 
# # Update package lists (if needed)
# # CMD apt-get update -y  # This line is commented out as it seems unrelated to the Java application setup
# 
# # Set the default command to run the Java application
# ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
