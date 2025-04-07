#!/bin/bash

# Ferma e rimuove i contenitori di Docker
echo "Stopping and removing containers..."
docker-compose down

mvn clean install

# Entra nella directory del progetto gateway e costruisce il JAR
echo "Building gateway project..."
cd gateway && mvn clean install
cd ..

# Ricostruisce e avvia i container con il contesto corretto
echo "Rebuilding and starting containers..."
docker-compose -f docker-compose.yml up --build