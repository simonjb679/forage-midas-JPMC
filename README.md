# Midas Core

A Spring Boot financial transaction processing microservice built for the JPMorgan Chase Software Engineering Virtual Experience. Processes real-time transactions with validation, incentive calculation, and balance management through event-driven architecture.

## Tech Stack

- **Spring Boot** - Application framework
- **Apache Kafka** - Event streaming for transactions
- **Spring Data JPA/Hibernate** - Database ORM
- **H2 Database** - In-memory SQL database
- **RestTemplate** - External API integration
- **Maven** - Build management

## Architecture

Event-driven microservice with:
- Kafka consumer for real-time transaction processing
- REST API for balance queries
- External incentive service integration
- Comprehensive transaction validation

## Features

### Transaction Processing
- Real-time Kafka message consumption from `midas-transactions` topic
- Multi-step validation: user existence, balance verification
- Automatic balance updates for sender/recipient
- External incentive calculation and application
- Transaction persistence and audit logging

### REST API
- **GET /balance?userId={id}** - Query user balance
- Returns JSON: `{"amount": 1234.56}`
- Handles non-existent users gracefully

### Validation Rules
- Sender/recipient must exist in system
- Sender must have sufficient balance
- Invalid transactions logged and rejected

## Quick Start

```bash
# Start external incentive service
java -jar services/transaction-incentive-api.jar

# Run tests
./mvnw test

# Start application (port 33400)
./mvnw spring-boot:run
```

## Implementation Tasks

**Task 1**: Project foundation and Spring Boot setup  
**Task 2**: Kafka integration with TransactionListener component  
**Task 3**: Database entities, repositories, and transaction validation  
**Task 4**: External incentive API integration via RestTemplate  
**Task 5**: REST endpoint implementation with BalanceController  

## Database Schema

**UserRecord**: `id`, `name`, `balance`  
**TransactionRecord**: `id`, `sender_id`, `recipient_id`, `amount`, `incentive`

## Key Components

- `TransactionListener` - Kafka consumer with business logic
- `BalanceController` - REST API endpoint
- `UserRepository/TransactionRepository` - Data access layer
- `Transaction/Balance/Incentive` - Core models

---

*Completed as part of JPMorgan Chase Software Engineering Virtual Experience*