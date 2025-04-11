# TAAS-Activity

TAAS-Activity è un'applicazione per la gestione delle attività di gruppo. Il progetto utilizza un'architettura basata su microservizi con un backend sviluppato in **Java Spring Boot**, un frontend in **React** e uno **strato di persistenza** basato su **PostgreSQL**, tutto orchestrato tramite **Docker**.

## Indice

- [Tecnologie utilizzate](#tecnologie-utilizzate)
- [Prerequisiti](#prerequisiti)
- [Primo avvio e setup](#primo-avvio-e-setup)
- [Avvio in locale](#avvio-in-locale)
- [Struttura del progetto](#struttura-del-progetto)
- [API disponibili](#api-disponibili)
- [Troubleshooting](#troubleshooting)
- [Contribuire](#contribuire)

## Tecnologie utilizzate

- **Backend**: 
  - Java 17
  - Spring Boot (per la gestione dei microservizi)
  - RabbitMQ (per la gestione dei messaggi asincroni)
  
- **Frontend**: 
  - React
  - TailwindCSS (per la progettazione del layout responsivo)

- **Database**:
  - PostgreSQL (gestito tramite Docker)

- **DevOps**:
  - Docker e Docker Compose per la gestione dei container
  - Maven come gestore di dipendenze per il backend
  
- **Monitoraggio**:
  - Splunk per il monitoraggio e la gestione dei log

- **Versionamento**:
  - Git per il controllo di versione e GitHub per l'hosting del codice

## Prerequisiti

Prima di iniziare, assicurati di avere installato i seguenti software sulla tua macchina:

- **Docker** (versione 20.10.0 o superiore)
- **Docker Compose** (versione 2.0.0 o superiore)
- **Git** (versione 2.30.0 o superiore)
- **Java Development Kit (JDK)** (versione 17 o superiore)
- **Maven** (versione 3.8.0 o superiore)
- **Node.js** (versione 16.0.0 o superiore)
- **npm** (versione 8.0.0 o superiore)

Puoi verificare le versioni installate con i seguenti comandi:

```bash
docker --version
docker-compose --version
git --version
java --version
mvn --version
node --version
npm --version
```

## Primo avvio e setup

### 1. Clonare il repository

```bash
git clone https://github.com/AleRaunisi/TAAS-Activity.git
cd TAAS-Activity
```

### 2. Configurazione dell'ambiente

Il progetto utilizza file di configurazione per le variabili d'ambiente. Crea un file `.env` nella directory principale del progetto con il seguente contenuto (modifica i valori secondo le tue esigenze):

```
# Database
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_DB=taas_activity

# RabbitMQ
RABBITMQ_DEFAULT_USER
RABBITMQ_DEFAULT_PASS

# Spring Boot
SPRING_PROFILES_ACTIVE=dev
```

### 3. Avvio con script di deploy

Il progetto include uno script di deploy che automatizza il processo di avvio:

```bash
chmod +x deploy.sh
./deploy.sh
```

Questo script eseguirà le seguenti operazioni:
- Costruzione delle immagini Docker
- Avvio dei container (PostgreSQL, RabbitMQ, backend, frontend)
- Inizializzazione del database
- Configurazione di RabbitMQ

### 4. Verifica dell'installazione

Una volta completato il processo di avvio, puoi verificare che tutto funzioni correttamente:

- **Frontend**: Accedi a `http://localhost:8000` nel tuo browser
- **Gateway**: Accedi a `http://localhost:9000` nel tuo browser
- **Backend API**: Accedi a `http://localhost:8080/api/attivita` nel tuo browser
- **RabbitMQ Management**: Accedi a `http://localhost:15672`
- **PostgreSQL**: Connettiti al database sulla porta 5432 

## Struttura del progetto

Il progetto è organizzato in diversi componenti:

- **src/main/java/com/TASS**: Contiene il codice sorgente del backend
  - **controller**: Controller REST per gestire le richieste HTTP
  - **model**: Classi del modello di dominio (Attivita, Utente, Partecipazione)
  - **repository**: Interfacce per l'accesso ai dati
  - **service**: Implementazione della logica di business
  - **config**: Configurazioni Spring Boot

- **src**: Contiene il codice del frontend React
  - **components**: Componenti React riutilizzabili
  - **pages**: Pagine dell'applicazione
  - **App.js**: Componente principale dell'applicazione

- **mobile_app**: Applicazione mobile Flutter

- **notification-service**: Microservizio per la gestione delle notifiche

- **gateway**: API Gateway per la gestione delle richieste

## API disponibili

Il backend espone le seguenti API principali:

- **GET /attivita**: Recupera tutte le attività (supporta filtro con parametro `keyword`)
- **GET /attivita/{id}**: Recupera un'attività specifica per ID
- **POST /attivita**: Crea una nuova attività
- **PUT /attivita/{id}**: Aggiorna un'attività esistente
- **DELETE /attivita/{id}**: Elimina un'attività
- **GET /attivita/prenotazioni**: Recupera le prenotazioni dell'utente corrente
- **DELETE /attivita/{id}/cancel-booking**: Annulla una prenotazione

- **GET /auth/user**: Recupera informazioni sull'utente corrente