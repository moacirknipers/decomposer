FROM maven:3.6.3-jdk-11-slim AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean package

FROM openjdk:11-jdk-slim
COPY --from=MAVEN_BUILD /target/decomposer-0.0.1-SNAPSHOT.jar /decomposer.jar
CMD ["java", "-jar", "/decomposer.jar"]