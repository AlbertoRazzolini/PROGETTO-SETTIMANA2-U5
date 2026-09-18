# Chat App — Progetto Settimana 2 U5

Applicazione di chat 1:1 in tempo reale, con autenticazione JWT, suggerimenti generati da un'IA e statistiche utente inviabili via email.

## Stack tecnologico

**Back-end** (cartella `BE/`)
- Java 25, Spring Boot 4.1.1
- Spring Data JPA + PostgreSQL
- Spring Security + JWT (jjwt) per l'autenticazione stateless
- Spring WebSocket / STOMP per la messaggistica in tempo reale
- Spring Mail + Thymeleaf per l'invio delle statistiche via email
- RestClient verso OpenRouter (protocollo compatibile OpenAI) per i suggerimenti IA
- Lombok

**Front-end** (cartella `FE/`)
- React 19 + TypeScript, Vite
- React Router per la navigazione
- Axios per le chiamate REST
- `@stomp/stompjs` per la connessione WebSocket

## Funzionalità

- Registrazione e login (JWT)
- Lista utenti registrati e apertura di una chat 1:1 con uno di loro
- Cronologia messaggi e invio/ricezione in tempo reale via WebSocket
- Conferma di lettura dei messaggi
- Suggerimento IA: pulsante **"Suggerimento"** nella chat che propone un messaggio da inviare, basato sulla cronologia recente della conversazione
- Statistiche personali (messaggi inviati/ricevuti, chat aperte) e invio delle stesse via email

## Prerequisiti

- JDK 25
- Node.js 18+ e npm
- PostgreSQL in esecuzione localmente (o un'istanza raggiungibile)
- Un account SMTP (es. Gmail con password per le app) per l'invio email
- Una API key di [OpenRouter](https://openrouter.ai) per i suggerimenti IA

## Avvio del back-end

1. Crea il database (se non esiste):
   ```sql
   CREATE DATABASE "db-PS2U5";
   ```
2. Configura le variabili d'ambiente necessarie (vedi `BE/.env.example` per l'elenco completo). Le principali:
   - `DB_USERNAME`, `DB_PASSWORD` — se diverse dai default (`postgres` / `1234`)
   - `MAIL_USERNAME`, `MAIL_PASSWORD` — credenziali SMTP, richieste per l'invio statistiche via email
   - `OPENROUTER_API_KEY` — richiesta per i suggerimenti IA (senza, quella funzionalità risponde con errore 401/503)

   Su Windows imposta le variabili d'ambiente **prima** di aprire l'IDE (un IDE già aperto non le rilegge finché non viene riavviato), oppure impostale direttamente nella Run Configuration.
3. Avvia l'applicazione:
   ```bash
   cd BE
   ./mvnw spring-boot:run
   ```
   Il server parte su `http://localhost:8080`.

## Avvio del front-end

1. Installa le dipendenze:
   ```bash
   cd FE
   npm install
   ```
2. Copia `.env.example` in `.env` (già presente con i valori di default per lo sviluppo locale):
   ```
   VITE_API_BASE_URL=http://localhost:8080/api
   VITE_WS_URL=ws://localhost:8080/ws
   ```
3. Avvia il dev server:
   ```bash
   npm run dev
   ```
   Il front-end è disponibile su `http://localhost:5173`.

## Note

- Il back-end espone CORS solo per l'origine indicata in `app.cors.allowed-origin` (default `http://localhost:5173`); se cambi la porta del front-end aggiorna anche `ALLOWED_ORIGIN`.
- Non esistono utenti precaricati: registra un account dalla pagina "Registrati" del front-end (o via `POST /api/auth/register`).
- Per provare la chat servono almeno due utenti registrati (es. due schede del browser).
