FROM openjdk:17-jdk-slim
ADD /build/libs/*.jar footprint.jar
ENTRYPOINT ["java", "-jar","/footprint.jar"]