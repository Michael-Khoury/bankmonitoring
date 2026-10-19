# Bank Transaction Monitoring System

A Java/Spring Boot backend with REST APIs and PostgreSQL for managing users, accounts, and transactions, featuring rule-based checks for suspicious transaction activity.

## Overview

This personal project explores REST API development, database integration, and service-layer business logic in a financial transaction-monitoring context.

## Features

- REST API components for users, accounts, and transactions.
- Separate controller, model, repository, and service layers.
- PostgreSQL-backed data persistence.
- Account-specific transaction checks using amount thresholds and recent activity.
- A dedicated `FraudCheckService` for evaluating potentially suspicious transactions.

## Tech Stack

| Technology | Purpose |
| --- | --- |
| Java | Backend application logic |
| Spring Boot | Application framework and REST APIs |
| PostgreSQL | Relational data storage |
| Maven | Dependency management and build tooling |
| Postman | API testing |

## Transaction Monitoring Logic

The `isSuspicious(Transaction newTxn)` method evaluates an incoming transaction against stored transaction records.

It returns `true` when both conditions are met:

1. The incoming transaction amount is **greater than 1,000**.
2. At least **two stored transactions** meet all of the following criteria:
   - Belong to the same account.
   - Have an amount greater than 1,000.
   - Have a timestamp later than the current time minus one minute.

Otherwise, the method returns `false`.

| Incoming Amount | Qualifying Stored Transactions | Result |
| --- | --- | --- |
| 500 | 2 | `false` |
| 1,000 | 2 | `false` |
| 1,500 | 1 | `false` |
| 1,500 | 2 | `true` |

The count reflects the records present in the repository when the method runs. If the incoming transaction has already been saved, it may be included in that count.

A positive result indicates potentially suspicious activity; it does not establish that fraud has occurred.

## Project Structure

Java source files are organized under `com.michael.bankmonitoring`.

| Package / File | Responsibility |
| --- | --- |
| `controller` | Handles API requests through `UserController`, `AccountController`, and `TransactionController`. |
| `model` | Defines the `User`, `Account`, and `Transaction` domain models. |
| `repository` | Provides data access through `UserRepository`, `AccountRepository`, and `TransactionRepository`. |
| `service/FraudCheckService.java` | Evaluates the suspicious-transaction rule. |
| `BankMonitoringApplication.java` | Spring Boot application entry point. |
| `src/main/resources/application.properties` | Application and database configuration. |

## Getting Started

### Prerequisites

- Java version compatible with the project's `pom.xml`.
- A running PostgreSQL instance.
- Database connection details configured in `src/main/resources/application.properties`.

### Run the Application

Open a terminal in the directory containing `pom.xml` and the Maven wrapper files.

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

Alternatively, run `BankMonitoringApplication` directly from your IDE.

### Test the API

Use Postman to send requests to the routes defined in:

- `UserController`
- `AccountController`
- `TransactionController`

## Implementation Notes

The current fraud-checking method retrieves records using `findAll()` and filters them with Java streams.

Moving the account, amount, and timestamp filtering into a database query would reduce the amount of data loaded as the transaction history grows.

## Author

**Michael Khoury**

[GitHub](https://github.com/Michael-Khoury) · [Portfolio](https://michael-khoury.github.io)
