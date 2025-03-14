# Stage 1: Build the application with Gradle 8.12.1
FROM gradle:8.12.1-jdk21 AS build

WORKDIR /app

# Copy only necessary Gradle files first for better caching
COPY gradlew ./
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./

# Verify Gradle wrapper is available
RUN ./gradlew --version

# Download dependencies first (better layer caching)
COPY . .
RUN ./gradlew clean build --no-daemon --stacktrace

# Stage 2: Run the application with Java 21 JDK
FROM openjdk:21-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/com-batch/build/libs/com-batch-*.jar com-batch.jar
COPY --from=build /app/common-export/build/libs/common-export-*.jar common-export.jar
COPY --from=build /app/job-scheduler/build/libs/job-scheduler-*.jar job-scheduler.jar
COPY --from=build /app/account-export/build/libs/account-export-*.jar account-export.jar

# Expose the port the app runs on
EXPOSE 8081

# Set RAM limit for the Java process (4 GB)
ENTRYPOINT ["java", "-Xmx4096m", "-Xms4096m", "-jar", "com-batch.jar"]
