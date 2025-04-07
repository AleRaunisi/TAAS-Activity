# TAAS-Activity

TAAS-Activity è un'applicazione per la gestione delle attività di gruppo. Il progetto utilizza un'architettura basata su microservizi con un backend sviluppato in **Java Spring Boot**, un frontend in **React** e uno **strato di persistenza** basato su **PostgreSQL**, tutto orchestrato tramite **Docker**.

## Indice

- [Tecnologie utilizzate](#tecnologie-utilizzate)
- [Primo avvio e setup](#primo-avvio-e-setup)
- [Avvio in locale](#avvio-in-locale)
- [Struttura del progetto](#struttura-del-progetto)
- [Test](#test)
- [Contribuire](#contribuire)
- [Licenza](#licenza)

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

## Primo avvio e setup

### 1. **Clonare il repository**

Per avviare il progetto in locale, la prima cosa da fare è clonare il repository:

```bash
git clone https://github.com/AleRaunisi/TAAS-Activity.git

eseguire ./deploy