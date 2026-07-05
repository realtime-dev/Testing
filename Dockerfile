# Stage 1: build with Maven (JDK 17)
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

# copy only pom first to cache dependencies layer
COPY pom.xml .
RUN mvn -B dependency:go-offline

# copy source and build
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: smaller runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# copy the built jar from builder stage (artifact name wildcard)
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]