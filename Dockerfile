FROM bellsoft/liberica-runtime-container:jdk-21-musl AS builder
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

# Runtime stage (Minimal JRE image)
FROM bellsoft/liberica-runtime-container:jre-21-musl
WORKDIR /app
COPY --from=builder /app/target/file-processor-service-0.0.1-SNAPSHOT.jar file-processor-service-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "file-processor-service-0.0.1-SNAPSHOT.jar"]