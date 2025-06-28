# Use an official Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the full project and build it
COPY . .
RUN mvn clean package -DskipTests

# Use a minimal JDK image to run the application
FROM eclipse-temurin:17-jdk-alpine

# Set working directory in the container
WORKDIR /app

# Copy the built jar file from the previous image
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8088

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
