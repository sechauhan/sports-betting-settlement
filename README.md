# Sports Betting Settlement Trigger Service

A Spring Boot backend that simulates **sports betting event outcome handling** and **bet settlement** using **in-memory queues** only.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4**
- **Event outcomes**: in-memory queue
- **Bet settlements**: in-memory queue
- **In-memory HashMap** for bets (no database)
- **JUnit 5** for tests

## Features

1. **API endpoint** to publish a sports event outcome to the **event-outcomes** in-memory queue.
2. **Event-outcomes consumer** that drains the queue and matches outcomes to pending bets.
3. **Settlement matching**: finds bets by Event ID and determines win/loss by Event Winner ID.
4. **Bet-settlements producer** that sends settlement messages to the **bet-settlements** in-memory queue.
5. **Bet-settlements consumer** that drains the queue and marks bets as SETTLED.

## How to Run

### Prerequisites

- **JDK 21**
- **Maven 3.8+**
- No message brokers required (everything is in-memory).

### Build

```bash
cd sports-betting-settlement
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

The app will:

- Start on **http://localhost:8080**
- Use an in-memory HashMap for bets
- **Event outcomes**: in-memory queue; consumer drains it and runs match/settle flow
- **Bet settlements**: in-memory queue; consumer drains it and marks bets SETTLED

### Run tests

```bash
mvn test
```

Tests use the `test` profile; no brokers are required.

## API Usage

### 1. Publish event outcome (to event-outcomes queue)

```bash
curl -X POST http://localhost:8080/api/v1/event-outcomes \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "evt-1",
    "eventName": "Team A vs Team B",
    "eventWinnerId": "winner-1"
  }'
```

Response: `202 Accepted` – outcome is published to the event-outcomes in-memory queue.

### 2. Place a bet (stored in memory)

```bash
curl -X POST http://localhost:8080/api/v1/bets \
  -H "Content-Type: application/json" \
  -d '{
    "betId": "bet-1",
    "userId": "user-1",
    "eventId": "evt-1",
    "eventMarketId": "market-1",
    "eventWinnerId": "winner-1",
    "betAmount": 100.00
  }'
```

### 3. List bets

```bash
curl http://localhost:8080/api/v1/bets
curl "http://localhost:8080/api/v1/bets?eventId=evt-1&status=PENDING"
```

### 4. Get bet by ID

```bash
curl http://localhost:8080/api/v1/bets/bet-1
```

## End-to-end flow

1. **Place one or more bets** via `POST /api/v1/bets` with the same `eventId` and chosen `eventWinnerId`.
2. **Publish the event outcome** via `POST /api/v1/event-outcomes` with the same `eventId` and the actual `eventWinnerId`.
3. The **event-outcomes consumer** reads from the in-memory queue, finds **pending** bets for that `eventId`, and for each bet builds a settlement message (win = outcome’s `eventWinnerId` equals bet’s `eventWinnerId`).
4. Each settlement is sent to the **bet-settlements** in-memory queue.
5. The **bet-settlements consumer** drains the queue and updates each bet’s status to **SETTLED** in the in-memory store.
