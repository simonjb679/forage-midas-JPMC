# Midas Core

A Spring Boot microservice for real-time financial transaction processing, built as part of the JPMorgan Chase Software Engineering Virtual Experience.

## Overview

Midas Core is a transaction processing system that handles real-time financial transactions through Apache Kafka messaging and validates/persists them using a relational database. The system demonstrates enterprise-level patterns for financial data processing with proper validation, error handling, and data persistence.

## Tech Stack

- **Spring Boot** - Application framework
- **Apache Kafka** - Message streaming platform for transaction events
- **Spring Data JPA** - Database abstraction layer
- **H2 Database** - In-memory SQL database for development
- **Hibernate** - ORM for database operations
- **Maven** - Build and dependency management

## Architecture

The application follows a microservice architecture with:

- **Event-driven processing** via Kafka consumers
- **Database persistence** with JPA entities and repositories  
- **Transaction validation** with business logic constraints
- **Separation of concerns** between messaging, validation, and persistence layers

## Features

### Task 1: Basic Flow Implementation
- Implemented core application structure
- Set up Spring Boot configuration
- Established foundational architecture

### Task 2: Kafka Integration
- Configured Kafka producer and consumer
- Implemented `TransactionListener` for real-time message processing
- Set up JSON serialization/deserialization for transaction objects
- Validated message consumption from Kafka topics

### Task 3: Database Transaction Processing
- Created `TransactionRecord` JPA entity with many-to-one relationships
- Implemented transaction validation logic:
  - Sender/recipient ID validation
  - Insufficient balance checking
  - Automatic balance updates for valid transactions
- Added `TransactionRepository` for database operations
- Integrated Hibernate for SQL operations and transaction management

## Project Structure

```
src/main/java/com/jpmc/midascore/
├── component/
│   └── TransactionListener.java     # Kafka message consumer
├── entity/
│   ├── UserRecord.java             # User entity with balance tracking
│   └── TransactionRecord.java      # Transaction entity with relationships
├── foundation/
│   └── Transaction.java            # Transaction data model
└── repository/
    ├── UserRepository.java         # User data access layer
    └── TransactionRepository.java  # Transaction data access layer
```

## Configuration

Key application properties:
- Kafka broker configuration
- H2 database settings
- JPA/Hibernate configuration
- Topic configuration for transaction streaming

## Business Logic

The system validates transactions based on:
1. **Valid sender ID** - Sender must exist in the database
2. **Valid recipient ID** - Recipient must exist in the database  
3. **Sufficient balance** - Sender must have adequate funds

Valid transactions result in:
- Balance deduction from sender account
- Balance addition to recipient account
- Transaction record persistence to database

Invalid transactions are rejected with appropriate logging.

## Running the Application

```bash
# Run tests
./mvnw test

# Run specific test suites
./mvnw test -Dtest=TaskTwoTests
./mvnw test -Dtest=TaskThreeTests

# Start the application
./mvnw spring-boot:run
```

## Development Notes

This project demonstrates enterprise Java development practices including:
- Event-driven architecture patterns
- Database transaction management
- Input validation and error handling
- Separation of business logic from infrastructure concerns
- Proper Git workflow with feature branches

## Database Schema

The application uses the following entities:
- `UserRecord` - Stores user information and account balances
- `TransactionRecord` - Stores processed transaction history with foreign key relationships

Relationships:
- Each transaction has one sender (UserRecord)
- Each transaction has one recipient (UserRecord)
- Users can have multiple transactions as sender or recipient

---

*Built as part of the JPMorgan Chase Software Engineering Virtual Experience Program*