# Usa un'immagine base con JDK 17
FROM openjdk:17-jdk-slim

# Imposta la directory di lavoro nel container
WORKDIR /app

# Copia il file JAR della tua applicazione nel container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta 8080 (quella di default di Spring Boot)
EXPOSE 8080

# Esegui l'applicazione Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]